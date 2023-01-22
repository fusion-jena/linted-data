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
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * checks that are executed on a single graph
 *
 */
public abstract class GraphCheck extends MultiGraphCheck {

	protected GraphCheck(Scope scope, Severity severity, String name) {
		super(scope, severity, name);
	}

	@Override
	public List<Failure> execute(Dataset dataset, String failureDescription) {

		List<Failure> failures = new ArrayList<Failure>();
		
		if (dataset.getDefaultModel() != null) {
			String currentFailureDescription = failureDescription + "\n";
			currentFailureDescription += "Model: " + "Default Model";
			failures.addAll(this.execute(dataset.getDefaultModel(), currentFailureDescription));
		}
		// check the named models
		Iterator<String> it = dataset.listNames();
		while (it.hasNext()) {
			String name = it.next();
			String currentFailureDescription = failureDescription + "\n";
			currentFailureDescription += "Model: " + name;
			failures.addAll(this.execute(dataset.getNamedModel(name), currentFailureDescription));
		}
		return failures;
	}

	public abstract List<Failure> execute(Model model, String failureDescription);
}
