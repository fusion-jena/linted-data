package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks.sparql_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks.CheckMissingDomainRangeDefinition;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestMissingDomainRangeDefinition {

	private List<Failure> createCheck(String path) throws URISyntaxException{
		File file = new File(this.getClass().getClassLoader().getResource(path).toURI());
		Dataset dataset = DatasetFactory.create();
		try {
			RDFParser.source(file.getAbsolutePath()).parse(dataset);
		}catch(RiotException e) {
			fail("Error when parsing " + path);
		}
		CheckMissingDomainRangeDefinition check = new CheckMissingDomainRangeDefinition();
		return check.execute(file, "");
	}

@Test
public void definedDomainRange() throws URISyntaxException {
	List<Failure> failures = createCheck("TestDomainRangeDefinition/domain_range_defined.ttl");
	assertNotNull(failures);
	assertEquals(0, failures.size());
}

@Test
public void definedDomainMissingRange() throws URISyntaxException {
	String path = "TestDomainRangeDefinition/domain_missing_range_01.rdf";
	List<Failure> failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	Failure failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain, range or neither", failure.getText());

	path = "TestDomainRangeDefinition/domain_missing_range_02.jsonld";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());

	path = "TestDomainRangeDefinition/domain_missing_range_03.ttl";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(2, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());
	failure = failures.get(1);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no domain, range or neither", failure.getText());
	}

@Test
public void definedRangeMissingDomain() throws URISyntaxException {
	String path = "TestDomainRangeDefinition/range_missing_domain_01.rdf";
	List<Failure> failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	Failure failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain, range or neither", failure.getText());

	path = "TestDomainRangeDefinition/range_missing_domain_02.jsonld";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());
	
	path = "TestDomainRangeDefinition/range_missing_domain_03.ttl";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(2, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());
	failure = failures.get(1);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no domain, range or neither", failure.getText());
	
}

@Test
public void missingDomainRange() throws URISyntaxException {
	String path = "TestDomainRangeDefinition/missing_domain_range_01.rdf";
	List<Failure> failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	Failure failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP-2 has no domain, range or neither", failure.getText());

	path = "TestDomainRangeDefinition/missing_domain_range_02.jsonld";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(1, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());
	
	path = "TestDomainRangeDefinition/missing_domain_range_03.ttl";
	failures = createCheck(path);
	assertNotNull(failures);
	assertEquals(2, failures.size());
	failure = failures.get(0);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleOP has no domain, range or neither", failure.getText());
	failure = failures.get(1);
	assertEquals(failure.getFailureElement(), "http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP");
	assertEquals(Severity.WARN, failure.getSeverity());
	assertEquals("\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-5#ExampleDP has no domain, range or neither", failure.getText());
	
}

}
