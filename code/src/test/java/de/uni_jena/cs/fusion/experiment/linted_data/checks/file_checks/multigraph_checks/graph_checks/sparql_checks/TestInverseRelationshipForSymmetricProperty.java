package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

public class TestInverseRelationshipForSymmetricProperty {
	
	private List<Failure> init(String path){
		CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();
		File file = new File(TestInverseRelationshipForSymmetricProperty.class.getClassLoader().getResource(path).getFile());
		return check.execute(file, "");
	}
	
	@Test
	public void symmetricProperty1() {
		/*
		 * default model 
		 * symmetric property is not an inverse of another relation
		 */
		List<Failure> failures = init("CheckInverseRelationshipForSymmetricProperty/SymmetricProperty_01.rdf");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void symmetricProperty2() {
		/*
		 * dataset + no errors
		 */
		CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		OntProperty property = model.createSymmetricProperty("http://example.org#symmetric-property");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property = model.createObjectProperty("http://example.org#property-2");
		property2.addInverseOf(property);
		dataset.setDefaultModel(model);
		
		model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ot", "http://other-exaple.org#");
		property2 = model.createObjectProperty("http://other-exaple.org#property-a");
		property = model.createSymmetricProperty("http://other-exaple.org#symmetric-property");
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
	
	@Test
	public void symmetricPropertyWithInverse1() {
		/*
		 * default model 
		 * defined property as inverse of a symmetric property
		 */
		List<Failure> failures = init("CheckInverseRelationshipForSymmetricProperty/SymmetricPropertyWithInverse_01.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b", f.getFailureElement());
		assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b is defined as inverse property of the symmetric property http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-d", f.getText());
	}
	
	@Test
	public void symmetricPropertyWithInverse2() {
		/* dataset with 2 named graphs
		 * first model -> one property has as inverse a symmetric property defined
		 * second model -> two properties have as inverse a symmetric property defined
		 */
		CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		model.setNsPrefix("ex", "http://example.org#");
		OntProperty property = model.createSymmetricProperty("http://example.org#symmetric-property");
		OntProperty property2 = model.createOntProperty("http://example.org#property");
		property2.addInverseOf(property);
		property = model.createObjectProperty("http://example.org#property-2");
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
		Failure f = failures.get(0);
		assertEquals("http://example.org#property", f.getFailureElement());
		assertEquals("\nModel: http://example.org\nhttp://example.org#property is defined as inverse property of the symmetric property http://example.org#symmetric-property", f.getText());
		f = failures.get(2);
		assertEquals("http://other-exaple.org#property-a", f.getFailureElement());
		assertEquals("\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-a is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property", f.getText());
		f = failures.get(1);
		assertEquals("http://other-exaple.org#property-b", f.getFailureElement());
		assertEquals("\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-b is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property", f.getText());
	}
	
	@Test
	public void sysmmetricPorpertyWithInverse3() {
		/*
		 * default graph + 2 named models, one without error 
		 */
		CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();
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
		Failure f = failures.get(0);
		assertEquals("http://example.org#property", f.getFailureElement());
		assertEquals("\nModel: Default Model\nhttp://example.org#property is defined as inverse property of the symmetric property http://example.org#symmetric-property", f.getText());
		f = failures.get(2);
		assertEquals("http://other-exaple.org#property-a", f.getFailureElement());
		assertEquals("\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-a is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property", f.getText());
		f = failures.get(1);
		assertEquals("http://other-exaple.org#property-b", f.getFailureElement());
		assertEquals("\nModel: http://other-exaple.org\nhttp://other-exaple.org#property-b is defined as inverse property of the symmetric property http://other-exaple.org#symmetric-property", f.getText());
	}
}
