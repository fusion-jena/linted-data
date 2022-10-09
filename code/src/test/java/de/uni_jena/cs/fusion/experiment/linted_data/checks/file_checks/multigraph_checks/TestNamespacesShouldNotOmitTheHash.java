package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.CheckNamespacesShouldNotOmitTheHash;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestNamespacesShouldNotOmitTheHash {
	
	private Dataset dataset;
	private CheckNamespacesShouldNotOmitTheHash check;
	
	@BeforeEach
	private void initalisation() {
		dataset =  DatasetFactory.createGeneral();
		check = new CheckNamespacesShouldNotOmitTheHash();
	}
	
	/**
	 * named model
	 */
	@Test
	public void allNamespacesEndWithHash_1() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		dataset.addNamedModel("model1", model);
		assertTrue(dataset.containsNamedModel("model1"));
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	/**
	 * named model + default model
	 */
	@Test
	public void allNamespacesEndWithHash_2() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		dataset.addNamedModel("model1", model);
	
		model = ModelFactory.createMemModelMaker().createModel("model2");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.setDefaultModel(model);
		
		
		assertTrue(dataset.containsNamedModel("model1"));
		assertNotNull(dataset.getDefaultModel());
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	/**
	 * named model + default model
	 * 
	 * namespace occurs only without hash
	 */
	@Test
	public void namespacesOmitHash_1() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		dataset.addNamedModel("model1", model);
	
		model = ModelFactory.createMemModelMaker().createModel("model2");
		res = model.createResource("http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.setDefaultModel(model);
		
		
		assertTrue(dataset.containsNamedModel("model1"));
		assertNotNull(dataset.getDefaultModel());
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nthe namespace http://www.w3.org/2004/02/skos/core omits the # at the end", f.getText());
	}
	
	/**
	 * named models
	 * 
	 * namespace occurs once with hash and once without 
	 */
	@Test
	public void namespacesOmitHash_2() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		dataset.addNamedModel("model1", model);
	
		model = ModelFactory.createMemModelMaker().createModel("model2");
		res = model.createResource("http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.addNamedModel("model2", model);
		
		model = ModelFactory.createDefaultModel();
		res = model.createResource("http://my-test.com/onto#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("Thesaurus", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.addNamedModel("model3", model);
		
		
		assertTrue(dataset.containsNamedModel("model1"));
		assertTrue(dataset.containsNamedModel("model2"));
		assertTrue(dataset.containsNamedModel("model3"));
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nthe namespace http://www.w3.org/2004/02/skos/core occurs with and without #", f.getText());
	}
	
	/**
	 * named models + default graph
	 * 
	 * namespace1 occurs with and without hash 
	 * namespace2 occurs without hah
	 */
	@Test
	public void namespacesOmitHash_4() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		dataset.addNamedModel("model1", model);
	
		model = ModelFactory.createMemModelMaker().createModel("model2");
		res = model.createResource("http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		model.setNsPrefix("prefix1", "http://www.w3.org/2001/XMLSchema");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.addNamedModel("model2", model);
		
		model = ModelFactory.createDefaultModel();
		res = model.createResource("http://my-test.com/onto#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("Thesaurus", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		dataset.addNamedModel("model3", model);
		
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
		model.setNsPrefix("prefix1", "http://www.w3.org/2001/XMLSchema");
		dataset.setDefaultModel(model);
		
		
		assertTrue(dataset.containsNamedModel("model1"));
		assertTrue(dataset.containsNamedModel("model2"));
		assertTrue(dataset.containsNamedModel("model3"));
		assertNotNull(dataset.getDefaultModel());
		List<Failure> failures = check.execute(dataset, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2001/XMLSchema", f.getFailureElement());
		assertEquals("\nthe namespace http://www.w3.org/2001/XMLSchema omits the # at the end", f.getText());
		f = failures.get(1);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nthe namespace http://www.w3.org/2004/02/skos/core occurs with and without #", f.getText());
		
	}
}
