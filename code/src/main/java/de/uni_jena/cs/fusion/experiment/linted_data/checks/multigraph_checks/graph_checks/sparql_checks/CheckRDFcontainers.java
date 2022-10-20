package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * Check if RDF containers use illegal, repeated or missed out index numbers
 * 
 * see
 * {@link https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/}
 *
 */
public class CheckRDFcontainers extends SPARQLSelectCheck {

	public CheckRDFcontainers() {
		// SPARQL query from
		// https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/72449757#72449757
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN,
				"The RDF container uses  one of illegal, repeated, missed out index numbers",
				new File(CheckRDFcontainers.class.getClassLoader().getResource("CheckRDFContainers.rq").getPath()));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();

		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			String text = "\n" + qs.get("container").toString() + " " + qs.get("containerMembershipProperty").toString()
					+ " " + qs.get("item").toString();
			Failure failure = new Failure(qs.get("error").toString(), this.severity, qs.get("item").toString(),
					failureDescription + text);
			failures.add(failure);
		}

		return failures;
	}

}
