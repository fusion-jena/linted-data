package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-5]",
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
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has the 3 defined ranges [http://www.w3.org/2001/XMLSchema#boolean, http://www.w3.org/2001/XMLSchema#int, http://www.w3.org/2001/XMLSchema#integer]",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]",
				Severity.WARN));
	}

	/**
	 * Properties that have multiple domains and ranges defined
	 */
	@Test
	void multipleDomainAndRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/multiple_domain_range_01.ttl", check);
		assertNotNull(failures);
		assertEquals(6, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4] and the 3 defined ranges [http://www.w3.org/2001/XMLSchema#boolean, http://www.w3.org/2001/XMLSchema#int, http://www.w3.org/2001/XMLSchema#integer]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3] and the 2 defined ranges [http://www.w3.org/2001/XMLSchema#integer, http://www.w3.org/2001/XMLSchema#short]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-8",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-8 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4] and the 2 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
	}

	@Test
	void singleDomainRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/single_domain_range_01.xml", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	void intersectionInsteadOfConjunction() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckMultipleDomainRange/intersection_domain_range_01.xml",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

}
