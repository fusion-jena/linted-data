/**
 * Copyright 2022 Merle Gänßinger (merle.gaenssinger@uni-jena.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_jena.cs.fusion.linted_data.checks;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Check if RDF containers use illegal, repeated or missed out index numbers
 * 
 * see
 * {@link https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/}
 *
 */
public final class CheckRdfContainers extends SparqlSelectCheck {

	public CheckRdfContainers() {
		// SPARQL query from
		// https://stackoverflow.com/questions/72434963/validating-rdfseq-with-sparql/72449757#72449757
		super(Scope.RDF, Severity.WARN, "The RDF container uses  one of illegal, repeated, missed out index numbers",
				CheckRdfContainers.class.getClassLoader().getResourceAsStream("CheckRdfContainers.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();

		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			String text = "\n" + qs.get("error").toString() + " in " + qs.get("container").toString() + " " + qs.get("containerMembershipProperty").toString()
					+ " " + qs.get("item").toString();
			Failure failure = new Failure(name, this.severity, qs.get("item").toString(),
					failureDescription + text);
			failures.add(failure);
		}

		return failures;
	}
	

}
