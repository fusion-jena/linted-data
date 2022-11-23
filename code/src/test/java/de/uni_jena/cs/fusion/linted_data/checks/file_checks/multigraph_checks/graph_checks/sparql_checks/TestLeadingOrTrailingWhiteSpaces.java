package de.uni_jena.cs.fusion.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckLeadingOrTrailingSpaces;
import de.uni_jena.cs.fusion.linted_data.types.Severity;

public class TestLeadingOrTrailingWhiteSpaces {

	private CheckLeadingOrTrailingSpaces check = new CheckLeadingOrTrailingSpaces();

	private Model model;
	private Resource johnSmith;

	@BeforeEach
	private void createDefaultModel() {
		Model model = ModelFactory.createDefaultModel();
		this.johnSmith = model.createResource("http://somewhere/JohnSmith");
		this.model = model;
	}

	/**
	 * tests that Strings with a leading white space are correctly detected
	 */
	@Test
	public void leadingWhiteSpaces() {
		// datatype -> string
		// character and numbers
		List<String> values = Arrays.asList("  john smith", " johnSmith", "\tjohn smith",
				" this is a long\ntext that goes over more than\n one line", "      123 25 43256",
				"\n123 this is text with num83r7", "              ");
		for (String value : values) {
			johnSmith.addLiteral(VCARD.FN, value);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.FN);
		}
		// datatype -> XSDdecimal
		values = Arrays.asList(" 125989845635", "         3333333333", "    275",
				"   1478525465655646654654645654654654546");
		for (String value : values) {
			johnSmith.addProperty(VCARD.TEL, value, XSDDatatype.XSDdecimal);

			List<Failure> failures = check.execute(model, "");

			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.TEL);
		}
	}

	/**
	 * all Strings end with a white space
	 */
	@Test
	public void trailingWhiteSpaces() {
		// datatype -> string
		List<String> values = Arrays.asList("john smith  ", "johnSmith. ", "john .smith                         ",
				"this is a long\ntext that goes over more than\n one line  ", "123 25 43256  ", "25/87885\t",
				"123 this is text with num83r7 \n");
		for (String value : values) {
			johnSmith.addProperty(VCARD.FN, value, XSDDatatype.XSDstring);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.FN);
		}
		// datatype -> XSDdecimal
		values = Arrays.asList("0.5 ", "0.3333333333           ", "0.25 ", "1.25        ");
		for (String value : values) {
			johnSmith.addProperty(VCARD.TEL, value, XSDDatatype.XSDdecimal);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.TEL);
		}
	}

	/**
	 * all strings start with white spaces and end with white spaces
	 */
	@Test
	public void leadingAndTrailingWhiteSpaces() {
		// datatype -> string
		List<String> values = Arrays.asList("   john, smith  ", " johnSmith ",
				"            john smith!                         ",
				" this is a long\ntext that goes over more than\n one line  ", "   123 25 43256  ",
				"            123 this is text with num83r7\t", "\nare you serious?\n");
		for (String value : values) {
			johnSmith.addProperty(VCARD.FN, value);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.FN);
		}
		//datatype -> XSDdate
		values = Arrays.asList(" 2022-05-31 ", "        2014-07-29           ", " 2010-28-01        ",
				"       1987-11-16        ");
		for (String value : values) {
			johnSmith.addProperty(VCARD.BDAY, value, XSDDatatype.XSDdate);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.INFO);
			assertEquals(failure.getFailureElement(), value);

			johnSmith.removeAll(VCARD.BDAY);
		}
	}

	/**
	 * all literals don't start or end with a white space
	 */
	@Test
	public void correctLiterals() {
		// datatype -> string
		List<String> values = Arrays.asList("dfsahjkwerkj sdfwearkl, ewrlk sdfaklwer",
				"this is no text ... about guinea pigs", "test test test!");
		for (String value : values) {
			johnSmith.addProperty(VCARD.FN, value);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 0);

			johnSmith.removeAll(VCARD.FN);
		}
		// datatype -> XSDboolean
		values = Arrays.asList("true", "false", "true", "true");
		for (String value : values) {
			johnSmith.addProperty(VCARD.Other, value, XSDDatatype.XSDboolean);

			List<Failure> failures = check.execute(model, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 0);

			johnSmith.removeAll(VCARD.Other);
		}
	}

}
