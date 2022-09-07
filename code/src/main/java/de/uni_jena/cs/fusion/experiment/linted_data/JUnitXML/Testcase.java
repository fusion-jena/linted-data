package de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import de.uni_jena.cs.fusion.experiment.linted_data.checks.Check;

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
	 * elements where the test failed
	 */
	@JacksonXmlProperty(localName = "failure")
	@JacksonXmlElementWrapper(useWrapping = false)
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

	public Testcase(Check check, List<Failure> failures) {
		this.id = check.getClass().getCanonicalName();
		this.check = check;
		this.failures = failures;
	}

	public Check getCheck() {
		return check;
	}

	public List<Failure> getFailures() {
		return failures;
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
		return (double) check.getTime()/1000; 
	}
	
	
}
