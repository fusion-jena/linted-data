package de.uni_jena.cs.fusion.linted_data.util;

import java.math.BigDecimal;
import java.util.List;

public abstract class StringUtil {

	public final static String checkForFileExtension(String s, List<String> extensions) {
		for (String e : extensions) {
			if (s.contains(e)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Checks if a double should be replaced by decimal
	 * 
	 * extracted from {@link https://zenodo.org/record/6338129}
	 * <p>
	 * class: UnpreciseRepresentableInDouble
	 * <p>
	 * Not explicitly tested in this project because it is tested in the other
	 * project
	 * 
	 * @param lexicalValue of the objects literal, checked if it is a valid double
	 * @return true if the value can be represented exactly as a double, otherwise
	 *         false
	 */
	public static boolean isPreciseRepresentableAsDouble(String lexicalValue) {
		try {
			Double doubleValue = Double.valueOf(lexicalValue);
			// special values of double, that can't be represented by decimal
			if (doubleValue.isInfinite() || doubleValue.isNaN()) {
				return true;
			}

			// see
			// https://docs.oracle.com/javase/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
			// Get the exact representation of the value of the double
			// Exact conversion -> "unpredictable behavior"
			BigDecimal doubleDecimal = new BigDecimal(doubleValue);

			// Convert the lexical value into Big Decimal
			// represents the expected value
			// "Predictable behavior"
			BigDecimal stringDecimal = new BigDecimal(lexicalValue);

			// Compare exact representation with the expected value
			// A decimal should be used, if the values are not the same
			return doubleDecimal.compareTo(stringDecimal) == 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Checks if a float datatype should be replaced by decimal
	 * 
	 * extracted from {@link https://zenodo.org/record/6338129}
	 * <p>
	 * class: UnpreciseRepresentableInFloat *
	 * <p>
	 * Not explicitly tested in this project because it is tested in the other
	 * project
	 * 
	 * @param lexicalValue of the objects literal, checked if it is a valid float
	 * @return true if the value can be represented exactly as a float, otherwise
	 *         false
	 */
	public static boolean isPreciseRepresentableAsFloat(String lexicalValue) {
		try {
			Float floatValue = Float.valueOf(lexicalValue);

			// special values of float, that can't be represented by decimal
			if (floatValue.isInfinite() || floatValue.isNaN()) {
				return true;
			}

			// see
			// https://docs.oracle.com/javase/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
			// Get the exact representation of the value of the double
			// Exact conversion -> "unpredictable behavior"
			BigDecimal floatDecimal = new BigDecimal(floatValue);

			// Convert the lexical value of the float variable into Big Integer
			// represents the expected value
			// "Predictable behavior"
			BigDecimal stringDecimal = new BigDecimal(lexicalValue);

			// Compare exact representation with the expected value
			// A decimal should be used, if the values are not the same
			return floatDecimal.compareTo(stringDecimal) == 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
