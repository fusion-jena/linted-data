package de.uni_jena.cs.fusion.linted_data;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.Testcase;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.Testsuite;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.TestsuiteManager;
import de.uni_jena.cs.fusion.linted_data.checks.CheckOwlInverseRelationshipForSymmetricProperty;
import de.uni_jena.cs.fusion.linted_data.checks.CheckOwlNoLicenseDeclared;
import de.uni_jena.cs.fusion.linted_data.checks.CheckOwlSelfInverseProperty;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfContainers;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfIriContainsFileExtension;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfIrisTooLong;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfLeadingOrTrailingSpaces;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfNamespacesShouldNotBeReferredByMultiplePrefixes;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfNamespacesShouldNotOmitTheSeperator;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfPrefixesReferToOneNamespace;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfRoundedFloatingPointValue;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfsMultipleDomainRange;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfsPropertyHasMissingDomainRangeDefinition;
import de.uni_jena.cs.fusion.linted_data.checks.CheckRdfsSeveralClassesWithTheSameLabel;
import de.uni_jena.cs.fusion.linted_data.checks.FileCheck;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

public class Runner {

	/**
	 * the checks that will be executed grouped by their scope
	 */
	private Map<Scope, List<FileCheck>> checks;
	/**
	 * the testsuites that group the checks
	 */
	private List<Testsuite> testsuites = new ArrayList<Testsuite>();
	/**
	 * defines how the execution in seconds time is formated as string when exported
	 * as xml
	 */
	private DecimalFormat decimalFormat;

	/**
	 * executes the checks on the resource and saves the results in XML-format in
	 * the output file
	 * 
	 * creates first the needed checks, afterwards executes them on the resource
	 * <p>
	 * the results are then grouped to testcases -> testsuite -> testsuites and then
	 * exported in XML format in the output file
	 * 
	 * @param scopes   defines the scope of the executed checks
	 * @param resource the file on which the checks will be performed
	 * @param output   where the xml output should be stored
	 * @throws Exception when during one of the execution of the checks occurred an
	 *                   exception
	 */
	public Runner(Scope[] scope, File resource, File output) throws Exception {
		initDecimalFormatter();
		createChecks(scope);
		createTestsuites(resource);
		TestsuiteManager manager = new TestsuiteManager("Linted Data", testsuites, decimalFormat);

		// export
		System.out.println("Exporting the results");
		XmlMapper xmlMapper = new XmlMapper();

		try {
			xmlMapper.writerWithDefaultPrettyPrinter().writeValue(output, manager);
		} catch (JsonProcessingException e) {
			System.err.println("Error when exporting the testsuites");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error when writing to " + output.getAbsolutePath());
			e.printStackTrace();
		}
	}

	/**
	 * executes the checks on the file, "summarises" the results into testsuites
	 * 
	 * 
	 * for each target language a testsuite is created
	 * <p>
	 * the corresponding tests are executed and their failures added to the testcase
	 * elements
	 * <p>
	 * the testcase elements are then added to the corresponding testsuite
	 * 
	 * @param resource the file that is investigated
	 * @throws Exception when during one of the execution of the checks occurred an
	 *                   exception
	 */
	private void createTestsuites(File resource) throws Exception {
		// split the testcases into the languages
		Map<Scope, List<Testcase>> testcases = new HashMap<Scope, List<Testcase>>();

		testsuites = new ArrayList<Testsuite>();
		System.out.println("Start executing tests");
		for (Scope targetLanguage : checks.keySet()) {
			System.out.println("Executing tests for: " + targetLanguage);
			// for each language create a new list of testcases
			testcases.put(targetLanguage, new ArrayList<Testcase>());
			for (FileCheck check : checks.get(targetLanguage)) {
				// execute all the checks of the corresponding language
				List<Failure> failures = check.startExecution(resource);
				// create a new testcase containing the check and its failures
				testcases.get(targetLanguage).add(new Testcase(check, failures,
						Scope.class.getCanonicalName() + "." + targetLanguage, decimalFormat));
			}
			// create a testsuite for the language with its testcases
			testsuites.add(new Testsuite(Scope.class.getCanonicalName() + "." + targetLanguage,
					targetLanguage + " testsuite", testcases.get(targetLanguage), decimalFormat));
			System.out.println("Finished tests for: " + targetLanguage);
		}
	}

	/**
	 * creates the map containing all test of the specified level
	 * 
	 * the tests are split according to their scope (rdf, rdfs, owl)
	 * 
	 * @param scopes which scopes will be checked
	 */
	private void createChecks(Scope[] scopes) {
		checks = new HashMap<Scope, List<FileCheck>>();
		// create a list that contains all implemented checks
		List<FileCheck> allChecks = createAllChecks();

		for (Scope language : scopes) {
			checks.put(language, new ArrayList<>());
		}

		// add only fitting checks to the map
		for (FileCheck check : allChecks) {
			if (checks.containsKey(check.getScope())) {
				checks.get(check.getScope()).add(check);
			}
		}
		System.out.println("Created checks for: " + Arrays.toString(scopes));
	}

	/**
	 * add all checks to a list
	 * 
	 * @return a list containing all implemented checks
	 */
	private List<FileCheck> createAllChecks() {
		List<FileCheck> allChecks = new ArrayList<FileCheck>();
		allChecks.add(new CheckRdfLeadingOrTrailingSpaces());
		allChecks.add(new CheckRdfNamespacesShouldNotOmitTheSeperator());
		allChecks.add(new CheckRdfNamespacesShouldNotBeReferredByMultiplePrefixes());
		allChecks.add(new CheckRdfPrefixesReferToOneNamespace());
		allChecks.add(new CheckRdfIrisTooLong());
		allChecks.add(new CheckRdfContainers());
		allChecks.add(new CheckRdfRoundedFloatingPointValue());
		allChecks.add(new CheckOwlNoLicenseDeclared());
		allChecks.add(new CheckRdfIriContainsFileExtension());
		allChecks.add(new CheckRdfsSeveralClassesWithTheSameLabel());
		allChecks.add(new CheckOwlInverseRelationshipForSymmetricProperty());
		allChecks.add(new CheckOwlSelfInverseProperty());
		allChecks.add(new CheckRdfsMultipleDomainRange());
		allChecks.add(new CheckRdfsPropertyHasMissingDomainRangeDefinition());
		return allChecks;
	}

	/**
	 * sets how the execution time in seconds is formated
	 */
	private void initDecimalFormatter() {
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
		decimalSymbols.setDecimalSeparator('.');
		decimalFormat = new DecimalFormat("0.00", decimalSymbols);
	}

}
