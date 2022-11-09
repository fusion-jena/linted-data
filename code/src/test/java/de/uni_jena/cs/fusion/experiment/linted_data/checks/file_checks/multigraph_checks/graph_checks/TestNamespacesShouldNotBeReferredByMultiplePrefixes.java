package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.CheckNamespacesShouldNotBeReferredByMultiplePrefixes;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;
import de.uni_jena.cs.fusion.experiment.linted_data.util.TestUtil;

public class TestNamespacesShouldNotBeReferredByMultiplePrefixes {

	private CheckNamespacesShouldNotBeReferredByMultiplePrefixes check = new CheckNamespacesShouldNotBeReferredByMultiplePrefixes();

	/**
	 * default model where each namespace is associated with exactly one prefix
	 */
	@Test
	public void onePrefixPerNamespace() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://other-example.org#");
		Resource r = model.createResource("http://example.org#first");
		Property p = model.createProperty("http://example.org#test-property");
		model.add(r, p, "0.45", XSDDatatype.XSDdecimal);
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://other-example.org#");
		r = model.createResource("http://example.org#first");
		p = model.createProperty("http://example.org#test-property");
		model.add(r, p, "0.45", XSDDatatype.XSDdecimal);
		model.setNsPrefix("prefix-3", "http://third-example.org/");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(0, failures.size());

		// twice the same prefix definition is not possible to check, after parsing the
		// file only the last association between a prefix and a namespace is present
		// this is also the case when using the Jena API for modelling
	}

	/**
	 * associate a namespace with more than one prefix, default model
	 */
	@Test
	public void multiplePrefixesPerNamespace() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://example.org#");
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals("http://example.org#", failure.getFailureElement());
		assertEquals("\nNamespace http://example.org# is referred by the 2 prefixes: prefix-1, prefix-2",
				failure.getText());

		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://other-example.org#");
		Resource r = model.createResource("http://example.org#first");
		Property p = model.createProperty("http://example.org#test-property");
		model.add(r, p, "0.45", XSDDatatype.XSDdecimal);
		model.setNsPrefix("prefix-3", "http://third-example.org/");
		model.setNsPrefix("prefix-4", "http://third-example.org/");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals("http://third-example.org/", failure.getFailureElement());
		assertEquals("\nNamespace http://third-example.org/ is referred by the 2 prefixes: prefix-3, prefix-4",
				failure.getText());

		// two namespaces with two prefixes
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://example.org#");
		p = model.createProperty("http://example.org#test-property");
		model.add(r, VCARD.KEY, "0.45", XSDDatatype.XSDdecimal);
		model.setNsPrefix("prefix-3", "http://third-example.org/");
		model.setNsPrefix("prefix-4", "http://third-example.org/");
		model.add(r, VCARD.BDAY, "2023-04-12");
		model.setNsPrefix("prefix-5", "http://my-ontology.org#");
		model.setNsPrefix("prefix-6", "http://test.org#");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(2, failures.size());
		assertTrue(TestUtil.contains(failures, "http://example.org#",
				"\nNamespace http://example.org# is referred by the 2 prefixes: prefix-1, prefix-2", Severity.INFO));
		assertTrue(TestUtil.contains(failures, "http://third-example.org/",
				"\nNamespace http://third-example.org/ is referred by the 2 prefixes: prefix-3, prefix-4",
				Severity.INFO));

		// one namespace associated with more than two prefixes
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://example.org#");
		model.setNsPrefix("prefix-3", "http://example.org#");
		model.setNsPrefix("prefix-4", "http://example.org#");
		failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals("http://example.org#", failure.getFailureElement());
		assertEquals(
				"\nNamespace http://example.org# is referred by the 4 prefixes: prefix-1, prefix-2, prefix-3, prefix-4",
				failure.getText());
	}
}
