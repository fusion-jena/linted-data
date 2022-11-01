package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
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

/**
 * The file extension at the end of the function name indicates the file format
 * as well as the found extensions
 */
public class TestURIcontainsFileExtension {

	private CheckURIcontainsFileExtension check = new CheckURIcontainsFileExtension();

	@Test
	void URIcontains_rj() throws Exception {
		// rj in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontainsRJ_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#datatype-property-a contains the file extension rj",
				failures.get(0).getText());
		// rj in subject URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontainsRJ_02.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#datatype-property-a contains the file extension rj",
				failures.get(0).getText());
		// rj in object URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontainsRJ_03.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#individual-1", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#individual-1 contains the file extension rj",
				failures.get(0).getText());
	}

	@Test
	void URIcontains_trix() throws Exception {
		// trix in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_trix_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix#datatype-property-a contains the file extension trix",
				failures.get(0).getText());
		// trix in subject URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_trix_02.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix/property-a", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix/property-a contains the file extension trix",
				failures.get(0).getText());
		// trix in object URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_trix_03.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix#individual-1", failures.get(0).getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix#individual-1 contains the file extension trix",
				failures.get(0).getText());
	}

	@Test
	void URIcontains_trig() throws Exception {
		// trig in subject URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_trig_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.trig#property-a", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trig#property-a contains the file extension trig",
				f.getText());
		f = failures.get(1);
		assertEquals("http://my-example.org/test.trig/individual-1", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trig/individual-1 contains the file extension trig",
				f.getText());
	}

	@Test
	void URIcontains_rpb() throws Exception {
		// rpb in object and predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_rpb_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.rpb#individual-1", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#individual-1 contains the file extension rbp",
				f.getText());
		f = failures.get(2);
		assertEquals("http://my-example.org/test.rpb#property-b", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb",
				f.getText());
		f = failures.get(1);
		assertEquals("http://my-example.org/test.trix#individual-1", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#individual-2 contains the file extension rpb",
				f.getText());
	}

	@Test
	void URIcontains_pbrdf() throws Exception {
		// pbrdf in subject URI and rj in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_pbrdf_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(1);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.pbrdf#pizza-423", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.pbrdf#pizza-423 contains the file extension pbrdf",
				f.getText());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.rpb#property-b", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb",
				f.getText());

	}

	@Test
	void URIcontains_nq() throws Exception {
		// nq in subject and object URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_nq_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr#pizza-423.nq", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr#pizza-423.nq contains the file extension nq",
				f.getText());
		f = failures.get(2);
		assertEquals("http://my-example.org/test.nq#pizza", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.nq#pizza contains the file extension nq",
				f.getText());
		f = failures.get(1);
		assertEquals("http://my-example.org/test.nq/individual-1", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
		+ "\nModel: Default Model\nhttp://my-example.org/test.nq/individual-1 contains the file extension nq",
		f.getText());
		
	}

	@Test
	void URIcontains_rt() throws Exception{
		// rt in subject, predicate and object iri
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_rt_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		Failure f = failures.get(2);
		assertEquals("http://my-example.org#testr.rt#pizza", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org#testr.rt#pizza contains the file extension rt",
				f.getText());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.rt/individual-1", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rt/individual-1 contains the file extension rt",
				f.getText());
		f = failures.get(1);
		assertEquals("http://example.rt/test#property-b", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
		+ "\nModel: Default Model\nhttp://example.rt/test#property-b contains the file extension rt",
		f.getText());
	}

	@Test
	void URIcontains_trdf() throws Exception {
		// trdf in subject URI and rpb in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_trdf_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.trdf#pizza-423", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.trdf#pizza-423 contains the file extension trdf",
				f.getText());
		f = failures.get(1);
		assertEquals("http://my-example.org/test.rpb#property-b", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb",
				f.getText());
	}

	@Test
	void URIcontains_shacl() throws Exception {
		// shacl in subject URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_shaclc_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.shaclc#pizza-423", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.shaclc#pizza-423 contains the file extension shaclc",
				f.getText());
	}

	@Test
	void URIcontains_shc() throws Exception{
		// shc in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/URIcontains_shc_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.shc#property-b", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.shc#property-b contains the file extension shc",
				f.getText());
	}

	/*
	 * the following tests are not necessary the bad test design was noticed too
	 * late, but they are not wrong and test that the file extensions in URIs are
	 * detected
	 * 
	 * later this should be updated to the same format as the tests above
	 * 
	 * The file extension at the end of the function name indicates the file format
	 * as well as the found extensions
	 */
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
	void URIcontains_jsonld() throws Exception {
		File file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/jsonld_01.ttl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld#Local contains the file extension jsonld",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/jsonld_02.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.jsonld#Country", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.example/foo.jsonld#Country contains the file extension jsonld",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/jsonld_03.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.jsonld#Name", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.example/foo.jsonld#Name contains the file extension jsonld",
				f.getText());

		file = new File(this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/jsonld_04.jsonld")
				.getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld#Local contains the file extension jsonld",
				f.getText());
	}

	@Test
	void URIcontains_n3() {
		OntModel model = ModelFactory.createOntologyModel();
		OntProperty p = model.createOntProperty("http://my-example.org/test.n3#property-a");
		p.addDomain(model.createClass("http://example.com/test/class-a"));
		p.addRange(model.createClass("http://example.com/test/class-b"));
		p.addLabel("this is a test.nt label", null);

		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.n3#property-a", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.n3#property-a contains the file extension n3", f.getText());

		model = ModelFactory.createOntologyModel();
		p = model.createSymmetricProperty("http://example.com/test/property-a");
		DatatypeProperty dp = model.createDatatypeProperty("http://my-example.org/test.n3#datatype-property-a");
		OntClass c1 = model.createClass("http://example.com/test/class-a");
		Individual i = c1.createIndividual();
		i.addLiteral(dp, 2580L);
		c1.addComment("this comment contains the file extension .n3", "en");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.n3#datatype-property-a", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.n3#datatype-property-a contains the file extension n3", f.getText());

		model = ModelFactory.createOntologyModel();
		c1 = model.createClass();
		i = c1.createIndividual("http://my-example.org/test.n3#individual-1");
		i.addLabel("individual with .nt", null);
		c1.addComment("dieser kommentar enthaelt .n3", "de");
		model.createProperty("http://example.com/test-owl./property-a");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.n3#individual-1", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/test.n3#individual-1 contains the file extension n3", f.getText());
	}

	@Test
	void URIcontains_jsonld10() throws Exception {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld10_01.ttl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld10#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld10#Local contains the file extension jsonld10",
				f.getText());

		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld10_02.jsonld10").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld10#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld10#Local contains the file extension jsonld10",
				f.getText());

		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld10_03.jsonld10").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.jsonld#Country", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.example/foo.jsonld#Country contains the file extension jsonld",
				f.getText());
	}

	@Test
	void URIcontains_jsonld11() throws Exception {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld11_01.ttl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld11_i#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld11_i#Local contains the file extension jsonld11",
				f.getText());

		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld11_02.jsonld11").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld10#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld10#Local contains the file extension jsonld10",
				f.getText());

		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/jsonld11_03.jsonld11").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.jsonld11#Country", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.example/foo.jsonld11#Country contains the file extension jsonld11",
				f.getText());

	}

	@Test
	void URIcontains_rdf() throws Exception {
		File file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/rdf_01.rdf").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.rdf-15#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.rdf-15#Local contains the file extension rdf",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/rdf_02.rdf").getFile());
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
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/rdf_03.rdf").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.xml-58#Name", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.xml-58#Name contains the file extension xml",
				f.getText());
	}

	@Test
	void URIcontains_owl() throws Exception {
		File file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/owl_01.owl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.owl#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.owl#Local contains the file extension owl",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/owl_02.owl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/foo.owl-23#Country", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/foo.owl-23#Country contains the file extension owl",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/owl_03.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.owl#Name", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.owl#Name contains the file extension owl",
				f.getText());

	}

	@Test
	void URIcontains_xml() throws Exception {
		File file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/xml_01.xml").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.xml_t#Local", f.getFailureElement());
		assertEquals("\nFile: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.xml_t#Local contains the file extension xml",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/xml_02.xml").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.xml#Country", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.xml#Country contains the file extension xml",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/xml_03.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.xml#Name", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.xml#Name contains the file extension xml",
				f.getText());

		file = new File(
				this.getClass().getClassLoader().getResource("CheckURIcontainsFileExtension/xml_03.ttl").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.example/foo.xml#Name", f.getFailureElement());
		assertEquals(
				"\nFile: " + file.getPath()
						+ "\nModel: Default Model\nhttp://www.example/foo.xml#Name contains the file extension xml",
				f.getText());
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
	void noFileExtensionInURI() throws Exception {
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/noFileExtension_01.ttl").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);

		assertEquals(0, failures.size());
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckURIcontainsFileExtension/noFileExtension_02.rdf").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
}
