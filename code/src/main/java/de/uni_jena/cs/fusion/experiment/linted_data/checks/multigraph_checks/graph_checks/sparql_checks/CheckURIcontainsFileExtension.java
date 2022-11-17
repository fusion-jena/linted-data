package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * Check whether any occuring URI contains a file extension 
 */
public final class CheckURIcontainsFileExtension extends SPARQLSelectCheck {
	
	
	public CheckURIcontainsFileExtension() {
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN,
				"file extensions such as .owl, .rdf, .ttl, .n3 and .rdfxml are not allowed in an URI", 
				CheckURIcontainsFileExtension.class.getClassLoader().getResourceAsStream("CheckURIcontainsFileExtension.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while(resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("iri").toString(), failureDescription + "\n" + qs.get("iri").toString() + " contains the file extension " + qs.get("fileExtension").toString());
			failures.add(failure);
		}
		return failures;
	}


}
