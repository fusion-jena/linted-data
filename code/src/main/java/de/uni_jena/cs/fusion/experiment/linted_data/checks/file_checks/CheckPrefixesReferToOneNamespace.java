package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.jena.riot.RDFLanguages;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * checks that when a serialisation language supports prefixes, they are only
 * used once
 * 
 * only for Turtle, TriG and N3
 */
public class CheckPrefixesReferToOneNamespace extends FileCheck {

	public CheckPrefixesReferToOneNamespace() {
		super(Level.FILE, TargetLanguage.RDFS, Severity.WARN, "Prefixes should not refer to multiple namespaces");
	}

	@Override
	public List<Failure> execute(File file, String failureDescription) throws FileNotFoundException {
		List<Failure> failures = new ArrayList<Failure>();

		String[] substrings = file.getName().split("\\.");
		String fileExtension = substrings[substrings.length - 1];

		// prefix : List<corresponding namespaces>
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (RDFLanguages.TURTLE.getFileExtensions().contains(fileExtension)
				|| RDFLanguages.TRIG.getFileExtensions().contains(fileExtension)) {
			processPrefixesPerLine("PREFIX", ">", ":", file, map);
			processPrefixesPerLine("@prefix", ">", ":", file, map);
		}else if(RDFLanguages.N3.getFileExtensions().contains(fileExtension)) {
			processPrefixesPerLine("@prefix", ">", ":", file, map);
		}
		// for each namespace check that it has only one corresponding prefix
		for (String namespace : map.keySet()) {
			if (map.get(namespace).size() > 1) {
				Failure failure = new Failure(this.name, this.severity, namespace, failureDescription + "\n" + namespace
						+ " has the " + map.get(namespace).size() + " namespaces: " + map.get(namespace).toString());
				failures.add(failure);
			}
		}
		return failures;
	}

	/**
	 * parses a file line by line and checks if a line defines a new prefix
	 * 
	 * can only be used for serialisation languages that define a prefix in one line
	 * <p>
	 * prefixes are added with their corresponding namespace to the map
	 * <p>
	 * a failure is created for each prefix, that corresponds to more than one
	 * namespace
	 * 
	 * @param lineBeginning how lines with a prefix definition start
	 * @param iriEndTag     the character that specifies how an IRI ends, for
	 *                      example '>' in Turtle
	 * @param seperator     what separates the prefix from the IRI, for example ':'
	 *                      for turtle
	 * @param file          parsed file
	 * @param map           stores the prefixes as keys with the corresponding list
	 *                      of namespaces as value
	 * @throws FileNotFoundException won't occur in runtime, because file existance
	 *                               is checked before calling this method
	 */
	private void processPrefixesPerLine(String lineBeginning, String iriEndTag, String seperator, File file,
			Map<String, List<String>> map) throws FileNotFoundException {
		Scanner myReader = new Scanner(file);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			data = data.strip();
			if (data.startsWith(lineBeginning)) {
				// IRI definition ends with this sign, don't need anything after this sign
				String[] s = data.split(iriEndTag, 2);

				s = s[0].split(seperator, 2);
				String prefix = s[0].replace(lineBeginning, "").strip();
				// remove the opening tag
				String namespace = s[1].strip().substring(1);
				// get the corresponding namespaces of the prefix
				List<String> namespaces = map.get(prefix);
				if (namespaces == null) {
					namespaces = new ArrayList<String>();
					map.put(prefix, namespaces);
				}
				// TODO is it valid to add twice the same namespace?
				// add the new namespace to the corresponding namespaces of the prefix
				namespaces.add(namespace);
			}
		}
		myReader.close();
	}

}
