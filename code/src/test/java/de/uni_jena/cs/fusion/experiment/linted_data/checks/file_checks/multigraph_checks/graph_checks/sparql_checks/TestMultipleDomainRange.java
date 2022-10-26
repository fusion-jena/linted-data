package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckMultipleDomainRange;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestMultipleDomainRange {

	@Test
	void multipleDomain() {
		CheckMultipleDomainRange check = new CheckMultipleDomainRange();

		List<Failure> failures = check.execute(new File(this.getClass().getClassLoader()
				.getResource("CheckMultipleDomainRange/multiple_domain_01.xml").getFile()), "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(1);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple domains, ranges or both",
				f.getText());
	}

	@Test
	void multipleRanges() {
		CheckMultipleDomainRange check = new CheckMultipleDomainRange();

		List<Failure> failures = check.execute(new File(this.getClass().getClassLoader()
				.getResource("CheckMultipleDomainRange/multiple_domain_01.xml").getFile()), "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(1);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple domains, ranges or both",
				f.getText());
	}

	@Test
	void multipleDomainAndRange() {
		CheckMultipleDomainRange check = new CheckMultipleDomainRange();

		List<Failure> failures = check.execute(new File(this.getClass().getClassLoader()
				.getResource("CheckMultipleDomainRange/multiple_domain_range_01.ttl").getFile()), "");
		assertNotNull(failures);
		assertEquals(5, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(2);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(4);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(3);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has multiple domains, ranges or both",
				f.getText());
		f = failures.get(1);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				f.getFailureElement());
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has multiple domains, ranges or both",
				f.getText());
	}
	
	@Test
	void singleDomainRange() {
		fail();
	}
	
	@Test
	void intersectionInsteadOfConjunction() {
		CheckMultipleDomainRange check = new CheckMultipleDomainRange();
		fail();
//		List<Failure> failures = check.execute(model, "");
//		assertNotNull(failures);
//		assertEquals(0, failures.size());
	}

}
