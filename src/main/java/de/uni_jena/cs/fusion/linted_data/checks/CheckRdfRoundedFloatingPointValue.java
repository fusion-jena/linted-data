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

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;
import de.uni_jena.cs.fusion.linted_data.util.StringUtil;

/**
 * Check whether the lexical values of xsd:double and xsd:float can be exactly
 * represented
 *
 */
public final class CheckRdfRoundedFloatingPointValue extends GraphCheck {

	public CheckRdfRoundedFloatingPointValue() {
		super(Level.GRAPH, Scope.RDF, Severity.WARN,
				"Lexical representation is not in the values space of the selected floating point datatype and will get rounded.");
	}

	@Override
	public List<Failure> execute(Model model, String failureDescription) {
		List<Failure> failures = new ArrayList<Failure>();

		// iterate over all statements
		StmtIterator statements = model.listStatements();
		while (statements.hasNext()) {
			Statement statement = statements.next();
			RDFNode object = statement.getObject();

			// only statements with an literal as object are investigated
			if (!object.isLiteral()) {
				continue;
			}

			RDFDatatype type = object.asLiteral().getDatatype();
			// only literals with the data type float or double are investigated
			// only if the literal can't be exactly represented in the data type, a new
			// failure is created and added to the list
			if (type.equals(XSDDatatype.XSDfloat)
					&& !StringUtil.isPreciseRepresentableAsFloat(object.asLiteral().getLexicalForm())) {
				Failure failure = new Failure(this.name, this.severity, object.asLiteral().toString(),
						failureDescription + "\n" + statement.toString().replace("[", "").replace("]", "").replace(",", ""));
				failures.add(failure);
			} else if (type.equals(XSDDatatype.XSDdouble)
					&& !StringUtil.isPreciseRepresentableAsDouble(object.asLiteral().getLexicalForm())) {
				Failure failure = new Failure(this.name, this.severity, object.asLiteral().toString(),
						failureDescription + "\n" + statement.toString().replace("[", "").replace("]", "").replace(",", ""));
				failures.add(failure);
			}
		}
		return failures;
	}

}
