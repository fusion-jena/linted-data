package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;
import de.uni_jena.cs.fusion.experiment.linted_data.util.StringUtil;

public class CheckLexicalRepresentationOfFloatingPointDatatypes extends GraphCheck {

	public CheckLexicalRepresentationOfFloatingPointDatatypes() {
		super(Level.GRAPH, TargetLanguage.RDFS, Severity.WARN,
				"The lexical value can't be represented with the selected floating point data type");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		StmtIterator statements = model.listStatements();
		while (statements.hasNext()) {
			// iterate over all statements
			Statement statement = statements.next();
			RDFNode object = statement.getObject();

			// only statements with an literal as object are investigated
			if (!object.isLiteral()) {
				continue;
			}

			RDFDatatype type = object.asLiteral().getDatatype();
			// only literals with the data type float or double are investigated
			// only if the literal can't be exactly represented in the data type, a new
			// failure is created and added to the list
			if (type.equals(XSDDatatype.XSDfloat)
					&& !StringUtil.isPreciseRepresentableAsFloat(object.asLiteral().getLexicalForm())) {
				Failure failure = new Failure(this.name, this.severity, object.asLiteral().toString(),
						failureDescription + "\n" + statement.toString());
				failures.add(failure);
			} else if (type.equals(XSDDatatype.XSDdouble)
					&& !StringUtil.isPreciseRepresentableAsDouble(object.asLiteral().getLexicalForm())) {
				Failure failure = new Failure(this.name, this.severity, object.asLiteral().toString(),
						failureDescription + "\n" + statement.toString());
				failures.add(failure);
			}
		}
		return failures;
	}

}
