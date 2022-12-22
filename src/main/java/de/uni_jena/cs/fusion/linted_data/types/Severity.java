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

package de.uni_jena.cs.fusion.linted_data.types;

/**
 * represents the severity of a failure
 */
public enum Severity {
	/**
	 * critical failure that needs to be fixed
	 * <p>
	 * failure cause prevents any use of the RDF data
	 */
	ERROR,
	/**
	 * failure that should be fixed but that is not critical
	 * <p>
	 * failure cause prevents use of the RDF data in many cases
	 */
	WARN,
	/**
	 * failure that can be fixed, but is not urgent
	 * <p>
	 * failure cause prevents use of the RDF data in some rarer cases
	 */
	INFO
}
