package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * namespaces should not omit the hash
 * 
 * if a namespace omits the hash, this is a failure
 * <p>
 * also if a namespace omits the hash, the same namespace shouldn't occur with
 * the hash
 *
 */
public class CheckNamespacesShouldNotOmitTheHash extends MultiGraphCheck {

	public CheckNamespacesShouldNotOmitTheHash() {
		super(Level.MULTIGRAPH, TargetLanguage.RDFS, Severity.WARN, "namespaces should not omit the hash");
	}

	@Override
	public List<Failure> execute(Dataset dataset, String failureDescription) {
		List<Failure> failures = new ArrayList<>();

		// list of valid namespaces
		Set<String> namespacesWithHash = new HashSet<String>();
		// namespaces that omit the hash
		Set<String> namespacesWithoutHash = new HashSet<String>();
		if (dataset.getDefaultModel() != null) {
			checkNamespace(dataset.getDefaultModel(), namespacesWithHash, namespacesWithoutHash);
		}
		// iterate over all namespaces within the model
		Iterator<Resource> it = dataset.listModelNames();
		while (it.hasNext()) {
			checkNamespace(dataset.getNamedModel(it.next()), namespacesWithHash, namespacesWithoutHash);
		}
		
		// check all invalid entries, if the namespace occurs with and without the hash
		// or if it always omits the hash
		for (String namespaceWithoutHash : namespacesWithoutHash) {
			Failure f = null;
			if (namespacesWithHash.contains(namespaceWithoutHash + "#")) {
				f = new Failure(name, severity, namespaceWithoutHash,
						failureDescription + "\nthe namespace " + namespaceWithoutHash + " occurs with and without #");
			} else {
				f = new Failure(name, severity, namespaceWithoutHash,
						failureDescription + "\nthe namespace " + namespaceWithoutHash + " omits the # at the end");
			}
			failures.add(f);
		}

		return failures;
	}

	/**
	 * check all namespaces from a model
	 * 
	 * add the occurring namespaces to the corresponding list
	 * 
	 * @param m                     Model to be checked
	 * @param namespacesWithHash    all namespaces that end with hash
	 * @param namespacesWithoutHash all namespaces that omit the hash
	 */
	private void checkNamespace(Model m, Set<String> namespacesWithHash, Set<String> namespacesWithoutHash) {
		// get all prefix definitions from the current model
		Map<String, String> map = m.getNsPrefixMap();
		for (String s : map.values()) {
			// when the namespace ends with #, it is a valid namespace
			if (s.endsWith("#")) {
				namespacesWithHash.add(s);
			} else {
				// else it is an invalid namespace
				namespacesWithoutHash.add(s);
			}
		}
	}

}
