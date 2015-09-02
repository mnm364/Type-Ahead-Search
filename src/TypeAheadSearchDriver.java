//TODO input error detection

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/* */
public class TypeAheadSearchDriver {

	/* Master map of all entries. */
	public static DoubleHashedHashMap<Entry> allEntries;

	/* Trie to store data strings */
	public static CompressedHashTrie trie;

	/* */
	public static PrintWriter outputFile;

	/**
	 * 
	 * @param lineScanner Scanner object
	 * @param addIndex index of the add
	 */
	public static void add(Scanner lineScanner, int addIndex) {

		/* Parse data. */
		String type = lineScanner.next().toLowerCase();
		String id = lineScanner.next();
		float score = lineScanner.nextFloat();
		String data = lineScanner.nextLine().substring(1);

		/* Create temp entry to add to database. */
		Entry tempEntry = new Entry(id, type.charAt(0), score, addIndex, data);

		/* Add to main id map. */
		allEntries.put(tempEntry);

		/* Add to trie. */
		trie.insert(tempEntry);
	}

	/**
	 * 
	 * @param lineScanner Scanner object
	 */
	public static void query(Scanner lineScanner) {
		//TODO check numQuery < 20
		int numQuery = lineScanner.nextInt();
		String queryString = lineScanner.nextLine().substring(1);
		List<String> idList = trie.search(numQuery, queryString);
		//TODO output to file
		System.out.println(idList);
	}

	/**
	 * 
	 * @param lineScanner Scanner object
	 */
	public static void wquery(Scanner lineScanner) {
		int numQuery = lineScanner.nextInt();
		int numBoost = lineScanner.nextInt();
		List<Boost> boostList = new ArrayList<Boost>();
		for (int i = 0; i < numBoost; i++) {
			String boost = lineScanner.next();
			String boostId = boost.substring(0, boost.indexOf(":"));
			float boostValue = Float.parseFloat(boost.substring(boost.indexOf(":") + 1)); 
			Boost tempBoost = new Boost(boostId, boostValue);
			boostList.add(tempBoost);
		}
		String queryString = lineScanner.nextLine();
		List<String> idList = trie.weightedSearch(numQuery, queryString, numBoost, boostList);
		//TODO output to file
	}

	/**
	 * 
	 * @param lineScanner Scanner object
	 */
	public static boolean remove(Scanner lineScanner) {
		String id = lineScanner.next();
		
		/* Create dummy instance of entry to remove. */
		Entry tempEntry = allEntries.get(new Entry(id));

		if (tempEntry != null) {
			
			/* Remove from trie. */
			trie.remove(tempEntry);

			/* Remove from master id map. */
			allEntries.remove(tempEntry);

			return true;
		} else {
			return false;
		}
	}

	/** for testing */
	public static void print() {
		System.out.println("________");
		trie.breadthFirstTraversal();
		System.out.println(allEntries);
	}

	/**
	 * Parse input.
	 * @param lineScanner Scanner object
	 */
	public static void readInput(Scanner input) {

		int addIndex = 0;

		int numTotalCommands = input.nextInt();
		input.nextLine();
		while (input.hasNext()) {
			String inputLine = input.nextLine();
			Scanner lineScanner = new Scanner(inputLine);
			if (lineScanner.hasNext()) {
				String command = lineScanner.next();
				switch(command.toUpperCase()) {
					case "ADD":
						add(lineScanner, addIndex);
						addIndex++;
						break;
					case "QUERY":
						query(lineScanner);
						break;
					case "WQUERY":
						wquery(lineScanner);
						break;
					case "DEL":
						remove(lineScanner);
						break;
					case "PRINT": //for testing
						print();
						break;
					default:
						break;
				}
			}
			lineScanner.close();
		}
		input.close();
	}

	public static void main(String[] args) {
		
		/* check cmd line arguements */
		if (args.length == 0) {
			System.out.println("Proper Usage: ");
			System.exit(0);
		}
		// TODO Auto-generated method stub
		// TODO read in arguments 
		trie = new CompressedHashTrie();
		allEntries = new DoubleHashedHashMap<Entry>();
		Scanner input = null;
		try {
			input = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			System.err.println("File" + args[0] + " not found.");
			e.printStackTrace();
		}

		readInput(input);

	}

}
