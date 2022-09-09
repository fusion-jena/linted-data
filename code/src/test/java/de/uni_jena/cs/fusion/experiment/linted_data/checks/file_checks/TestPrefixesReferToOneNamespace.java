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

	@Test
	public void TurtleOneNamespacePerPrefix() throws FileNotFoundException {
		File file = null;
		try {
			file = File.createTempFile("temp", ".ttl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(file);
		assertTrue(writeToFile(
				" #hello PREFIX abc:<http://www.semanticweb.org/abc#>\n PREFIX abc:<http://www.semanticweb.org/abc#>\n@prefix def:<http://www.semanticweb.org/def#> .",
				file));
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	public void TurtleMultipleNamespacesPerPrefix() throws FileNotFoundException {
		File file = null;
		try {
			file = File.createTempFile("temp", ".ttl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(file);
		assertTrue(writeToFile(
				"PREFIX abc:<http://www.semanticweb.org/abc#>\n@prefix abc:<http://www.semanticweb.org/def#> . # this is a text for testing\nPREFIX ghi:<http://www.semanticweb.org/ghi#>\n@prefix jkl:<http://www.semanticweb.org/jkl#> .\n#this is nothing",
				file));
		CheckPrefixesReferToOneNamespace check = new CheckPrefixesReferToOneNamespace();
		List<Failure> failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(f.getFailureElement(), "abc");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\nabc has the 2 namespaces: [http://www.semanticweb.org/abc#, http://www.semanticweb.org/def#]");

		assertNotNull(file);
		assertTrue(writeToFile(
				"PREFIX abc:<http://www.semanticweb.org/abc#>   #this is an unnecessary comment\n@prefix def:<http://www.semanticweb.org/def#>\nPREFIX ghi:<http://www.semanticweb.org/ghi#>\n@prefix def : <http://www.semanticweb.org/jkl#>\n\n@prefix def : <http://www.semanticweb.org/mno#>",
				file));
		failures = check.execute(file, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals(f.getFailureElement(), "def");
		assertEquals(f.getSeverity(), Severity.WARN);
		assertEquals(f.getText(),
				"\ndef has the 3 namespaces: [http://www.semanticweb.org/def#, http://www.semanticweb.org/jkl#, http://www.semanticweb.org/mno#]");
	}

}
