package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * check if a symmetric property is defined as an inverse property of another
 * property
 * 
 * If a symmetric property is the inverse of an other property, these two
 * properties are equivalent. This could be stated easier with
 * owl:equivalentProperty.
 *
 */
public class CheckInverseRelationshipForSymmetricProperty extends SPARQLSelectCheck {

	public CheckInverseRelationshipForSymmetricProperty() {
		super(Level.SPARQL, TargetLanguage.OWL, Severity.WARN,
				"Defined an inverse relationship for a symmetric relationship.",
				new File(CheckInverseRelationshipForSymmetricProperty.class.getClassLoader().getResource("CheckInverseRelationshipForSymmetricProperty.rq").getFile()));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			System.out.println(0);
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("inverse").toString(),
					failureDescription + "\n" + qs.get("inverse").toString() + " is defined as inverse property of the symmetric property "
							+ qs.get("property").toString());
			failures.add(failure);
		}

		return failures;
	}

}
