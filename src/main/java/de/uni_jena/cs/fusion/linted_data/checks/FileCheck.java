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

	/**
	 * what kind of check is created
	 * 
	 * all other validators extend this class
	 */
	private Level level;

	/**
	 * to which modeling language the check is applicable
	 */
	private Scope scope;

	/**
	 * severity of the check
	 */
	protected Severity severity;

	/**
	 * generate a new FileCheck
	 * 
	 * @param level    what kind of check is created
	 * @param scope    addressed modelling language
	 * @param severity how important it is to fix occuring failures
	 * @param name     general description of the testcase
	 */
	protected FileCheck(Level level, Scope scope, Severity severity, String name) {
		super(name);
		this.level = level;
		this.scope = scope;
		this.severity = severity;
	}

	/**
	 * executes the validation, collects all found errors into a list and measures
	 * the run time
	 * 
	 * @param file to be processed
	 * @return list of failures matching the criteria of the check
	 * @throws Exception in case of an error during parsing
	 *                   <p>
	 *                   can't occur during runtime
	 */
	public final List<Failure> startExecution(File file) throws Exception {
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
	 * executes the validation and collects all found errors into a list
	 * 
	 * @param file               to be processed
	 * @param failureDescription describes the failure
	 *                           <p>
	 *                           the parameter is updated through the calls of
	 *                           parent methods
	 * 
	 * @return list of failures matching the criteria of the check
	 */
	public abstract List<Failure> execute(File file, String failureDescription) throws Exception;

	public Level getLevel() {
		return level;
	}

	public Scope getScope() {
		return scope;
	}
}
