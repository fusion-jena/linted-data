package de.uni_jena.cs.fusion.experiment.linted_data.util;

import java.util.List;

public abstract class StringUtil {
	
	public final static String checkForFileExtension(String s, List<String> extensions) {
		for(String e : extensions) {
			if(s.contains(e)) {
				return e;
			}
		}
		return null;
	}
}
