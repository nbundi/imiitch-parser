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
	MessageParser parsers;
	ParseDS parseDS;

	public Parser(String filename, String yamlFile) {

		// Via YAML, create the Data Structures
		try {
			parseDS = new ParseDS(yamlFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found...Check Filename");
		}
		// Create the parsers with the YAML data structures
		parsers = new MessageParser(parseDS);

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

	}

	// read single message from binary file
	public ArrayList<String> parseNext() throws IOException, InterruptedException {
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

	// read and print to console single message from binary file
	public ArrayList<String> parseNPrintNext() throws IOException, InterruptedException {

		ArrayList<String> messageArr = parseNext();
		System.out.println(messageArr);

		return messageArr;
	}

	// read all messages from binary file
	public ArrayList<ArrayList<String>> parse() throws IOException, InterruptedException {
		ArrayList<ArrayList<String>> msgAll = new ArrayList<ArrayList<String>>();

		// parse messages and add to collection
		ArrayList<String> msg = parseNext();
		while (msg!=null) {
			msgAll.add(msg);
			msg=parseNext();
		}
		return msgAll;
	}

	// read all messages from binary file
	public ArrayList<ArrayList<String>> parse(boolean typeCast) throws IOException, InterruptedException {

		if(!typeCast) {
			return this.parse();
		} else {

			// instantiate utilities
			FieldCaster caster = new FieldCaster();
			ArrayList<ArrayList<String>> msgAll = new ArrayList<ArrayList<String>>();

			// parse and cast messages and add to collection
			ArrayList<String> msg = caster.cast(parseNext());
			while (msg != null) {
				msgAll.add(msg);
				msg = caster.cast(parseNext());
			}
			return msgAll;
		}
	}

	// read all message from binary file
	public void parse(String file, boolean cast) throws IOException, InterruptedException {

		// open output stream
		PrintWriter output=null;
		try {
			output = new PrintWriter(new FileWriter(file));
		} catch (IOException e) {
			System.out.println("File (" + file +
					") could not be created...Check Filename");
		}

		if(!cast) { // parse and write messages
			ArrayList<String> msg = parseNext();
			while (msg!=null) {
				output.println(String.join(",",msg));
				msg=parseNext();
			}
		} else {
			FieldCaster caster = new FieldCaster();
			ArrayList<String> msg = caster.cast(parseNext());
			while (msg!=null) { // parse, cast and write messages
				msg=caster.cast(msg);
				output.println(String.join(",",msg));
				msg=parseNext();
			}
		}
		output.close(); 
	}

	// main method
	// TODO: complete
	/*
	public static void main(String args[]) throws IOException, InterruptedException {
		String yamlPath = readYamlPathArgs(args);
		String infile = readItchPathArgs(args);
		String outfile = readOutfilePathArgs(args);
		boolean cast = readCastArgs(args);
		Parser parse = new Parser(infile, yamlPath);
		if(outfile==null) {
			while (parse.parseNPrintNext() != null) {
			}
		} else {
			//parse(outfile,cast);
		}
	}

	// utilities
	public static boolean hasCmdArgsYamlFlag (String args[]) {
		return args.length >= 1 && args[0].equals("-y");
	}

	public static String readYamlPathArgs (String args[]) {
		if (hasCmdArgsYamlFlag(args)) {
			return args[cmdArgsYamlFlagPos(args)];
		} else {
			return "";
		}
	}

	public static String readItchPathArgs (String args[]) {
		if (hasCmdArgsInfileFlag(args)) {
			return args[cmdArgsIntfileFlagPos(args)];
		} else {
			return null;
		}
	}

	public static String readOutfilePathArgs (String args[]) {
		if (hasCmdArgsOutfileFlag(args)) {
			return args[cmdArgsOutfileFlagPos(args)];
		} else {
			return null;
		}
	}

	public static boolean readCastArgs (String args[]) {
		if (hasCmdArgsCastFlag(args)) {
			return (boolean) args[cmdArgsCastFlagPos(args)];
		} else {
			return false;
		}
	}
	*/

}

