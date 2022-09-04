package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.GraphCheck;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * Checks that can be formulated as SPARQL query
 */
public abstract class SPARQLCheck extends GraphCheck {
	/**
	 * the query that will be executed against the model
	 */
	private final String query;
	
	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, String query) {
		super(level, targetLanguage, severity, name);
		this.query = query;
	}
	
	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet results = queryExecution.execSelect();
		return execute(results, failureDescription);
	}
	
	protected abstract List<Failure> execute(ResultSet rs, String failureDescription);

}
