/**
 * Copyright 2022 Merle Gänßinger (merle.gaenssinger@uni-jena.de)
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

class CheckRdfIrisTooLongTest {

	private CheckRdfIrisTooLong check = new CheckRdfIrisTooLong();

	/**
	 * all IRIs have a local name with less than 30 characters
	 */
	@Test
	void noIriTooLong() {
		OntModel model = ModelFactory.createOntologyModel();
		model.createOntProperty("http://my-example.org/property-1_NAME");
		model.createDatatypeProperty("http://my-example.org#6d573906-de16-4790-b65f-76d9448dfc47");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		OntProperty p = model.createOntProperty("http://cerrado.linkeddata.es/ecology/ccon#affects");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		OntClass c1 = model.createClass("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16632");
		c1.addLabel("Geographic Area", "en");
		OntClass c2 = model.createClass("http://cerrado.linkeddata.es/ecology/ccon#Temperature");
		p.hasDomain(c2);
		p.hasRange(c1);
		model.add(c1, model.createDatatypeProperty("http://purl.obolibrary.org/obo/IAO_0000004"),
				"Some long nonsense name that is longer than 30 characters");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		Individual i1 = c1.createIndividual("http://example.com#inidvidual-1");
		i1.addComment("This is an individual for testing", null);
		Individual i2 = c2.createIndividual("http://example.com#Inidvidual-115");
		i2.addProperty(p, i1);
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * only IRIs at the subject position have a local name with more than 36
	 * characters
	 */
	@Test
	void subjectIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/subjectIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://my-example.org/this_is_änother+long+identifier-just-to-test_foo_bar",
				"\nModel: Default Model\nhttp://my-example.org/this_is_änother+long+identifier-just-to-test_foo_bar has a local name that has "
						+ 16 + " more characters than 36",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/very-long-class-name_that+will_create_a_failure",
				"\nModel: Default Model\nhttp://my-example.org/very-long-class-name_that+will_create_a_failure has a local name that has "
						+ 11 + " more characters than 36",
				Severity.INFO));
	}

	/**
	 * only IRIs at the predicate position have a local name with more than 36
	 * characters
	 */
	@Test
	void predicateIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/predicateIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/e0bce6f5-d21b-412f-a339-f331bc007f66-234u90_vcdx9u", f.getFailureElement());
		assertEquals(
				"\nModel: Default Model\nhttp://my-example.org/e0bce6f5-d21b-412f-a339-f331bc007f66-234u90_vcdx9u has a local name that has "
						+ 14 + " more characters than 36",
				f.getText());
	}

	/**
	 * only IRIs at the object position have a local name with more than 36
	 * characters
	 */
	@Test
	void objectIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/objectIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://my-example.org#9e7deb50-7329-435a-975d-c71480600f3a-12a-3d4-5e6-7f8-9g",
				"\nModel: Default Model\nhttp://my-example.org#9e7deb50-7329-435a-975d-c71480600f3a-12a-3d4-5e6-7f8-9g has a local name that has "
						+ 19 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org#very-long-object-identifier-to-test_theValidator",
				"\nModel: Default Model\nhttp://my-example.org#very-long-object-identifier-to-test_theValidator has a local name that has "
						+ 12 + " more characters than 36"));
	}

	/**
	 * some of the local names at subject and predicate position are longer than 36
	 * characters
	 */
	@Test
	void subjectPredicateIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/subjectPredicateIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://bar.org/f00-bar-f00-bar-f00-bar-147852369-erdfcv",
				"\nModel: Default Model\nhttp://bar.org/f00-bar-f00-bar-f00-bar-147852369-erdfcv has a local name that has "
						+ 4 + " more characters than 36",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "http://foo.com#class-0b5f8258-6ba0-4887-9843-7c630a06ecea",
				"\nModel: Default Model\nhttp://foo.com#class-0b5f8258-6ba0-4887-9843-7c630a06ecea has a local name that has "
						+ 6 + " more characters than 36",
				Severity.INFO));
	}

	/**
	 * some of the local names at subject and object position are longer than 36
	 * characters
	 */
	@Test
	void subjectObjectIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/subjectObjectIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(4, failures.size());
		assertTrue(TestUtil.contains(failures, "http://foo.com#class-0b5f8258-6ba0-4887-9843-7c630a06ecea",
				"\nModel: Default Model\nhttp://foo.com#class-0b5f8258-6ba0-4887-9843-7c630a06ecea has a local name that has "
						+ 6 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://bar.org/ca23e303-8819-4652-individual-bb40-f7a7bd8fc342",
				"\nModel: Default Model\nhttp://bar.org/ca23e303-8819-4652-individual-bb40-f7a7bd8fc342 has a local name that has "
						+ 11 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://bar.org/test-441a62c7-8a8b-4d41-8cde-a6436af261b2",
				"\nModel: Default Model\nhttp://bar.org/test-441a62c7-8a8b-4d41-8cde-a6436af261b2 has a local name that has "
						+ 5 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://foo.com#üther-identifier-344de7f9-8dd5-4e68-b400-4819a6459f44",
				"\nModel: Default Model\nhttp://foo.com#üther-identifier-344de7f9-8dd5-4e68-b400-4819a6459f44 has a local name that has "
						+ 17 + " more characters than 36"));
	}

	/**
	 * some of the IRIs at predicate and object position have more than 36
	 * characters as local name
	 */
	@Test
	void predicateObjectIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/predicateObjectIrisTooLong_01.nt", check);
		assertNotNull(failures);
		assertEquals(4, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org#property-longIdentifier-9e7deb50-7329-435a",
				"\nModel: Default Model\nhttp://example.org#property-longIdentifier-9e7deb50-7329-435a has a local name that has "
						+ 6 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/long-identifier-property-975d-c71480600f3a",
				"\nModel: Default Model\nhttp://my-example.org/long-identifier-property-975d-c71480600f3a has a local name that has "
						+ 6 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/individual-6d573906-de16-4790-b65f-76d9448dfc47",
				"\nModel: Default Model\nhttp://my-example.org/individual-6d573906-de16-4790-b65f-76d9448dfc47 has a local name that has "
						+ 11 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/individual-2a652854-8974-47d5-8760-d53b65a6000d",
				"\nModel: Default Model\nhttp://my-example.org/individual-2a652854-8974-47d5-8760-d53b65a6000d has a local name that has "
						+ 11 + " more characters than 36"));
	}

	/**
	 * IRIs at all three positions are too long
	 */
	@Test
	void subjectPredicateObjectIrisTooLong() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIrisTooLong/subjectPredicateObjectIrisTooLong_01.nt",
				check);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://my-example.org#very-long-object-identifier-to-test_theValidator",
				"\nModel: Default Model\nhttp://my-example.org#very-long-object-identifier-to-test_theValidator has a local name that has "
						+ 12 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/very-long-class-name_that+will_create_a_failure",
				"\nModel: Default Model\nhttp://my-example.org/very-long-class-name_that+will_create_a_failure has a local name that has "
						+ 11 + " more characters than 36"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/2a652854-8974-47d5-8760-d53b65a6000d1",
				"\nModel: Default Model\nhttp://my-example.org/2a652854-8974-47d5-8760-d53b65a6000d1 has a local name that has "
						+ 1 + " more characters than 36"));

	}

}
