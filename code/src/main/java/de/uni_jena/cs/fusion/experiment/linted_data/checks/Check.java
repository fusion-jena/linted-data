package de.uni_jena.cs.fusion.experiment.linted_data.checks;

/**
 * generic representation of a check 
 * 
 * independent from different check types, 
 * bundles all general information on a check like its ID, name and execution time
 */
public abstract class Check {

	/**
	 * counter for the id of the different checks
	 */
	private static long idCounter = 0;
	/**
	 * id of check object
	 */
	protected long id;
	/**
	 * measurement of the execution time
	 */
	protected long time;
	
	protected String name;
	
	public Check(String name) {
		this.name = name;
		this.time = 0L;
		this.id = Check.idCounter;
		Check.idCounter++;
	}
	
	public String getName() {
		return name;
	}
	
	public long getTime() {
		return time;
	}
	
	public long getID() {
		return id;
	}
}
