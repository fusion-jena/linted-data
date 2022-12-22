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

package de.uni_jena.cs.fusion.linted_data.JUnitXML;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import de.uni_jena.cs.fusion.linted_data.types.Severity;

/**
 * represents a single failure
 * 
 * contains the elements from JUnit XML format
 * see {@link https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format}
 */
public class Failure {
	
	/**
	 * the rule that is violated
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String message;
	/**
	 *  severity of the rule / failure
	 */
	@JacksonXmlProperty(isAttribute = true)
	private Severity severity;
	/**
	 * the element that created the failure 
	 */
	@JsonIgnore
	private String failureElement;
	/**
	 * description of the location of the failure element
	 * can also contain context
	 * 
	 * can vary from: file and line, to graph, statement
	 */
	@JacksonXmlText
	private String text;
	
	public Failure(String message, Severity severity, String failureElement, String text) {
		this.message = message;
		this.severity = severity;
		this.failureElement = failureElement;
		this.text = text;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Severity getSeverity() {
		return severity;
	}
	
	@JsonIgnore
	public String getFailureElement() {
		return failureElement;
	}
	
	public String getText() {
		return text;
	}
}
