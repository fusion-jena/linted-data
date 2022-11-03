package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckInverseRelationshipForSymmetricProperty;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestInverseRelationshipForSymmetricProperty {

	private CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();

	private List<Failure> init(String path) {
		File file = new File(
				TestInverseRelationshipForSymmetricProperty.class.getClassLoader().getResource(path).getFile());
		return check.execute(file, "");
	}

	/**
	 * default model symmetric property is not an inverse of another relation
	 */
	@Test
	public void symmetricProperty1() {
		List<Failure> failures = init("CheckInverseRelationshipForSymmetricProperty/SymmetricProperty_01.rdf");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * dataset with default model and two named models symmetric properties are not
	 * an inverse of another relation
	 */
	@Test
	public void symmetricProperty2() {
		Dataset dataset = DatasetFactory.createGeneral();

		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		model.createSymmetricProperty("http://example.org#symmetric-property");
		OntProperty property = model.createObjectProperty("http://example.org#property-2");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property2.addInverseOf(property);
		dataset.setDefaultModel(model);

		model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ot", "http://other-exaple.org#");
		model.createObjectProperty("http://other-exaple.org#property-a");
		model.createSymmetricProperty("http://other-exaple.org#symmetric-property");
		property2 = model.createObjectProperty("http://other-exaple.org#property-b");
		property2.addLabel("2nd propeperty", "en");
		dataset.addNamedModel("http://other-exaple.org", model);

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

	/*
	 * default model defined property as inverse of a symmetric property
	 */
	@Test
	public void symmetricPropertyWithInverse1() {
		List<Failure> failures = init(
				"CheckInverseRelationshipForSymmetricProperty/SymmetricPropertyWithInverse_01.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b",
				f.getFailureElement());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b is defined as inverse property of the symmetric property http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-d",
				f.getText());
		assertEquals(Severity.WARN, f.getSeverity());
	}

	/*
	 * dataset with 2 named graphs first model -> one property has as inverse a
	 * symmetric property defined second model -> two properties have as inverse a
	 * symmetric property defined
	 */
	@Test
	public void symmetricPropertyWithInverse2() {
		Dataset dataset = DatasetFactory.createGeneral();

		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		OntProperty property = model.createSymmetricProperty("http://example.org#symmetric-property");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property2.addInverseOf(property);
		model.createObjectProperty("http://example.org#property-2");
		dataset.addNamedModel("http://example.org", model);

		model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ot", "http://other-exaple.org#");
		property2 = model.createObjectProperty("http://other-exaple.org#property-a");
		property = model.createSymmetricProperty("http://other-exaple.org#symmetric-property");
		property2.addInverseOf(property);
		property2 = model.createObjectProperty("http://other-exaple.org#property-b");
		property2.addLabel("2nd propeperty", "en");
		property2.addInverseOf(property);
		dataset.addNamedModel("http://other-exaple.org", model);

		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org#property",
				"\nModel: http://example.org\nhttp://example.org#property is defined as inverse property of the symmetric property http://example.org#symmetric-property",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://other-exaple.org#property-a",
				"\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-a is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://other-exaple.org#property-b",
				"\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-b is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property",
				Severity.WARN));
	}

	/*
	 * default graph + 2 named models, one where no property is defined as inverse
	 * of an symmetric property
	 */
	@Test
	public void sysmmetricPorpertyWithInverse3() {
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		OntProperty property = model.createSymmetricProperty("http://example.org#symmetric-property");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property2.addInverseOf(property);
		property = model.createObjectProperty("http://example.org#property-2");
		dataset.setDefaultModel(model);

		model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ot", "http://other-exaple.org#");
		property2 = model.createObjectProperty("http://other-exaple.org#property-a");
		property = model.createSymmetricProperty("http://other-exaple.org#symmetric-property");
		property2.addInverseOf(property);
		property2 = model.createObjectProperty("http://other-exaple.org#property-b");
		property2.addLabel("2nd propeperty", "en");
		property2.addInverseOf(property);
		dataset.addNamedModel("http://other-exaple.org", model);

		model = ModelFactory.createOntologyModel();
		OntClass c = model.createClass("http://my-example.com#class-a");
		c.addLabel("Class A", null);
		property = model.createSymmetricProperty("http://my-example.com#property");
		property.addDomain(c);
		property.addRange(c);
		dataset.addNamedModel("http://my-example.com", model);

		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org#property", "\nModel: Default Model\nhttp://example.org#property is defined as inverse property of the symmetric property http://example.org#symmetric-property"));
		assertTrue(TestUtil.contains(failures, "http://other-exaple.org#property-a", "\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-a is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property"));
		assertTrue(TestUtil.contains(failures, "http://other-exaple.org#property-b", "\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-b is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property"));
	}
}
