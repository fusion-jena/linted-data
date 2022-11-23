package de.uni_jena.cs.fusion.linted_data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.Testcase;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.Testsuite;
import de.uni_jena.cs.fusion.linted_data.JUnitXML.TestsuiteManager;
import de.uni_jena.cs.fusion.linted_data.checks.file_checks.CheckPrefixesReferToOneNamespace;
import de.uni_jena.cs.fusion.linted_data.checks.file_checks.FileCheck;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.CheckSeveralClassesWithTheSameLabel;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.CheckNamespacesShouldNotBeReferredByMultiplePrefixes;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.CheckNamespacesShouldNotOmitTheSeperator;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.CheckRdfRoundedFloatingPointValue;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckIRIsTooLong;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckInverseRelationshipForSymmetricProperty;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckLeadingOrTrailingSpaces;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckMultipleDomainRange;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckPropertyHasMissingDomainRangeDefinition;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRDFcontainers;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRdfIriContainsFileExtension;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRdfNoLicenseDeclared;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckSelfInverseProperty;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.TargetLanguage;

public class Runner {

	private Map<TargetLanguage, List<FileCheck>> checks;
	private List<Testsuite> testsuites = new ArrayList<Testsuite>();

	/**
	 * executes the checks on the resource and saves the results in XML-format in
	 * the output file
	 * 
	 * creates first the needed checks, afterwards executes them on the resource the
	 * results are then grouped to testcases -> testsuite -> testsuites and then
	 * exported in XML format in the output file
	 * 
	 * @param level    speciefies which checks will be executed
	 * @param resource the file on which the checks will be performed
	 * @param output   where the xml output should be stored
	 * @throws Exception when during one of the execution of the checks occurred an
	 *                   exception
	 */
	public Runner(Level level, File resource, File output) throws Exception {
		createChecks(level);
		createTestsuites(resource);

		TestsuiteManager manager = new TestsuiteManager("Linted Data", testsuites);

		// export
		System.out.println("Exporting the results");
		XmlMapper xmlMapper = new XmlMapper();

		try {
			xmlMapper.writerWithDefaultPrettyPrinter().writeValue(output, manager);
//			String employeeXml = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(manager);
//			System.out.println(employeeXml);
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
	 * 
	 * @param resource the file that is investigated
	 * @throws Exception when during one of the execution of the checks occurred an
	 *                   exception
	 */
	private void createTestsuites(File resource) throws Exception {
		// split the testcases into the languages
		Map<TargetLanguage, List<Testcase>> testcases = new HashMap<TargetLanguage, List<Testcase>>();

		testsuites = new ArrayList<Testsuite>();
		System.out.println("Start executing tests");
		for (TargetLanguage targetLanguage : checks.keySet()) {
			System.out.println("Executing tests for: " + targetLanguage);
			// for each language create a new list of testcases
			testcases.put(targetLanguage, new ArrayList<Testcase>());
			for (FileCheck check : checks.get(targetLanguage)) {
				// execute all the checks of the corresponding language
				List<Failure> failures = check.startExecution(resource);
				// create a new testcase containing the check and its failures
				testcases.get(targetLanguage).add(new Testcase(check, failures, TargetLanguage.class.getCanonicalName() + "." + targetLanguage));
			}
			// create a testsuite for the language with its testcases
			testsuites.add(new Testsuite(TargetLanguage.class.getCanonicalName() + "." + targetLanguage,
					targetLanguage + " testsuite", testcases.get(targetLanguage)));
			System.out.println("Finished tests for: " + targetLanguage);
		}
	}

	/**
	 * creates the map containing all test of the specified level
	 * 
	 * the tests are split according to their target language (rdfs, owl)
	 * 
	 * @param level specifies which level the tests to be executed should belong to
	 */
	private void createChecks(Level level) {
		checks = new HashMap<TargetLanguage, List<FileCheck>>();
		// create a list that contains all implemented checks
		List<FileCheck> allChecks = createAllChecks();
		// add only fitting checks to the map
		for (FileCheck check : allChecks) {
			if (level.equals(check.getLevel())) {
				// if the target language is not yet contained in the map
				// create an empty list for it
				if (checks.get(check.getTargetLangugage()) == null) {
					checks.put(check.getTargetLangugage(), new ArrayList<FileCheck>());
				}
				checks.get(check.getTargetLangugage()).add(check);
			}
		}
		System.out.println("Created checks from level: " + level);
	}

	/**
	 * add all checks to a list
	 * 
	 * @return a list containing all implemented checks
	 */
	private List<FileCheck> createAllChecks() {
		List<FileCheck> allChecks = new ArrayList<FileCheck>();
		allChecks.add(new CheckLeadingOrTrailingSpaces());
		allChecks.add(new CheckNamespacesShouldNotOmitTheSeperator());
		allChecks.add(new CheckNamespacesShouldNotBeReferredByMultiplePrefixes());
		allChecks.add(new CheckPrefixesReferToOneNamespace());
		allChecks.add(new CheckIRIsTooLong());
		allChecks.add(new CheckRDFcontainers());
		allChecks.add(new CheckRdfRoundedFloatingPointValue());
		allChecks.add(new CheckRdfNoLicenseDeclared());
		allChecks.add(new CheckRdfIriContainsFileExtension());
		allChecks.add(new CheckSeveralClassesWithTheSameLabel());
		allChecks.add(new CheckInverseRelationshipForSymmetricProperty());
		allChecks.add(new CheckSelfInverseProperty());
		allChecks.add(new CheckMultipleDomainRange());
		allChecks.add(new CheckPropertyHasMissingDomainRangeDefinition());
		return allChecks;
	}

}
