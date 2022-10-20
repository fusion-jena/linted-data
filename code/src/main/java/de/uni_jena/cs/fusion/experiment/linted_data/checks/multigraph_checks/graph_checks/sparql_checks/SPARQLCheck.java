package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryType;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.GraphCheck;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;
import de.uni_jena.cs.fusion.experiment.linted_data.util.FileUtil;

/**
 * Checks that can be formulated as SPARQL query
 */
public abstract class SPARQLCheck extends GraphCheck {
	/**
	 * the query that will be executed against the model
	 */
	private final String query;
	private final QueryType type;

	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, String query, QueryType type) {
		super(level, targetLanguage, severity, name);
		this.query = query;
		this.type = type;
	}

	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, File queryFile, QueryType type) {
		super(level, targetLanguage, severity, name);
		this.query  = readQuery(queryFile);
		this.type = type;
	}

	/**
	 * parses the file containing the SPARQL query and returns the query as a String
	 * 
	 * the IOException can't occur during runtime
	 * 
	 * @param file contains the SPARQL query, is parsed
	 * @return String representing the SPARQL query
	 */
	private String readQuery(File file) {
		try {
			return FileUtil.readFile(file);
		}catch(IOException e) {
			return null;
		}
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		switch(type) {
		case ASK:
			return execute(queryExecution.execAsk(), failureDescription); 
		case SELECT:
			return execute(queryExecution.execSelect(), failureDescription);
		default:
			return execute(queryExecution.execSelect(), failureDescription);
		}
	}

	protected abstract List<Failure> execute(ResultSet rs, String failureDescription);
	
	protected abstract List<Failure> execute(boolean b, String failureDescription);

}
