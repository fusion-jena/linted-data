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

package de.uni_jena.cs.fusion.linted_data.util;

import java.math.BigDecimal;

public abstract class StringUtil {

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
