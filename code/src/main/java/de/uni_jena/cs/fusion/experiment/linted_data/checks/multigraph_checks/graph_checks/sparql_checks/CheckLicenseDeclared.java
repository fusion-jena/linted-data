package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * Check if an ontology has a license defined
 * 
 * check if one of the four license properties: dc:rights, dcterms:license,
 * schema:license or cc:license is used
 */
public class CheckLicenseDeclared extends SPARQLSelectCheck {

	public CheckLicenseDeclared() {
		super(Level.SPARQL, TargetLanguage.OWL, Severity.ERROR,
				"The ontology metadata omits information about the license that applies to the ontology",
				CheckLicenseDeclared.class.getClassLoader().getResourceAsStream("CheckLicenseDeclared.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure f = new Failure(name, severity, qs.get("ontology").toString(), failureDescription + "\n"
					+ "Ontology " + qs.get("ontology").toString()
					+ " should use one the following properties to add a license dc:rights, dcterms:license, schema:license, cc:license");
			failures.add(f);
		}
		return failures;
	}

}