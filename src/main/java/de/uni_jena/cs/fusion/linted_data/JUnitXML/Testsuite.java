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
	 * ID of the provider
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	/**
	 * label of the provider
	 */
	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JsonIgnore
	private DecimalFormat decimalFormat;

	public Testsuite(String id, String name, List<Testcase> testcases, DecimalFormat decimalFormat) {
		this.name = name;
		this.testcases = testcases;
		this.id = id;
		this.decimalFormat = decimalFormat;
	}

	/**
	 * @return total number of rules that were applied
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "tests")
	public long getTests() {
		return testcases.size();
	}

	/**
	 * @return total number of rule violations
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "failures")
	public long getFailures() {
		long sum = 0L;
		for (Testcase testcase : testcases) {
			sum += testcase.failureSize();
		}
		return sum;
	}

	/**
	 * @return time that was required to process the rules in the provider in
	 *         milliseconds
	 */
	@JsonIgnore
	public long getTime_ms() {
		long sum = 0L;
		for (Testcase testcase : testcases) {
			sum += testcase.getTime_ms();
		}
		return sum;
	}

	/**
	 * @return time that was required to process the rules in the provider in
	 *         seconds
	 */
	@JacksonXmlProperty(isAttribute = true, localName = "time")
	public String getTimeInSeconds() {
		return decimalFormat.format(getTime_s());
	}

	/**
	 * @return time that was required to process the rules in the provider in
	 *         seconds
	 */
	@JsonIgnore
	public double getTime_s() {
		long sum = 0L;
		for (Testcase testcase : testcases) {
			sum += testcase.getTime_ms();
		}
		// convert from ms to s
		return (double) sum / 1000;
	}

	public List<Testcase> getTestcases() {
		return testcases;
	}

}
