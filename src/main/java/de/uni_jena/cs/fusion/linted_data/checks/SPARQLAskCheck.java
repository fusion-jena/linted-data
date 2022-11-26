package de.uni_jena.cs.fusion.linted_data.checks;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.TargetLanguage;

public abstract class SPARQLAskCheck extends SPARQLCheck {

	public SPARQLAskCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, String query) {
		super(level, targetLanguage, severity, name, query);
	}

	public SPARQLAskCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name, File queryFile) {
		super(level, targetLanguage, severity, name, queryFile);
	}
	
	public SPARQLAskCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name,
			InputStream queryFile) {
		super(level, targetLanguage, severity, name, queryFile);
	}
	
	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);

		return execute(queryExecution.execAsk(), failureDescription); 
	}
	
	protected abstract List<Failure> execute(boolean b, String failureDescription);

}
