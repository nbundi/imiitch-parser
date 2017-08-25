package org.bund;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import amay22.ParseDS;

public class Parser {
	private InputStream input;
	private PrintWriter output;
	ParseIMIITCH parsers;
	ParseDS parseDS;

	public Parser(String filename, String yamlFile, String outfile) {

		// Via YAML, create the Data Structures
		try {
			parseDS = new ParseDS(yamlFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found...Check Filename");
		}
		// Create the parsers with the YAML data structures
		parsers = new ParseIMIITCH(parseDS);
		// Open the input stream...
		try {
			if (filename.length() > 0) {
				// From the file's path if specified...
				input = new FileInputStream(new File(filename));
			} else {
				/// ...or stdin otherwise
				input = System.in;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File (" + filename +
					") not found...Check Filename");
		}

		try {
			output = new PrintWriter(new FileWriter(outfile));
		} catch (IOException e) {
			System.out.println("File (" + outfile +
					") could not be created...Check Filename");
		}
	}

	// Read the message from binary file
	public ArrayList<String> parse() throws IOException, InterruptedException {
		if (input.read() == -1) { // EOF
			return null;
		}
		int payLength = input.read();
		byte[] payBytes = new byte[payLength]; // Get the payload

		int offset = 0;  // Loop until we've read the full payload size
		while (offset < payLength) {
			offset += input.read(payBytes, offset, payLength - offset);
		}

		// Parse to string
		ArrayList<String> messageArr = null;
		// Send payBytes byte[] to Parsers for processing
		// An arraylist will be returned with the message
		messageArr = (parsers.messageIn(payBytes));

		//System.out.println(messageArr);

		return messageArr;
	}

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

	// Read and print to console the message from binary file
	public ArrayList<String> parseNPrint() throws IOException, InterruptedException {

		ArrayList<String> messageArr = parse();
		System.out.println(messageArr);

		return messageArr;
	}

	// Read and write to file single message from binary file
	public ArrayList<String> parseNWrite() throws IOException, InterruptedException {

		// parse message
		ArrayList<String> msg = parse();

		// write message
		if(msg!=null) {
			output.println(String.join(",",msg));
		}

		return msg;
	}

	// Read all message from binary file
	public void parseNWriteAll() throws IOException, InterruptedException {

		// parse messages and add to collection
		ArrayList<String> msg = parse();
		while (msg!=null) {
			output.println(String.join(",",msg));
			msg=parse();
		}
		output.close(); 
	}

	public void closeOutput() {
		output.close();
	}


	public static boolean hasCmdArgsYamlFlag (String args[]) {
		return args.length >= 1 && args[0].equals("-y");
	}

	public static String readYamlPathArgs (String args[]) {
		if (hasCmdArgsYamlFlag(args)) {
			return args[1];
		} else {
			return "..//itch5.yaml";
		}
	}

	public static String readItchPathArgs (String args[]) {
		int argsPos = hasCmdArgsYamlFlag(args) ? 2 : 0;
		if (args.length > argsPos) {
			return args[argsPos];
		} else {
			return "";
		}
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		String yamlPath = readYamlPathArgs(args);
		String filename = readItchPathArgs(args);
		Parser parse = new Parser(filename, yamlPath,
				"./test.txt"); // true to print otherwise enter false
		while (parse.parseNPrint() != null) {}
	}
}

