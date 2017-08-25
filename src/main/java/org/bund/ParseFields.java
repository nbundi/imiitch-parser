package org.bund;

import java.io.IOException;
import java.util.ArrayList;

public class ParseFields {

    // Read all message from binary file
    public ArrayList<ArrayList<String>> parseAll() throws IOException, InterruptedException {
        ArrayList<ArrayList<String>> msgAll = new ArrayList<ArrayList<String>>();

        // parse messages and add to collection
        ArrayList<String> msg = parse();
        while (msg!=null) {
            msgAll.add(msg);
            msg=parse();
        }
        return msgAll;
    }

}
