package de.uni_jena.cs.fusion.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.TargetLanguage;

/**
 * Finds all namespace definitions where the IRI doesn't end with a # or /
 */
public final class CheckRdfNamespacesShouldNotOmitTheSeperator extends GraphCheck {

	public CheckRdfNamespacesShouldNotOmitTheSeperator() {
		super(Level.MULTIGRAPH, TargetLanguage.RDF, Severity.WARN, "Namespaces should not omit the seperator (#, /)");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<>();

		// prefix URIs that omit / or #
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
					failureDescription + "\nThe namespace " + namespace + " omits the seperator at the end");
			failures.add(f);
		}

		return failures;
	}

}
