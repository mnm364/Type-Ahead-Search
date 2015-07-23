import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/* */
public class TypeAheadSearchDriver {

	/* */
	public static DoubleHashedHashMap<Entry> allEntries;

	/* */
	public static CompressedHashTrie trie;

	/* */
	public static PrintWriter outputFile;

	/* */
	public static void add(Scanner lineScanner) {
		
		/* Parse data. */
		String type = lineScanner.next().toLowerCase();
		String id = lineScanner.next();
		float score = lineScanner.nextFloat();
		String data = lineScanner.next();

		/* Create temp entry to add to database. */
		Entry tempEntry = new Entry(id, type.charAt(0), score, data);

		/* Add to main map. */
		allEntries.put(tempEntry);

		/* Add to trie. */
		trie.insert(tempEntry);
	}

	public static void query(Scanner lineScanner) {
		int numQuery = lineScanner.nextInt();
		String queryString = lineScanner.nextLine();
		List<String> idList = trie.search(numQuery, queryString);
		//TODO output to file
	}

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

	public static boolean remove(Scanner lineScanner) {
		String id = lineScanner.next();
		// int index = allEntries.findIndex(id);
		Entry tempEntry = allEntries.get(new Entry(id, 'a', 0, null));

		if (tempEntry != null) {
			//Entry tempEntry = (Entry) allEntries.getVal(index);
			trie.remove(tempEntry);
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

	public static void readInput(Scanner input) {
		int numTotalCommands = input.nextInt();
		input.nextLine();
		while (input.hasNext()) {
			String inputLine = input.nextLine();
			Scanner lineScanner = new Scanner(inputLine);
			if (lineScanner.hasNext()) {
				String command = lineScanner.next();
				switch(command.toUpperCase()) {
					case "ADD":
						add(lineScanner);
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
		// TODO Auto-generated method stub
		// TODO read in arguments 
		trie = new CompressedHashTrie();
		allEntries = new DoubleHashedHashMap<Entry>();
		Scanner input = null;
		try {
			input = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		readInput(input);

	}

}
