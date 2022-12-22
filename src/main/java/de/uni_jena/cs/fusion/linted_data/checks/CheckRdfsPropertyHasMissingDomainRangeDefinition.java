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
 * Object and datatype properties should have a defined domain and range
 * 
 * Properties that have defined only domain OR range are reported as well as
 * properties that are missing both
 */
public final class CheckRdfsPropertyHasMissingDomainRangeDefinition extends SparqlSelectCheck {

	public CheckRdfsPropertyHasMissingDomainRangeDefinition() {
		super(Level.SPARQL, Scope.RDFS, Severity.WARN,
				"The property doesn't have a domain or range or none of them",
				CheckRdfsPropertyHasMissingDomainRangeDefinition.class.getClassLoader()
						.getResourceAsStream("CheckRdfsPropertyHasMissingDomainRangeDefinition.rq"));
	}

	@Override
	protected List<Failure> execute(ResultSet rs, String failureDescription) {
		List<Failure> failures = new ArrayList<>();
		while (rs.hasNext()) {
			QuerySolution qs = rs.next();
			String uri = qs.get("property").toString();
			String error = qs.get("error").toString();
			Failure f = new Failure(name, severity, uri,
					failureDescription + "\n" + uri + " " + error);
			failures.add(f);
		}
		return failures;
	}

}
