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
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * check if multiple domains or ranges are defined for a property
 * 
 * multiple domain and range are allowed, but are treated as conjunction, what
 * is equivalent to owl:intersectionOf
 *
 */
public final class CheckRdfsMultipleDomainRange extends SparqlSelectCheck {

	public CheckRdfsMultipleDomainRange() {
		super(Level.SPARQL, Scope.RDFS, Severity.WARN,
				"Multiple domains or ranges are interpreted as conjunction and should therefore be replaced with an owl:intersectionOf.",
				CheckRdfsMultipleDomainRange.class.getClassLoader().getResourceAsStream("CheckRdfsMultipleDomainRange.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure f = new Failure(name, severity, qs.get("property").toString(), failureDescription + "\n"
					+ qs.get("property").toString() + " " + qs.get("error").toString());
			failures.add(f);
		}
		return failures;
	}

}
