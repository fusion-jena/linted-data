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

package de.uni_jena.cs.fusion.linted_data.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

public class CheckRdfsMultipleDomainRangeTest {

	private CheckRdfsMultipleDomainRange check = new CheckRdfsMultipleDomainRange();

	/**
	 * properties that have more than one defined domain
	 */
	@Test
	void multipleDomain() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsMultipleDomainRange/multipleDomain_01.xml", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-5]",
				Severity.WARN));
	}

	/**
	 * properties that have more than one defined range
	 */
	@Test
	void multipleRanges() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsMultipleDomainRange/multipleRange_01.ttl", check);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has the 3 defined ranges [http://www.w3.org/2001/XMLSchema#boolean, http://www.w3.org/2001/XMLSchema#int, http://www.w3.org/2001/XMLSchema#integer]",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]",
				Severity.WARN));
	}

	/**
	 * Properties that have multiple domains and ranges defined
	 */
	@Test
	void multipleDomainAndRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsMultipleDomainRange/multipleDomainAndRange_01.ttl", check);
		assertNotNull(failures);
		assertEquals(6, failures.size());
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-3 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-2 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-1 has the 3 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-3 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-1, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4] and the 3 defined ranges [http://www.w3.org/2001/XMLSchema#boolean, http://www.w3.org/2001/XMLSchema#int, http://www.w3.org/2001/XMLSchema#integer]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#d-property-1 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-2, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3] and the 2 defined ranges [http://www.w3.org/2001/XMLSchema#integer, http://www.w3.org/2001/XMLSchema#short]"));
		assertTrue(TestUtil.contains(failures,
				"http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-8",
				"\nModel: Default Model\nhttp://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#property-8 has the 2 defined domains [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4] and the 2 defined ranges [http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-3, http://www.semanticweb.org/ontologies/2022/9/untitled-ontology-12#class-4]"));
	}

	@Test
	void singleDomainRange() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsMultipleDomainRange/singleDomainAndRange_01.xml", check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

	@Test
	void intersectionInsteadOfConjunction() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfsMultipleDomainRange/intersectionDomainRange_01.xml",
				check);
		assertNotNull(failures);
		assertEquals(0, failures.size());
	}

}
