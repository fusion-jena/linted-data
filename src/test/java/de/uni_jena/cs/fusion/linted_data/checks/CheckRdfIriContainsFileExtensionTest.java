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

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.types.Severity;
import de.uni_jena.cs.fusion.linted_data.util.TestUtil;

/**
 * The file extension at the end of the function name indicates the file format
 * as well as the found extensions
 */
public class CheckRdfIriContainsFileExtensionTest {

	private CheckRdfIriContainsFileExtension check = new CheckRdfIriContainsFileExtension();

	@Test
	void iriContainsRj() throws Exception {
		// rj in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsRj_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#datatype-property-a contains the file extension rj",
				failures.get(0).getText());
		// rj in subject URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsRj_02.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#datatype-property-a contains the file extension rj",
				failures.get(0).getText());
		// rj in object URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsRj_03.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.rj#individual-1", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rj#individual-1 contains the file extension rj",
				failures.get(0).getText());
	}

	@Test
	void iriContainsTrix() throws Exception {
		// trix in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsTrix_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix#datatype-property-a", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix#datatype-property-a contains the file extension trix",
				failures.get(0).getText());
		// trix in subject URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsTrix_02.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix/property-a", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix/property-a contains the file extension trix",
				failures.get(0).getText());
		// trix in object URI
		file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsTrix_03.nt").getFile());
		failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		assertEquals("http://my-example.org/test.trix#individual-2", failures.get(0).getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trix#individual-2 contains the file extension trix",
				failures.get(0).getText());
	}

	@Test
	void iriContainsTrig() throws Exception {
		// trig in subject URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsTrig_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.trig#property-a", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trig#property-a contains the file extension trig"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.trig/individual-1", "File: "
				+ file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.trig/individual-1 contains the file extension trig"));
	}

	@Test
	void iriContainsRpb() throws Exception {
		// rpb in object and predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsRpb_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rpb#individual-1", "File: "
				+ file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#individual-1 contains the file extension rpb"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rpb#property-b", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rpb#individual-2", "File: "
				+ file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#individual-2 contains the file extension rpb"));
	}

	@Test
	void iriContainsPbrdf() throws Exception {
		// pbrdf in subject URI and rj in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsPbrdf_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://www.city.ac.uk/ds/inm713/gr.pbrdf#pizza-423", "File: "
				+ file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.pbrdf#pizza-423 contains the file extension pbrdf"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rpb#property-b", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb"));
	}

	@Test
	void iriContainsNq() throws Exception {
		// nq in subject and object URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsNq_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.nq#pizza", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.nq#pizza contains the file extension nq"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.nq/individual-1", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.nq/individual-1 contains the file extension nq"));
	}

	@Test
	void iriContainsRt() throws Exception {
		// rt in subject, predicate and object iri
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsRt_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://my-example.org#testr.rt#pizza", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org#testr.rt#pizza contains the file extension rt"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rt/individual-1", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rt/individual-1 contains the file extension rt"));
		assertTrue(TestUtil.contains(failures, "http://example.rt/test#property-b", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://example.rt/test#property-b contains the file extension rt"));
	}

	@Test
	void iriContainsTrdf() throws Exception {
		// trdf in subject URI and rpb in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsTrdf_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://www.city.ac.uk/ds/inm713/gr.trdf#pizza-423", "File: "
				+ file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.trdf#pizza-423 contains the file extension trdf"));
		assertTrue(TestUtil.contains(failures, "http://my-example.org/test.rpb#property-b", "File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.rpb#property-b contains the file extension rpb"));
	}

	@Test
	void iriContainsShacl() throws Exception {
		// shacl in subject URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsShaclc_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.shaclc#pizza-423", f.getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.shaclc#pizza-423 contains the file extension shaclc",
				f.getText());
	}

	@Test
	void iriContainsShc() throws Exception {
		// shc in predicate URI
		File file = new File(this.getClass().getClassLoader()
				.getResource("CheckRdfIriContainsFileExtension/iriContainsShc_01.nt").getFile());
		List<Failure> failures = check.startExecution(file);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.shc#property-b", f.getFailureElement());
		assertEquals("File: " + file.getPath()
				+ "\nModel: Default Model\nhttp://my-example.org/test.shc#property-b contains the file extension shc",
				f.getText());
	}

	@Test
	void iriContainsNt() throws Exception {
		// nt in subject URI
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsNt_01.nt", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://my-example.org/test.nt#property-a", f.getFailureElement());
		assertEquals("\nModel: Default Model\nhttp://my-example.org/test.nt#property-a contains the file extension nt",
				f.getText());
		// nt in subject and predicate URI
		failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsNt_02.nt", check);
		assertNotNull(failures);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://my-example.org/test.nt#property-a", f.getFailureElement());
		assertEquals("\nModel: Default Model\nhttp://my-example.org/test.nt#property-a contains the file extension nt",
				f.getText());
	}

	@Test
	void iriContainsJsonld() throws Exception {
		// jsonld in subject URI
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsJsonld_01.nt", check);
		assertEquals(1, failures.size());
		Failure f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld/Local", f.getFailureElement());
		assertEquals(
				"\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld/Local contains the file extension jsonld",
				f.getText());
		// jsonld in subject URI
		failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsJsonld_02.nt", check);
		assertEquals(1, failures.size());
		f = failures.get(0);
		assertEquals("http://www.city.ac.uk/ds/inm713/gr.jsonld#Local", f.getFailureElement());
		assertEquals(
				"\nModel: Default Model\nhttp://www.city.ac.uk/ds/inm713/gr.jsonld#Local contains the file extension jsonld",
				f.getText());
	}

	@Test
	void iriContainsJsonld10() throws Exception {
		// jsonld10 in subject and object URI
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsJsonld10_01.nt",
				check);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://foo.de/example.jsonld10#property-a",
				"\nModel: Default Model\nhttp://foo.de/example.jsonld10#property-a contains the file extension jsonld10",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://www.bar.de/file.jsonld10/property-b",
				"\nModel: Default Model\nhttp://www.bar.de/file.jsonld10/property-b contains the file extension jsonld10"));
	}

	@Test
	void iriContainsJsonld11() throws Exception {
		// jsonld11 in predicate and object URI
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsJsonld11_01.nt",
				check);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://foo.de/example.jsonld11#property-a",
				"\nModel: Default Model\nhttp://foo.de/example.jsonld11#property-a contains the file extension jsonld11",
				Severity.WARN));
		assertTrue(TestUtil.contains(failures, "http://www.bar.de/file.jsonld11/property-b",
				"\nModel: Default Model\nhttp://www.bar.de/file.jsonld11/property-b contains the file extension jsonld11"));
	}

	@Test
	void iriContainsN3() throws Exception {
		// n3 in subject and object IRI
		// n3 also not before / or # -> should not be detected
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsN3_01.nt", check);
		assertEquals(1, failures.size());
		assertTrue(TestUtil.contains(failures, "http://www.foo.org/bar.n3/i-1", "\nModel: Default Model\nhttp://www.foo.org/bar.n3/i-1 contains the file extension n3"));
	}

	@Test
	void iriContainsRdf() throws Exception {
		// rdf in subject, predicate and object URI
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsRdf_01.nt", check);
		assertEquals(3, failures.size());
		assertTrue(TestUtil.contains(failures, "http://www.foo.bar/file.rdf#individual-1", "\nModel: Default Model\nhttp://www.foo.bar/file.rdf#individual-1 contains the file extension rdf"));
		assertTrue(TestUtil.contains(failures, "http://www.foo.bar/file.rdf#individual-2", "\nModel: Default Model\nhttp://www.foo.bar/file.rdf#individual-2 contains the file extension rdf"));
		assertTrue(TestUtil.contains(failures, "http://www.foo.bar/file.rdf#property-1", "\nModel: Default Model\nhttp://www.foo.bar/file.rdf#property-1 contains the file extension rdf"));
	}
	
	@Test
	void iriContainsOwl() throws Exception {
		// different models
		// subject and object URIs with owl
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsOwl_01.nq", check);
		assertEquals(4, failures.size());
		assertTrue(TestUtil.contains(failures, "http://disney.com/inside-out.owl/sadness", "\nModel: http://example.org/graphs/inside-out\nhttp://disney.com/inside-out.owl/sadness contains the file extension owl"));
		assertTrue(TestUtil.contains(failures, "http://disney.com/inside-out.owl/joy", "\nModel: http://example.org/graphs/inside-out\nhttp://disney.com/inside-out.owl/joy contains the file extension owl"));
		assertTrue(TestUtil.contains(failures, "http://pixar.com/minions.owl#gru", "\nModel: http://pixar.com/graphs/minions\nhttp://pixar.com/minions.owl#gru contains the file extension owl"));
		assertTrue(TestUtil.contains(failures, "http://pixar.com/minions.owl#lucy", "\nModel: http://pixar.com/graphs/minions\nhttp://pixar.com/minions.owl#lucy contains the file extension owl"));
	}
	
	@Test
	void iriContainsXml() throws Exception {
		// one named model
		// subject and object URI with xml
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsXml_01.nq", check);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://www.example.com/file.xml/individual-1", "\nModel: http://www.graphs.com/graph-1\nhttp://www.example.com/file.xml/individual-1 contains the file extension xml"));
		assertTrue(TestUtil.contains(failures, "http://www.example.com/file.xml/individual-2", "\nModel: http://www.graphs.com/graph-1\nhttp://www.example.com/file.xml/individual-2 contains the file extension xml"));
	}
	
	@Test
	void iriContainsTtl() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsTtl_01.nq", check);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals("http://www.example.com/file.ttl/individual-1", failure.getFailureElement());
		assertEquals("\nModel: http://www.graphs.com/graph-1\nhttp://www.example.com/file.ttl/individual-1 contains the file extension ttl", failure.getText());
	}
	
	@Test
	void iriContainsNoFileExtension() throws Exception {
		List<Failure> failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsNoFileExtension_01.nq", check);
		assertEquals(0, failures.size());
		failures = TestUtil.executeCheck("CheckRdfIriContainsFileExtension/iriContainsNoFileExtension_02.nq", check);
		assertEquals(0, failures.size());
	}
	
}
