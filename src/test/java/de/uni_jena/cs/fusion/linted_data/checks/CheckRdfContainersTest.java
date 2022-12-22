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

public class CheckRdfContainersTest {

	private CheckRdfContainers check = new CheckRdfContainers();

	/**
	 * an index is used twice
	 */
	@Test
	public void duplicatedIndex() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfContainers/duplicatedIndex_01.ttl", check);

		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org/b1",
				"\nModel: Default Model\nDuplicated Index in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_1 http://example.org/b1",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org/b2",
				"\nModel: Default Model\nDuplicated Index in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_1 http://example.org/b2",
				Severity.WARN));
	}

	/**
	 * an element doesn't have a predecessor
	 */
	@Test
	public void missingPredecessor() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfContainers/missingPredecessor_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/a1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_2 http://example.org/a1",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfContainers/missingPredecessor_02.ttl", check);
		assertNotNull(failures);
		assertEquals(failures.size(), 2);
		assertTrue(TestUtil.contains(failures, "http://example.org/a1",
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_2 http://example.org/a1",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://example.org/a3",
				"\nModel: Default Model\nMissing Predecessor in http://example.org/a http://www.w3.org/1999/02/22-rdf-syntax-ns#_5 http://example.org/a3",
				Severity.WARN));
	}

	/**
	 * an element uses an illegal index -> a number <= 0
	 */
	@Test
	public void illegalIndex() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfContainers/illegalIndex_01.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://example.org/d1", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nIllegal Index in http://example.org/d http://www.w3.org/1999/02/22-rdf-syntax-ns#_0 http://example.org/d1",
				failure.getText());

		failures = TestUtil.executeCheck("CheckRdfContainers/illegalIndex_02.ttl", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals("http://example.org/d2", failure.getFailureElement());
		assertEquals(Severity.WARN, failure.getSeverity());
		assertEquals(
				"\nModel: Default Model\nIllegal Index in http://example.org/d http://www.w3.org/1999/02/22-rdf-syntax-ns#_-1 http://example.org/d2",
				failure.getText());
	}

	/**
	 * all indices are valid
	 */
	@Test
	public void validContainers() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfContainers/validContainers_01.ttl", check);
		assertNotNull(failures);
		assertEquals(failures.size(), 0);
	}

}
