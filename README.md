# LintedData

LintedData is a tool to find common mistakes in RDF-files.

## Building

To use LintedData checkout the project and compile it using Maven:

```
mvn clean
mvn compile
mvn package
```

This will create the .jar at `target/LintedData.jar`.

## Execution

LintedData can be run with the following options and parameters:

```
Usage: LintedData [-hV] [-s=<scopes>[,<scopes>...]]... <inputFile> <outputFile>
Performs checks on a provided RDF-file.
      <inputFile>    The file that will be checked
      <outputFile>   The file where the result will be stored (must be xml)
  -h, --help         Show this help message and exit.
  -s, --scope=<scopes>[,<scopes>...]
                     Which scopes will be checked (RDF, RDFS or OWL)
                     default is all three
  -V, --version      Print version information and exit.
```

### Examples

- show the help message
```
java -jar target/LintedData.jar -h
```
- run all tests again an example file and store the result
```
java -jar target/LintedData.jar <inputFile> <outputFile.xml>
```
- run only RDF tests again an example file and store the result
```
java -jar target/LintedData.jar --scope "RDF" <inputFile> <outputFile.xml>
```
- run RDF and OWL tests again an example file and store the result
```
java -jar target/LintedData.jar -s "RDF","OWL" <inputFile> <outputFile.xml>
```
or
```
java -jar target/LintedData.jar -s "RDF" -s "OWL" <inputFile> <outputFile.xml>
```

## CI Usage with Docker

A Docker image for LintedData is provided on GitHub Packages.
<!-- TODO make it public available -->
The `LintedData.jar` file is inside located at `opt/LintedData.jar` but it can also be accessed with the alias `LintedData` instead of `java -jar /opt/LintedData.jar`.

### Example configuration for use in an CI pipeline on GitLab:
```
image: ghcr.io/fusion-jena/linted-data:main

linted-data:
  stage: test
  script:
    - LintedData -s "RDF","OWL" <inputFile> <outputFile>
  artifacts:
    paths:
      - <outputFile>
    reports:
      junit: <outputFile>
```

### Example configuration for use in an CI pipeline on GitHub:
```
name: Check Maya the bee ontology
on: workflow_dispatch

jobs:
  report:
    runs-on: ubuntu-latest
    permissions:
      contents: read  # for dorny/test-reporter
      id-token: write # for dorny/test-reporter
      checks: write # for dorny/test-reporter
    container:
      image: ghcr.io/fusion-jena/linted-data
      credentials:
         username: ${{ github.actor }}
         password: ${{ secrets.github_token }}
    steps:
      - name: Setup git
        run: apt-get update && apt-get install git -y
      - name: Checkout Project
        uses: actions/checkout@main
      - name: Run LintedData with RDF and RDFS scope
        run: LintedData -s "RDF" -s "RDFS" <inputFile> <outputFile>
      - name: Archive Reports
        uses: actions/upload-artifact@v3
        with:
          name: junit-reports
          path: <outputFile>
      - name: Report
        uses: dorny/test-reporter@v1.4.2
        with:
          name: Maven Tests
          path: <outputFile>
          reporter: java-junit
```

### Example project

A project that uses LintedData in its CI pipeline can be seen <a href="https://github.com/fusion-jena/LintedDataExample">here<a>.
