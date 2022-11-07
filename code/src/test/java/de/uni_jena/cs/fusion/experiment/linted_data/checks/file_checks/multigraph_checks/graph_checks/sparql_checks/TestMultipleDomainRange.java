package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckMultipleDomainRange;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestMultipleDomainRange {

	private CheckMultipleDomainRange check = new CheckMultipleDomainRange();

	/**
	 * properties that have more than one defined domain
	 */
	@Test
	void multipleDomain() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/multiple_domain_01.xml", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has multiple domains defined",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple domains defined",
				Severity.WARN));
	}

	/**
	 * properties that have more than one defined range
	 */
	@Test
	void multipleRanges() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/multiple_range_01.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has multiple ranges defined",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple ranges defined",
				Severity.WARN));
	}

	/**
	 * Properties that have multiple domains and ranges defined
	 */
	@Test
	void multipleDomainAndRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/multiple_domain_range_01.ttl", check);
		assertNotNull(failures);
		assertEquals(5, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3 has multiple domains defined"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2 has multiple domains defined"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has multiple ranges defined"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has multiple domains and ranges defined"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has multiple domains and ranges defined"));
	}

	@Test
	void singleDomainRange() {
		fail();
	}

	@Test
	void intersectionInsteadOfConjunction() {
		fail();
//		List<Failure> failures = check.execute(model, "");
//		assertNotNull(failures);
//		assertEquals(0, failures.size());
	}

}
