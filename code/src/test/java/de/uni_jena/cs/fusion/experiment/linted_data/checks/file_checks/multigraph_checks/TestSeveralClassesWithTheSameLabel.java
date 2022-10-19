package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.CheckSeveralClassesWithTheSameLabel;

public class TestSeveralClassesWithTheSameLabel {

	private CheckSeveralClassesWithTheSameLabel check = new CheckSeveralClassesWithTheSameLabel();
	
	@Test
	public void eachLabelOnce() {
		Dataset dataset = DatasetFactory.createGeneral();
		Model m = ModelFactory.createDefaultModel();
		m.add(m.createResource("http://example.org#John"), RDFS.label, "John");
		dataset.setDefaultModel(m);
		m = ModelFactory.createDefaultModel();
		m.add(m.createResource("http://example.org#Tom"), RDFS.label, "Tom");
		m.add(m.getResource("http://example.org#Tom"), RDFS.label, "Tommy");
		dataset.addNamedModel("named-model", m);
		
		assertTrue(dataset.containsNamedModel("named-model"));
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void oneLabelTwoClasses() {
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel m = ModelFactory.createOntologyModel();
		OntClass c = m.createClass("http://example.org#Person");
		c.addLabel("Person", "en");
		dataset.setDefaultModel(m);
		m = ModelFactory.createOntologyModel();
		c = m.createClass("http://example.org#HumanBeeing");
		c.addLabel("Person", "en");
		c.addLabel("Human", null);
		dataset.addNamedModel("named-model", m);
				
		assertTrue(dataset.containsNamedModel("named-model"));
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
	}

}
