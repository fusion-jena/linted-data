package de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

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
	@JacksonXmlProperty(isAttribute = true)
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
	
	
	public String getFailureElement() {
		return failureElement;
	}
	
	public String getText() {
		return text;
	}
}
