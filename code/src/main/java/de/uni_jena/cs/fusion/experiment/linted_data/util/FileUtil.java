package de.uni_jena.cs.fusion.experiment.linted_data.util;

import java.io.File;
import java.util.Arrays;

public abstract class FileUtil {

	/**
	 * check if the file is in a valid format
	 * 
	 * valid formats are: owl, ttl, rdf
	 * 
	 * @param file its extension will be checked
	 * @return true if the file extension is in the list, else false
	 */
	public static final boolean checkFileExtension(File file) {
		String[] partOfFileName = file.getName().split(".");
		String fileExtension = partOfFileName[partOfFileName.length - 1];

		return Arrays.asList("owl", "ttl", "rdf", "xml", "nt", "nq", "trig").contains(fileExtension);
	}
}
