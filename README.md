# LintedData

LintedData is a tool to find common mistakes in RDF files.

## Building

To use LintedData, checkout the project and compile it using Maven:

```bash
mvn clean package
```

This will create the `.jar` at `target/LintedData.jar`.

## Execution

LintedData can be run with the following options and parameters:

```bash
Usage: LintedData [-hV] [-s=<scopes>[,<scopes>...]]... <inputFile> <outputFile>
Performs checks on a provided RDF file.
      <inputFile>    The file that will be checked
      <outputFile>   The file where the result will be stored (must be xml)
  -h, --help         Show this help message and exit.
  -s, --scope=<scopes>[,<scopes>...]
                     Which scopes will be checked (RDF, RDFS or OWL)
                     default is all three
  -V, --version      Print version information and exit.
```

The tool can validate RDF data that uses RDF, RDFS, or OWL vocabulary.
It can't process files using the OWL/XML format.
With the scope parameter the executed checks can be filtered.
Currently the following checks are implemented:

| Scope         | Executed Check     |
|--------------|-----------|
| RDF | IRIs too long <br> Leading or trailing Spaces <br> Namespaces refered by multiple prefixes <br> Namespaces omit the seperator <br> Floating point numbers are not exactly represented |
| RDFS | Properties with multiple domains or ranges <br> Properties without domain or range <br> Several classes with the same label |
| OWL | A symmetric property is used in an inverse statement <br> No license is declared <br> Property is defined as inverse to itself |

### Examples

- display the help message

  ```bash
  java -jar target/LintedData.jar -h
  ```

- run all tests against an example file and store the result

  ```bash
  java -jar target/LintedData.jar <inputFile> <outputFile>
  ```

  ```bash
  java -jar target/LintedData.jar maya_the_bee.ttl result.xml
  ```

- run only RDF tests against an example file and store the result

  ```bash
  java -jar target/LintedData.jar --scope RDF <inputFile> <outputFile>
  ```

- run RDF and OWL tests against an example file and store the result

  ```bash
  java -jar target/LintedData.jar -s RDF,OWL <inputFile> <outputFile>
  ```

  or

  ```bash
  java -jar target/LintedData.jar -s RDF -s OWL <inputFile> <outputFile>
  ```

## CI Usage with Docker

A Docker image for LintedData is provided on GitHub Packages.

The `LintedData.jar` file is located inside at `opt/LintedData.jar`, but it can also be accessed with the alias `LintedData` instead of `java -jar /opt/LintedData.jar`.

### Example configuration for use in an CI pipeline on GitLab:

Create a YAML file named `.gitlab-ci.yml` containing:

```yaml
image: ghcr.io/fusion-jena/linted-data:v1.0.0

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

In line **6**, the arguments passed to `LindedData` are adapted to the use case.
As shown in the examples, it is possible to set the scope of the tool in this line.
In the same line, `inputFile` and `outputFile` must be set.
The `outputFile` must also be defined in lines **9** and **11**.

### Example configuration for use in an CI pipeline on GitHub:

Create a YAML file at `.github/workflows/<filename>.yml` containing:

```yaml
jobs:
  report:
    runs-on: ubuntu-latest
    permissions:
      contents: read  # for dorny/test-reporter
      id-token: write # for dorny/test-reporter
      checks: write # for dorny/test-reporter
    container:
      image: ghcr.io/fusion-jena/linted-data:v1.0.0
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

In line **19**, the arguments passed to `LindedData` must be adapted to the use case.
As shown in the examples, it is possible to set the scope of the tool.
In the same line, `inputFile` and `outputFile` must be set.
Lines **24** and **29** must also include the `outputFile` definition.

### Example project

A project that uses LintedData in its CI pipeline can be seen <a href="https://github.com/fusion-jena/LintedDataExample">here<a>.

## Acknowledgements

LintedData was initially developed by Merle Gänßinger during a project supervised by Jan Martin Keil at the University of Jena.
