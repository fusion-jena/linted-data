package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.CheckSeveralClassesWithTheSameLabel;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestSeveralClassesWithTheSameLabel {

	private CheckSeveralClassesWithTheSameLabel check = new CheckSeveralClassesWithTheSameLabel();
		
	@Test
	public void eachLabelOnce() {
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel m = ModelFactory.createOntologyModel();
		OntClass c = m.createClass("http://example.org#Person");
		c.addLabel("Person", "en");
		dataset.setDefaultModel(m);
		m = ModelFactory.createOntologyModel();
		c = m.createClass("http://example.org#HumanBeeing");
		c.addLabel("Human beeing", null);
		dataset.addNamedModel("named-model", m);
				
		assertTrue(dataset.containsNamedModel("named-model"));
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void oneLabelMultipleLanguages() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader().getResource("CheckSeveralClassesWithTheSameLabel/sameLabelDifferentLanguage.ttl").toURI());
		List<Failure> failures = check.execute(file, "");
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
		Failure f = failures.get(0);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("Person@en", f.getFailureElement());
		assertEquals("\nPerson@en is shared by two or more classes", f.getText());
	}
	
	@Test
	public void multipleClassesOneLabel() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader().getResource("CheckSeveralClassesWithTheSameLabel/multipleClassesOneLabel.rdf").toURI());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("Class A", f.getFailureElement());
		assertEquals("\nClass A is shared by two or more classes", f.getText());
	}
	
	@Test
	public void differentLanguagesSameLabel() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader().getResource("CheckSeveralClassesWithTheSameLabel/differentLanguagesSameLabel.ttl").toURI());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(3, failures.size());
		Failure f = failures.get(0);
		assertEquals("Label 1@en", f.getFailureElement());
		f = failures.get(1);
		assertEquals("Label 2@de", f.getFailureElement());
		f = failures.get(2);
		assertEquals("Label 1@de", f.getFailureElement());
	}

}
