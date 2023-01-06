# How to add a new validator to LintedData  

## 1. Structure  

The aim of this document is that the reader is then able to extend the tool on his own.
Therefore at first the general structure of the classes within the tool are explained.
Afterwards the different types of checks, that are modelled, are presented.
<!--Which way? -->
The 4th section is a general description which steps need to be followed, to add a new validator to the tool.
In the last section for each chek type an example is given. It is discussed, why the check needs to be implemented at this level.
But the implementation of the checks are not content of this section.   

## 2. General architecture  

In this section at first the architecture is displayed.
Additionally some of the classes and their functionality are explained.  

The general structure of the classes, used to implement the LintedData, is displayed in the following image:  

![Class diagram for LintedData](graphics/class_diagram.png "Class diagram for LintedData")  

The classes belong to three different packages:

| Package         | Description     |
|--------------|-----------|
| *linted_data* | The classes from this package are used to execute the tool. |
| *linted_data/checks* | This package contains all the validators.     |
| *linted_data/JUnitXML*      | All classes in this package are used to build the structure of [JUnitXML](https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format)  |

The different classes in the package `checks` are described in section 3.
<!-- TODO keep track of section number -->
In general all of these classes have a function called `execute` that must be implemented.
Each of the abstract classes overwrites the `execute` of its superclass, except for `FileCheck` as the first level.
`Check` is a superclass of `FileCheck` and used to capture the general attributes of a check.
It can be used to extend the tool later with different types of checks.  

Each of the `FileCheck`s has one level it applies to, this could also be modelled via the subclass relation.
Also each one has a `Scope`.
This attribute is used to assign the validator to the corresponding Testsuite in the result.
It represents the modeling language of the semantic web which is needed to realise the checked concept. <!--TODO is concept a good word? -->

The abstract subclasses of the different levels each implement the abstract `execute` method of its superclass.
In these implementations the argument, that is not `failureDescription`, is further processed and along with the `failureDescription` passed to the new abstract method `execute`.  

