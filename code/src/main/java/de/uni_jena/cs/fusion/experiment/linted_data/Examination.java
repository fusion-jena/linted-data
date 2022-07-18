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
	File file;

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

	public static void main(String[] args) {
		int exitCode = new CommandLine(new Examination()).execute(args);
		System.exit(exitCode);
	}

	public Integer call() {
		if (!file.exists()) {
			System.err.println(file.getAbsolutePath() + " doesn't exist.\n" + "Please check the path and start again.");
			return -1;
		}
		if (FileUtil.checkFileExtension(file)) {
			System.err.println("The file format can't be processed.");
		}
		System.out.println(level.toString());

		return 0;
	}

}
