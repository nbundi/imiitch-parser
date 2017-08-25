
import org.bund.Parser;

import java.io.IOException;

public class TestParser {

	public static void main(String[] args) throws IOException, InterruptedException {

		Parser parser = new Parser("./src/test/resources/testfile.bin",
				"./src/main/resources/yaml/imiitch.yaml");
		parser.parse("./test.txt", true);
	}

}
