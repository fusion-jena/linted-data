package de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * represent a single testuite
 * 
 * contains the elements from JUnit XML format
 * see {@link https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format}
 */
public class Testsuite {
	
	/**
	 * testcases that belong to the testsuite
	 */
	@JacksonXmlProperty(localName = "testcase")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Testcase> testcases;
	/**
	 * reference for the initialisation of the id
	 */
	@JsonIgnore
	private static long idCounter = 0;
	/**
	 * ID of the provider
	 */
	@JacksonXmlProperty(isAttribute = true)
	private long id;
	/**
	 * label of the provider
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	public Testsuite(String name, List<Testcase> testcases) {
		this.name = name;
		this.testcases = testcases;
		this.id = Testsuite.idCounter;
		Testsuite.idCounter++;
	}
	
	/**
	 * @return total number of rules that were applied
	 */
	@JacksonXmlProperty(isAttribute=true, localName ="tests")
	public long getTests() {
		return testcases.size();
	}
	
	/**
	 * @return total number of rule violations
	 */
	@JacksonXmlProperty(isAttribute=true, localName ="failures")
	public long getFailures() {
		long sum = 0L;
		for (Testcase testcase : testcases) {
			sum += testcase.getFailures().size();
		}
		return sum;
	}
	
	/**
	 * @return time that was required to process the rules in the provider
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "time")
	public long getTime() {
		long sum = 0L;
		for (Testcase testcase : testcases) {
			sum += testcase.getTime();
		}
		return sum;
	}
	
	public List<Testcase> getTestcases(){
		return testcases;
	}
	
	
}
