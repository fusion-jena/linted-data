package de.uni_jena.cs.fusion.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckPropertyHasMissingDomainRangeDefinition;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class TestPropertyHasMissingDomainRangeDefinition {

	private CheckPropertyHasMissingDomainRangeDefinition check = new CheckPropertyHasMissingDomainRangeDefinition();

	/**
	 * each property has domain and range defined
	 */
	@Test
	public void definedDomainRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/domain_range_defined.ttl",
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
				.executeCheck("CheckPropertyHasDomainRangeDefinition/domain_missing_range_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/domain_missing_range_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/domain_missing_range_03.ttl", check);
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
				.executeCheck("CheckPropertyHasDomainRangeDefinition/range_missing_domain_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/range_missing_domain_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/range_missing_domain_03.ttl", check);
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
				.executeCheck("CheckPropertyHasDomainRangeDefinition/missing_domain_range_01.rdf", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain and range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/missing_domain_range_02.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain and range defined",
				failure.getText());

		failures = TestUtil.executeCheck("CheckPropertyHasDomainRangeDefinition/missing_domain_range_03.ttl", check);
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
