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

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

class CheckOwlSelfInversePropertyTest {

	private CheckOwlSelfInverseProperty check = new CheckOwlSelfInverseProperty();

	/**
	 * correct modeling
	 * 
	 * dataset with default graph + named model
	 */
	@Test
	void usingSymmetricInsteadOfSelfInverse() {
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		OntProperty property = model.createSymmetricProperty("http://example.org#symmetric-property");
		property.addLabel("Symmetric property", "en");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property2.addLabel("Property 1", null);
		property2 = model.createObjectProperty("http://example.org#property-2");
		property2.addLabel("Property 2", null);
		model.createDatatypeProperty("http://example.org#dataproperty", true);
		dataset.setDefaultModel(model);

		model = ModelFactory.createOntologyModel();
		dataset.addNamedModel("empty-model", model);

		model = ModelFactory.createOntologyModel();
		OntClass c = model.createClass("http://my-example.com#class-a");
		c.addLabel("Class A", null);
		property = model.createSymmetricProperty("http://my-example.com#property");
		property.addDomain(c);
		property.addRange(c);
		dataset.addNamedModel("http://my-example.com", model);

		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * no symmetric properties inverse relationship between properties in a dataset
	 */
	@Test
	void noSymmetricProperties() {
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		OntClass c1 = model.createClass("http://my-example.com#class-a");
		c1.addLabel("Class A", null);
		OntProperty p1 = model.createOntProperty("http://my-example.com#property-a");
		p1.addDomain(c1);
		OntClass c2 = model.createClass("http://my-example.com#class-b");
		p1.addRange(c2);
		model.createProperty("http://my-example.com#property-b");
		OntProperty p2 = model.createOntProperty("http://my-example.com#property-c");
		p1.addDomain(c2);
		p1.addRange(c1);
		p2.addInverseOf(p1);

		dataset.addNamedModel("my-ex", model);
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * model where a a property is inverse to itself
	 */
	@Test
	void selfInverseProperty1() {
		OntModel model = ModelFactory.createOntologyModel();
		OntClass c1 = model.createClass("http://my-example.com#class-a");
		c1.addLabel("Class A", null);
		OntProperty p1 = model.createOntProperty("http://my-example.com#property-a");
		p1.addDomain(c1);
		p1.addRange(c1);
		p1.addInverseOf(p1);
		assertTrue(p1.isInverseOf(p1));
		OntClass c2 = model.createClass("http://my-example.com#class-b");
		c2.addLabel("Class B", "en");
		p1 = model.createOntProperty("http://my-example.com#property-b");
		p1.addRange(c1);
		p1.addDomain(c2);

		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.com#property-a", f.getFailureElement());
		assertEquals("\nhttp://my-example.com#property-a is defined as inverse property to itself", f.getText());
	}

	/**
	 * a model containing two self inverse properties
	 */
	@Test
	void selfInverseProperty2() {
		OntModel model = ModelFactory.createOntologyModel();
		model.createClass("http://example.org#class-a");
		model.createClass("http://my-example.com#class-a");
		OntProperty p1 = model.createOntProperty("http://example.org#property-a");
		p1.addInverseOf(p1);
		assertTrue(p1.isInverseOf(p1));
		OntProperty p2 = model.createOntProperty("http://example.org#property-b");
		p2.addLabel("Property 2", null);
		p2 = model.createSymmetricProperty("http://example.org#sym-prop");
		p2.addInverseOf(p2);
		p1 = model.createOntProperty("http://example.org#property-c");
		p1.addInverseOf(p1);

		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org#property-a",
				"\nhttp://example.org#property-a is defined as inverse property to itself", Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org#property-c",
				"\nhttp://example.org#property-c is defined as inverse property to itself", Severity.WARN));
	}

	/**
	 * a dataset with two named models and default model
	 */
	@Test
	void selfInverseProperty3() {
		Dataset dataset = DatasetFactory.createGeneral();

		OntModel model = ModelFactory.createOntologyModel();
		model.createOntProperty("http://example.org/property-1");
		OntProperty p = model.createOntProperty("http://example.org/property-2");
		p.addInverseOf(p);
		assertTrue(p.isInverseOf(p));
		dataset.addNamedModel("1-self-inverse-model", model);
		model = ModelFactory.createOntologyModel();
		model.createOntProperty("http://foo.bar#p-a");
		model.createOntProperty("http://foo.bar#p-b");
		p = model.createOntProperty("http://foo.bar#p-c");
		p.addInverseOf(p);
		assertTrue(p.isInverseOf(p));
		dataset.setDefaultModel(model);
		model = ModelFactory.createOntologyModel();
		model.createClass("http://bar.com/class-a");
		model.createOntProperty("http://my-example.de/property-12");
		dataset.addNamedModel("2nd-named-model", model);

		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://foo.bar#p-c",
				"\nModel: Default Model\nhttp://foo.bar#p-c is defined as inverse property to itself", Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org/property-2",
				"\nModel: 1-self-inverse-model\nhttp://example.org/property-2 is defined as inverse property to itself",
				Severity.WARN));
	}

}
