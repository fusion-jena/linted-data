package de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * represent a single testuites
 * renamed it to TestuiteManager to make the classes more differenciable
 * 
 * contains the elements from JUnit XML format for testuites
 * see {@link https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format}
 */
@JsonRootName(value = "testsuites")
public class TestsuiteManager {
	/**
	 * testsuites that belong to all testuiteManager
	 * 
	 * all testsuites that are executed
	 */
	@JacksonXmlProperty(localName = "testsuite")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Testsuite> testsuites; 
	/**
	 * reference for the initialisation of the id
	 */
	@JsonIgnore
	private static long idCounter = 0;
	/**
	 * ID of the scan
	 */
	@JacksonXmlProperty(isAttribute = true)
	private long id;
	
	/**
	 * label of the scan
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	
	public TestsuiteManager(String name, List<Testsuite> testsuites) {
		this.id = TestsuiteManager.idCounter;
		TestsuiteManager.idCounter++;
		this.name = name;
		this.testsuites = testsuites;
	}

	/**
	 * @return time that was required to process all the rules in seconds
	 */
	@JacksonXmlProperty(isAttribute = true, localName ="time")
	public double getTime() {
		long sum = 0L;
		for (Testsuite testsuite : testsuites) {
			sum += testsuite.getTime_ms();
		}
		// convert from ms to s
		return (double) sum / 1000;
	}
	
	/**
	 * @return total number of rules that were applied
	 */
	@JacksonXmlProperty(isAttribute=true, localName ="tests")
	public long getTests() {
		long sum = 0L;
		for (Testsuite testsuite : testsuites) {
			sum += testsuite.getTests();
		}
		return sum;
	}
	
	/**
	 * @return total number of rule violations
	 */
	@JacksonXmlProperty(isAttribute=true, localName ="failures")
	public long getFailures() {
		long sum = 0L;
		for (Testsuite testsuite : testsuites) {
			sum += testsuite.getFailures();
		}
		return sum;
	}
	
	public List<Testsuite> getTestsuites(){
		return testsuites;
	}
	
	public String getName() {
		return name;
	}
	
}
