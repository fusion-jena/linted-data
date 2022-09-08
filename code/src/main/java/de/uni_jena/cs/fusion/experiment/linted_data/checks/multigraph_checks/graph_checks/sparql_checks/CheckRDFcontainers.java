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
 * Check if RDF containers use illegal, repeated or missed out index numbers
 * 
 * see {@link https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/}
 *
 */
public class CheckRDFcontainers extends SPARQLCheck {

	public CheckRDFcontainers() {
		// SPARQL query from 
		// https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/72449757#72449757
		super(Level.SPARQL, TargetLanguage.RDFS, Severity.WARN,
				"The RDF container uses  one of illegal, repeated, missed out index numbers",
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
"SELECT ?container ?containerMembershipProperty ?item ?error WHERE {\n" + 
"  {\n" + 
"    ?container a rdf:Seq .\n" + 
"  } UNION {\n" + 
"    ?container a rdf:Bag .\n" + 
"  } UNION {\n" + 
"    ?container a rdf:Alt .\n" + 
"  }\n" + 
"  {\n" + 
"    BIND(\"Duplicated Index\" AS ?error)\n" + 
"    ?container ?containerMembershipProperty ?item .\n" + 
"    FILTER strstarts(str(?containerMembershipProperty),\"http://www.w3.org/1999/02/22-rdf-syntax-ns#_\")\n" + 
"    FILTER EXISTS {\n" + 
"      ?container ?containerMembershipProperty ?item2 .\n" + 
"      FILTER (?item!=?item2)\n" + 
"    }\n" + 
"  } UNION {\n" + 
"    BIND(\"Missing Predecessor\" AS ?error)\n" + 
"    ?container ?containerMembershipProperty ?item .\n" + 
"    BIND(xsd:integer(STRAFTER(STR(?containerMembershipProperty),\"http://www.w3.org/1999/02/22-rdf-syntax-ns#_\")) - 1 AS ?index)\n" + 
"    BIND(IRI(CONCAT(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#_\", STR(?index))) AS ?previousContainerMembershipProperty)\n" + 
"    FILTER (?index >= 1)\n" + 
"    FILTER NOT EXISTS {\n" + 
"      ?container ?previousContainerMembershipProperty ?item3\n" + 
"    }\n" + 
"  } UNION {\n" + 
"    BIND(\"Illegal Index\" AS ?error)\n" + 
"    ?container ?containerMembershipProperty ?item .\n" + 
"    BIND(xsd:integer(STRAFTER(STR(?containerMembershipProperty),\"http://www.w3.org/1999/02/22-rdf-syntax-ns#_\")) AS ?index)\n" + 
"    FILTER (?index < 1)\n" + 
"  }\n" + 
"}\n" + 
"ORDER BY ?container ?containerMembershipProperty ?item");
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		
		while(rs.hasNext()) {
			QuerySolution qs = rs.next();
			String text = "\n" + qs.get("container").toString() + " " + qs.get("containerMembershipProperty").toString() + " " + qs.get("item").toString(); 
			Failure failure = new Failure(qs.get("error").toString(), this.severity, qs.get("item").toString() ,failureDescription + text);
			failures.add(failure);
		}
		
		return failures;
	}

}
