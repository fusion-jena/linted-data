package de.uni_jena.cs.fusion.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRDFcontainers;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class TestRDFcontainers {

	private CheckRDFcontainers check = new CheckRDFcontainers();

	/**
	 * an index is used twice
	 */
	@Test
	public void duplicatedIndex() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRDFcontainers/duplicatedIndex.ttl", check);

		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org/b1",
				"\nModel: Default Model\nDuplicated Index in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_1 http://example.org/b1",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org/b2",
				"\nModel: Default Model\nDuplicated Index in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_1 http://example.org/b2",
				Severity.WARN));
	}

	/**
	 * an element doesn't have a predecessor
	 */
	@Test
	public void missingPredecessor() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRDFcontainers/missingPredecessor_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/a1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_2 http://example.org/a1",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRDFcontainers/missingPredecessor_02.ttl", check);
		assertNotNull(failures);
		assertEquals(failures.size(), 2);
		assertTrue(TestUtil.contains(failures, "http://example.org/a1",
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_2 http://example.org/a1",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org/a3",
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_5 http://example.org/a3",
				Severity.WARN));
	}

	/**
	 * an element uses an illegal index -> a number <= 0
	 */
	@Test
	public void illegalIndex() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRDFcontainers/illegalIndex_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/d1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nIllegal Index in http://example.org/d http://www.w3.org/1999/02/22-rdf-syntax-ns#_0 http://example.org/d1",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRDFcontainers/illegalIndex_02.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://example.org/d2", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nIllegal Index in http://example.org/d http://www.w3.org/1999/02/22-rdf-syntax-ns#_-1 http://example.org/d2",
				failure.getText());
	}

	/**
	 * all indices are valid
	 */
	@Test
	public void validContainers() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRDFcontainers/validContainers.ttl", check);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);
	}

}
