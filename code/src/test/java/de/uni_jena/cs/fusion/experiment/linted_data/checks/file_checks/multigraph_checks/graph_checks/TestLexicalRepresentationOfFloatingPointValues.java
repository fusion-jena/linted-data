package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.CheckLexicalRepresentationOfFloatingPointDatatypes;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestLexicalRepresentationOfFloatingPointValues {

	/**
	 * creates a named model which contains a subject linked to a literal and
	 * returns the failures of CheckLexicalRepresentationOfFloatingPointDatatypes
	 * 
	 * @param lexicalValue value of the literal
	 * @param datatype     datatype of the literal
	 * @return list of failures of
	 *         CheckLexicalRepresentationOfFloatingPointDatatypes
	 */
	private List<Failure> init(String lexicalValue, RDFDatatype datatype) {
		CheckLexicalRepresentationOfFloatingPointDatatypes check = new CheckLexicalRepresentationOfFloatingPointDatatypes();

		Model model = ModelFactory.createMemModelMaker().createModel("http://named-model.com#");
		Resource res = model.createResource("http://somewhere/John_Smith");
		res.addProperty(VCARD.KEY, lexicalValue, datatype);

		return check.execute(model, "");
	}

	/**
	 * literals are exactly representable in float
	 */
	@Test
	public void validFloatRepresentation() {
		List<String> values = Arrays.asList("+0.5", "NaN", "Infinity", "-Infinity", "-0.25", "-325E3", "325E-2");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDfloat);

			assertNotNull(failures);
			assertEquals(failures.size(), 0);
		}
	}

	/**
	 * literals are not exactly representable in float
	 */
	@Test
	public void invalidFloatRepresentation() {
		List<String> values = Arrays.asList("0.2", "3.14159", "0.1", "200E-3", "-10.71", "5876E-2", "0.1", "-0.3",
				"0.00000000002", "+20.0001");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDfloat);

			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.WARN);
			assertEquals(failure.getFailureElement(), value + "^^" + XSDDatatype.XSDfloat.getURI());
		}
	}

	/**
	 * literals are exactly representable in float
	 */
	@Test
	public void validDoubleRepresentation() {
		List<String> values = Arrays.asList("+2.5", "NaN", "Infinity", "-Infinity", "-0.625E-1", "375E3", "3015625E-1");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDdouble);

			assertNotNull(failures);
			assertEquals(failures.size(), 0);
		}
	}
	
	/**
	 * literals are not exactly representable in float
	 */
	@Test
	public void invalidDoubleRepresentation() {
		List<String> values = Arrays.asList("0.2", "3.14159", "0.1", "200E-3", "-10.71", "5876E-2", "0.1", "-0.3",
				"0.00000000002", "+20.0001");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDdouble);

			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			Failure failure = failures.get(0);
			assertEquals(failure.getSeverity(), Severity.WARN);
			assertEquals(failure.getFailureElement(), value + "^^" + XSDDatatype.XSDdouble.getURI());
		}
	}

	/**
	 * check that other data types are ignored
	 */
	@Test
	public void otherDatatypes() {
		List<Failure> failures = init("3.14159", XSDDatatype.XSDboolean);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);

		failures = init("this is an interesting text ", XSDDatatype.XSDstring);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);

		failures = init("2020-05-03T23:45:23", XSDDatatype.XSDdateTime);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);

		CheckLexicalRepresentationOfFloatingPointDatatypes check = new CheckLexicalRepresentationOfFloatingPointDatatypes();
		Model model = ModelFactory.createDefaultModel();
		Resource res = model.createResource("http://somewhere/0.2_Smith");
		res.addLiteral(VCARD.KEY, "0.2");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(failures.size(), 0);
	}
}
