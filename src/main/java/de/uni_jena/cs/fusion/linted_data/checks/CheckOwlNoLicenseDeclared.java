/**
 * Copyright © 2022 Merle Gänßinger (merle.gaenssinger@uni-jena.de)
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
 * Check if an ontology has a license defined
 * 
 * check if one of the four license properties: dc:rights, dcterms:license,
 * schema:license or cc:license is used
 */
public class CheckOwlNoLicenseDeclared extends SparqlSelectCheck {

	public CheckOwlNoLicenseDeclared() {
		super(Scope.OWL, Severity.ERROR, "A licence should be declared for the ontology.",
				CheckOwlNoLicenseDeclared.class.getClassLoader().getResourceAsStream("CheckOwlNoLicenseDeclared.rq"));
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