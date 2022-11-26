package de.uni_jena.cs.fusion.linted_data.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class CheckOwlNoLicenseDeclaredTest {

	private CheckOwlNoLicenseDeclared check = new CheckOwlNoLicenseDeclared();

	@Test
	public void license_declared() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/licenseDeclared_01.rdf", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/licenseDeclared_02.ttl", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/licenseDeclared_03.ttl", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/licenseDeclared_04.rdf", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	public void wrong_license_declared() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/otherLicenseDeclared.xml", check);
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
	public void no_license_declared() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/noLicenseDeclared.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nOntology http://www.semanticweb.org/test-ontology/ should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",
				failure.getText());
		assertEquals("http://www.semanticweb.org/test-ontology/", failure.getFailureElement());

		failures = TestUtil.executeCheck("CheckOwlNoLicenseDeclared/noLicenseDeclared.jsonld", check);
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