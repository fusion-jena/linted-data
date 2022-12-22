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

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * represent a single testuites renamed it to TestuiteManager to make the
 * classes more differenciable
 * 
 * contains the elements from JUnit XML format for testuites see
 * {@link https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format}
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
	 * ID of the scan
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String id;

	/**
	 * label of the scan
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	@JsonIgnore
	private DecimalFormat decimalFormat;
	
	public TestsuiteManager(String name, List<Testsuite> testsuites, DecimalFormat decimalFormat) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		this.id = dtf.format(now);
		this.name = name;
		this.testsuites = testsuites;
		this.decimalFormat = decimalFormat;
	}
	
	/**
	 * @return time that was required to process all the rules in seconds
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "time")
	public String getTimeInSeconds() {
		return decimalFormat.format(getTime_s());
	}
	
	/**
	 * @return time that was required to process all the rules in seconds
	 */
	@JsonIgnore
	public double getTime_s() {
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
	@JacksonXmlProperty(isAttribute = true, localName = "tests")
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
	@JacksonXmlProperty(isAttribute = true, localName = "failures")
	public long getFailures() {
		long sum = 0L;
		for (Testsuite testsuite : testsuites) {
			sum += testsuite.getFailures();
		}
		return sum;
	}

	public List<Testsuite> getTestsuites() {
		return testsuites;
	}

	public String getName() {
		return name;
	}

}
