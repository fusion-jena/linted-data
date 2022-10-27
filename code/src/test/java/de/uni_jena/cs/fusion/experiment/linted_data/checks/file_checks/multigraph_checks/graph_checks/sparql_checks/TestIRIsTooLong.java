package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
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
		assertEquals(
				"\nhttp://my-example.org/very-long-class-name_that+will_create_a_failure has a local name that has 17 more characters than 30",
				f.getText());
		f = failures.get(0);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://example.org#this_lokal-n4m3-15-t00-l0ng_12345678123456789", f.getFailureElement());
		assertEquals(
				"\nhttp://example.org#this_lokal-n4m3-15-t00-l0ng_12345678123456789 has a local name that has 15 more characters than 30",
				f.getText());
	}

	/**
	 * IRIs of properties are too long
	 */
	@Test
	void propertyIRIsTooLong() {
		CheckIRIsTooLong check = new CheckIRIsTooLong();

		OntModel model = ModelFactory.createOntologyModel();
		model.createProperty("http://my-example.org/property-1_NAME");
		model.createOntProperty("http://my-example.org/property-1ü-has-a*long_NAME-when-adding:more_characters");
		assertNotNull(model.getProperty("http://my-example.org/property-1ü-has-a*long_NAME-when-adding:more_characters"));
		model.createOntProperty("http://cerrado.linkeddata.es/ecology/ccon#affects");
		model.createSymmetricProperty("http://example.org#ä-symmetric-property:withALongLocaleName");
		model.createSymmetricProperty("http://example.org#ä-symmetric-property");
		DatatypeProperty dp = model
				.createDatatypeProperty("http://example.org#my-one-and-only-dataproperty-thats-too+long");
		dp.addDomain(model.createClass("http://purl.obolibrary.org/obo/NCBITaxon_9606"));
		OntClass c1 = model.createClass("http://my-example.org/class-a");
		c1.createIndividual("http://exämple.com#individual-nr-1");

		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(3, failures.size());
		Failure f = failures.get(1);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://example.org#my-one-and-only-dataproperty-thats-too+long", f.getFailureElement());
		assertEquals(
				"\nhttp://example.org#my-one-and-only-dataproperty-thats-too+long has a local name that has 13 more characters than 30",
				f.getText());
		f = failures.get(0);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://example.org#ä-symmetric-property:withALongLocaleName", f.getFailureElement());
		assertEquals(
				"\nhttp://example.org#ä-symmetric-property:withALongLocaleName has a local name that has 10 more characters than 30",
				f.getText());
		f = failures.get(2);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://my-example.org/property-1ü-has-a*long_NAME-when-adding:more_characters",
				f.getFailureElement());
		assertEquals(
				"\nhttp://my-example.org/property-1ü-has-a*long_NAME-when-adding:more_characters has a local name that has 25 more characters than 30",
				f.getText());
	}

	/**
	 * IRIs of individuals are too long
	 */
	@Test
	void individualIRIsTooLong() {
		CheckIRIsTooLong check = new CheckIRIsTooLong();

		OntModel model = ModelFactory.createOntologyModel();
		OntProperty p = model.createOntProperty("http://my-example.org/property-1_NAME");
		OntClass c1 = model.createClass("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16632");
		c1.addLabel("Geographic Area", "en");
		Individual i = c1.createIndividual("http://my-example.org/inidivudal-nö-1_having_a_longL483l987654321");
		i.addComment("First Individual that contains a verry long text for no reason", "en");
		OntClass c2 = model.createClass("http://cerrado.linkeddata.es/ecology/ccon#Temperature");
		c2.createIndividual("http://example.com#i-nd-ivi-dual-2-ö-adding+more:characters");
		p.addDomain(c1);
		p.addRange(c2);
		i.setPropertyValue(p, i);
		c2.createIndividual("http://my-example.com#Q345");
		
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		Failure f = failures.get(1);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://my-example.org/inidivudal-nö-1_having_a_longL483l987654321", f.getFailureElement());
		assertEquals(
				"\nhttp://my-example.org/inidivudal-nö-1_having_a_longL483l987654321 has a local name that has 13 more characters than 30",
				f.getText());
		f = failures.get(0);
		assertEquals(Severity.INFO, f.getSeverity());
		assertEquals("http://example.com#i-nd-ivi-dual-2-ö-adding+more:characters", f.getFailureElement());
		assertEquals(
				"\nhttp://example.com#i-nd-ivi-dual-2-ö-adding+more:characters has a local name that has 10 more characters than 30",
				f.getText());
	}

}
