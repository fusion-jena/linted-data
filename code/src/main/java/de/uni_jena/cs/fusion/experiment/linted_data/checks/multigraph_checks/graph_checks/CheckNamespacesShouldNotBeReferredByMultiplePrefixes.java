package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

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
		// create a failure for each of them
		for (String namespace : namespacePrefixMap.keySet()) {
			if (namespacePrefixMap.get(namespace).size() > 1) {
				Failure f = new Failure(name, severity, namespace,
						failureDescription + "\nNamespace " + namespace + " is referred by the "
								+ namespacePrefixMap.get(namespace).size() + " prefixes: "
								+ namespacePrefixMap.get(namespace).toString());
				failures.add(f);
			}
		}
		return failures;
	}

}
