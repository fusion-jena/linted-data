package de.uni_jena.cs.fusion.linted_data.checks;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Check whether any occuring URI contains a file extension 
 */
public final class CheckRdfIriContainsFileExtension extends SPARQLSelectCheck {
	
	
	public CheckRdfIriContainsFileExtension() {
		super(Level.SPARQL, Scope.RDF, Severity.WARN,
				"The IRI should not contain the file extension.", 
				CheckRdfIriContainsFileExtension.class.getClassLoader().getResourceAsStream("CheckRdfIriContainsFileExtension.rq"));
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
