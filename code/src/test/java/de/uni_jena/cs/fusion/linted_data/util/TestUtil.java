package de.uni_jena.cs.fusion.linted_data.util;

import java.io.File;
import java.util.List;

import de.uni_jena.cs.fusion.linted_data.JUnitXML.Failure;
import de.uni_jena.cs.fusion.linted_data.checks.file_checks.FileCheck;
import de.uni_jena.cs.fusion.linted_data.types.Severity;

/**
 * Util methods that can be used in each test
 */
public class TestUtil {

	/**
	 * check whether a failure is contained in a list
	 * 
	 * equality based on failure element and text, not on severity
	 * <p>
	 * should be used when failures.size() > 1 to prevent that the sequence differs
	 * on different machines
	 * 
	 * @param failures       list of failures created by the validator
	 * @param failureElement element that generates the error,
	 *                       {@link Failure#failureElement}
	 * @param text           description of the failure (where it can be found),
	 *                       {@link Failure#Text}
	 * @return true if the failure is contained in failures
	 */
	public static boolean contains(List<Failure> failures, String failureElement, String text) {
		return failures.stream().filter(f -> f.getFailureElement().equals(failureElement) && f.getText().equals(text))
				.findFirst().isPresent();
	}

	/**
	 * check whether a failure is contained in a list
	 * 
	 * equality based on failure element and text and severity
	 * <p>
	 * should be used when failures.size() > 1 to prevent that the sequence differs
	 * on different machines
	 * 
	 * @param failures       list of failures created by the validator
	 * @param failureElement element that generates the error,
	 *                       {@link Failure#failureElement}
	 * @param text           description of the failure (where it can be found),
	 *                       {@link Failure#Text}
	 * @param severity       severity of the failure, {@link Failure#severity}
	 * @return true if the failure is contained in failures
	 */
	public static boolean contains(List<Failure> failures, String failureElement, String text, Severity severity) {
		return failures.stream().filter(f -> f.getFailureElement().equals(failureElement)
				&& f.getSeverity().equals(severity) && f.getText().equals(text)).findFirst().isPresent();
	}

	/**
	 * passes the file of a given path to the check and starts its execute method
	 * 
	 * @param path  to the test resource
	 * @param check the validator that will be executed
	 * @return all found failures during the execution of the chek
	 * @throws Exception from the check execution
	 */
	public static List<Failure> executeCheck(String path, FileCheck check) throws Exception {
		File file = new File(TestUtil.class.getClassLoader().getResource(path).getPath());
		return check.execute(file, "");
	}
}