package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * prefix URIs should not omit the hash
 * 
 * collects all namespace definitions if one of them doesn't end with a # or /
 * <p>
 * for each of these which occured with a # in an URI create a failure
 */
public class CheckNamespacesShouldNotOmitTheHash extends GraphCheck {

	public CheckNamespacesShouldNotOmitTheHash() {
		super(Level.MULTIGRAPH, TargetLanguage.RDFS, Severity.WARN, "namespaces should not omit the hash");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<>();

		// prefix URIs that omit the hash
		Set<String> namespacesWithoutHash = new HashSet<String>();
		Map<String, String> map = model.getNsPrefixMap();
		for (String s : map.values()) {
			// when the namespace ends with # or /, it is a valid namespace
			if (!s.endsWith("#") && !s.endsWith("/")) {
				// else it is an invalid namespace
				namespacesWithoutHash.add(s);
			}
		}

		// find all URIs in the model that contain a #, get the corresponding namespace
		// URI (substring before #)
		Set<String> usedNamespaces = model.getGraph().stream().flatMap(this::processTriple)
				// select only the namespaces whose URI occured in the prefix definition without
				// the # at the end
				.filter(s -> namespacesWithoutHash.contains(s)).collect(Collectors.toSet());

		// for each namespace that occurs with hash and without create a failure and add
		// it to the list
		for (String namespace : usedNamespaces) {
			Failure f = new Failure(name, severity, namespace,
					failureDescription + "\nthe namespace " + namespace + " omits the # at the end");
			failures.add(f);
		}

		return failures;
	}

	/**
	 * Tries to get the URI of the subject, predicate and object and get the
	 * corresponding namespace URI
	 * 
	 * This function only splits at the #, not at the '/'
	 * <p>
	 * The # will cause problems in SPARQL queries when it is needed to extend an
	 * prefix with it
	 * <p>
	 * In case one of the triple
	 * 
	 * @param t Triple
	 * @return stream of the occuring namespaces within the triple
	 */
	private Stream<String> processTriple(Triple t) {
		List<String> l = new ArrayList<>();
		if (t.getSubject().isURI()) {
			l.add(t.getSubject().getURI().split("#", 2)[0]);
		}
		if (t.getPredicate().isURI()) {
			l.add(t.getPredicate().getURI().split("#", 2)[0]);
		}
		if (t.getObject().isURI()) {
			l.add(t.getObject().getURI().split("#", 2)[0]);
		}
		return l.stream();
	}

}
