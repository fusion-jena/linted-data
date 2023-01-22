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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Finds all namespace definitions where the IRI doesn't end with a # or /
 */
public final class CheckRdfNamespacesShouldNotOmitTheSeperator extends GraphCheck {

	public CheckRdfNamespacesShouldNotOmitTheSeperator() {
		super(Scope.RDF, Severity.WARN, "Namespaces should not omit the seperator (#, /)");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<>();

		// prefix URIs that omit / or #
		Set<String> namespacesWithoutSeparator = new HashSet<String>();
		Map<String, String> map = model.getNsPrefixMap();
		for (String s : map.values()) {
			// when the namespace ends with # or /, it is a valid namespace
			if (!s.endsWith("#") && !s.endsWith("/")) {
				// else it is an invalid namespace
				namespacesWithoutSeparator.add(s);
			}
		}

		// for each namespace that omits the separator create a failure
		for (String namespace : namespacesWithoutSeparator) {
			Failure f = new Failure(name, severity, namespace,
					failureDescription + "\nThe namespace " + namespace + " omits the seperator at the end");
			failures.add(f);
		}

		return failures;
	}

}
