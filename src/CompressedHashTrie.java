// Function prompt {"quora> "}

/* NOTES
 * -midstring compression is not implemented yet
 * ^^b/c of this, inputting same string in twice will make the whole string single chars.. :(
 * Change entries to hash b/c of constant lookup time
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * need to make a full write up on this
 * 
 * @author Michael Miller, Jeffrey Sham
 * 
 */
public class CompressedHashTrie {

	/* Root of the current trie. */
	private DoubleHashedHashMap<TrieHashNode> root;
	
	//TODO - when done change protected to private
	/** Nodes to be stored in the hash maps of the trie. */
	private class TrieHashNode {
		
		/* Reference to child map. */
		protected CompressedHashTrie child;

		//	TODO - make this a skip list data structure (or a hashMap so constant lookup time)
		/* list of object references to unordered master Set */
		protected Set<Entry> entries;

		/* Data (sequence of chars) */
		private ByteArrayCharSequence val;

		/* Boolean to mark leaf nodes. */
		protected boolean leaf;

		//TODO unneccssarry parameters (child)
		/**
		 * Constructor for hash node in compressed trie.
		 * @param child child trie hashmap of node
		 * @param e one entry associated with the node
		 */
		private TrieHashNode(CompressedHashTrie child, Entry e) {
			child = new CompressedHashTrie();
			entries = new Set<Entry>();

			this.child = child;
			this.entries.add(e);
		}

		/**
		 * Constructor for hash node in compressed trie.
		 * @param val the data
		 * @param child child trie hashmap of node
		 * @param e one entry associated with the node
		 */
		private TrieHashNode(String val, CompressedHashTrie child, Entry e) {
			this(child, e);
			this.val = new ByteArrayCharSequence(val);
		}

		/**
		 * Constructor for hash node in compressed trie.
		 * @param val the data
		 * @param child child trie hashmap of node
		 * @param e one entry associated with the node
		 */
		private TrieHashNode(ByteArrayCharSequence val, CompressedHashTrie child, Entry e) {
			this(child, e);
			this.val = val;
		}

		private void changeStr(ByteArrayCharSequence seq) {
			this.val = seq;
		}

		protected void addEntry(Entry e) {
			this.entries.add(e);
		}

		protected Entry getFirstEntry() {
			return (Entry) entries.getVal(0);
		}

		private ByteArrayCharSequence getVal() {
			return val;
		}

		/**
		 * Determine if leaf or not.
		 * @return true if leaf; false otherwise
		 */
		protected boolean isLeaf() {
			return leaf;
		}

		@Override
		public String toString() {
			return "(" + this.val.toString() + ":" + this.entries + ")" + "il:" + this.leaf;
		}

		@Override
		public int hashCode() {
			Character c = new Character(val.charAt(0));
			return c.hashCode();
		}

		/**
		 * Checks to see if first letter of val is equal.
		 * @param o the other object
		 */
		//TODO - possibly use getClass() for better performance? not sure...
		@Override
		public boolean equals(Object o) {
			if (o instanceof TrieHashNode) {
				TrieHashNode that = (TrieHashNode) o;
				return this.val.charAt(0) == that.val.charAt(0);
			}
			return false;
		}
	}

	/**
	 * Constructor for trie
	 * @param root the hashmap that holds the nodes for this level of the trie
	 */
	public CompressedHashTrie(DoubleHashedHashMap<TrieHashNode> root) {
		this.root = root;
	}
	/**
	 * Default constructor for trie
	 */
	public CompressedHashTrie() {
		this(new DoubleHashedHashMap<TrieHashNode>(5));//was NULL
	}
	//TODO - stuff (copy constructor, etc...)

