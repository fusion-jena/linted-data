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
 * Finds all literals that start or end with a whitespace character
 * 
 * 
 * \s doesn't exist in SPARQL so the equivalent form [\r\n\t\f\v ] is used
 * <p>
 * \v also doesn't exist in SPARQL and is hence not checked
 */
public final class CheckRdfLeadingOrTrailingSpaces extends SPARQLSelectCheck {

	private static final String literal = "literal";
	private static final String subject = "subject";
	private static final String predicate = "predicate";
	private static final String object = "object";

	public CheckRdfLeadingOrTrailingSpaces() {
		super(Level.SPARQL, Scope.RDF, Severity.INFO, "Literals shouldn't start or end with white spaces",
				CheckRdfLeadingOrTrailingSpaces.class.getClassLoader()
						.getResourceAsStream("CheckRdfLeadingOrTrailingSpaces.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (rs.hasNext()) {
			String description = failureDescription + "\n";
			QuerySolution qs = rs.next();
			description += qs.get(subject).toString() + " " + qs.get(predicate).toString() + " "
					+ qs.get(object).toString() + "\n";
			Failure f = new Failure(this.name, this.severity, qs.getLiteral(literal).getLexicalForm(), description);
			failures.add(f);
		}
		return failures;
	}

}
