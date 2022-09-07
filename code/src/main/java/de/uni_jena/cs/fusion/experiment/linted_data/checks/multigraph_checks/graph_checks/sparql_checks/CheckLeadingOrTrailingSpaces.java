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
 * Finds all literals that start or end with a whitespace character
 * 
 * 
 * \s doesn't exist in SPARQL so the equivalent form [\r\n\t\f\v ] is used
 * \v also doesn't exist in SPARQL and is hence not checked
 */
public class CheckLeadingOrTrailingSpaces extends SPARQLCheck {
	
	private static final String literal = "literal";
	private static final String subject = "s";
	private static final String predicate = "p";
	private static final String object = "o";
	
	public CheckLeadingOrTrailingSpaces() {
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.INFO, "Literals shouldn't start or end with white spaces", 
				"SELECT DISTINCT ?" + literal + " ?" + subject + " ?" +predicate +" ?" + object + " WHERE {\n" + 
				"  {\n" + 
				"	SELECT (STR(?" + object +") AS ?" + literal + ") ?" + subject + " ?" +predicate +" ?" + object+ " WHERE {\n" + 
				"		?" + subject + " ?" +predicate +" ?" + object + ".\n" + 
				"		FILTER(isLiteral(?" + object + "))\n" + 
				"	}\n" + 
				"	}\n" + 
				"  	FILTER (regex(?" + literal + ", \"^[\\r\\n\\t\\f ]|[\\r\\n\\t\\f ]$\"))\n" + 
				"}" 
				); 
	}	
	
	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while(rs.hasNext()) {
			String description = failureDescription + "\n";
			QuerySolution qs = rs.next();
			description += qs.get(subject).toString() + " " + qs.get(predicate).toString() + " " + qs.get(object).toString() + "\n";
			Failure f = new Failure(this.name, this.severity, qs.getLiteral("literal").getLexicalForm(), description);
			failures.add(f);
		}
		return failures;
	}


}
