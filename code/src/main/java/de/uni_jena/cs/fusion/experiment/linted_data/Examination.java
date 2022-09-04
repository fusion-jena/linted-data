package de.uni_jena.cs.fusion.experiment.linted_data;

import java.io.File;
import java.util.concurrent.Callable;

import de.uni_jena.cs.fusion.experiment.linted_data.types.Level;
import de.uni_jena.cs.fusion.experiment.linted_data.util.FileUtil;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "examination", mixinStandardHelpOptions = true, version = "0.0.1", description = "Performs checks on a ontology provided via file.")
public class Examination implements Callable<Integer> {

	@Parameters(index = "0", description = "The file that will be checked")
	File inputFile;
	
	@Parameters(index = "1", description = "The file where the result will be stored")
	File outputFile;
	
	@Option(names = { "-l", "--level" }, description = "What kind of checks will be executed\n"
			+ "SPARQL, FILE, GRAPH, MULTIGRAPH, ALL", defaultValue = "ALL")
	Level level;

	class LevelConverter implements ITypeConverter<Level> {
		public Level convert(String value) throws Exception {
			Level level = Level.ALL;
			switch (value) {
			case "SPARQL":
				level = Level.SPARQL;
				break;
			case "FILE":
				level = Level.FILE;
				break;
			case "GRAPH":
				level = Level.GRAPH;
				break;
			case "MULTIGRAPH":
				level = Level.MULTIGRAPH;
				break;
			case "ALL":
				level = Level.ALL;
				break;
			default:
				throw new Exception();
			}
			return level;
		}
	}
	
	public Integer call() {
		if (!inputFile.exists()) {
			System.err.println(inputFile.getAbsolutePath() + " doesn't exist.\n" + "Please check the path and start again.");
			return -1;
		}
		if (! FileUtil.checkInputFileExtension(inputFile)) {
			System.err.println("The file format of the ontology can't be processed.");
			return -1;
		}
		if (! FileUtil.checkOutputFileExtension(outputFile)) {
			System.err.println("The output file must be an xml file");
			return -1;
		}
		
		System.out.println("Input parsed successfull");
		new Runner(level, inputFile, outputFile);
		
		System.out.println("Finished");
		
		return 0;
	}
	
	public static void main(String[] args) {
		int exitCode = new CommandLine(new Examination()).execute(args);
		System.exit(exitCode);
	}

	

}
