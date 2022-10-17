package de.uni_jena.cs.fusion.experiment.linted_data.checks.file_checks.multigraph_checks.graph_checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Test;

import de.uni_jena.cs.fusion.experiment.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.experiment.linted_data.checks.multigraph_checks.graph_checks.CheckNamespacesShouldNotBeReferredByMultiplePrefixes;
import de.uni_jena.cs.fusion.experiment.linted_data.types.Severity;

public class TestNamespacesShouldNotBeReferredByMultiplePrefixes {
	
	@Test
	public void onePrefixPerNamespace() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://other-example.org#");
		Resource r = model.createResource("http://example.org#first");
		Property p = model.createProperty("http://example.org#test-property");
		model.add(r, p, "0.45", XSDDatatype.XSDdecimal);
		CheckNamespacesShouldNotBeReferredByMultiplePrefixes check = new CheckNamespacesShouldNotBeReferredByMultiplePrefixes();
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
	}
	
	@Test
	public void multiplePrefixesPerNamespace() {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("prefix-1", "http://example.org#");
		model.setNsPrefix("prefix-2", "http://example.org#");
		CheckNamespacesShouldNotBeReferredByMultiplePrefixes check = new CheckNamespacesShouldNotBeReferredByMultiplePrefixes();
		List<Failure> failures = check.execute(model, "");
		assertNotNull(failures);
		assertEquals(1, failures.size());
		Failure failure = failures.get(0);
		assertEquals(Severity.INFO, failure.getSeverity());
		assertEquals("http://example.org#", failure.getFailureElement());
		assertEquals("\nNamespace http://example.org# is referred by the 2 prefixes: [prefix-1, prefix-2]", failure.getText());
	}
}
