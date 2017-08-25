
import org.bund.Parser;

import java.io.IOException;

public class TestParser {

	public static void main(String[] args) throws IOException, InterruptedException {

		Parser parse = new Parser("./src/test/resources/testfile.bin",
				"./src/main/resources/yaml/imiitch.yaml", 
				"./test.txt");
		parse.parseNWriteAll();
	}

}
