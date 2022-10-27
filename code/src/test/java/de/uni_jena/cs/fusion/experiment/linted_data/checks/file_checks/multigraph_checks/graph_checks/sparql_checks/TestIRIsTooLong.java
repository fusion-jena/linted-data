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

class TestIRIsTooLong {

	@Test
	void noIRITooLong() {
		/*
		 * all IRIs have a local name with less than 30 characters
		 */
		CheckIRIsTooLong check = new CheckIRIsTooLong();

		OntModel model = ModelFactory.createOntologyModel();
		model.createProperty("http://my-example.org/property-1_NAME");
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

}
