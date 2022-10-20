package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * 
 *
 */
public class CheckLicenseDeclared extends SPARQLAskCheck {

	public CheckLicenseDeclared() {
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.INFO,
				"The ontology metadata omits information about the license that applies to the ontology",
				new File(CheckLicenseDeclared.class.getClassLoader().getResource("CheckLicenseDeclared.rq").getPath()));
	}

	@Override
	protected List<Failure> execute(boolean b, String failureDescription) {
		List<Failure> failures = new ArrayList<>();
		if (!b) {
			Failure f = new Failure(name, severity, "", failureDescription + "\n"
					+ "Use one the following licenses: dc:rights, dcterms:license, schema:license, cc:license to add a license");
			failures.add(f);
		}
		return failures;
	}
	
}
