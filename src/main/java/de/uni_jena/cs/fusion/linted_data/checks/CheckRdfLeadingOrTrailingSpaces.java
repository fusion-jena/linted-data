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
 * Finds all literals that start or end with a whitespace character
 * 
 * 
 * \s doesn't exist in SPARQL so the equivalent form [\r\n\t\f\v ] is used
 * <p>
 * \v also doesn't exist in SPARQL and is hence not checked
 */
public final class CheckRdfLeadingOrTrailingSpaces extends SparqlSelectCheck {

	private static final String literal = "literal";
	private static final String subject = "subject";
	private static final String predicate = "predicate";
	private static final String object = "object";

	public CheckRdfLeadingOrTrailingSpaces() {
		super(Scope.RDF, Severity.INFO, "Literals shouldn't start or end with white spaces", CheckRdfLeadingOrTrailingSpaces.class.getClassLoader()
				.getResourceAsStream("CheckRdfLeadingOrTrailingSpaces.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (rs.hasNext()) {
			String description = failureDescription + "\n";
			QuerySolution qs = rs.next();
			description += qs.get(subject).toString() + " " + qs.get(predicate).toString() + " "
					+ qs.get(object).toString() + "\n";
			Failure f = new Failure(this.name, this.severity, qs.getLiteral(literal).getLexicalForm(), description);
			failures.add(f);
		}
		return failures;
	}

}
