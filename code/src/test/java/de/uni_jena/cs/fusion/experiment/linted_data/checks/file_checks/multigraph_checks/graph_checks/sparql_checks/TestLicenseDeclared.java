package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckLicenseDeclared;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestLicenseDeclared {

	@Test
	public void license_declared() {
		CheckLicenseDeclared check = new CheckLicenseDeclared();
		File file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/LicenseDeclared_01.rdf").getPath());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/LicenseDeclared_02.ttl").getPath());
		failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/LicenseDeclared_03.ttl").getPath());
		failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/LicenseDeclared_04.rdf").getPath());
		failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void wrong_license_declared() {
		CheckLicenseDeclared check = new CheckLicenseDeclared();
		File file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/otherLicenseDeclared.xml").getPath());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals("\nModel: Default Model\nOntology http://www.semanticweb.org/rs/ontologies/INBIO should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license", failure.getText());
		assertEquals("http://www.semanticweb.org/rs/ontologies/INBIO", failure.getFailureElement());
	}
	
	@Test
	public void no_license_declared() {
		CheckLicenseDeclared check = new CheckLicenseDeclared();
		File file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/noLicenseDeclared.ttl").getPath());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals("\nModel: Default Model\nOntology http://www.semanticweb.org/test-ontology/ should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",failure.getText());
		assertEquals("http://www.semanticweb.org/test-ontology/", failure.getFailureElement());
	
		file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/noLicenseDeclared.jsonld").getPath());
		failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.ERROR, failure.getSeverity());
		assertEquals("\nModel: Default Model\nOntology http://www.semanticweb.org/test-ontology/ should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license",failure.getText());
		assertEquals("http://www.semanticweb.org/test-ontology/", failure.getFailureElement());
	
		
	}
}