package de.uni_jena.cs.fusion.experiment.linted_data.types;

/**
 * describe what kind of checks / on which level checks will be performed
 */
public enum Level {

	SPARQL, 
	FILE,
	GRAPH,
	MULTIGRAPH,
	ALL;
	
	public boolean equals(Level o) {
		if(this == ALL || o == ALL) {
			return true;
		}else {
			return this == o;
		}
	}
}
