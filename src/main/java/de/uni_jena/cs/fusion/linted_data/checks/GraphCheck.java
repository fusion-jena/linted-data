package de.uni_jena.cs.fusion.linted_data.checks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * checks that are executed on a single graph
 *
 */
public abstract class GraphCheck extends MultiGraphCheck {

	protected GraphCheck(Level level, Scope scope, Severity severity, String name) {
		super(level, scope, severity, name);
	}

	@Override
	public List<Failure> execute(Dataset dataset, String failureDescription) {

		List<Failure> failures = new ArrayList<Failure>();
		
		if (dataset.getDefaultModel() != null) {
			String currentFailureDescription = failureDescription + "\n";
			currentFailureDescription += "Model: " + "Default Model";
			failures.addAll(this.execute(dataset.getDefaultModel(), currentFailureDescription));
		}
		// check the named models
		Iterator<String> it = dataset.listNames();
		while (it.hasNext()) {
			String name = it.next();
			String currentFailureDescription = failureDescription + "\n";
			currentFailureDescription += "Model: " + name;
			failures.addAll(this.execute(dataset.getNamedModel(name), currentFailureDescription));
		}
		return failures;
	}

	public abstract List<Failure> execute(Model model, String failureDescription);
}
