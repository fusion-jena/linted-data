package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckInverseRelationshipForSymmetricProperty;

public class TestInverseRelationshipForSymmetricProperty {
	
	private List<Failure> init(String path){
		CheckInverseRelationshipForSymmetricProperty check = new CheckInverseRelationshipForSymmetricProperty();
		File file = new File(TestInverseRelationshipForSymmetricProperty.class.getClassLoader().getResource(path).getFile());
		return check.execute(file, "");
	}
	
	@Test
	public void symmetricProperty() {
		List<Failure> failures = init("CheckInverseRelationshipForSymmetricProperty/SymmetricProperty_01.rdf");
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}
	
	@Test
	public void symmetricPropertyWithInverse1() {
		List<Failure> failures = init("CheckInverseRelationshipForSymmetricProperty/SymmetricPropertyWithInverse_01.ttl");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b", f.getFailureElement());
		assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-b is defined as inverse property of the symmetric property http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-11#property-d", f.getText());
	}
}
