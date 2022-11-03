package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
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

	private boolean writeToFile(String text, File file) {
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(text);
			myWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * creates a file of the specified type and writes the content to it executes
	 * CheckPrefixesReferToOneNamespace on the file and returns its failures
	 * 
	 * @param fileExtension type of the file
	 * @param text          content of the file
	 * @return failures occured during the execution of
	 *         CheckPrefixesReferToOneNamespace
	 * @throws IOException
	 */
	private List<Failure> executeCheck(String fileExtension, String text) throws IOException {
		File file = null;
		try {
			file = File.createTempFile("temp", fileExtension);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(file);
		assertTrue(writeToFile(text, file));
		Dataset dataset = DatasetFactory.create();
		try {
			RDFParser.source(file.getAbsolutePath()).parse(dataset);
		} catch (RiotException e) {
			fail("Error when parsing the text\n" + text);
		}
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		return check.execute(file, "");
	}

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
	public void TurtleOneNamespacePerPrefix() throws  URISyntaxException, IOException {
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_OneNamespacePerPrefix_01.ttl");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	/**
	 * defined multiple namespaces, some with the same prefix in turtle format
	 */
	@Test
	public void TurtleMultipleNamespacesPerPrefix() throws IOException, URISyntaxException {
		List<Failure> failures = executeCheck("CheckPrefixesReferToOneNamespace/Turtle_MultipleNamespacesPerPrefix_01.ttl");
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

	@Test
	public void TriG_oneNamespacePerPrefix() throws IOException{
		String text = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
				"PREFIX dc: <http://purl.org/dc/terms/> \n" + 
				"@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" + 
				"# PREFIX foaf: <http://xmlns.com/foaf/5/> \n" + 
				"\n" + 
				"# default graph\n" + 
				"    {\n" + 
				"      <http://example.org/bob> dc:publisher \"Bob\" .\n" + 
				"      <http://example.org/alice> dc:publisher \"Alice\" .\n" + 
				"    }\n" + 
				"\n" + 
				"<http://example.org/bob>\n" + 
				"    {\n" + 
				"       _:a foaf:name \"Bob\" .\n" + 
				"       _:a foaf:mbox <mailto:bob@oldcorp.example.org> .\n" + 
				"       _:a foaf:knows _:b .\n" + 
				"    }";
		

		List<Failure> failures = executeCheck(".trig", text);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	public void TriG_MultipleNamespacesPerPrefix() throws IOException, URISyntaxException{
		String text = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" + 
				"PREFIX dc: <http://purl.org/dc/terms/> \n" +
				"@prefix dc: <http://purl.org/test/syntax#>           .  #this is a comment at the end of a line\n" +
				"@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" + 
				"          # @prefix foaf: <http://xmlns.com/foaf/5/> .\n" + 
				"\n" + 
				"# default graph\n" + 
				"    {\n" + 
				"      <http://example.org/bob> dc:publisher \"Bob\" .\n" + 
				"      <http://example.org/alice> dc:publisher \"Alice\" .\n" + 
				"    }\n" + 
				"\n" + 
				"<http://example.org/bob>\n" + 
				"    {\n" + 
				"       _:a foaf:name \"Bob\" .\n" + 
				"       _:a foaf:mbox <mailto:bob@oldcorp.example.org> .\n" + 
				"       _:a foaf:knows _:b .\n" + 
				"    }";
		

		List<Failure> failures = executeCheck(".trig", text);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "dc");
		assertEquals(failure.getSeverity(), Severity.WARN);
		text = "# This document encodes one graph.\n" + 
				"@prefix ex: <http://www.example.org/vocabulary#> .\n" + 
				"@prefix : <http://www.example.org/exampleDocument#> .\n" + 
				"\n" + 
				":G1 { :Monica a ex:Person ;\n" + 
				"              ex:name \"Monica Murphy\" ;\n" + 
				"              ex:homepage <http://www.monicamurphy.org> ;\n" + 
				"              ex:email <mailto:monica@monicamurphy.org> ;\n" + 
				"              ex:hasSkill ex:Management ,\n" + 
				"                          ex:Programming . }\n" +
				"PREFIX ex: <http://www.2ndexample.org/document#> \n";
		assertEquals(failure.getText(),
				"\ndc has the 2 namespaces: [http://purl.org/dc/terms/, http://purl.org/test/syntax#]");

		text = "# This document encodes one graph.\n" + "@prefix ex: <http://www.example.org/vocabulary#> .\n"
				+ "@prefix : <http://www.example.org/exampleDocument#> .\n" + "\n" + ":G1 { :Monica a ex:Person ;\n"
				+ "              ex:name \"Monica Murphy\" ;\n"
				+ "              ex:homepage <http://www.monicamurphy.org> ;\n"
				+ "              ex:email <mailto:monica@monicamurphy.org> ;\n"
				+ "              ex:hasSkill ex:Management ,\n" + "                          ex:Programming . }\n"
				+ "PREFIX ex: <http://www.2ndexample.org/document#> \n";
		failures = executeCheck(".trig", text);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "ex");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nex has the 2 namespaces: [http://www.2ndexample.org/document#, http://www.example.org/vocabulary#]");
	
		failures = executeCheck("TriG_MultipleNamespacesPerPrefix_01.trig");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(1);
		assertEquals(failure.getFailureElement(), "ex");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nex has the 2 namespaces: [http://www.2ndexample.org/document#, http://www.example.org/vocabulary#]");
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "ex");
		assertEquals(failure.getSeverity(), Severity.INFO);
		assertEquals(failure.getText(),
				"\nex has 2 times the namespace http://www.example.org/vocabulary#");
	}

	@Test
	public void N3_oneNamespacePerPrefix() throws IOException{
		String text = "@prefix my: <http://my.ontology#> . \n" + 
			"@prefix : <n3_notation#> .\n" +
			"my:Peter :suffers my:acrophobia.\n";
		List<Failure> failures = executeCheck(".n3", text);
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		text = "  @prefix my: <http://my.ontology#> . \n" + 
				"@prefix apple : <n3_notation#>.\n" +
				"my:Peter apple:suffers my:acrophobia.                 \n" +
				"    my:foo apple:bar my:alice.\n";
		failures = executeCheck(".n3", text);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void N3_MultipleNamespacesPerPrefix() throws IOException{
		String text = "@prefix my: <http://my.ontology#> . \n" + 
			"@prefix : <n3_notation#> .\n" +
			"@prefix my: <http://test.org/notation/> .\n" +
			"my:Peter :suffers my:acrophobia.\n";
		List<Failure> failures = executeCheck(".n3", text);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "my");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nmy has the 2 namespaces: [http://my.ontology#, http://test.org/notation/]");
		
		text = "@prefix my: <http://my.ontology#> . \n" + 
				"@prefix apple : <http://my_second.ontology#>.\n" +
				"@prefix test_ns : <http://my_third.ontology#>.\n" +
				"@prefix foo : <http://my_last.ontology/>.\n" +
				"@prefix apple : <http://my_third.ontology#>.      \n" +
				"@prefix foo : <http://foo.ontology#>.\n" +
				"my:Peter apple:suffers my:acrophobia.                 \n" +
				"my:foo apple:bar my:alice.\n" +
				"test_ns:s foo:bar apple:o .";
		failures = executeCheck(".n3", text);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "apple");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\napple has the 2 namespaces: [http://my_second.ontology#, http://my_third.ontology#]");
		failure = failures.get(1);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(),
				"\nfoo has the 2 namespaces: [http://my_last.ontology/, http://foo.ontology#]");
	}
	
	@Test
	public void JSONLD_oneNamespacePerPrefix() throws URISyntaxException, IOException{
		List<Failure> failures =  executeCheck("JSONLD_oneNamespacePerPrefix_01.jsonld");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures =  executeCheck("JSONLD_oneNamespacePerPrefix_02.jsonld10");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		failures =  executeCheck("JSONLD_oneNamespacePerPrefix_03.jsonld11");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		failures =  executeCheck("JSONLD_oneNamespacePerPrefix_04.jsonld");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void JSONLD_multipleNamespacesPerPrefix() throws URISyntaxException, IOException { 
		List<Failure> failures =  executeCheck("JSONLD_multipleNamespacesPerPrefix_01.jsonld");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foaf");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), 
				"\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.test#]");
		
		failures =  executeCheck("JSONLD_multipleNamespacesPerPrefix_02.jsonld10");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), 
				"\nfoo has the 2 namespaces: [http://onto-foo.bar#, http://foo.bar/]");
		
		failures =  executeCheck("JSONLD_multipleNamespacesPerPrefix_03.jsonld11");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(1);
		assertEquals(failure.getFailureElement(), "foaf");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), 
				"\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.bar2#]");
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), 
				"\nfoo has the 2 namespaces: [http://onto-foo.bar#, http://foo.bar/]");
		failures =  executeCheck("JSONLD_multipleNamespacesPerPrefix_04.jsonld11");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "foo");
		assertEquals(failure.getSeverity(), Severity.INFO);
		assertEquals("\nfoo has 3 times the namespace http://onto-foo.bar#", failure.getText());
		failure = failures.get(1);
		assertEquals(failure.getSeverity(), Severity.WARN);
		assertEquals(failure.getText(), 
				"\nfoaf has the 2 namespaces: [http://xmlns.com/foaf/0.1/, http://foo.bar2#]");
	}

	@Test
	public void RDFXML_onePrefixPerNamespace() throws URISyntaxException, IOException {
		List<Failure> failures = executeCheck("RDFXML_oneNamespacePerPrefix_01.owl");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		failures = executeCheck("RDFXML_oneNamespacePerPrefix_02.xml");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		failures = executeCheck("RDFXML_oneNamespacePerPrefix_03.xml");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void RDFXML_multipleNamespacesPerPrefix() throws URISyntaxException, IOException {
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		List<Failure> failures = check.execute(new File(this.getClass().getClassLoader().getResource("RDFXML_multipleNamespacesPerPrefix_01.owl").toURI()), "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals("ccon", failure.getFailureElement());
		assertEquals("\nccon has the 2 namespaces: [http://cerrado.linkeddata.es/ecology/ccon#, http://cerrado.linkeddata.es/ecology/ccon#2]",failure.getText());
		
		failures = check.execute(new File(this.getClass().getClassLoader().getResource("RDFXML_multipleNamespacesPerPrefix_02.owl").toURI()), "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals(failure.getText(), "\nbar has 3 times the namespace http://www.city.ac.uk/ds/inm713/bar#");
		assertEquals("bar", failure.getFailureElement());
		failure = failures.get(1);
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals("foo", failure.getFailureElement());
		assertEquals("\nfoo has the 2 namespaces: [http://www.city.ac.uk/ds/inm713/foo#, http://www.city.ac.uk/ds/inm713/foo2#]",failure.getText());
		
		failures = check.execute(new File(this.getClass().getClassLoader().getResource("RDFXML_multipleNamespacesPerPrefix_03.xml").toURI()), "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals(failure.getText(), "\nbar has 3 times the namespace http://www.city.ac.uk/ds/inm713/bar#");
		assertEquals("bar", failure.getFailureElement());
		failure = failures.get(1);
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals("bar", failure.getFailureElement());
		assertEquals("\nbar has the 2 namespaces: [http://www.city.ac.uk/ds/inm713/bar#, http://www.city.ac.uk/ds/inm713/bar2#]",failure.getText());
	}

}
