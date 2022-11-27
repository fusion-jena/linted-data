package de.uni_jena.cs.fusion.linted_data.checks;

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

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;
import de.uni_jena.cs.fusion.linted_data.util.FileUtil;

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
public final class CheckRdfsSeveralClassesWithTheSameLabel extends MultiGraphCheck {

	private final String query;
	private final String label = "label";

	public CheckRdfsSeveralClassesWithTheSameLabel() {
		super(Level.MULTIGRAPH, Scope.RDFS, Severity.INFO, "Other classes have the same label");
		String text = "";
		// read SPARQL query from the resource file
		// SPARQL query which finds all classes with the same label
		try {
			text = FileUtil.readFile(
					this.getClass().getClassLoader().getResourceAsStream("CheckRdfsSeveralClassesWithTheSameLabel.rq"));
		} catch (IOException e) {
			// this exception can't occur during runtime
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
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			String description = failureDescription + "\n" + qs.get(label).toString()
					+ " is shared by the " + qs.get("numClasses").asLiteral().getInt() +" classes: [" + qs.get("classes").toString() + "]";
			Failure failure = new Failure(name, severity, qs.get(label).toString(), description);
			failures.add(failure);
		}

		return failures;
	}

}
