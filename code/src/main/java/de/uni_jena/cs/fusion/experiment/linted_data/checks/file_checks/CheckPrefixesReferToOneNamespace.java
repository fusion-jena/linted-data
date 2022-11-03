package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;
import de.uni_jena.cs.fusion.experiment.linted_data.util.FileUtil;

/**
 * checks that when a serialisation language supports prefixes, they are only
 * used once
 * 
 * only for Turtle, TriG, JSONLD (10, 11)
 * <p>
 * these are standardised by the W3C, support prefixes and the document is valid
 * although a prefix is defined more than once
 */
public final class CheckPrefixesReferToOneNamespace extends FileCheck {

	private static final List<Lang> languages = Arrays.asList(RDFLanguages.TURTLE, RDFLanguages.TRIG,
			RDFLanguages.JSONLD, RDFLanguages.JSONLD10, RDFLanguages.JSONLD11);

	public CheckPrefixesReferToOneNamespace() {
		super(Level.FILE, TargetLanguage.RDFS, Severity.WARN, "Prefixes should not refer to multiple namespaces");
	}

	@Override
	public List<Failure> execute(File file, String failureDescription) throws IOException {
		Lang language = FileUtil.getLang(file, languages);

		List<Failure> failures = new ArrayList<Failure>();

		// prefix : List<corresponding namespaces>
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		if (language == RDFLanguages.TURTLE || language == RDFLanguages.TRIG) {
			processPrefixesPerLine("PREFIX", ">", ":", file, map);
			processPrefixesPerLine("@prefix", ">", ":", file, map);
		} else if (language == RDFLanguages.JSONLD || language == RDFLanguages.JSONLD10
				|| language == RDFLanguages.JSONLD11) {
			processJSONDocument(file, map);
		}

		// search for namespaces that are assigned more than once to the same prefix
		for (String prefix : map.keySet()) {
			// how often each namespace occurs
			Map<String, Integer> count = new HashMap<String, Integer>();
			for (String namespace : map.get(prefix)) {
				count.putIfAbsent(namespace, 0);
				count.compute(namespace, (k, v) -> v + 1);
			}
			// namespaces that occur more than once
			Map<String, Integer> filtered = count.entrySet().stream().filter(x -> x.getValue() > 1)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			// create a failure for each namespace that occured more than once
			for (String namespace : filtered.keySet()) {
				Failure failure = new Failure("Prefix refers multiple times to the same namespace", Severity.INFO,
						prefix, failureDescription + "\n" + prefix + " has " + count.get(namespace)
								+ " times the namespace " + namespace);
				failures.add(failure);
				int firstIndex = map.get(prefix).indexOf(namespace);
				// remove all occurences of the namespace and insert it again at the first
				// position it occured
				for (int i = 0; i < filtered.get(namespace); i++) {
					map.get(prefix).remove(namespace);
				}
				map.get(prefix).add(firstIndex, namespace);
			}

			// for each namespace check that it has only one corresponding prefix
			if (map.get(prefix).size() > 1) {
				Failure failure = new Failure(this.name, this.severity, prefix, failureDescription + "\n" + prefix
						+ " has the " + map.get(prefix).size() + " namespaces: " + map.get(prefix).toString());
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

				addNamespace(prefix, namespace, map);
			}
		}
		myReader.close();
	}

	/**
	 * checks a jsonld file for defined prefixes in context elements
	 * 
	 * parses the file and searchs for '@context
	 * <p>
	 * the elements between {} are checked whether its a namespace definition
	 * <p>
	 * if so, the prefix and the namespace are added to the map
	 * <p>
	 * it would be good to switch to JSONpath instead of a self written parser
	 * 
	 * @param file that is parsed
	 * @param map  stores the prefixes as keys with the corresponding list of
	 *             namespaces as value
	 * @throws IOException in case of an error when closing the reader
	 */
	private void processJSONDocument(File file, Map<String, List<String>> map) throws IOException {
		FileInputStream reader = new FileInputStream(file);
		char token = (char) getNext(reader);

		while (token != (char) -1) {
			// need to search for @context
			if (token == '@') {
				String readText = "";
				while (token != '"') {
					readText += token;
					token = getNext(reader);
				}
				if (readText.contentEquals("@context")) {
					token = getNext(reader);
					// skip till {
					// { opens context elements
					while (token != '{') {
						token = getNext(reader);
					}
					// everything before } belongs to the context definition
					while (token != '}') {
						// current token is {, ',' or }
						// need to get the next token
						token = getNext(reader);
						readText = "";
						// one element ends with ,
						// if it is the last element and contains {...}, it ends with }
						while (token != ',' && token != '}') {
							// skip whitespaces and new lines
							if (token != ' ' && token != '\n') {
								readText += token;
							}
							if (token == '{') {
								// when a element contains a { it can't be a namespace
								// need to skip all the characters between the two {}
								// and set the text empty
								readText = "";
								while (token != '}') {
									token = getNext(reader);
								}
							}
							// get the next token of the element
							token = getNext(reader);
						}

						// prefix : namespace
						String[] splitText = readText.split(":", 2);

						if (splitText.length == 2) {
							String prefix = splitText[0].strip().replace("\"", "");
							String namespace = splitText[1].strip().replace("\"", "");
							// tags are no namespace definition
							if (!prefix.startsWith("@")) {
								addNamespace(prefix, namespace, map);
							}
						}
					}
				}

			}
			token = (char) getNext(reader);
		}
		reader.close();
	}

	/**
	 * read the next character of a file
	 * 
	 * @param reader stream of the file
	 * @return the next character of the file, character of -1 in case of EOF
	 */
	private char getNext(FileInputStream reader) {
		try {
			return (char) reader.read();
		} catch (IOException e) {
			return (char) -1;
		}
	}

	/**
	 * add the namespace to the prefix in the map
	 * 
	 * @param prefix    key to use
	 * @param namespace to add to the list of namespaces of the prefix
	 * @param map       stores the prefixes as keys with the corresponding list of
	 *                  namespaces as value
	 */
	private void addNamespace(String prefix, String namespace, Map<String, List<String>> map) {
		map.putIfAbsent(prefix, new ArrayList<String>());
		List<String> namespaces = map.get(prefix);
		// add the new namespace to the corresponding namespaces of the prefix
		namespaces.add(namespace);
	}

}
