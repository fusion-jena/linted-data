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
package de.uni_jena.cs.fusion.linted_data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;

/**
 * contains utility methods for files
 */
public abstract class FileUtil {

	/**
	 * check if the output file is an xml document
	 * 
	 * @param file its extension will be checked
	 * @return true if the file extension is xml, else false
	 */
	public static final boolean checkOutputFileExtension(File file) {
		return checkFileExtension(file, Arrays.asList("xml"));
	}

	/**
	 * check if the file is in a valid format
	 * 
	 * @param file       its extension will be checked
	 * @param extensions valid file extensions
	 * @return true if the file extension is in extensions, else false
	 */
	private static final boolean checkFileExtension(File file, List<String> extensions) {
		String[] partOfFileName = file.getName().split("\\.");
		// the file extension is the substring after the last .
		String fileExtension = partOfFileName[partOfFileName.length - 1];

		return extensions.contains(fileExtension);
	}

	public static final Lang getLang(File file, List<Lang> allowedLanguages) {
		for (Lang language : allowedLanguages) {
			Dataset dataset = DatasetFactory.create();
			try {
				RDFParser.source(file.getAbsolutePath()).forceLang(language).parse(dataset);
				return language;
			} catch (Throwable e) {
				continue;
			}
		}
		return null;
	}

	/**
	 * reads a file line by line and returns its content as String
	 * 
	 * @param file that will be parsed
	 * @return String representation of the file content
	 * @throws IOException in case that the file not exists, or an error occurs
	 *                     while reading the file or when closing the reader
	 */
	public static final String readFile(File file) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(file));
		StringBuilder builder = new StringBuilder();

		String currentLine = r.readLine();
		while (currentLine != null) {
			builder.append(currentLine);
			builder.append("\n");
			currentLine = r.readLine();
		}
		r.close();
		return builder.toString();
	}

	public static final String readFile(InputStream file) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(file));
		StringBuilder builder = new StringBuilder();

		String currentLine = r.readLine();
		while (currentLine != null) {
			builder.append(currentLine);
			builder.append("\n");
			currentLine = r.readLine();
		}
		r.close();
		return builder.toString();
	}
}
