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
 * check for IRIs whose local name is longer than 36 characters
 * 
 * the number of 36 is chosen arbitrary based on the following considerations:
 * <p>
 * 1. the number of characters divided by 3 which fit in one row with 1920x1080
 * and 11pt font size is larger
 * <p>
 * 2. number of characters that are too much to type
 * <p>
 * 3. UUIDs are valid local names
 * <p>
 * TODO more considerations?
 */
public final class CheckRdfIrisTooLong extends SparqlSelectCheck {

	public CheckRdfIrisTooLong() {
		super(Scope.RDF, Severity.INFO, "Local names shouldn't be longer than 36 characters", CheckRdfIrisTooLong.class.getClassLoader().getResourceAsStream("CheckRdfIrisTooLong.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();

		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("iri").toString(),
					failureDescription + "\n" + qs.get("iri").toString() + " has a local name that has "
							+ qs.get("numExcessCharacters").asLiteral().getInt() + " more characters than 36");
			failures.add(failure);
		}

		return failures;
	}

}
