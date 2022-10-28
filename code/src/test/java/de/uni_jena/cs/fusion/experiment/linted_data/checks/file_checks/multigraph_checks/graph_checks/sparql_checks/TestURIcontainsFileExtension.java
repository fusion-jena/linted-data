package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckURIcontainsFileExtension;


public class TestURIcontainsFileExtension {

	private CheckURIcontainsFileExtension check = new CheckURIcontainsFileExtension();
	
	@Test
	void URIcontains_nt() {
		OntModel model = ModelFactory.createOntologyModel();
		OntProperty p = model.createOntProperty("http://my-example.org/test.nt#property-a");
		p.addDomain(model.createClass("http://example.com/test/class-a"));
		p.addRange(model.createClass("http://example.com/test/class-b"));
		p.addLabel("this is a test.nt label", null);
		
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.nt#property-a", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.nt#property-a contains the file extension nt", f.getText());
		
		model = ModelFactory.createOntologyModel();
		p = model.createSymmetricProperty("http://example.com/test/property-a");
		DatatypeProperty dp = model.createDatatypeProperty("http://my-example.org/test.nt#datatype-property-a");
		OntClass c1 = model.createClass("http://example.com/test/class-a");
		Individual i = c1.createIndividual();
		i.addLiteral(dp, 2580L);
		c1.addComment("this comment contains the file extension .nt", "en");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.nt#datatype-property-a", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.nt#datatype-property-a contains the file extension nt", f.getText());
		
		model = ModelFactory.createOntologyModel();
		c1 = model.createClass();
		i = c1.createIndividual("http://my-example.org/test.nt#individual-1");
		i.addLabel("individual with .nt", null);
		c1.addComment("dieser kommentar enthaelt .nt", "de");
		model.createProperty("http://example.com/test/property-a");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.nt#individual-1", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.nt#individual-1 contains the file extension nt", f.getText());
		
	}

	@Test
	void URIcontains_jsonld() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_n3() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_rj() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_trig() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_rpb() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_pbrdf() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_jsonld10() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_jsonld11() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_nq() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_rt() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_trdf() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_shacl() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_shc() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_rdf() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_owl() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_xml() {
		fail("not yet implemented");
	}

	@Test
	void URIcontains_ttl() throws Exception {
		File file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/ttl_01.ttl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.ttl#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.ttl#Local contains the file extension ttl",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/ttl_02.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.ttl#Country", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.ttl#Country contains the file extension ttl",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/ttl_03.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.ttl#Name", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.ttl#Name contains the file extension ttl",
				f.getText());
	}

	@Test
	void URIcontains_trix() {
		fail("not yet implemented");
	}
	
	@Test
	void noFileExtensionInURI() {
		fail("not yet implemented");
	}
}
