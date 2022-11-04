package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckPropertyHasMissingDomainRangeDefinition;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestPropertyHasMissingDomainRangeDefinition {

	private List<Failure> createCheck(String path) throws URISyntaxException {
		File file = new File(this.getClass().getClassLoader().getResource(path).toURI());
		Dataset dataset = DatasetFactory.create();
		try {
			RDFParser.source(file.getAbsolutePath()).parse(dataset);
		} catch (RiotException e) {
			fail("Error when parsing " + path);
		}
		CheckPropertyHasMissingDomainRangeDefinition check = new CheckPropertyHasMissingDomainRangeDefinition();
		return check.execute(file, "");
	}

	/**
	 * each property has domain and range defined
	 */
	@Test
	public void definedDomainRange() throws URISyntaxException {
		List<Failure> failures = createCheck("CheckPropertyHasDomainRangeDefinition/domain_range_defined.ttl");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * each property has a defined domain, but no range definition
	 */
	@Test
	public void definedDomainMissingRange() throws URISyntaxException {
		String path = "CheckPropertyHasDomainRangeDefinition/domain_missing_range_01.rdf";
		List<Failure> failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no range defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/domain_missing_range_02.jsonld";
		failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no range defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/domain_missing_range_03.ttl";
		failures = createCheck(path);
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
	public void definedRangeMissingDomain() throws URISyntaxException {
		String path = "CheckPropertyHasDomainRangeDefinition/range_missing_domain_01.rdf";
		List<Failure> failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/range_missing_domain_02.jsonld";
		failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/range_missing_domain_03.ttl";
		failures = createCheck(path);
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
	public void missingDomainRange() throws URISyntaxException {
		String path = "CheckPropertyHasDomainRangeDefinition/missing_domain_range_01.rdf";
		List<Failure> failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2",
				failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain and range defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/missing_domain_range_02.jsonld";
		failures = createCheck(path);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(),
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain and range defined",
				failure.getText());

		path = "CheckPropertyHasDomainRangeDefinition/missing_domain_range_03.ttl";
		failures = createCheck(path);
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