	/**
	 * Insert an entry into the trie.
	 * @param e the entry to insert
	 */
	public void insert(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		String words[] = e.getDataStr().toLowerCase().split("\\s+");

		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			System.out.printf("- insert %s:{%s}\n", words[i],e);
			//allEntries.add(e);
			this.insert(new TrieHashNode(words[i], null, e));
		}
	}

	/**
	 * Helper method; insert node into trie.
	 * @param node the node to insert
	 * @return ...? MAKE IT RETURN SOMETHING USEFUL
	 */
	private boolean insert(TrieHashNode node) {
		TrieHashNode tempNode = this.root.get(node);

		//The first letter of node is already in the hash map
		if (tempNode != null) {

			if (tempNode.child == null) {
				tempNode.child = new CompressedHashTrie();
			}

			ByteArrayCharSequence value = tempNode.getVal();
			tempNode.leaf = false;

			int index = compress(value, node.val);

			tempNode.changeStr(value.subSequence(0, index));

			Set<Entry> entries = node.entries;
			for (int i = 0; i < entries.size(); i++) {
				tempNode.addEntry((Entry)entries.getVal(i));
			}

			if (value.length() > index) {
				TrieHashNode tempHashShorter = new TrieHashNode(value.subSequence(index), null, 
						tempNode.getFirstEntry()); //TODO the child param is not neccessary in this constructor...
				tempHashShorter.leaf = true;
				tempNode.child.insert(tempHashShorter);
			}

			if (node.getVal().length() > index) {
				node.changeStr(node.getVal().subSequence(index));
				node.leaf = true;
				tempNode.child.insert(node);
			}

		//data doesn't exist in hash map yet
		} else {
			this.root.put(node);
			node.leaf = true;
		}

		return true;
	}

	/**
	 * Remove an entry from the trie.
	 * @param e the entry
	 */
	public void remove(Entry e) {
		String words[] = e.getDataStr().toLowerCase().split("\\s+");
		for (int i = 0; i < words.length; i++) {
			System.out.printf("- remove %s:{%s}\n", e, words[i]);
			TrieHashNode tempNode = this.root.get(new TrieHashNode(words[i], null, null));
			if (tempNode != null) {
				this.remove(tempNode, new ByteArrayCharSequence(words[i]), e.getId());
			}
		}
	}

	/**
	 * Remove helper method.
	 * @param node node recursively removed from trie
	 * @param seq word to remove from the trie
	 * @param id the id of the entry to remove
	 */
	private void remove(TrieHashNode node, ByteArrayCharSequence seq, String id) {

		Set<Entry> entries = node.entries;

		if (node.child != null) {
			ByteArrayCharSequence value = node.getVal();

			/* Find index of strings that start to differ. */
			int index = compress(value, seq);

			/* Create sequence starting at point of difference. */
			seq = seq.subSequence(index);

			TrieHashNode tempNode = null;

			if (seq.length() > 0) {
				tempNode = node.child.root.get(new TrieHashNode(seq, null, null));
			}

			if (tempNode != null) {

				//if the node is a leaf, it is safe to remove it
				if (tempNode.isLeaf()) {

					node.child.root.remove(tempNode); //remove child

					if (node.child.root.size() == 0) {
						node.leaf = true; //setting the node to a leaf
					}

					if (node.child.root.size() == 1) {
						//recompression
						Iterator<TrieHashNode> it = node.child.root.iterator();
						TrieHashNode theOtherChild = null; 
						
						if (it.hasNext()) {
							theOtherChild = it.next();
						}
						
						this.pullNodesUp(node, theOtherChild);
					}

				}
				this.remove(tempNode, seq, id);
			}
		}

		entries.remove(id);
	}

	/**
	 * This method pulls up the child's children and make them children of the parent.
	 * It then changes the parent's data string to accommodate and removes the child.
	 * @param parent the parent node
	 * @param child the child node
	 */
	private void pullNodesUp(TrieHashNode parent, TrieHashNode child) {
		if (child.isLeaf()) {
			parent.leaf = true;
			parent.changeStr(parent.val.append(child.val));
			parent.child.root.remove(child);
		} else {
			Iterator<TrieHashNode> iterator = child.child.root.iterator();
			ArrayList<TrieHashNode> childArray = new ArrayList<TrieHashNode>(); 
			if (iterator.hasNext()) {
				childArray.add(iterator.next());
			}
			for (int i = 0; i < childArray.size(); i++) {
				parent.child.root.put(childArray.get(i));
			}
			parent.changeStr(parent.val.append(child.val));
			parent.child.root.remove(child);
		}
	}

	/**
	 * This method gets two strings as input and finds the
	 * index of the first character that is different between
	 * the two strings.
	 * @param first the first string
	 * @param second the second string
	 * @return index of the character that is different 
	 */
	private int compress(ByteArrayCharSequence first, ByteArrayCharSequence second) {
		int i = 0, j = 0;
		boolean foundDiff = false;
		char charNull = '\u0000';
		char firstLetter = charNull, secondLetter = charNull;
		while (!foundDiff) {
			if (i < first.length()) {
				firstLetter = first.charAt(i);// + "";
			}

			if (j < second.length()) {
				secondLetter = second.charAt(j);// + "";
			}

			if (firstLetter == charNull || secondLetter == charNull){
				foundDiff = true;
				if (i > j) {
					i = j;
				}
			} else if (firstLetter != secondLetter) {
				foundDiff = true;
			} else {
				i++;
				j++;
				firstLetter = charNull;
				secondLetter = charNull;
			}
		}
		return i;
	}

	//TODO - look over these two methods and make sure they work!
	/**
	 * This method searches for the queryString in the map
	 * @param numberOfResults The number of results wanted
	 * @param queryString The query string
	 * @return A list of strings of ids
	 */
	public List<String> search(int numberOfResults, String queryString) {
		ArrayList<Entry> entryList = (ArrayList<Entry>) genericSearch(queryString);
		Collections.sort(entryList);
		ArrayList<String> idList = new ArrayList<String>();
		
		for (int i = 0; i < entryList.size(); i++) {
			if (idList.size() < numberOfResults) {
				idList.add(entryList.get(i).getId());
			}
		}

		return idList;
	}


	/**
	 * This is a generic search method. 
	 * @param queryString The string to find
	 * @return The list of entry objects that have been found from the query
	 */
	private List<Entry> genericSearch(String queryString) {
		DoubleHashedHashMap<QueryEntry> map = new DoubleHashedHashMap<QueryEntry>();
		ArrayList<Entry> idList = new ArrayList<Entry>();
		String words[] = queryString.toLowerCase().split("\\s+");

		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			TrieHashNode temp = new TrieHashNode(words[i], null, null);
			this.search(temp, map);
		}

		Iterator<QueryEntry> iterator = map.iterator();
		while(iterator.hasNext()) {
			QueryEntry tempEntry = iterator.next();
			//checks if the entry is correct
			if (tempEntry.getFrequency() == words.length) {
				//limits the number of results returned
				idList.add(tempEntry.getEntry());
			}
		}

		return idList;
	}

	/**
	 * This is a weighted query for a string in the map.
	 * @param numberOfResults The number of results wanted
	 * @param queryString The string to find
	 * @param numberOfBoosts The number of boosts
	 * @param boosts The List of boosts
	 * @return A list of strings of ids
	 */
	public List<String> weightedSearch(int numberOfResults, String queryString, int numberOfBoosts, List<Boost> boosts) {
		ArrayList<Entry> idListUnsorted = (ArrayList<Entry>) genericSearch(queryString);
		
		if (numberOfBoosts != 0) {
			//Weight the search
			Collections.sort(boosts);
			
			//index 0: user
			//index 1: topic
			//index 2: question
			//index 3: board
			float[] boostValues = {1, 1, 1, 1};
			List<Boost> boostsForId = new ArrayList<Boost>();
			for (int i = 0; i < boosts.size(); i++) {
				Boost tempBoost = boosts.get(i);
				
				switch (tempBoost.getType().toLowerCase()) {
					case "user":
						boostValues[0] = tempBoost.getBoostValue();
						break;
					case "topic":
						boostValues[1] = tempBoost.getBoostValue();
						break;
					case "question":
						boostValues[2] = tempBoost.getBoostValue();
						break;
					case "board":
						boostValues[3] = tempBoost.getBoostValue();
						break;
					default:
						boostsForId.add(tempBoost);
						break;
				}
			}
			
			for (int i = 0; i < idListUnsorted.size(); i++) {
				Entry tempEntry = idListUnsorted.get(i);
				int boostValue = 0;
				switch (tempEntry.getType()) {
					case 'u':
						boostValue += boostValues[0];
						break;
					case 't':
						boostValue += boostValues[1];
						break;
					case 'q':
						boostValue += boostValues[2];
						break;
					case 'b':
						boostValue += boostValues[3];
						break;
					default:
						break;
				}
				for (int j = 0; j < boostsForId.size(); j++) {
					Boost tempBoost = boostsForId.get(j);
					if (tempEntry.getId().equals(tempBoost.getType())) {
						boostValue += tempBoost.getBoostValue();
						boostsForId.remove(j);
						break;
					}
				}
				
				if (boostValue != 0) {
					tempEntry.setScore(boostValue);
				}
			}
			
			List <String> idList = new ArrayList<String>();
			Collections.sort(idListUnsorted);
			for (int i = 0; i < idListUnsorted.size(); i++) {
				if (idList.size() < numberOfResults) {
					idList.add(idListUnsorted.get(i).getId());
				}
			}
			return idList;
			
		} else {
			List <String> idList = new ArrayList<String>();
			Collections.sort(idListUnsorted);
			for (int i = 0; i < idListUnsorted.size(); i++) {
				if (idList.size() < numberOfResults) {
					idList.add(idListUnsorted.get(i).getId());
				}
			}
			return idList;
		}

	}
	
	/**
	 * This method searches for the a String in the trie.
	 * It fills the map with QueryEntry objects when it finds a node that should be
	 * in the map.
	 * @param node The query node
	 * @param map The map of QueryEntries
	 */
	private void search(TrieHashNode node, DoubleHashedHashMap<QueryEntry> map) {
		TrieHashNode tempNode = this.root.get(node);
		
		//Node exists
		if (tempNode != null){

			ByteArrayCharSequence value = tempNode.val;
			
			if (!node.val.equals(value) && node.val.length() >= value.length()) {
				//Check if we need to go down more levels
				int index = compress(value, node.val);
				if (node.getVal().length() > index) {
					node.changeStr(node.getVal().subSequence(index));
					tempNode.child.search(node, map);
				}
			} else {
				//We want the entries from this node
				for (int i = 0; i < tempNode.entries.size(); i++) {
					Entry tempEntry = (Entry)tempNode.entries.getVal(i);
					QueryEntry tempQueryEntry = new QueryEntry(tempEntry,1);
					QueryEntry prevQueryEntry = map.get(tempQueryEntry);
					if (prevQueryEntry != null) {
						prevQueryEntry.setFrequency(prevQueryEntry.getFrequency() + 1);
					} else {
						map.put(tempQueryEntry);
					}
				}
				
			}

		}
	}


	public void breadthFirstTraversal() {
		this.breadthFirstTraversal(this);
	}
	/**
	 * Breadth first recursive traversal of trie.
	 * for testing purposes
	 * @param trie the root of the trie to traverse
	 */
	private void breadthFirstTraversal(CompressedHashTrie trie) {
		if (trie == null) {
			return;
		}
		Queue<TrieHashNode> queue = new LinkedList<>();
		Iterator<TrieHashNode> iter = trie.root.iterator();
		while (iter.hasNext()) {
			TrieHashNode in = iter.next();
			queue.add(in);
		}
		System.out.println();
		while (queue.peek() != null) {
			TrieHashNode curr = queue.remove();
			System.out.printf("%s --> %s\n", curr, curr.child);
			breadthFirstTraversal(curr.child);
		}
	}

	/**
	 * Represent trie hash as string.
	 * @return a string representation of the trie hash	 
	 */
	@Override
	public String toString() {
		return this.root.toString();
	}

	/**
	 * Test driver main method
	 */ 
	public static void main(String[] args) {
		System.out.println("testing compressed hash trie...");
		CompressedHashTrie trie = new CompressedHashTrie();
		String[] words = {"sappling","b soda","bob","by sad sodaman sapplingerman"};
		/*for (int i = 0; i < 4; i++) {
			Entry e = new Entry("e" + Integer.toString(i), 'b', 10, words[i]);
			System.out.printf("ENTRY #%d\n", i);
			trie.insert(e);
		}*/

		Entry e0 = new Entry("e" + Integer.toString(0), 'u', 10, words[0]);
		System.out.printf("ENTRY #%d\n", 0);
		trie.insert(e0);

		System.out.printf("\n0%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e1 = new Entry("e" + Integer.toString(1), 't', 10, words[1]);
		System.out.printf("ENTRY #%d\n", 1);
		trie.insert(e1);
		
		System.out.printf("\n1%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e2 = new Entry("e" + Integer.toString(2), 'q', 10, words[2]);
		System.out.printf("ENTRY #%d\n", 2);
		trie.insert(e2);
		
		System.out.printf("\n2%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e3 = new Entry("e" + Integer.toString(3), 'b', 10, words[3]);
		System.out.printf("ENTRY #%d\n", 3);
		trie.insert(e3);

		System.out.printf("\n3%s\n", trie);
		trie.breadthFirstTraversal(trie);

		System.out.println("TESTING search functionality:");

		//normal search
		System.out.println(trie.search(3, "b"));

		//weighted search, no boosts
		System.out.println(trie.weightedSearch(2, "b", 0, null));
		
		//weighted search, boosts
		List<Boost> boostList = new ArrayList<Boost>();
		boostList.add(new Boost("board", 50));
		boostList.add(new Boost("question", 100));
		boostList.add(new Boost("e3", 51));
		System.out.println(trie.weightedSearch(4, "b", 3, boostList));
		//trie.remove2("e3"); //TODO - needs to be changed so just uses ID

		//System.out.printf("After remove...\n%s\n", trie);
		//trie.breadthFirstTraversal(trie);

		//testing ByteArrayCharSequence stuff
		ByteArrayCharSequence cseq = new ByteArrayCharSequence("byte array, yay!");
		System.out.println(cseq.subSequence(0,3).hashCode());
		ByteArrayCharSequence empty = new ByteArrayCharSequence("");
		System.out.println(empty.hashCode());

		ByteArrayCharSequence cseq_c = cseq.subSequence(0, cseq.length()-1);
		System.out.println(cseq.equals(cseq_c)); //should be NOT true
		System.out.println(cseq.subSequence(5,11).subSequence(3)); //should print "e"
	}
}