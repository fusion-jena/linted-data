package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
	protected final String query;

	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, String query) {
		super(level, targetLanguage, severity, name);
		this.query = query;
	}

	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, File queryFile) {
		super(level, targetLanguage, severity, name);
		this.query  = readQuery(queryFile);
	}

	protected SPARQLCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, InputStream queryFile) {
		super(level, targetLanguage, severity, name);
		this.query  = readQuery(queryFile);
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
	
	private String readQuery(InputStream file) {
		try {
			return FileUtil.readFile(file);
		}catch(IOException e) {
			return null;
		}
	}

}
