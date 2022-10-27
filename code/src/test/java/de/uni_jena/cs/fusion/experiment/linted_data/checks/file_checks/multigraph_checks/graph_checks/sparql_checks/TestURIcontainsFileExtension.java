package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckURIcontainsFileExtension;

public class TestURIcontainsFileExtension {

	private Dataset dataset;
	private CheckURIcontainsFileExtension check;
	
	@BeforeEach
	private void initalisation() {
		dataset = DatasetFactory.createGeneral();
		check = new CheckURIcontainsFileExtension();
	}

	@Test
	public void fileExtensionInURIUnnamedModel() {
		List<String> uris = Arrays.asList("http://www.semanticweb.org/testontology.owl#",
				"http://www.semanticweb.org/test.ttl/ontology#", "http://www.semanticweb.org/test/onto.n3logy#",
				"http://www.semanticweb.org/test/ontology.rdfs#", 
				"http://www.semanticweb.org/test.ttl/ontology/.rdfxml");
		
		for (String uri : uris) {
			Model model = ModelFactory.createDefaultModel();
			Resource res = model.createResource("http://somewhere/JohnSmith.ttl");
			res.addLiteral(VCARD.FN, "John Smith");
			dataset.addNamedModel(uri, model);
			// precondition for the check
			assertTrue(dataset.containsNamedModel(uri));
			List<Failure> failures = check.execute(dataset, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			assertEquals(failures.get(0).getFailureElement(), uri);
			// clean the dataset for the next testcase
			dataset.removeNamedModel(uri);
			assertFalse(dataset.containsNamedModel(uri));
		}
	}
	
	@Test
	public void fileExtensionInURINamedModel() {
		List<String> uris = Arrays.asList("http://www.semanticweb.org/testontology.owl#",
				"http://www.semanticweb.org/test.ttl/ontology#graph", "http://www.semanticweb.org/test/onto.n3logy#",
				"http://www.semanticweb.org/test/ontology.rdfs#", 
				"http://www.semanticweb.org/test.ttl/ontology/.rdfxml");
		
		for (String uri : uris) {
			Model model = ModelFactory.createMemModelMaker().createModel(uri);
			Resource res = model.createResource("http://somewhere/JohnSmith");
			res.addLiteral(VCARD.FN, "John Smith");
			dataset.addNamedModel(uri, model);
			// precondition for the check
			assertTrue(dataset.containsNamedModel(uri));
			List<Failure> failures = check.execute(dataset, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			assertEquals(failures.get(0).getFailureElement(), uri);
			// clean the dataset for the next testcase
			dataset.removeNamedModel(uri);
			assertFalse(dataset.containsNamedModel(uri));
		}
	}
	
	/**
	 * create a model and use it as default graph
	 * 
	 * also add an named model with an uri that contains the file 
	 */
	@Test
	public void defaultGraphAndNamedGraphWithFileExtensionInURI() {
		List<String> uris = Arrays.asList("http://www.semanticweb.org/testontology.owl#",
				"http://www.semanticweb.org/test.ttl/ontology#graph", "http://www.semanticweb.org/test/onto.n3logy#",
				"http://www.semanticweb.org/test/ontology.rdfs#", 
				"http://www.semanticweb.org/test.ttl/ontology/.rdfxml");
		for (String uri : uris) {
			Model model = ModelFactory.createDefaultModel();
			Resource res = model.createResource("http://somewhere.owl/JohnSmith");
			res.addLiteral(VCARD.FN, "John Smith");
			dataset = DatasetFactory.create(model);
			dataset.addNamedModel(uri , ModelFactory.createDefaultModel());
			assertTrue(dataset.containsNamedModel(uri));
			List<Failure> failures = check.execute(dataset, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 1);
			assertEquals(failures.get(0).getFailureElement(), uri);
		}
	}
	
	@Test
	public void namedGraphsWithWithoutFileExtensionInURI() {
		List<String> uris = Arrays.asList("http://www.semanticweb.org/testontology#",
				"http://www.semanticweb.org/test/ontology#graph", "http://www.semanticweb.org/test/onto.logy#",
				"http://www.semanticweb.org/test/ontology#", 
				"http://www.semanticweb.org/test.csv/ontology/.txt");
		for (String uri : uris) {
			Model model = ModelFactory.createDefaultModel();
			Resource res = model.createResource("http://somewhere/JohnSmith");
			Property p = model.createProperty("http://somewhere.n3#name");
			res.addLiteral(p, "John Smith");
			dataset = DatasetFactory.create(model);
			dataset.addNamedModel(uri , ModelFactory.createDefaultModel());
			assertTrue(dataset.containsNamedModel(uri));
			List<Failure> failures = check.execute(dataset, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 0);
			// clean the dataset for the next testcase
			dataset.removeNamedModel(uri);
			assertFalse(dataset.containsNamedModel(uri));
		}
	}
	
	@Test
	public void fileExtensionInURIWithoutDot() {
		List<String> uris = Arrays.asList("http://www.semanticweb.org/testontologyowl#",
				"http://www.semanticweb.org/testtl/ontology#", "http://www.semanticweb.org/test/onton3logy#",
				"http://www.semanticweb.org/test/ontologyrdfs#", 
				"http://www.semanticweb.org/test/ontology/rdfxml");
		
		for (String uri : uris) {
			Model model = ModelFactory.createDefaultModel();
			dataset.addNamedModel(uri, model);
			// precondition for the check
			assertTrue(dataset.containsNamedModel(uri));
			List<Failure> failures = check.execute(dataset, "");
			assertNotNull(failures);
			assertEquals(failures.size(), 0);
			// clean the dataset for the next testcase
			dataset.removeNamedModel(uri);
			assertFalse(dataset.containsNamedModel(uri));
		}
	}
}
