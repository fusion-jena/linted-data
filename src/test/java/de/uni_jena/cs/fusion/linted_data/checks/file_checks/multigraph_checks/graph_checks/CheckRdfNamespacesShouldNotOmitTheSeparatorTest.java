package de.uni_jena.cs.fusion.linted_data.checks.file_checks.multigraph_checks.graph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks.CheckRdfNamespacesShouldNotOmitTheSeperator;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class CheckRdfNamespacesShouldNotOmitTheSeparatorTest {
	
	private CheckRdfNamespacesShouldNotOmitTheSeperator check = new CheckRdfNamespacesShouldNotOmitTheSeperator();
	
	/**
	 * all namespace IRIs end with #
	 */
	@Test
	public void allNamespacesEndWithSeperator_1() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		res.addLiteral(VCARD.BDAY, "31.03.2023");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	/**
	 * named model instead of default model 
	 * <p>
	 * namespace IRIs end with # or /
	 */
	@Test
	public void allNamespacesEndWithSeperator_2() {
		Model model = ModelFactory.createMemModelMaker().createModel("model2");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core/JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns/");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core/");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim/");
		
		
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
		
		model = ModelFactory.createDefaultModel();
		res = model.createResource("http://my-test.com/onto#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		model.setNsPrefix("Thesaurus", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core/");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	/**
	 * named and default model
	 * <p>
	 * some namespace IRIs don't end with # or /
	 */
	@Test
	public void namespacesOmitTheSeperator_1() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nThe namespace http://www.w3.org/2004/02/skos/core omits the seperator at the end", f.getText());
		
		model = ModelFactory.createMemModelMaker().createModel("model2");
		res = model.createResource("http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		Property p = model.createProperty("http://www.w3.org/2004/02/skos/core#license");
		model.add(res, p, "CC-BY-4.0");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos-p", "http://www.w3.org/2004/02/skos/core");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core/");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nThe namespace http://www.w3.org/2004/02/skos/core omits the seperator at the end", f.getText());
	}
	
	/**
	 * default model
	 * <p>
	 * some namespace IRIs don't end with # or /
	 */
	@Test
	public void namespacesOmitHash_2() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		Property p = model.createProperty("http://www.w3.org/2004/02/skos/core#bday");
		model.add(res, p, "2023-03-06", XSDDatatype.XSDdate);
		model.createResource("http://www.w3.org/2004/02/skos/core#John-Smith-2");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2004/02/skos/core", f.getFailureElement());
		assertEquals("\nThe namespace http://www.w3.org/2004/02/skos/core omits the seperator at the end", f.getText());
				
		model = ModelFactory.createDefaultModel();
		res = model.createResource("http://my-test.com/onto#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		res.addLiteral(VCARD.BDAY, "1998-02-24");
		model.setNsPrefix("VCARD", "http://www.w3.org/2001/vcard-rdf/3.0");
		model.setNsPrefix("Thesaurus", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		model.setNsPrefix("taxslim", "http://purl.obolibrary.org/obo/ncbitaxon/subsets/taxslim#");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals(Severity.WARN, f.getSeverity());
		assertEquals("http://www.w3.org/2001/vcard-rdf/3.0", f.getFailureElement());
		assertEquals("\nThe namespace http://www.w3.org/2001/vcard-rdf/3.0 omits the seperator at the end", f.getText());
	}
	
	/**
	 * default model
	 * <p>
	 * some namespace IRIs don't end with # or /
	 * <p>
	 * more than one failure
	 */
	@Test
	public void namespacesOmitHash_3() {
		Model model = ModelFactory.createDefaultModel();
		Property p = model.createProperty("http://example#birthday");
		model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core");
		model.setNsPrefix("ex", "http://example.com");
		Resource res = model.createResource("http://www.w3.org/2004/02/skos/core#JohnSmith");
		res.addLiteral(VCARD.FN, "John Smith");
		res.addProperty(p, "1965-11-29", XSDDatatype.XSDdate);
		res = model.createResource("http://example.com#John-2nd-king");
		res.addLiteral(VCARD.EMAIL, "king-john@example.com");
		res.addProperty(p, "0.5", XSDDatatype.XSDdouble);
	
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.com", "\nThe namespace http://example.com omits the seperator at the end", Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://www.w3.org/2004/02/skos/core", "\nThe namespace http://www.w3.org/2004/02/skos/core omits the seperator at the end", Severity.WARN));
	}
	
}
