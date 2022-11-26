package de.uni_jena.cs.fusion.linted_data.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class CheckRdfPrefixesReferToOneNamespaceTest {

	private CheckRdfPrefixesReferToOneNamespace check = new CheckRdfPrefixesReferToOneNamespace();

	/**
	 * defined multiple namespaces, each with its own prefix in turtle format
	 */
	@Test
	public void turtleOneNamespacePerPrefix() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/turtleOneNamespacePerPrefix_01.ttl", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * defined multiple namespaces, some with the same prefix in turtle format
	 */
	@Test
	public void turtleMultipleNamespacesPerPrefix() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/turtleMultipleNamespacesPerPrefix_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(f.getFailureElement(), "abc");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\nabc has the 2 namespaces: [http://www.semanticweb.org/abc#, http://www.semanticweb.org/def#]");

		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/turtleMultipleNamespacesPerPrefix_02.ttl",
				check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals(f.getFailureElement(), "def");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\ndef has the 3 namespaces: [http://www.semanticweb.org/def#, http://www.semanticweb.org/jkl#, http://www.semanticweb.org/mno#]");
	}

	/*
	 * defined a multiple namespaces, some of them occur multiple times with the
	 * same IRI and some with different IRIs, in turtle format
	 */
	@Test
	public void turtleOnePrefixOneNamespaceMultipleTimes() throws Exception {
		// one prefix -> same IRI multiple times
		List<Failure> failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/turtleOnePrefixOneNamespaceMultipleTimes_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("test1", f.getFailureElement());
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals(f.getText(), "\ntest1 has 2 times the namespace http://www.test-1.org/o#");

		// one prefix -> same IRI multiple times + multiple IRIs
		failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/turtleOnePrefixOneNamespaceMultipleTimes_02.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "test1", "\ntest1 has 2 times the namespace http://www.test-1.org/o#",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "test1",
				"\ntest1 has the 2 namespaces: [http://www.test-1.org/o#, http://www.test-2.org/o#]", Severity.WARN));

		// prefix a -> same IRI multiple times
		// prefix b -> multiple IRIs
		failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/turtleOnePrefixOneNamespaceMultipleTimes_03.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "test1", "\ntest1 has 2 times the namespace http://www.test-1.org/o#",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "test2",
				"\ntest2 has the 2 namespaces: [http://www.test-2.com/onto#, http://www.test-2.org/o#]",
				Severity.WARN));
	}

	/**
	 * defined multiple namespaces, each with its own prefix in TriG format
	 */
	@Test
	public void trigOneNamespacePerPrefix() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/trigOneNamespacePerPrefix_01.trig", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/*
	 * defined a multiple namespaces, some of them occur multiple times with the
	 * same IRI and some with different IRIs, in TriG format
	 */
	@Test
	public void trigMultipleNamespacesPerPrefix() throws Exception {
		// one prefix, different IRIs
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/trigMultipleNamespacesPerPrefix_01.trig", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "dc");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\ndc has the 2 namespaces: [http://purl.org/dc/terms/, http://purl.org/test/syntax#]");

		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/trigMultipleNamespacesPerPrefix_02.trig",
				check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "ex");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nex has the 2 namespaces: [http://www.2ndexample.org/document#, http://www.example.org/vocabulary#]");

		// one prefix refering multiple times to the same IRI and to at least one
		// different
		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/trigMultipleNamespacesPerPrefix_03.trig",
				check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "ex",
				"\nex has the 2 namespaces: [http://www.2ndexample.org/document#, http://www.example.org/vocabulary#]",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "ex",
				"\nex has 2 times the namespace http://www.example.org/vocabulary#", Severity.INFO));
	}

	/**
	 * defined multiple namespaces, each with its own prefix in JSONLD(10,11) format
	 */
	@Test
	public void jsonldOneNamespacePerPrefix() throws Exception {
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/jsonldOneNamespacePerPrefix_01.jsonld", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/jsonldOneNamespacePerPrefix_02.jsonld10",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/jsonldOneNamespacePerPrefix_03.jsonld11",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = TestUtil.executeCheck("CheckRdfPrefixesReferToOneNamespace/jsonldOneNamespacePerPrefix_04.jsonld",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/*
	 * defined a multiple namespaces, some of them occur multiple times with the
	 * same IRI and some with different IRIs, in JSONLD(10,11) format
	 */
	@Test
	public void jsonldMultipleNamespacesPerPrefix() throws Exception {
		// one prefix multiple namespaces
		List<Failure> failures = TestUtil
				.executeCheck("CheckRdfPrefixesReferToOneNamespace/jsonldMultipleNamespacesPerPrefix_01.jsonld", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foaf");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), "\nfoaf has the 2 namespaces: [http://foo.test#, http://xmlns.com/foaf/0.1/]");

		failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/jsonldMultipleNamespacesPerPrefix_02.jsonld10", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), "\nfoo has the 2 namespaces: [http://foo.bar/, http://onto-foo.bar#]");

		failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/jsonldMultipleNamespacesPerPrefix_03.jsonld11", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "foaf",
				"\nfoaf has the 2 namespaces: [http://foo.bar2#, http://xmlns.com/foaf/0.1/]", Severity.WARN));
		assertTrue(TestUtil.contains(failures, "foo",
				"\nfoo has the 2 namespaces: [http://foo.bar/, http://onto-foo.bar#]", Severity.WARN));

		// one prefix -> multiple namespaces
		// one prefix -> multiple times the same namespace
		failures = TestUtil.executeCheck(
				"CheckRdfPrefixesReferToOneNamespace/jsonldMultipleNamespacesPerPrefix_04.jsonld11", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "foo", "\nfoo has 3 times the namespace http://onto-foo.bar#",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "foaf",
				"\nfoaf has the 2 namespaces: [http://foo.bar2#, http://xmlns.com/foaf/0.1/]", Severity.WARN));
	}

}
