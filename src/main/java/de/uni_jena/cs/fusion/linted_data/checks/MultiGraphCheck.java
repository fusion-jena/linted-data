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

import java.io.File;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Level;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.types.Scope;

/**
 * Checks that are executed on multiple graphs
 * 
 * 
 */
public abstract class MultiGraphCheck extends FileCheck {
	
	protected MultiGraphCheck (Level level, Scope scope, Severity severity, String name) {
		super(level, scope, severity, name);
	}
	
	@Override
	public List<Failure> execute(File file, String failureDescription) {
		Dataset dataset = DatasetFactory.create();

		RDFParser.source(file.getAbsolutePath()).parse(dataset);
		
		return this.execute(dataset, failureDescription);
	}
	
	public abstract List<Failure> execute(Dataset dataset, String failureDescription);


}
