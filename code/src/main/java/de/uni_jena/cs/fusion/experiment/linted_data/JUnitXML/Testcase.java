package de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import de.uni_jena.cs.fusion.experiment.linted_data.checks.Check;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

/**
 * represent a single testcase
 *
 * contains the elements from JUnit XML format see
 * {@link https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format}
 */
public class Testcase {

	/**
	 * child elements
	 * 
	 * elements where the test failed exported with
	 * {@link Testcase#getFailuresAsOne}
	 */
	@JsonIgnore
	private List<Failure> failures;
	/**
	 * ID of the rule
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String id;

	/**
	 * the check the the testcase is representing
	 */
	@JsonIgnore
	private Check check;

	@JacksonXmlProperty(localName = "classname", isAttribute = true)
	private String testsuiteName;

	public Testcase(Check check, List<Failure> failures, String testsuiteName) {
		this.id = check.getClass().getCanonicalName();
		this.testsuiteName = testsuiteName;
		this.check = check;
		this.failures = failures;
	}

	public Check getCheck() {
		return check;
	}

	/**
	 * takes up to 100 failures that were found at the testcase, combines their
	 * information into one object
	 * 
	 * @return list with a maximum of one failure, containing the description of all
	 *         failures
	 */
	@JacksonXmlProperty(isAttribute = false, localName = "failure")
	@JacksonXmlElementWrapper(useWrapping = false)
	public List<Failure> getFailuresAsOne() {
		List<Failure> failures = this.getFailures();
		if (failures.size() != 0) {
			Iterator<Failure> it = failures.iterator();
			Failure f = it.next();
			String message = f.getMessage();
			Severity severity = f.getSeverity();
			String failureElement = f.getFailureElement();
			String text = f.getText();
			while (it.hasNext()) {
				text += "\n" + it.next().getText();
			}
			failures.clear();
			failures.add(new Failure(message, severity, failureElement, text));
		}
		return failures;
	}

	@JsonIgnore
	public List<Failure> getFailures() {
		return failures.subList(0, Math.min(100, failures.size()));
	}

	@JsonIgnore
	public int failureSize() {
		return failures.size();
	}

	public String getId() {
		return id;
	}

	@JacksonXmlProperty(isAttribute = true, localName = "name")
	public String getName() {
		return check.getName();
	}

	/**
	 * @return the duration of the check in milliseconds
	 */
	@JsonIgnore
	public long getTime_ms() {
		return check.getTime();
	}

	/**
	 * @return the duration of the check in seconds
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "time")
	public double getTime_s() {
		// convert from ms to s
		return (double) check.getTime() / 1000;
	}

}
