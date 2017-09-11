
import org.bund.Parser;

import java.io.IOException;

public class TestParser {

	public static void main(String[] args) throws IOException, InterruptedException {

		/*
		Parser parser = new Parser("./src/test/resources/testfile.bin",
				"./src/main/resources/yaml/imiitch.yaml");
		parser.parse("./test.txt", true);
		*/
		Parser.main(new String[]{ "-f", "./src/test/resources/testfile.bin",
								"-y", "./src/main/resources/yaml/imiitch.yaml",
								"-o", "test_wCasting.txt",
								"-c", "true"});
	}

}
