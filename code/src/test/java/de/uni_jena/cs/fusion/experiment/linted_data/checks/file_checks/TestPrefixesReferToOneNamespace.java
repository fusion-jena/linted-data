package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

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
	 */
	private List<Failure> executeCheck(String fileExtension, String text) throws FileNotFoundException {
		File file = null;
		try {
			file = File.createTempFile("temp", fileExtension);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(file);
		assertTrue(writeToFile(text, file));
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		return check.execute(file, "");
	}

	@Test
	public void TurtleOneNamespacePerPrefix() throws FileNotFoundException {
		List<Failure> failures = executeCheck(".ttl",
				" #hello PREFIX abc:<http://www.semanticweb.org/abc#>\n PREFIX abc:<http://www.semanticweb.org/abc#>\n@prefix def:<http://www.semanticweb.org/def#> .");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	public void TurtleMultipleNamespacesPerPrefix() throws FileNotFoundException {
		List<Failure> failures = executeCheck(".ttl",
				"PREFIX abc:<http://www.semanticweb.org/abc#>\n@prefix abc:<http://www.semanticweb.org/def#> . # this is a text for testing\nPREFIX ghi:<http://www.semanticweb.org/ghi#>\n@prefix jkl:<http://www.semanticweb.org/jkl#> .\n#this is nothing");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(f.getFailureElement(), "abc");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\nabc has the 2 namespaces: [http://www.semanticweb.org/abc#, http://www.semanticweb.org/def#]");

		failures = executeCheck(".ttl",
				"PREFIX abc:<http://www.semanticweb.org/abc#>   #this is an unnecessary comment\n@prefix def:<http://www.semanticweb.org/def#>\nPREFIX ghi:<http://www.semanticweb.org/ghi#>\n@prefix def : <http://www.semanticweb.org/jkl#>\n\n@prefix def : <http://www.semanticweb.org/mno#>");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals(f.getFailureElement(), "def");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\ndef has the 3 namespaces: [http://www.semanticweb.org/def#, http://www.semanticweb.org/jkl#, http://www.semanticweb.org/mno#]");
	}

	@Test
	public void TriG_oneNamespacePerPrefix() throws FileNotFoundException{
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
	public void TriG_MultipleNamespacesPerPrefix() throws FileNotFoundException{
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
	}

	@Test
	public void N3_oneNamespacePerPrefix() throws FileNotFoundException{
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
	public void N3_MultipleNamespacesPerPrefix() throws FileNotFoundException{
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

}
