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

package de.uni_jena.cs.fusion.linted_data;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RiotException;

import de.uni_jena.cs.fusion.linted_data.types.Scope;
import de.uni_jena.cs.fusion.linted_data.util.FileUtil;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "LintedData", mixinStandardHelpOptions = true, version = "0.0.1", description = "Performs checks on a provided RDF file.")
public class LintedData implements Callable<Integer> {

	@Parameters(index = "0", description = "The file that will be checked")
	File inputFile;

	@Parameters(index = "1", description = "The file where the result will be stored")
	File outputFile;

	@Option(names = { "-s",
			"--scope" }, split = ",", description = "Which scopes will be checked (RDF, RDFS or OWL)\ndefault is all three", defaultValue = "RDF,RDFS,OWL")
	Scope[] scopes;

	class ScopeConverter implements ITypeConverter<Scope> {
		@Override
		public Scope convert(String value) throws Exception {
			switch (value) {
			case "RDF":
				return Scope.RDF;
			case "RDFS":
				return Scope.RDFS;
			case "OWL":
				return Scope.OWL;
			default:
				throw new Exception();
			}
		}
	}

	public Integer call() {
		if (!inputFile.exists()) {
			System.err.println(
					inputFile.getAbsolutePath() + " doesn't exist.\n" + "Please check the path and start again.");
			return -1;
		}
		Dataset dataset = DatasetFactory.create();
		try {
			RDFParser.source(inputFile.getAbsolutePath()).parse(dataset);
		} catch (RiotException e) {
			System.err.println("The file can't be parsed");
			return -1;
		}
		System.out.println("Input parsed successfull");
		if (!FileUtil.checkOutputFileExtension(outputFile)) {
			System.err.println("The output file must be an xml file");
			return -1;
		}

		// to prevent errors when saving the results, create missing parent directories
		// if necessary
		if (!outputFile.exists() && outputFile.getParentFile() != null && outputFile.getParentFile().mkdirs()) {
			System.out.println("Created missing parent directories");
		}

		try {
			new Runner(scopes, inputFile, outputFile);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		System.out.println("Finished");

		return 0;
	}

	public static void main(String[] args) {
		int exitCode = new CommandLine(new LintedData()).execute(args);
		System.exit(exitCode);
	}

}
