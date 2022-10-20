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
	}
	
	@Test
	public void wrong_license_declared() {
		CheckLicenseDeclared check = new CheckLicenseDeclared();
		File file = new File(this.getClass().getClassLoader().getResource("CheckLicenseDeclared/otherLicenseDeclared.xml").getPath());
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals("\nModel: Default Model\nUse one the following licenses: dc:rights, dcterms:license, schema:license, cc:license to add a license",failure.getText());
		assertEquals("", failure.getFailureElement());
	}
}
