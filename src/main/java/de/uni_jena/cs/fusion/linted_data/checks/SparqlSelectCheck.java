package de.uni_jena.cs.fusion.linted_data.checks;

import java.io.InputStream;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

import org.apache.jena.query.ResultSet;

/**
 * SPARQL check whose query is of type SELECT
 */
public abstract class SparqlSelectCheck extends SparqlCheck {

	public SparqlSelectCheck(Level level, Scope scope, Severity severity, String name, InputStream queryFile) {
		super(level, scope, severity, name, queryFile);
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		return execute(queryExecution.execSelect(), failureDescription);
	}

	protected abstract List<Failure> execute(ResultSet resultSet, String failureDescription);

}
