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
 * check if a symmetric property is defined as an inverse property of another
 * property
 * 
 * If a symmetric property is the inverse of an other property, these two
 * properties are equivalent. This could be stated easier with
 * owl:equivalentProperty.
 *
 */
public class CheckOwlInverseRelationshipForSymmetricProperty extends SparqlSelectCheck {

	public CheckOwlInverseRelationshipForSymmetricProperty() {
		super(Scope.OWL, Severity.WARN, "An inverse relationship is declared for a symmetric relationship. Better use \"owl:equivalentProperty.\"",
				CheckOwlInverseRelationshipForSymmetricProperty.class.getClassLoader().getResourceAsStream("CheckOwlInverseRelationshipForSymmetricProperty.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet resultSet, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();
		while (resultSet.hasNext()) {
			QuerySolution qs = resultSet.next();
			Failure failure = new Failure(name, severity, qs.get("inverse").toString(),
					failureDescription + "\n" + qs.get("inverse").toString() + " is defined as inverse property of the symmetric property "
							+ qs.get("property").toString());
			failures.add(failure);
		}

		return failures;
	}

}
