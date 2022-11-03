package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestPrefixesReferToOneNamespace {

	private List<Failure> executeCheck(String path) throws IOException, URISyntaxException {
		File file = new File(this.getClass().getClassLoader().getResource(path).toURI());
		Dataset dataset = DatasetFactory.create();
		try {
			RDFParser.source(file.getAbsolutePath()).parse(dataset);
		} catch (RiotException e) {
			fail("Error when parsing " + path);
		}
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		return check.execute(file, "");
	}

	/**
	 * defined multiple namespaces, each with its own prefix in turtle format
	 */
	@Test
	public void TurtleOneNamespacePerPrefix() throws URISyntaxException, IOException {
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_OneNamespacePerPrefix_01.ttl");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * defined multiple namespaces, some with the same prefix in turtle format
	 */
	@Test
	public void TurtleMultipleNamespacesPerPrefix() throws IOException, URISyntaxException {
		List<Failure> failures = executeCheck(
				"CheckPrefixesReferToOneNamespace/Turtle_MultipleNamespacesPerPrefix_01.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(f.getFailureElement(), "abc");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\nabc has the 2 namespaces: [http://www.semanticweb.org/abc#, http://www.semanticweb.org/def#]");

		failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_MultipleNamespacesPerPrefix_02.ttl");
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
	public void TurtleOnePrefixOneNamespaceMultipleTimes() throws IOException, URISyntaxException {
		// one prefix -> same IRI multiple times
		List<Failure> failures = executeCheck(
				"CheckPrefixesReferToOneNamespace/Turtle_OnePrefixOneNamespaceMultipleTimes_01.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("test1", f.getFailureElement());
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals(f.getText(), "\ntest1 has 2 times the namespace http://www.test-1.org/o#");

		// one prefix -> same IRI multiple times + multiple IRIs
		failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_OnePrefixOneNamespaceMultipleTimes_02.ttl");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "test1", "\ntest1 has 2 times the namespace http://www.test-1.org/o#",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "test1",
				"\ntest1 has the 2 namespaces: [http://www.test-1.org/o#, http://www.test-2.org/o#]", Severity.WARN));

		// prefix a -> same IRI multiple times
		// prefix b -> multiple IRIs
		failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_OnePrefixOneNamespaceMultipleTimes_03.ttl");
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
	public void TriG_oneNamespacePerPrefix() throws IOException, URISyntaxException {
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/TriG_oneNamespacePerPrefix_01.trig");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/*
	 * defined a multiple namespaces, some of them occur multiple times with the
	 * same IRI and some with different IRIs, in TriG format
	 */
	@Test
	public void TriG_MultipleNamespacesPerPrefix() throws IOException, URISyntaxException {
		// one prefix, different IRIs
		List<Failure> failures = executeCheck(
				"CheckPrefixesReferToOneNamespace/TriG_MultipleNamespacesPerPrefix_01.trig");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "dc");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\ndc has the 2 namespaces: [http://purl.org/dc/terms/, http://purl.org/test/syntax#]");

		failures = executeCheck("CheckPrefixesReferToOneNamespace/TriG_MultipleNamespacesPerPrefix_02.trig");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "ex");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nex has the 2 namespaces: [http://www.2ndexample.org/document#, http://www.example.org/vocabulary#]");

		// one prefix refering multiple times to the same IRI and to at least one
		// different
		failures = executeCheck("CheckPrefixesReferToOneNamespace/TriG_MultipleNamespacesPerPrefix_03.trig");
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
	public void JSONLD_oneNamespacePerPrefix() throws URISyntaxException, IOException {
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_oneNamespacePerPrefix_01.jsonld");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_oneNamespacePerPrefix_02.jsonld10");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_oneNamespacePerPrefix_03.jsonld11");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_oneNamespacePerPrefix_04.jsonld");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/*
	 * defined a multiple namespaces, some of them occur multiple times with the
	 * same IRI and some with different IRIs, in JSONLD(10,11) format
	 */
	@Test
	public void JSONLD_multipleNamespacesPerPrefix() throws URISyntaxException, IOException {
		// one prefix multiple namespaces
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_multipleNamespacesPerPrefix_01.jsonld");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foaf");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), "\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.test#]");

		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_multipleNamespacesPerPrefix_02.jsonld10");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), "\nfoo has the 2 namespaces: [http://onto-foo.bar#, http://foo.bar/]");

		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_multipleNamespacesPerPrefix_03.jsonld11");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "foaf",
				"\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.bar2#]", Severity.WARN));
		assertTrue(TestUtil.contains(failures, "foo",
				"\nfoo has the 2 namespaces: [http://onto-foo.bar#, http://foo.bar/]", Severity.WARN));

		// one prefix -> multiple namespaces
		// one prefix -> multiple times the same namespace
		failures = executeCheck("CheckPrefixesReferToOneNamespace/JSONLD_multipleNamespacesPerPrefix_04.jsonld11");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "foo", "\nfoo has 3 times the namespace http://onto-foo.bar#",
				Severity.INFO));
		assertTrue(TestUtil.contains(failures, "foaf",
				"\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.bar2#]", Severity.WARN));
	}

}