The package `JUnitXML` contains classes that are needed to represent the elements from  [JUnitXML](https://www.ibm.com/docs/en/developer-for-zos/14.1.0?topic=formats-junit-xml-format).
To prevent misunderstandings, elements of *Testsuites* are represented with instances of the class `TestsuiteManager`.
When adding a new validator, no changes need to be done to this package.

The `Severity` has the three values `ERROR, WARN` and `INFO`.
Where `ERROR` is used for critical failures that must be fixed to ensure correctness and functionality.
`WARN` represents failures that are non critical but should be fixed for correct behaviour.
The weakest value is `INFO` which is for non critical failures that don't affect the correctness but should be fixed.  

The `main`-method is contained in `LintedData`.
Within this class the command-line arguments are processed and a new instance of `Runner` is created.
The checks are only executed, when the file can be parsed successfully, else the program stops.
`Runner` executes the selected checks and stores the result to the destination file.
When a new validator is added, it must be added to the method `Runner.createAllChecks`, there are no further changes needed.  

In the following section the classes of the package `check` are described more in detail.
<!-- TODO
* should the classes be described more in detail?
* more details on failure? $\rightarrow$ most important class from JUnitXML
* Apache Jena for parsing, processing?
-->
## 3. Check types  

In this section the different types of validators are explained.
The aim of this section is it, to understand, when to implement which type of check.

As seen in the class diagram in the previous section, the different types of validators are implemented as subclass from `Check`.

A general rule is, that a validator should be implemented as subclass of the most specific check.
It is always possible to realise a validator as a subclass of `FileCheck`.
But this leads to code duplication that can be prevented.
Also each level provides a method that only has the required arguments.
<!--TODO
should there be more?
does this even make sense?-->

## 3.1 `FileCheck`
<!--TODO don't like this sentence -->

`FileCheck` is the first "real" check that applies to the structure of the tool.
All non abstract subclasses of this class need to process the file in its raw format.
The failures detected in those classes can't be detected after parsing the file into a [Jena](https://jena.apache.org/) dataset.  

### 3.1.1 `CheckRdfPrefixesReferToOneNamespace`

One example for this type is the check if a prefix is defined multiple times, see `CheckRdfPrefixesReferToOneNamespace`.
This can only occur in serialisation languages, that allow prefix definitions, for example Turtle.
In Turtle and JSONLD it is possible to generate a valid document, where a prefix is defined multiple times.
This error can't be detected after parsing the file because then only the last set prefix definition would be available.

## 3.2 `MultiGraphCheck`

To easily interact with the file content it is helpful to parse the file into an API.
In LintedData the JenaAPI is used therefore.
At this level the file gets parsed into a `dataset`.
A `dataset` gains access to the default graph, and if serialised in a language supporting multiple graphs within one document, also to all other named graphs.
This level should be used, when it is necessary to access all contained models and combine the results from them.  

### 3.2.1 `CheckRdfsSeveralClassesWithTheSameLabel`

The only example created at this stage is `CheckRdfsSeveralClassesWithTheSameLabel`.
This check should be implemented as `MultiGraphSparqlCheck`, but due to time issues, this class doesn't exist yet.
It is not possible to realise this validator at another level, because in the next level, each graph contained in the `dataset` is processed on its own.
It is not possible to get all used labels from each model and check if they are also used for another concept.

## 3.3 `GraphCheck`

A `MultiGraphCheck` allows the user to access all models at the same time, but the dataset does not allow direct access to the statements contained in the different models.

A `GraphCheck` gains access to only one model of a dataset at the same time, but therefore it is possible to access its contained statements.
If it is necessary to combine results or statements from multiple graphs within a check, this must be implemented as a `MultiGraphCheck`.


### 3.3.1 `CheckRdfRoundedFloatingPointValue`

Whether a value can be exactly represented as a double or float is an example for a `GraphCheck`.
In the check `CheckRdfRoundedFloatingPointValue` each statement of a model is examined.
In case that the object of a statement is a literal with the datatype `xsd:float` or `xsd:double`, it is checked if this value can be exactly represented in this datatype, or if its datatype should be `xsd:decimal`.
The comparison of the `decimal` value and the `double` or `float` value can't be realised as SPARQL query.
So this validator can't be realised as `SparqlCheck`.

## 3.4 `SparqlCheck`

Checks that can be formulated as a SPARQL query, are a subclass of `SparqlCheck`.
Although implementing a validator as a subclass of `SparqlCheck` might not be the best efficient solution, it should be the superclass to choose.
This enables the reuse of  the SPARQL query in other frameworks as well.

This abstract class `SparqlCheck` has an attribute in which the query is stored as String.
`SparqlAskCheck` and `SparqlSelectCheck` are its two abstract subclasses used to model and execute the different types of request queries in SPARQL.

`SparqlAskCheck` is used to model `ASK` queries.
It defines an abstract method with a boolean parameter that needs to be processed.
With `SparqlSelectCheck` `SELECT FROM WHERE` queries are modelled.
The abstract `execute` function has one argument of the type  `ResultSet`.
This contains the answer from the SPARQL query and can then be processed further.  

In both cases, it is only possible to access the result of the query, not the queried model.

### 3.4.1 `CheckRdfIrisTooLong`

This validator checks, if the locale name of an IRI, the part after `#` or after the last `/`, contains not more than 36 character.
36 might look like an arbitary decission, and is based on the following aspects:

- three IRIs should fit into one line
- IRIs should be easy to tipe
- UUIDs are 36 character long and should therefore not be detected when used as locale name

Since for each IRI whose locale name is longer than 36 characters, a failure entry should be created, this validator is realised as a subclass of `SparqlSelectCheck`.
A realisation as `SparqlAskCheck` would only provide the answer if in a model exists an IRI that is too long.

It would also be possible to implement this check without SPARQL, but since the aim is to possible reuse the checks for SPARQL endpoints.  

The next section describes the procedure when implementing a new validator.

## 4. Implementation of a new validator  

After describing the different types of check that are included in LintedData, this section describes which steps need to be followed when implementing a new validator.  

The first step is to assign the validator a level.
The differences between those are described in the previous section.
The examples also given in the section may help to find a suitable level.
It may also help to ask, what information is needed to solve the problem.
When choosing the level, it should be considered that the most appropriate class should always be chosen.

Next to choosing the level of the check, it must be considered to which `Scope` it applies.
The scope of an validator defines, which modeling language of the semantic web the check applies to.
For example, `CheckRdfIrisTooLong` only uses the concept IRI from RDF.
Whereas `CheckRdfsSeveralClassesWithTheSameLabel` uses the concept Label from RDFS. <!-- TODO concept a valid word at this position? -->
The validators are grouped by their `scope` to the different `Testsuite`s in `Runner` as well as in the exported XML result file.

The name of a check should be created in the following way:

1. Check
2. its scope (RDF, RDFS, OWL)
3. short description of the validation

All checks call the constructor of their abstract superclass.
They have the following arguments:  

| Argument         | Description |
|--------------|-----------------------|
| `level` | As described before, corresponding value of the enum `Level`  |
| `scope`      | Corresponding semantic web modelling language, determines corresponding `Testuite`, a value of `Scope`. |
| `severity` | How important it is to fix an occurring failure of this validator, see section 2 for a description of the different values. |
| `name`| This attribute does not describe the validator itself, but is a general description of the occurring failures found by the validator. |

When the validator is a `SparqlCheck` the constructor has an additional argument for the query.
The query can be passed as `String`, as File or as `InputStream`.

All these arguments should be set in the constructor itself, so that the constructor itself doesn't require any argument.  

Besides the creation of a constructor, the implementation of the `execute` method is an essential step.
This function returns a list of `Failure`s and always has two arguments:

One is a String called `failureDescription`.
This argument is used to set the text attribute of a failure.
The text will later be the only option for the user to locate the failure within his file.
To help the user, the description gets specified with the more specific subclasses.
To keep the text formatted, the first character added to it must be a line break.
Afterwards should be added information that describes the element, or elements, that lead to the failure.

Depending on the level the second argument is different.
The content of `failureDescription` and the second argument for each level can be found in the following table.

| Level         | `failureDescription`     | 2nd argument |
|--------------|-----------|------------|
| `FileCheck` | Canonical path to the file | Raw file      
| `MultiGraphCheck` | Canonical path to the file | File parsed as dataset |
| `GraphCheck` | Canonical path to the file, Name of the model | Single model from the `dataset` |
| `SparqlSelectCheck` | Canonical path to the file, Name of the model | `ResultSet` from the query
| `SparqlAskCheck` | Canonical path to the file, Name of the model | boolean answer from the query

When creating a new failure, and adding it to the list that will be returned at the end,
the following information must be given:

| Argument | Description |
| ---------- | ----------- |
| `message` | Describes the error in a general way, in most cases the attribute `name` of the check can be used. |
| `severity` | How critical the found error is. This information can also be often extracted from the attributes. |
| `failureElement` | This is only for testing, not for productive environment. This argument is used to describe what especially caused the failure. For example the number that can't be represented exactly. |
| `text` | Description of the error position, should be the extended  `failureDescription`. |

Before the validator is added to the tool, use JUnit tests to ensure its correctness.

The last step is to add the check to all the checks that are executed when the tool is executed.
Therefore the function `createAllChecks` in the class `Runner` must be adopted.
Within this function the new class must be added to the list `allChecks`, that's the only needed change in this class.
When executing LintedData the next time, the check will also be executed, if its `scope` is chosen.
No further steps are needed.
