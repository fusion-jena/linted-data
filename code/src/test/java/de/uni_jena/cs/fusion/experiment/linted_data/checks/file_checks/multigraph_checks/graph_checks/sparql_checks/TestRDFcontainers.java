package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRDFcontainers;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestRDFcontainers {

	private CheckRDFcontainers check = new CheckRDFcontainers();

	/**
	 * an index is used twice
	 */
	@Test
	public void duplicatedIndex() {
		List<Failure> failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/duplicatedIndex.ttl").getFile()), "");

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
	public void missingPredecessor() {
		List<Failure> failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/missingPredecessor_01.ttl").getFile()),
				"");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/a1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_2 http://example.org/a1",
				failure.getText());

		failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/missingPredecessor_02.ttl").getFile()),
				"");
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
	public void illegalIndex() {
		List<Failure> failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/illegalIndex_01.ttl").getFile()), "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/d1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nIllegal Index in http://example.org/d http://www.w3.org/1999/02/22-rdf-syntax-ns#_0 http://example.org/d1",
				failure.getText());

		failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/illegalIndex_02.ttl").getFile()), "");
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
	public void validContainers() {
		List<Failure> failures = check.execute(new File(
				this.getClass().getClassLoader().getResource("CheckRDFcontainers/validContainers.ttl").getFile()), "");
		assertNotNull(failures);
		assertEquals(failures.size(), 0);
	}

}
