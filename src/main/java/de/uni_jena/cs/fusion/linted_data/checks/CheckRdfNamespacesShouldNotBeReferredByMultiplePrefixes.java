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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * each namespace should be referred by exactly one prefix
 * 
 * checks whether each namespace IRI is associated with a set of prefixes of
 * size 1
 * <p>
 * in case of p1 : IRI1 ... p1 : IRI1 no failure is created, this case is
 * checked in {@link CheckRdfPrefixesReferToOneNamespace}
 * <p>
 * for each namespace with more than one prefix a failure is created
 */
public final class CheckRdfNamespacesShouldNotBeReferredByMultiplePrefixes extends GraphCheck {

	public CheckRdfNamespacesShouldNotBeReferredByMultiplePrefixes() {
		super(Scope.RDF, Severity.INFO, "The namespace is refered by multiple prefixes");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		// for each namespace URI the associated prefixes
		Map<String, Set<String>> namespacePrefixMap = new HashMap<>();

		Map<String, String> prefixMap = model.getNsPrefixMap();
		// iterate over all prefixes and add them to the reverse map
		for (String prefix : prefixMap.keySet()) {
			String namespace = prefixMap.get(prefix);
			namespacePrefixMap.putIfAbsent(namespace, new HashSet<String>());
			namespacePrefixMap.get(namespace).add(prefix);
		}

		List<Failure> failures = new ArrayList<>();
		// find all namespaces that have two or more associated prefixes
		// for each of these namespaces create a failure
		for (String namespace : namespacePrefixMap.keySet()) {
			if (namespacePrefixMap.get(namespace).size() > 1) {
				Failure f = new Failure(name, severity, namespace, failureDescription + "\nNamespace " + namespace
						+ " is referred by the " + namespacePrefixMap.get(namespace).size() + " prefixes: "
						+ namespacePrefixMap.get(namespace).stream().sorted().collect(Collectors.joining(", ")));
				failures.add(f);
			}
		}
		return failures;
	}

}
