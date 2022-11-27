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
 * Check if a property is defined as its own inverse
 * 
 * If a property is inverse to itself, it is a symmetric property. To state that
 * a property is symmetric, one should use owl:SymmetricProperty
 *
 */
public class CheckOwlSelfInverseProperty extends SPARQLSelectCheck {

	public CheckOwlSelfInverseProperty() {
		super(Level.SPARQL, Scope.OWL, Severity.WARN, "A property is defined as inverse of itself. Better use owl:SymmetricProperty.",
				CheckOwlSelfInverseProperty.class.getClassLoader().getResourceAsStream("CheckOwlSelfInverseProperty.rq"));
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
