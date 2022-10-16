package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.sparql_checks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

public final class CheckMissingDomainRangeDefinition extends SPARQLCheck {

	public CheckMissingDomainRangeDefinition() {

		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN, "The property doesn't have a domain or range or none of them", 
				readQuery());
	}
	
	private static String readQuery() {
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader("CheckDomainRangeDefinition.rq"));
		} catch (FileNotFoundException e) {
		}
		StringBuilder builder = new StringBuilder();
	    String currentLine;
		try {
			currentLine = r.readLine();
			 while (currentLine != null) {
	        builder.append(currentLine);
	        builder.append("\n");
	        currentLine = r.readLine();
	    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			r.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return builder.toString();
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<>();
		while (rs.hasNext()) {
			String uri = rs.next().get("p").toString();
			Failure f = new Failure(name, severity, uri, failureDescription + "\n" + uri + " has no domain, range or neither");
			failures.add(f);
		}
		return failures;
	}

}
