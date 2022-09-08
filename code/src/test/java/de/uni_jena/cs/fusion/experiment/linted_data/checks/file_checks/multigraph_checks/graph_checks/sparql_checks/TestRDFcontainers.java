package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckRDFcontainers;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestRDFcontainers {
	
	/**
	 * creates a model from the text and perform the CheckRDFcontainers on it
	 * 
	 * @param modelText the text that will be parsed into the model
	 * @return the failures that occured during the test
	 */
	private List<Failure> init(String modelText){
		CheckRDFcontainers check = new CheckRDFcontainers();
		Model model = ModelFactory.createDefaultModel();
		File file = null;
		try {
			file = File.createTempFile("temp", ".ttl");
			FileWriter myWriter = new FileWriter(file.getCanonicalFile());
			myWriter.write(modelText);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		InputStream in = RDFDataMgr.open(file.toString());
		model.read(in, null, "TTL");
		return check.execute(model, "");
	}

	@Test
	public void duplicatedIndex() {
		String modelText = ""
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:a a rdf:Bag;\n" 
				+ "    rdf:_1 ex:b1 ;\n" 
				+ "    rdf:_1 ex:b2 .";
		
		List<Failure> failures = init(modelText);
		
		assertNotNull(failures);
		assertEquals(failures.size(), 2);
		Failure failure1 = failures.get(0);
		Failure failure2 = failures.get(1);
		assertEquals(failure1.getFailureElement(), "http://example.org/b1");
		assertEquals(failure2.getFailureElement(), "http://example.org/b2");
		assertEquals(failure1.getSeverity(), Severity.WARN);
		assertEquals(failure2.getSeverity(), Severity.WARN);
	}
	
	@Test
	public void missingPredecessor() {
		String modelText = "" 
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" 
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:a a rdf:Alt;\n" 
				+ "    rdf:_2 ex:a1 ;\n" 
				+ "    rdf:_3 ex:a2 .\n";
		List<Failure> failures = init(modelText);
		assertNotNull(failures);
		assertEquals(failures.size(), 1);
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "http://example.org/a1");
		assertEquals(failure.getSeverity(), Severity.WARN);
		
		modelText = "" 
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" 
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:a a rdf:Alt;\n" 
				+ "    rdf:_2 ex:a1 ;\n" 
				+ "    rdf:_3 ex:a2 ;\n"
				+ "    rdf:_5 ex:a3 .";
		failures = init(modelText);
		assertNotNull(failures);
		assertEquals(failures.size(), 2);
		Failure failure1 = failures.get(0);
		Failure failure2 = failures.get(1);
		assertEquals(failure1.getFailureElement(), "http://example.org/a1");
		assertEquals(failure2.getFailureElement(), "http://example.org/a3");
		assertEquals(failure1.getSeverity(), Severity.WARN);
		assertEquals(failure2.getSeverity(), Severity.WARN);
	}
	
	@Test
	public void illegalIndex() {
		String modelText = "" 
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" 
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:d a rdf:Seq;\n"
				+ "rdf:_0 ex:d1 ;\n"
				+ "rdf:_1 ex:d2 .\n";
		List<Failure> failures = init(modelText);
		assertNotNull(failures);
		assertEquals(failures.size(), 1);
		Failure failure = failures.get(0);
		assertEquals(failure.getFailureElement(), "http://example.org/d1");
		assertEquals(failure.getSeverity(), Severity.WARN);
		
		modelText = "" 
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" 
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:d a rdf:Seq;\n"
				+ "rdf:_-1 ex:d2 .\n";
		failures = init(modelText);
		assertNotNull(failures);
		assertEquals(failures.size(), 1);
		Failure failure1 = failures.get(0);
		assertEquals(failure1.getFailureElement(), "http://example.org/d2");
		assertEquals(failure1.getSeverity(), Severity.WARN);
	}
	
	@Test
	public void validContainers() {
		String modelText = ""
				+ "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" 
				+ "@prefix ex: <http://example.org/>\n" + "\n" 
				+ "ex:a a rdf:Seq;\n" 
				+ "    rdf:_1 ex:a1 ;\n" 
				+ "    rdf:_2 ex:b2 .\n"
				+ "ex:b a rdf:Bag; \n"
				+ "    rdf:_1 ex:b1 ;\n"
				+ "    rdf:_2 ex:b2 ;\n"
				+ "    rdf:_3 ex:b3 .\n"
				+ "ex:c a rdf:Alt;\n" 
				+ "    rdf:_1 ex:c1 ."
				+ "ex:d a rdf:Seq; \n" 
				+ "    rdf:_1 ex:d1 ; \n"
				+ "    rdf:_2 ex:d2 .";
		List<Failure> failures = init(modelText);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);
	}
	
}
