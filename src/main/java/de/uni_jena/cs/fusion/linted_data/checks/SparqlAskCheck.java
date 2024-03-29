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

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * SPARQL check whose query is a SPARQL ASK query
 */
public abstract class SparqlAskCheck extends SparqlCheck {

	public SparqlAskCheck(Scope scope, Severity severity, String name, String query) {
		super(scope, severity, name, query);
	}

	public SparqlAskCheck(Scope scope, Severity severity, String name, File queryFile) {
		super(scope, severity, name, queryFile);
	}

	public SparqlAskCheck(Scope scope, Severity severity, String name, InputStream queryFile) {
		super(scope, severity, name, queryFile);
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);

		return execute(queryExecution.execAsk(), failureDescription);
	}

	protected abstract List<Failure> execute(boolean b, String failureDescription);

}
