package de.uni_jena.cs.fusion.experiment.linted_data.types;

/**
 * represents the severity of a failure 
 */
public enum Severity {
	/**
	 * critical failure that needs to be fixed
	 */
	ERROR, 
	/**
	 * failure that should be fixed but that is not critical
	 */
	WARN,
	/**
	 * failure that can be fixed, but is not urgent
	 */
	INFO
}
