/**
 * Copyright © 2022 Merle Gänßinger (merle.gaenssinger@uni-jena.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uni_jena.cs.fusion.linted_data.checks;

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

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;

public class CheckRdfRoundedFloatingPointValueTest {

	private CheckRdfRoundedFloatingPointValue check = new CheckRdfRoundedFloatingPointValue();

	/**
	 * creates a named model which contains a subject linked to a literal and
	 * returns the failures of CheckLexicalRepresentationOfFloatingPointDatatypes
	 * 
	 * @param lexicalValue value of the literal
	 * @param datatype     datatype of the literal
	 * @return list of failures where the lexicalValue can't be exactly represented
	 *         in the value space of the datatype
	 */
	private List<Failure> init(String lexicalValue, RDFDatatype datatype) {
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
			assertEquals(0, failures.size());
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
			assertEquals(1, failures.size());
			Failure failure = failures.get(0);
			assertEquals(Severity.WARN, failure.getSeverity());
			assertEquals(value + "^^" + XSDDatatype.XSDfloat.getURI(), failure.getFailureElement());
			assertEquals("\n" + "http://somewhere/John_Smith " + VCARD.KEY.getURI() + " \"" + value + "\"^^"
					+ XSDDatatype.XSDfloat.getURI(), failure.getText());
		}
	}

	/**
	 * literals are exactly representable in double
	 */
	@Test
	public void validDoubleRepresentation() {
		List<String> values = Arrays.asList("+2.5", "NaN", "Infinity", "-Infinity", "-0.625E-1", "375E3", "3015625E-1");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDdouble);
			assertNotNull(failures);
			assertEquals(0, failures.size());
		}
	}

	/**
	 * literals are not exactly representable in double
	 */
	@Test
	public void invalidDoubleRepresentation() {
		List<String> values = Arrays.asList("0.2", "3.14159", "0.1", "200E-3", "-10.71", "5876E-2", "0.1", "-0.3",
				"0.00000000002", "+20.0001");

		for (String value : values) {
			List<Failure> failures = init(value, XSDDatatype.XSDdouble);

			assertNotNull(failures);
			assertEquals(1, failures.size());
			Failure failure = failures.get(0);
			assertEquals(Severity.WARN, failure.getSeverity());
			assertEquals(value + "^^" + XSDDatatype.XSDdouble.getURI(), failure.getFailureElement());
			assertEquals("\n" + "http://somewhere/John_Smith " + VCARD.KEY.getURI() + " \"" + value + "\"^^"
					+ XSDDatatype.XSDdouble.getURI(), failure.getText());
		}
	}

	/**
	 * check that other data types are ignored
	 */
	@Test
	public void otherDatatypes() {
		List<Failure> failures = init("3.14159", XSDDatatype.XSDboolean);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = init("this is an interesting text ", XSDDatatype.XSDstring);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = init("2020-05-03T23:45:23", XSDDatatype.XSDdateTime);
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		failures = init("-200E-3", XSDDatatype.XSDdateTime);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		CheckRdfRoundedFloatingPointValue check = new CheckRdfRoundedFloatingPointValue();
		Model model = ModelFactory.createDefaultModel();
		Resource res = model.createResource("http://somewhere/0.2_Smith");
		res.addLiteral(VCARD.KEY, "0.2");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
}
