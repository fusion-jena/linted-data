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
 * check if multiple domains or ranges are defined for a property
 * 
 * multiple domain and range are allowed, but are treated as conjunction, what
 * is equivalent to owl:intersectionOf
 *
 */
public final class CheckMultipleDomainRange extends SPARQLSelectCheck {

	public CheckMultipleDomainRange() {
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN,
				"Multiple domains or ranges are interpreted as conjunction and should therefore be replaced with an owl:intersectionOf.",
				CheckMultipleDomainRange.class.getClassLoader().getResourceAsStream("CheckMultipleDomainRange.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure f = new Failure(name, severity, qs.get("property").toString(), failureDescription + "\n"
					+ qs.get("property").toString() + " " + qs.get("error").toString());
			failures.add(f);
		}
		return failures;
	}

}
