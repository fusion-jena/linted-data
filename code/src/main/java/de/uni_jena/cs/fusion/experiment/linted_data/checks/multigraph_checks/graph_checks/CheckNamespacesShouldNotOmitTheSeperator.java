package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * prefix URIs should not omit the hash
 * 
 * collects all namespace definitions that don't end with a # or /
 */
public final class CheckNamespacesShouldNotOmitTheSeperator extends GraphCheck {

	public CheckNamespacesShouldNotOmitTheSeperator() {
		super(Level.MULTIGRAPH, TargetLanguage.RDFS, Severity.WARN, "namespaces should not omit the hash");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<>();

		// prefix URIs that omit the hash
		Set<String> namespacesWithoutSeparator = new HashSet<String>();
		Map<String, String> map = model.getNsPrefixMap();
		for (String s : map.values()) {
			// when the namespace ends with # or /, it is a valid namespace
			if (!s.endsWith("#") && !s.endsWith("/")) {
				// else it is an invalid namespace
				namespacesWithoutSeparator.add(s);
			}
		}
		
		// for each namespace that omits the separator create a failure
		for (String namespace : namespacesWithoutSeparator) {
			Failure f = new Failure(name, severity, namespace,
					failureDescription + "\nthe namespace " + namespace + " omits the seperator at the end");
			failures.add(f);
		}

		return failures;
	}

}
