package de.uni_jena.cs.fusion.linted_data.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class CheckRdfsPropertyHasMissingDomainRangeDefinitionTest {

	private CheckRdfsPropertyHasMissingDomainRangeDefinition check = new CheckRdfsPropertyHasMissingDomainRangeDefinition();

	/**
	 * each property has domain and range defined
	 */
	@Test
	public void definedDomainRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/domainAndRangeDefined.ttl",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * each property has a defined domain, but no range definition
	 */
	@Test
	public void definedDomainMissingRange() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/domainDefinedMissingRange_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/domainDefinedMissingRange_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/domainDefinedMissingRange_03.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no range defined",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no range defined",
				Severity.WARN));
	}

	/**
	 * each property has a defined range but no domain definition
	 */
	@Test
	public void definedRangeMissingDomain() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/rangeDefinedMissingDomain_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/rangeDefinedMissingDomain_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/rangeDefinedMissingDomain_03.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain defined",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no domain defined",
				Severity.WARN));
	}

	/**
	 * each property is missing domain and range definition
	 */
	@Test
	public void missingDomainRange() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/missingDomainAndRange_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain and range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/missingDomainAndRange_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain and range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfsPropertyHasMissingDomainRangeDefinition/missingDomainAndRange_03.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain and range defined"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no domain and range defined"));
	}

}
