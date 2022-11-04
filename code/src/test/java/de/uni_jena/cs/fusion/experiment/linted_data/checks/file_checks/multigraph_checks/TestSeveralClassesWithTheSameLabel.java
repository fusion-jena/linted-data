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
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestSeveralClassesWithTheSameLabel {

	private CheckSeveralClassesWithTheSameLabel check = new CheckSeveralClassesWithTheSameLabel();

	/**
	 * a dataset with a default model and a named model
	 * 
	 * each label appears only once in the union of the models
	 */
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

	/**
	 * the same label is used for different classes, but the language tag is
	 * different, hence the labels are different
	 */
	@Test
	public void oneLabelMultipleLanguages() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckSeveralClassesWithTheSameLabel/sameLabelDifferentLanguage.ttl").toURI());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * a label is shared by two classes
	 */
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

	/**
	 * a label is shared by more than two classes
	 */
	@Test
	public void multipleClassesOneLabel() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckSeveralClassesWithTheSameLabel/multipleClassesOneLabel.rdf").toURI());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("Class A", f.getFailureElement());
		assertEquals("\nClass A is shared by two or more classes", f.getText());
	}

	/**
	 * multiple classes share the same label with the same language tag also some
	 * classes have the same label with different language tags
	 */
	@Test
	public void differentLanguagesSameLabel() throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckSeveralClassesWithTheSameLabel/sameLabelSameLanguage.ttl").toURI());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "Label 1@en", "\nLabel 1@en is shared by two or more classes",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "Label 2@de", "\nLabel 2@de is shared by two or more classes",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "Label 1@de", "\nLabel 1@de is shared by two or more classes",
				Severity.INFO));
	}

}
