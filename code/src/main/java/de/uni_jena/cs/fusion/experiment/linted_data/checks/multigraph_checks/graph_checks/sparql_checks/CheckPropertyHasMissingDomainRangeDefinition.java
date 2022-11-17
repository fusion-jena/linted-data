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
 * Object and datatype properties should have a defined domain and range
 * 
 * Properties that have defined only domain OR range are reported as well as
 * properties that are missing both
 */
public final class CheckPropertyHasMissingDomainRangeDefinition extends SPARQLSelectCheck {

	public CheckPropertyHasMissingDomainRangeDefinition() {
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN,
				"The property doesn't have a domain or range or none of them",
				CheckPropertyHasMissingDomainRangeDefinition.class.getClassLoader()
						.getResourceAsStream("CheckPropertyHasDomainRangeDefinition.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<>();
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			String uri = qs.get("property").toString();
			String error = qs.get("error").toString();
			Failure f = new Failure(name, severity, uri,
					failureDescription + "\n" + uri + " " + error);
			failures.add(f);
		}
		return failures;
	}

}
