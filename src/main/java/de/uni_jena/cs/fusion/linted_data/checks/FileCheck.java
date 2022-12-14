package de.uni_jena.cs.fusion.linted_data.checks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Checks that are performed on the file itself
 * 
 * 
 * For errors that can't be detected, once the file is parsed
 */
public abstract class FileCheck extends Check {
	
	protected Level level;
	protected Scope targetLanguage;
	
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
	protected FileCheck(Level level, Scope targetLanguage, Severity severity, String name) {
		super(name);
		this.level = level;
		this.targetLanguage = targetLanguage;
		this.severity = severity;
	}
	
	public final List<Failure> startExecution(File file) throws Exception{
		long start = System.currentTimeMillis();
		String failureDescription;
		try {
			failureDescription = "File: " + file.getCanonicalPath();
		} catch (IOException e) {
			throw e;
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
	public abstract List<Failure> execute(File file, String failureDescription) throws Exception;
	
	public Level getLevel() {
		return level;
	}
	
	public Scope getTargetLangugage() {
		return targetLanguage;
	}
}
