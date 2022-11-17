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
 * Check if a property is defined as its own inverse
 * 
 * If a property is inverse to itself, it is a symmetric property. To state that
 * a property is symmetric, one should use owl:SymmetricProperty
 *
 */
public class CheckSelfInverseProperty extends SPARQLSelectCheck {

	public CheckSelfInverseProperty() {
		super(Level.SPARQL, TargetLanguage.OWL, Severity.WARN, "A relationship is defined as inverse of itself.",
				CheckSelfInverseProperty.class.getClassLoader().getResourceAsStream("CheckSelfInverseProperty.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure f = new Failure(name, severity, qs.get("property").toString(), failureDescription + "\n"
					+ qs.get("property").toString() + " is defined as inverse property to itself");
			failures.add(f);
		}
		return failures;
	}

}
