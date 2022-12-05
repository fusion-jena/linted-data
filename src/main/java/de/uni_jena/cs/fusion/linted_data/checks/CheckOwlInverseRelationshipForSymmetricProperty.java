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
 * check if a symmetric property is defined as an inverse property of another
 * property
 * 
 * If a symmetric property is the inverse of an other property, these two
 * properties are equivalent. This could be stated easier with
 * owl:equivalentProperty.
 *
 */
public class CheckOwlInverseRelationshipForSymmetricProperty extends SparqlSelectCheck {

	public CheckOwlInverseRelationshipForSymmetricProperty() {
		super(Level.SPARQL, Scope.OWL, Severity.WARN,
				"An inverse relationship is declared for a symmetric relationship. Better use \"owl:equivalentProperty.\"",
				CheckOwlInverseRelationshipForSymmetricProperty.class.getClassLoader().getResourceAsStream("CheckOwlInverseRelationshipForSymmetricProperty.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("inverse").toString(),
					failureDescription + "\n" + qs.get("inverse").toString() + " is defined as inverse property of the symmetric property "
							+ qs.get("property").toString());
			failures.add(failure);
		}

		return failures;
	}

}
