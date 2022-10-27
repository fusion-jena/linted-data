package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckIRIsTooLong;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

class TestIRIsTooLong {

	/**
	 * all IRIs have a local name with less than 30 characters
	 */
	@Test
	void noIRITooLong() {
		CheckIRIsTooLong check = new CheckIRIsTooLong();

		OntModel model = ModelFactory.createOntologyModel();
		model.createOntProperty("http://my-example.org/property-1_NAME");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		OntProperty p = model.createOntProperty("http://cerrado.linkeddata.es/ecology/ccon#affects");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		OntClass c1 = model.createClass("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16632");
		c1.addLabel("Geographic Area", "en");
		OntClass c2 = model.createClass("http://cerrado.linkeddata.es/ecology/ccon#Temperature");
		p.hasDomain(c2);
		p.hasRange(c1);
		model.add(c1, model.createDatatypeProperty("http://purl.obolibrary.org/obo/IAO_0000004"),
				"Some long nonsense name that is longer than 30 characters");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		Individual i1 = c1.createIndividual("http://example.com#inidvidual-1");
		i1.addComment("This is an individual for testing", null);
		Individual i2 = c2.createIndividual("http://example.com#Inidvidual-115");
		i2.addProperty(p, i1);
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	void classIRIsTooLong() {
		CheckIRIsTooLong check = new CheckIRIsTooLong();
		
		OntModel model = ModelFactory.createOntologyModel();
		model.createOntProperty("http://my-example.org/property-1_NAME");
		model.createOntProperty("http://cerrado.linkeddata.es/ecology/ccon#affects");
		model.createClass("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16632");
		OntClass c1 = model.createClass("http://my-example.org/very-long-class-name_that+will_create_a_failure");
		c1.createIndividual("http://example.com#inidvidual-1");
		model.createClass("http://example.org#this_lokal-n4m3-15-t00-l0ng_12345678123456789");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(1);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://my-example.org/very-long-class-name_that+will_create_a_failure", f.getFailureElement());
		assertEquals("\nhttp://my-example.org/very-long-class-name_that+will_create_a_failure has a local name that has 17 more characters than 30", f.getText());
		f = failures.get(0);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://example.org#this_lokal-n4m3-15-t00-l0ng_12345678123456789", f.getFailureElement());
		assertEquals("\nhttp://example.org#this_lokal-n4m3-15-t00-l0ng_12345678123456789 has a local name that has 15 more characters than 30", f.getText());
	}
}
