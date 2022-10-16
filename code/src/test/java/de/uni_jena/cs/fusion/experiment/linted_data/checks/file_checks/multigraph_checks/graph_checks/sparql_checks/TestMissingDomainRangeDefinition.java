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

public class TestMissingDomainRangeDefinition {
	
@Test
public void definedDomainRange() throws URISyntaxException {
	String path = "TestDomainRangeDefinition/domain_range_defined.ttl";
	File file = new File(this.getClass().getClassLoader().getResource(path).toURI());
	Dataset dataset = DatasetFactory.create();
	try {
		RDFParser.source(file.getAbsolutePath()).parse(dataset);
	}catch(RiotException e) {
		fail("Error when parsing " + path);
	}
	CheckMissingDomainRangeDefinition check = new CheckMissingDomainRangeDefinition();
	List<Failure> failures = check.execute(file, "");
	assertNotNull(failures);
	assertEquals(0, failures.size());
}

}
