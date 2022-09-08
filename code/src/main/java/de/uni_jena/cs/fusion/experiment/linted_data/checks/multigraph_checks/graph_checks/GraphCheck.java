package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.MultiGraphCheck;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * checks that are executed on a single graph
 *
 */
public abstract class GraphCheck extends MultiGraphCheck {

	protected GraphCheck(Level level, TargetLanguage targetLanguage, Severity severity, String name) {
		super(level, targetLanguage, severity, name);
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
		Iterator<Resource> it = dataset.listModelNames();
		while (it.hasNext()) {
			Resource r = it.next();
			String currentFailureDescription = failureDescription + "\n";
			currentFailureDescription += "Model: " + r.getLocalName();
			failures.addAll(this.execute(r.getModel(), currentFailureDescription));
		}

		return failures;
	}

	public abstract List<Failure> execute(Model model, String failureDescription);
}
