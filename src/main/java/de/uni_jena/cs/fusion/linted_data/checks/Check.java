package de.uni_jena.cs.fusion.linted_data.checks;

/**
 * generic representation of a check 
 * 
 * independent from different check types, 
 * bundles all general information on a check like its ID, name and execution time
 */
public abstract class Check {

	/**
	 * measurement of the execution time
	 */
	protected long time;
	
	protected String name;
	
	public Check(String name) {
		this.name = name;
		this.time = 0L;
	}
	
	public String getName() {
		return name;
	}
	
	public long getTime() {
		return time;
	}
	
}
