package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.CheckPrefixesReferToOneNamespace;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * each namespace should be referred by exactly one prefix
 * 
 * checks whether each namespace IRI is associated with a set of prefixes of
 * size 1
 * <p>
 * in case of p1 : IRI1 ... p1 : IRI1 no failure is created, this case is
 * checked in {@link CheckPrefixesReferToOneNamespace}
 * <p>
 * for each namespace with more than one prefix a failure is created
 */
public final class CheckNamespacesShouldNotBeReferredByMultiplePrefixes extends GraphCheck {

	public CheckNamespacesShouldNotBeReferredByMultiplePrefixes() {
		super(Level.GRAPH, TargetLanguage.RDFS, Severity.INFO, "The namespace is refered by multiple prefixes");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		// for each namespace URI the associated prefixes
		Map<String, Set<String>> namespacePrefixMap = new HashMap<>();

		Map<String, String> prefixMap = model.getNsPrefixMap();
		// iterate over all prefixes and add them to the reverse map
		for (String prefix : prefixMap.keySet()) {
			String namespace = prefixMap.get(prefix);
			namespacePrefixMap.putIfAbsent(namespace, new HashSet<String>());
			namespacePrefixMap.get(namespace).add(prefix);
		}

		List<Failure> failures = new ArrayList<>();
		// find all namespaces that have two or more associated prefixes
		// for each of these namespaces create a failure
		for (String namespace : namespacePrefixMap.keySet()) {
			if (namespacePrefixMap.get(namespace).size() > 1) {
				Failure f = new Failure(name, severity, namespace, failureDescription + "\nNamespace " + namespace
						+ " is referred by the " + namespacePrefixMap.get(namespace).size() + " prefixes: "
						+ namespacePrefixMap.get(namespace).stream().sorted().collect(Collectors.joining(", ")));
				failures.add(f);
			}
		}
		return failures;
	}

}
