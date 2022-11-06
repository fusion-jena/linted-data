package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckLicenseDeclared;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestLicenseDeclared {

	private CheckLicenseDeclared check = new CheckLicenseDeclared();

	private List<Failure> execute(String path) {
		File file = new File(this.getClass().getClassLoader().getResource(path).getPath());
		return check.execute(file, "");
	}

	@Test
	public void license_declared() {
		List<Failure> failures = execute("CheckLicenseDeclared/LicenseDeclared_01.rdf");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = execute("CheckLicenseDeclared/LicenseDeclared_02.ttl");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = execute("CheckLicenseDeclared/LicenseDeclared_03.ttl");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = execute("CheckLicenseDeclared/LicenseDeclared_04.rdf");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	public void wrong_license_declared() {
		List<Failure> failures = execute("CheckLicenseDeclared/otherLicenseDeclared.xml");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nOntology http://www.semanticweb.org/rs/ontologies/INBIO should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				failure.getText());
		assertEquals("http://www.semanticweb.org/rs/ontologies/INBIO", failure.getFailureElement());

	}

	@Test
	public void no_license_declared() {
		List<Failure> failures = execute("CheckLicenseDeclared/noLicenseDeclared.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nOntology http://www.semanticweb.org/test-ontology/ should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				failure.getText());
		assertEquals("http://www.semanticweb.org/test-ontology/", failure.getFailureElement());

		failures = execute("CheckLicenseDeclared/noLicenseDeclared.jsonld");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nOntology http://www.semanticweb.org/test-ontology/ should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				failure.getText());
		assertEquals("http://www.semanticweb.org/test-ontology/", failure.getFailureElement());

		// dataset with named model,
		// one ontoloty with a license, one without
		Dataset dataset = DatasetFactory.createGeneral();
		OntModel model = ModelFactory.createOntologyModel();
		Ontology ont = model.createOntology("o-1");
		ont.addProperty(DCTerms.creator, "John Smith");
		ont.addProperty(DCTerms.license, "https://creativecommons.org/licenses/by-nc-sa/4.0/");
		ont = model.createOntology("o-2");
		ont.addProperty(DCTerms.creator, "John Smith");
		dataset.addNamedModel("model-with-license", model);
		failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("o-2", failure.getFailureElement());
		assertEquals(
				"\nModel: model-with-license\nOntology o-2 should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				failure.getText());
		// extend testcase:
		// add a second named model without license
		model = ModelFactory.createOntologyModel();
		ont = model.createOntology("o-3");
		ont.addProperty(DCTerms.contributor, "John Smith");
		dataset.addNamedModel("missing-license", model);
		failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "o-3",
				"\nModel: missing-license\nOntology o-3 should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				Severity.ERROR));
		assertTrue(TestUtil.contains(failures, "o-2",
				"\nModel: model-with-license\nOntology o-2 should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				Severity.ERROR));

	}
}