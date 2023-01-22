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
import java.io.IOException;
import java.io.InputStream;

import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;
import de.uni_jena.cs.fusion.linted_data.util.FileUtil;

/**
 * Checks that can be formulated as SPARQL query
 */
public abstract class SparqlCheck extends GraphCheck {
	/**
	 * the query that will be executed against the model
	 */
	protected final String query;

	protected SparqlCheck(Scope scope, Severity severity, String name, String query) {
		super(scope, severity, name);
		this.query = query;
	}

	protected SparqlCheck(Scope scope, Severity severity, String name, File queryFile) {
		super(scope, severity, name);
		this.query = readQuery(queryFile);
	}

	protected SparqlCheck(Scope scope, Severity severity, String name, InputStream queryFile) {
		super(scope, severity, name);
		this.query = readQuery(queryFile);
	}

	/**
	 * parses the file containing the SPARQL query and returns the query as a String
	 * 
	 * the IOException can't occur during runtime
	 * 
	 * @param file contains the SPARQL query, is parsed
	 * @return String representing the SPARQL query
	 */
	private String readQuery(File file) {
		try {
			return FileUtil.readFile(file);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * parses the inputStream containing the SPARQL query and returns the query as a
	 * String
	 * 
	 * the IOException can't occur during runtime
	 * 
	 * @param inputStream contains the SPARQL query, is parsed
	 * @return String representing the SPARQL query
	 */
	private String readQuery(InputStream inputStream) {
		try {
			return FileUtil.readFile(inputStream);
		} catch (IOException e) {
			return null;
		}
	}

}
