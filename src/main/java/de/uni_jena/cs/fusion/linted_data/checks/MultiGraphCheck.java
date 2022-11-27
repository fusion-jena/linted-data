package de.uni_jena.cs.fusion.linted_data.checks;

import java.io.File;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Checks that are executed on multiple graphs
 * 
 * 
 */
public abstract class MultiGraphCheck extends FileCheck {
	
	protected MultiGraphCheck (Level level, Scope targetLanguage, Severity severity, String name) {
		super(level, targetLanguage, severity, name);
	}
	
	@Override
	public List<Failure> execute(File file, String failureDescription) {
		Dataset dataset = DatasetFactory.create();

		RDFParser.source(file.getAbsolutePath()).parse(dataset);
		
		return this.execute(dataset, failureDescription);
	}
	
	public abstract List<Failure> execute(Dataset dataset, String failureDescription);


}
