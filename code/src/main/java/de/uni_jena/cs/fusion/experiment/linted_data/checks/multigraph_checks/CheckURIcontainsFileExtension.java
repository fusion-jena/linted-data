package de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.types.TargetLanguage;
import de.uni_jena.cs.fusion.experiment.linted_data.util.StringUtil;

public class CheckURIcontainsFileExtension extends MultiGraphCheck {
	
	private List<String> extensions = Arrays.asList(".owl", ".rdf", ".ttl", ".n3", ".rdfxml");
	
	public CheckURIcontainsFileExtension() {
		super(Level.MULTIGRAPH, TargetLanguage.RDFS, Severity.WARN,
				"file extensions such as .owl, .rdf, .ttl, .n3 and .rdfxml are not allowed in an ontology URI");
	}

	@Override
	public List<Failure> execute(Dataset dataset, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		Iterator<String> it = dataset.listNames();
		while(it.hasNext()) {
			String uri = it.next();
			String fileExtension = StringUtil.checkForFileExtension(uri, extensions);
			if(fileExtension != null) {
				failures.add(new Failure(this.name, this.severity, uri, "Graph URI " + uri + " contains file extension " + fileExtension));
			}
		}
		return failures;
	}

}
