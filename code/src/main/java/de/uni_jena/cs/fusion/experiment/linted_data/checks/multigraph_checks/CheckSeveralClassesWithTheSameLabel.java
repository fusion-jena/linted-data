package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;

/**
 * checks if two or more classes have the same label
 * 
 * this should be a subclass of UnionGraphSPARQLCheck later, when this class is
 * added
 * <p>
 * check only compares labels of the same language
 * <p>
 * TODO create class and update superclass
 *
 */
public final class CheckSeveralClassesWithTheSameLabel extends MultiGraphCheck {
	
	private final String query;
	private final String label = "?label";
	
	public CheckSeveralClassesWithTheSameLabel() {
		super(Level.MULTIGRAPH, TargetLanguage.RDFS, Severity.INFO, "Other classes have the same label");
		// parse query
		BufferedReader br = null;
		String text = "";
		try {
			br = new BufferedReader(new FileReader(
					this.getClass().getClassLoader().getResource("CheckSeveralClassesWithTheSameLabel.rq").getFile()));
			String currentLine = br.readLine();
			while(currentLine != null) {
				text += currentLine;
				currentLine = br.readLine();
			}
		}catch(IOException e) {
			//TODO
		}
		query = text;
	}

	@Override
	public List<Failure> execute(Dataset dataset, String failureDescription) {
		// create a model that contains the default model and all named models of the
		// dataset
		Model model = ModelFactory.createDefaultModel();
		dataset.listNames().forEachRemaining(m -> model.add(dataset.getNamedModel(m)));
		if (dataset.getDefaultModel() != null) {
			model.add(dataset.getDefaultModel());
		}
		
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet results = queryExecution.execSelect();
		
		List<Failure> failures = new ArrayList<Failure>();
		while(results.hasNext()) {
			QuerySolution qs = results.next();
			System.out.println(qs.toString());
			System.out.println(qs.get(label));
			String description = failureDescription + "\n" + qs.get("label").toString() + " is shared by two or more classes";
			Failure failure = new Failure(name, severity, qs.get("label").toString(), description);
			failures.add(failure);
		}

		return failures;
	}

}
