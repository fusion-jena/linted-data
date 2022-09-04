package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.Check;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * Checks that are performed on the file itself
 * 
 * 
 * For errors that can't be detected, once the file is parsed
 */
public abstract class FileCheck extends Check {
	
	protected Level level;
	protected TargetLanguage targetLanguage;
	
	/**
	 * severity of the check
	 */
	protected Severity severity;
	
	/**
	 * 
	 * @param level
	 * @param targetLanguage
	 * @param severity
	 * @param name general description of the testcase
	 */
	protected FileCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name) {
		super(name);
		this.level = level;
		this.targetLanguage = targetLanguage;
		this.severity = severity;
	}
	
	public final List<Failure> startExecution(File file){
		long start = System.currentTimeMillis();
		String failureDescription;
		try {
			failureDescription = "\n" + "File: " + file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			failureDescription = "";
		}
		List<Failure> failures = this.execute(file, failureDescription);
		this.time = System.currentTimeMillis() - start;
		return failures;
	}
	
	/**
	 * 
	 * @param file
	 * @param failureDescription describes the failure
	 * 
	 * the parameter is updated through the calls of parent methods
	 * 
	 * @return
	 */
	public abstract List<Failure> execute(File file, String failureDescription);
	
	public Level getLevel() {
		return level;
	}
	
	public TargetLanguage getTargetLangugage() {
		return targetLanguage;
	}
}
