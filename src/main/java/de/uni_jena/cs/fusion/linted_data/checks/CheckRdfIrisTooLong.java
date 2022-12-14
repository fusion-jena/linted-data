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
 * check for IRIs whose local name is longer than 36 characters
 * 
 * the number of 36 is chosen arbitrary based on the following considerations:
 * <p>
 * 1. the number of characters divided by 3 which fit in one row with 1920x1080
 * and 11pt font size is larger
 * <p>
 * 2. number of characters that are too much to type
 * <p>
 * 3. UUIDs are valid local names
 * <p>
 * TODO more considerations?
 */
public final class CheckRdfIrisTooLong extends SPARQLSelectCheck {

	public CheckRdfIrisTooLong() {
		super(Level.SPARQL, Scope.RDF, Severity.INFO, "Local names shouldn't be longer than 36 characters",
				CheckRdfIrisTooLong.class.getClassLoader().getResourceAsStream("CheckRdfIrisTooLong.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("iri").toString(),
					failureDescription + "\n" + qs.get("iri").toString() + " has a local name that has "
							+ qs.get("numExcessCharacters").asLiteral().getInt() + " more characters than 36");
			failures.add(failure);
		}

		return failures;
	}

}
