package de.uni_jena.cs.fusion.experiment.linted_data.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * contains utility methods for files
 */
public abstract class FileUtil {

	/**
	 * check if the input file is in a valid format
	 * 
	 * valid formats are: owl, ttl, rdf
	 * 
	 * @param file its extension will be checked
	 * @return true if the file extension is in the list, else false
	 */
	public static final boolean checkInputFileExtension(File file) {
		return checkFileExtension(file, Arrays.asList("owl", "ttl", "rdf", "xml", "nt", "nq", "trig"));
	}
	
	/**
	 * check if the output file is an xml document
	 * 
	 * @param file its extension will be checked
	 * @return true if the file extension is xml, else false
	 */
	public static final boolean checkOutputFileExtension(File file) {
		return checkFileExtension(file, Arrays.asList("xml"));
	}
	
	/**
	 * check if the file is in a valid format
	 * 
	 * @param file its extension will be checked
	 * @param extensions valid file extensions
	 * @return true if the file extension is in extensions, else false
	 */
	public static final boolean checkFileExtension(File file, List<String> extensions) {
		String[] partOfFileName = file.getName().split("\\.");
		// the file extension is the substring after the last . 
		String fileExtension = partOfFileName[partOfFileName.length - 1];
		
		return extensions.contains(fileExtension);
	}
}
