/* NOTES
 * -do all the checks to see if got is char or string justify the overhead if everything was a
 * ^^string?
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

	private Set<Entry> allEntries;
	
	//TODO - when done change protected to private
	/**
	 * Nodes to be stored in the hashes in the trie
	 */
	private class TrieHashNode {
		/* Pointer to subsequent hash map. */
		protected CompressedHashTrie child;

		//	TODO - make this a skip list data structure (or a hashMap so constant lookup time)
		/* list of object references to unordered master Set */
		protected Set<Entry> entries;

		/* ASCII character data */
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
			return "(" + this.val.toString() + ":" + this.entries + ")" + "isLeaf:" + this.leaf;
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
			/* OLD
			//TrieHashNode that = (TrieHashNode) o;
			if (this instanceof TrieStrHash) {
				TrieStrHash _this = (TrieStrHash) this;
				if (o instanceof TrieStrHash) {
					TrieStrHash that = (TrieStrHash) o;
					return _this.val.charAt(0) == that.val.charAt(0);
				} else if (o instanceof TrieCharHash) {
					TrieCharHash that = (TrieCharHash) o;
					return _this.val.charAt(0) == that.val;
				} else if (o instanceof TrieSeqHash){
					TrieSeqHash that = (TrieSeqHash) o;
					return _this.val.charAt(0) == that.val.charAt(0);
				} else {
					//dont care
					return false;
				}
			} else if (this instanceof TrieCharHash) {
				TrieCharHash _this = (TrieCharHash) this;
				if (o instanceof TrieStrHash) {
					TrieStrHash that = (TrieStrHash) o;
					return _this.val == that.val.charAt(0);
				} else if (o instanceof TrieCharHash) {
					TrieCharHash that = (TrieCharHash) o;
					return _this.val == that.val;
				} else if (o instanceof TrieSeqHash){
					TrieSeqHash that = (TrieSeqHash) o;
					return _this.val == that.val.charAt(0);
				} else {
					//dont care
					return false;
				}
			} else if (this instanceof TrieSeqHash){
				TrieSeqHash _this = (TrieSeqHash) this;
				if (o instanceof TrieStrHash) {
					TrieStrHash that = (TrieStrHash) o;
					return _this.val.charAt(0) == that.val.charAt(0);
				} else if (o instanceof TrieCharHash) {
					TrieCharHash that = (TrieCharHash) o;
					return _this.val.charAt(0) == that.val;
				} else if (o instanceof TrieSeqHash){
					TrieSeqHash that = (TrieSeqHash) o;
					return _this.val.charAt(0) == that.val.charAt(0);
				} else {
					//dont care
					return false;
				}
			} else {
				//dont care
				return false;
			}
			END OLD */
		}
	}
	/* OLD
	//if this works, it should be combined with TrieHashNode, no need for inheritence
	private class TrieSeqHash extends TrieHashNode {

		private ByteArrayCharSequence val;

		private TrieSeqHash(String val, CompressedHashTrie child, Entry e) {
			super(child, e);
			this.val = new ByteArrayCharSequence(val);
		}

		private TrieSeqHash(ByteArrayCharSequence val, CompressedHashTrie child, Entry e) {
			super(child, e);
			this.val = val;
		}

		private void changeStr(ByteArrayCharSequence seq) {
			this.val = seq;
		}

		private ByteArrayCharSequence getVal() {
			return val;
		}

		@Override
		public String toString() {
			return "SEQ(" + this.val.toString() + ":" + this.entries + ")" + "isLeaf:" + this.leaf;
		}

		@Override
		public int hashCode() {
			Character c = new Character(val.charAt(0));
			return c.hashCode();
		}
	}
	END OLD */
	// OLD
	// private class TrieStrHash extends TrieHashNode {

	// 	/* String to be used in Trie. */
	// 	private String val;

	// 	/**
	// 	 * Constructor for TrieStrHash
	// 	 * @param val value of the character to be put in trie
	// 	 * @param child the next got in the trie
	// 	 * @param e reference to the entry from which being inserted
	// 	 */
	// 	private TrieStrHash(String val, CompressedHashTrie child, Entry e) {
	// 		super(child, e);
	// 		this.val = val;
	// 	}

	// 	private void setChildNull() {
	// 		this.child = null;
	// 	}

	// 	private String getVal() {
	// 		return val;
	// 	}

	// 	private void changeStr(String str) {
	// 		this.val = str;
	// 	}

	// 	@Override
	// 	public String toString() {
	// 		return "(" + this.val + ":" + entries + ")" + "isLeaf:" + this.leaf;
	// 	}

	// 	@Override
	// 	public int hashCode() {
	// 		Character c = new Character(val.charAt(0));
	// 		return c.hashCode();
	// 	}
	// }

	// private final class TrieCharHash extends TrieHashNode {

	// 	/* Character to be used in Trie and hashed into TrieHashNode. */
	// 	private char val;

	// 	/**
	// 	 * Constructor for TrieCharHash
	// 	 * @param val value of the character to be put in trie
	// 	 * @param child the next got in the trie
	// 	 * @param e reference to the entry from which being inserted
	// 	 */
	// 	private TrieCharHash(char val, CompressedHashTrie child, Entry e) {
	// 		super(child, e);
	// 		this.val = val;
	// 	}

	// 	private char getVal() {
	// 		return val;
	// 	}

	// 	@Override
	// 	public String toString() {
	// 		return "(" + this.val +  ":" + entries + ")";
	// 	}

	// 	@Override
	// 	public int hashCode() {
	// 		Character c = new Character(val);
	// 		return c.hashCode();
	// 	}
	// } END OLD

	/**
	 * Constructor for trie
	 * @param root the hashmap that holds the nodes for this level of the trie
	 */
	public CompressedHashTrie(DoubleHashedHashMap<TrieHashNode> root) {
		this.root = root;
		this.allEntries = new Set<Entry>();
	}
	/**
	 * Default constructor for trie
	 */
	public CompressedHashTrie() {
		this(new DoubleHashedHashMap<TrieHashNode>(5));//was NULL
		this.allEntries = new Set<Entry>();
	}
	//TODO - stuff (copy constructor, etc...)

	/**
	 * Insert an entry into the trie.
	 * @param e the entry to insert
	 */
	public void insert6(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		String words[] = e.getDataStr().toLowerCase().split("\\s+");

		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			System.out.printf("- insert %s:{%s}\n", words[i],e);
			allEntries.add(e);
			this.insert6(new TrieHashNode(words[i], null, e));
		}
	}

	/**
	 * Helper method; insert node into trie.
	 * @param node the node to insert
	 * @return ...? MAKE IT RETURN SOMETHING USEFUL
	 */
	private boolean insert6(TrieHashNode node) {
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
				tempNode.child.insert6(tempHashShorter);
			}

			if (node.getVal().length() > index) {
				node.changeStr(node.getVal().subSequence(index));
				node.leaf = true;
				tempNode.child.insert6(node);
			}

		//data doesn't exist in hash map yet
		} else {
			this.root.put(node);
			node.leaf = true;
		}

		return true;
	}



	//*******OLD********//

	// public void insert5(Entry e) {
	// 	//TODO - split at commas, punctuation marks, etc.
	// 	String words[] = e.getDataStr().toLowerCase().split("\\s+");

	// 	 insert individual words into trie 
	// 	for (int i = 0; i < words.length; i++) {
	// 		System.out.printf("- insert %s:{%s}\n", words[i],e);
	// 		allEntries.add(e);
	// 		this.insert5(new TrieHashNode(words[i], null, e));
	// 	}
	// }

	// private boolean insert5(TrieHashNode seqNode) {
	// 	//boolean putSuccess = this.root.put(seqNode);
	// 	TrieHashNode tempNode = this.root.get(seqNode);

	// 	//The first letter of seqNode is already in the hash map
	// 	if (tempNode != null) {

	// 		if (tempNode.child == null) {
	// 			tempNode.child = new CompressedHashTrie();
	// 		}

	// 		if (tempNode instanceof TrieSeqHash && seqNode instanceof TrieSeqHash) {
	// 			TrieSeqHash tempNode = (TrieSeqHash) tempNode;
	// 			ByteArrayCharSequence value = tempNode.getVal();
	// 			tempNode.leaf = false;
	// 			TrieSeqHash newSeqNode = (TrieSeqHash) seqNode;

	// 			int index = compress(value, newSeqNode.val);

	// 			tempNode.changeStr(value.subSequence(0, index));

	// 			Set<Entry> entries = newSeqNode.entries;
	// 			for (int i = 0; i < entries.size(); i++) {
	// 				tempNode.addEntry((Entry)entries.getVal(i));
	// 			}

	// 			if (value.length() > index) {
	// 				TrieSeqHash tempSeqHashShorter = new TrieSeqHash(value.subSequence(index), null, 
	// 						tempNode.getFirstEntry());
	// 				tempSeqHashShorter.leaf = true;
	// 				tempNode.child.insert5(tempSeqHashShorter);
	// 			}

	// 			if (newSeqNode.getVal().length() > index) {
	// 				newSeqNode.changeStr(newSeqNode.getVal().subSequence(index));
	// 				newSeqNode.leaf = true;
	// 				tempNode.child.insert5(newSeqNode);
	// 			}
	// 		}
	// 	} else { //data doesn't exist in hash map yet
	// 		this.root.put(seqNode);
	// 		seqNode.leaf = true;
	// 	}
	// 	return true;
	// }

	// /**
	//  * Insert entry data into trie
	//  * break up entry string into single words.
	//  *
	//  * @param e the entry to be inserted into the trie
	//  */
	// public void insert(Entry e) {
	// 	//TODO - split at commas, punctuation marks, etc.
	// 	String words[] = e.getDataStr().toLowerCase().split("\\s+");

	// 	/* insert individual words into trie */
	// 	for (int i = 0; i < words.length; i++) {
	// 		System.out.printf("- insert %s:{%s}\n", words[i],e);
	// 		allEntries.add(e);
	// 		this.insert4(new TrieStrHash(words[i], null, e));
	// 	}
	// }

	// private boolean insert4(TrieHashNode strNode) {
	// 	//boolean putSuccess = this.root.put(strNode);
	// 	TrieHashNode tempNode = this.root.get(strNode);

	// 	if (tempNode != null) { //!putSuccess) {
	// 		//The first letter of strNode is already in the hash map
	// 		//TrieHashNode tempNode = this.root.get(strNode);

	// 		if (tempNode.child == null) {
	// 			tempNode.child = new CompressedHashTrie();
	// 		}

	// 		if (tempNode instanceof TrieStrHash && strNode instanceof TrieStrHash) {
	// 			TrieStrHash tempStrHash = (TrieStrHash) tempNode;
	// 			String value = tempStrHash.getVal();
	// 			tempStrHash.leaf = false;
	// 			TrieStrHash newStrNode = (TrieStrHash) strNode;

	// 			int index = compress(value, newStrNode.val);

	// 			tempStrHash.changeStr(value.substring(0, index));

	// 			Set<Entry> entries = newStrNode.entries;
	// 			for (int i = 0; i < entries.size(); i++) {
	// 				tempStrHash.addEntry((Entry)entries.getVal(i));
	// 			}

	// 			if (value.length() > index) {
	// 				TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(index), null, 
	// 						tempStrHash.getFirstEntry());
	// 				tempStrHashShorter.leaf = true;
	// 				tempStrHash.child.insert4(tempStrHashShorter);	
	// 			}

	// 			if (newStrNode.getVal().length() > index) {
	// 				newStrNode.changeStr(newStrNode.getVal().substring(index));
	// 				newStrNode.leaf = true;
	// 				tempStrHash.child.insert4(newStrNode);
	// 			}
	// 		}
	// 	} else {
	// 		//doesnt exist in hash map yet
	// 		this.root.put(strNode);
	// 		strNode.leaf = true;
	// 	}

	// 	return true;
	// }

	// /**
	//  * This method inserts a new TrieStrHash object into the CompressedHashTrie.
	//  * It also almost correctly keeps track of the entries that are in the root node.
	//  * It converts the TrieStrHash to a TrieCharHash if there are duplicate letters
	//  * in the hash map.
	//  * @param strNode The TrieHashNode to insert
	//  * @return true
	//  */
	// private boolean insert3(TrieHashNode strNode) {
	// 	//boolean putSuccess = this.root.put(strNode);
	// 	TrieHashNode tempNode = this.root.get(strNode);

	// 	if (tempNode != null) { //!putSuccess) {
	// 		//The first letter of strNode is already in the hash map
	// 		//TrieHashNode tempNode = this.root.get(strNode);

	// 		if (tempNode.child == null) {
	// 			tempNode.child = new CompressedHashTrie();
	// 		}

	// 		if (tempNode instanceof TrieStrHash && strNode instanceof TrieStrHash) {
	// 			TrieStrHash tempStrHash = (TrieStrHash) tempNode;
	// 			String value = tempStrHash.getVal();

	// 			TrieCharHash tempCharHash = new TrieCharHash(value.charAt(0),
	// 					new CompressedHashTrie(), tempStrHash.getFirstEntry());

	// 			//System.out.printf("%s - %s\n", tempStrHash, tempStrHash.entries);

	// 			//tempCharHash.entries = tempStrHash.entries;

	// 			//tempCharHash.addEntry(tempStrHash.getFirstEntry());
	// 			//System.out.printf("%s - %s\n", tempCharHash, tempCharHash.entries);

	// 			this.root.remove(tempStrHash);
	// 			this.root.put(tempCharHash);

	// 			TrieStrHash newStrNode = (TrieStrHash) strNode;

	// 			Set<Entry> entries = newStrNode.entries;
	// 			for (int i = 0; i < entries.size(); i++) {
	// 				tempCharHash.addEntry((Entry)entries.getVal(i));
	// 			}

	// 			if (value.length() > 1) {
	// 				TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(1), null, 
	// 						null);
	// 				tempStrHashShorter.entries = tempCharHash.entries;
	// 				tempCharHash.child.insert3(tempStrHashShorter);	
	// 			}

	// 			if (newStrNode.getVal().length() > 1) {
	// 				newStrNode.changeStr(newStrNode.getVal().substring(1));
	// 				tempCharHash.child.insert3(newStrNode);
	// 			}
	// 			//System.out.printf("%s\n%s --> %s\n", this.root, tempStrHash, 
	// 			//	tempStrHash.child.root);

	// 		} else if(tempNode instanceof TrieCharHash && strNode instanceof TrieStrHash) {
	// 			TrieCharHash tempCharHash = (TrieCharHash) tempNode;

	// 			TrieStrHash newStrNode = (TrieStrHash) strNode;

	// 			Set<Entry> entries = newStrNode.entries;
	// 			for (int i = 0; i < entries.size(); i++) {
	// 				tempCharHash.addEntry((Entry)entries.getVal(i));
	// 			}

	// 			if (newStrNode.getVal().length() > 1) {
	// 				newStrNode.changeStr(newStrNode.getVal().substring(1));
	// 				tempCharHash.child.insert3(newStrNode);
	// 			}
	// 			//System.out.printf("%s\n%s --> %s\n", this.root, tempCharHash, 
	// 			//	tempCharHash.child.root);

	// 		}
	// 	} else {
	// 		//doesnt exist in hash map yet
	// 		this.root.put(strNode);
	// 	}

	// 	return true;
	// }

	// private boolean insert2(TrieStrHash strNode) {
	// 	boolean putSuccess = this.root.put(strNode);

	// 	if (!putSuccess) {
	// 		//The first letter of strNode is already in the hash map
	// 		TrieHashNode tempNode = this.root.get(strNode);

	// 		if (tempNode.child == null) {
	// 			tempNode.child = new CompressedHashTrie();
	// 		}

	// 		if (tempNode instanceof TrieStrHash) {
	// 			TrieStrHash tempStrHash = (TrieStrHash) tempNode;
	// 			String value = tempStrHash.getVal();
	// 			tempStrHash.changeStr(value.charAt(0) + "");

	// 			if (value.length() > 1) {
	// 				TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(1), null,
	// 						null);
	// 				tempStrHashShorter.entries = tempStrHash.entries;
	// 				//tempStrHash.addEntry(strNode.getFirstEntry()); //update entry list
	// 				tempStrHash.child.insert2(tempStrHashShorter);	
	// 			}

	// 			if (strNode.getVal().length() > 1) {
	// 				strNode.changeStr(strNode.getVal().substring(1));
	// 				tempStrHash.child.insert2(strNode);
	// 			}

	// 			System.out.printf("%s\n%s --> %s\n", this.root, tempStrHash, 
	// 					tempStrHash.child.root);
	// 		}
	// 	} else {
	// 		System.out.printf("%s\n", this.root); //for testing
	// 	}

	// 	return false;
	// }

	// /**
	//  * Inserts a single string into the trie.
	//  * 
	//  * @param str the string to insert
	//  * @param e the entry that the string references
	//  * @return true if insert updated trie; false if failed or string already in trie
	//  */
	// private boolean insert(TrieStrHash strNode) {
	// 	/* base case
	// 	if (this.root.isEmpty()) {
	// 		this.root = new DoubleHashedHashMap<TrieHashNode>();
	// 		this.root.put(strNode);
	// 		System.out.printf("new level: %s\n", strNode);
	// 		System.out.printf("-->%s\n",this.root); //testing
	// 		return true;
	// 	}*/

	// 	//you know at least that none of the strings in the hash start with the same letter
	// 	//nor do they have any suffixes that are the same... (maybe) <-- NOT IMPLEMENTED
	// 	//every bucket is hashed by a single character, but values can be chars or strings... :?
	// 	//^^--> SO, need to find a way to hash new entry and check value of first char in buckets
	// 	//		if string (SOLVED with Overrides equal() in TrieHashNode)

	// 	//System.out.printf("search for: %s\n", strNode);
	// 	TrieHashNode got = this.root.get(strNode);
	// 	/* check to see if already in hashmap */
	// 	if (got != null) {
	// 		//System.out.printf("return on search: %s\n", got);
	// 		/* char or first char in string in hash */
	// 		/* OPTIONS
	// 		 * 1) If got is a TrieStrHash, then need to break up string and move it down one level
	// 		 * ^also need to take away first char in string, then pass back to insert. (2 ops)
	// 		 * 2) If got is a TrieCharHash, then need to move down one level and take away first
	// 		 * ^char in string, then pass back to insert.
	// 		 */
	// 		//TODO - need to get away from using instanceof so much!
	// 		if (got.child == null){
	// 			//System.out.printf("child of %s --> NEW\n", got);
	// 			got.child = new CompressedHashTrie();
	// 		} else {
	// 			//System.out.printf("child of %s --> %s\n", got, got.child.root);
	// 		}
	// 		if (got instanceof TrieStrHash) {

	// 			TrieStrHash tempStrNode = (TrieStrHash) got;

	// 			//roundabout way of moving string down a level, but should be more efficient
	// 			TrieCharHash tempCharNode = new TrieCharHash(tempStrNode.val.charAt(0), new CompressedHashTrie(),
	// 					null); //TODO - giving me issues with "got.child"
	// 			tempStrNode.child = new CompressedHashTrie();
	// 			//System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
	// 			tempCharNode.entries = got.entries; //copy over entries to char got
	// 			//System.out.printf("<%s\nremove attempt: %s \n",this.root, got);
	// 			this.root.remove(got); //remove string got from current level of trie
	// 			//System.out.printf("-->%s\n",this.root);
	// 			tempStrNode.val = tempStrNode.val.substring(1); //take away first char of string
	// 			//System.out.printf("inserting %s to child of %s\n", tempStrNode, tempCharNode);
	// 			tempCharNode.child.insert(tempStrNode); //add edited string got to level below
	// 			//System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
	// 			//System.out.printf("putting char: %s\n", tempCharNode);
	// 			this.root.put(tempCharNode); //add char got into current level of trie
	// 			//System.out.printf("-->%s\n", this.root);	

	// 			//TODO - At this point dont have to deal with children of string nodes, but keep in 
	// 			//^mind that this code does not deal with that if implemented in future
	// 		}

	// 		if (strNode.val.length() > 1) {
	// 			strNode.val = strNode.val.substring(1);
	// 			//System.out.printf("curr level: %s\n", this.root);
	// 			//System.out.printf("recurse with %s\n", strNode);
	// 			//System.out.printf("Child of %s --> %s\n", got, got.child.root);
	// 			got.child.insert(strNode); //recursion
	// 			//System.out.printf("Child of %s --> %s\n", got, got.child.root);
	// 		}

	// 	} else {
	// 		//System.out.printf("return on search: NULL\n");
	// 		if (strNode.val.length() > 1) {
	// 			/* insert full UNIQUE string into single trie hash got */ 
	// 			//System.out.printf("<%s\n", this.root);
	// 			//System.out.printf("putting {STR}: %s\n", strNode);
	// 			this.root.put(strNode);
	// 			//System.out.printf("-->%s\n",this.root); //testing
	// 			return true;
	// 		} else if (strNode.val.length() == 1) {
	// 			/* string is only one char, so insert just char hash got */
	// 			//TODO - is the overhead of these checks less than the overhead of just using
	// 			//		^strings?
	// 			//		^Maybe can make TrieStrHash convert itself to TrieCharHash when needed..?
	// 			//System.out.printf("<%s\n", this.root);
	// 			//System.out.printf("putting {CHAR}: %s\n", strNode);
	// 			this.root.put(new TrieCharHash(strNode.val.charAt(0), strNode.child, 
	// 					(Entry) strNode.entries.getVal(0))); //questionable entry logic...?
	// 			//System.out.printf("-->%s\n",this.root); //testing
	// 			return true;
	// 		} else {
	// 			/* end of string, therefore, string already in trie. Just add entry to Set */
	// 			got.entries.add((Entry) strNode.entries.getVal(0));
	// 			//System.out.println(this.root); //testing
	// 			return false;
	// 		}
	// 	}

	// 	return false;
	// }

	//******END OLD******//

	//TODO create remove(Entry e)
	/**
	 * Remove a string from the trie.
	 * @param id the entry id
	 * @return true if removed something; false otherwise
	 */
	public boolean remove4(String id) {
		int index = this.allEntries.search(id); //TODO put this in main implementation not here!

		if (index != -1) {
			Entry tempEntry = (Entry) this.allEntries.getVal(index);
			String words[] = tempEntry.getDataStr().toLowerCase().split("\\s+");
			for (int i = 0; i < words.length; i++) {
				TrieHashNode tempNode = this.root.get(new TrieHashNode(words[i], null, null));
				if (tempNode != null) {
					this.remove4(tempNode, new ByteArrayCharSequence(words[i]), id);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Remove helper method; remove ...???
	 * @param tempNode node looking in ...????
	 * @param seq word to remove from the trie
	 * @param id the id of the entry to remove
	 * @return ....? DOESNT RETURN ANYTHING USEFUL
	 */
	private boolean remove4(TrieHashNode node, ByteArrayCharSequence seq, String id) {

		Set<Entry> entries = node.entries;

		if (node.child != null) {
			ByteArrayCharSequence value = node.getVal();

			int index = compress(value, seq);

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
				this.remove4(tempNode, seq, id);
			}
		}

		entries.remove(id);
		
		//TODO - does this method have a usefule return?
		return true;
	}

	//*******OLD******//
	// public boolean remove3(String id) {
	// 	int index = this.allEntries.search(id);

	// 	if (index != -1) {
	// 		Entry tempEntry = (Entry) this.allEntries.getVal(index);
	// 		String words[] = tempEntry.getDataStr().toLowerCase().split("\\s+");
	// 		for (int i = 0; i < words.length; i++) {
	// 			TrieHashNode tempNode = this.root.get(new TrieSeqHash(words[i], null, null));
	// 			if (tempNode != null) {
	// 				this.remove3(tempNode, new ByteArrayCharSequence(words[i]), id);
	// 			}
	// 		}
	// 		return true;
	// 	}
	// 	return false;
	// }

	// private boolean remove3(TrieHashNode tempNode, ByteArrayCharSequence seq, String id) {

	// 	//Only expecting TrieSeqHashes...
	// 	if (tempNode instanceof TrieSeqHash) {
	// 		TrieSeqHash tempSeqNode = (TrieSeqHash) tempNode;
	// 		Set<Entry> entries = tempSeqNode.entries;

	// 		if (tempSeqNode.child != null) {
	// 			ByteArrayCharSequence value = tempSeqNode.getVal();

	// 			int index = compress(value, seq);

	// 			seq = seq.subSequence(index);
	// 			TrieHashNode tempHashNode = null;
	// 			if (seq.length() > 0) {
	// 				tempHashNode = tempSeqNode.child.root.get(new TrieSeqHash(seq, null, null));
	// 			}

	// 			if (tempHashNode != null) {
	// 				if (tempHashNode.isLeaf()){
	// 					//if the node is a leaf, it is safe to remove it
	// 					tempSeqNode.child.root.remove(tempHashNode);
	// 					if (tempSeqNode.child.root.size() == 0) {
	// 						//setting the node to a leaf
	// 						tempSeqNode.leaf = true;
	// 					} 

	// 					if (tempSeqNode.child.root.size() == 1) {
	// 						//recompression
	// 						Iterator<TrieHashNode> it = tempSeqNode.child.root.iterator();
	// 						TrieSeqHash theOtherChild = null; 
	// 						if (it.hasNext()) {
	// 							theOtherChild = (TrieSeqHash) it.next();
	// 						}
	// 						this.pullNodesUp(tempSeqNode, theOtherChild);
	// 					}

	// 				}
	// 				this.remove3(tempHashNode, seq, id);
	// 			}
	// 		}

	// 		entries.remove(id);
	// 	}

	// 	//does this method ever return true?
	// 	return false;
	// }

	// private boolean remove(TrieHashNode tempNode, String str, String id) {

	// 	if (tempNode instanceof TrieStrHash) {
	// 		TrieStrHash tempStrNode = (TrieStrHash) tempNode;
	// 		Set<Entry> entries = tempStrNode.entries;

	// 		if (entries.size() > 1) {
	// 			if (tempStrNode.child != null && str.length() > 1) {
	// 				String value = tempStrNode.getVal();
	// 				str = str.substring(str.indexOf(value.charAt(value.length()-1)));

	// 				TrieHashNode tempHashNode = tempStrNode.child.root.get(new TrieStrHash(str, null, null));
	// 				if (tempHashNode != null) {
	// 					if (tempHashNode.entries.size() == 1) {
	// 						tempStrNode.child.root.remove(tempHashNode);
	// 					}
	// 					this.remove(tempHashNode, str, id);
	// 				}
	// 			}
	// 		}
	// 		entries.remove(id);

	// 	} else {
	// 		TrieCharHash tempCharNode = (TrieCharHash) tempNode;

	// 		Set<Entry> entries = tempCharNode.entries;

	// 		if (entries.size() > 1) {
	// 			if (str.length() > 1) {
	// 				str = str.substring(1);
	// 			}

	// 			if (tempCharNode.child != null) {
	// 				TrieHashNode tempHashNode = tempCharNode.child.root.get(new TrieStrHash(str, null, null));
	// 				if (tempHashNode != null) {
	// 					if (tempHashNode.entries.size() == 1) {
	// 						tempCharNode.child.root.remove(tempHashNode);
	// 					}
	// 					this.remove(tempHashNode, str, id);
	// 				}
	// 			}
	// 		}
	// 		entries.remove(id);

	// 	}

	// 	return false;
	// }

	// /**
	//  * Removes string from trie, two different uses.
	//  * 	1) If string is non-unique or a prefix to another string, just remove id from ref list.
	//  * 	2) If string is unique (i.e. no other ref in list), remove entire string from trie.
	//  * ISSUES: need to search through Set of Entries to find one to delete (linear search is not 
	//  * 	efficient, others take up more space)
	//  * -maybe need a skip list structure...
	//  * 
	//  * @param str the str to remove
	//  * @param id the id associated with the str
	//  * @return true if removed string; false otherwise
	//  */
	// public boolean remove(String str, String id) {
	// 	String words[] = str.toLowerCase().split("\\s+");

	// 	for (int i = 0; i < words.length; i++) {
	// 		TrieHashNode tempNode = this.root.get(new TrieStrHash(words[i], null, null));
	// 		if (tempNode != null) {
	// 			this.remove(tempNode, words[i], id);
	// 		}
	// 	}
	// 	return false;
	// }

	// public boolean remove2(String id) {
	// 	int index = this.allEntries.search(id);

	// 	if (index != -1) {
	// 		Entry tempEntry = (Entry) this.allEntries.getVal(index);
	// 		String words[] = tempEntry.getDataStr().toLowerCase().split("\\s+");
	// 		for (int i = 0; i < words.length; i++) {
	// 			TrieHashNode tempNode = this.root.get(new TrieStrHash(words[i], null, null));
	// 			if (tempNode != null) {
	// 				this.remove2(tempNode, words[i], id);
	// 			}
	// 		}
	// 		return true;
	// 	}
	// 	return false;
	// }

	// private boolean remove2(TrieHashNode tempNode, String str, String id) {

	// 	//Only expecting TrieStrHashes...
	// 	if (tempNode instanceof TrieStrHash) {
	// 		TrieStrHash tempStrNode = (TrieStrHash) tempNode;
	// 		Set<Entry> entries = tempStrNode.entries;

	// 		if (tempStrNode.child != null) {
	// 			String value = tempStrNode.getVal();

	// 			int index = compress(value, str);

	// 			str = str.substring(index);
	// 			TrieHashNode tempHashNode = null;
	// 			if (str.length() > 0) {
	// 				tempHashNode = tempStrNode.child.root.get(new TrieStrHash(str, null, null));
	// 			}

	// 			if (tempHashNode != null) {
	// 				if (tempHashNode.isLeaf()){
	// 					//if the node is a leaf, it is safe to remove it
	// 					tempStrNode.child.root.remove(tempHashNode); 
	// 					if (tempStrNode.child.root.size() == 0) {
	// 						//setting the node to a leaf
	// 						tempStrNode.leaf = true;
	// 					} if (tempStrNode.child.root.size() == 1) {
	// 						//recompression
	// 						Iterator<TrieHashNode> it = tempStrNode.child.root.iterator();
	// 						TrieStrHash theOtherChild = null; 
	// 						if (it.hasNext()) {
	// 							theOtherChild = (TrieStrHash) it.next();
	// 						}
	// 						this.pullNodesUp(tempStrNode, theOtherChild);
	// 					}
	// 				}
	// 				this.remove2(tempHashNode, str, id);
	// 			}
	// 		}

	// 		entries.remove(id);

	// 	} 

	// 	return false;
	// }
	//******END OLD*******//

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
	
	//*******OLD******//
	// /**
	//  * This method pulls up the child's children and make them children of the parent.
	//  * It then changes the parent's data string to accommodate and removes the child.
	//  * @param parent the parent node
	//  * @param child the child node
	//  */
	// private void pullNodesUp(TrieHashNode parent, TrieHashNode child) {

	// 	if (parent instanceof TrieStrHash && child instanceof TrieStrHash) {
	// 		TrieStrHash strParent = (TrieStrHash) parent;
	// 		TrieStrHash strChild = (TrieStrHash) child;
			
	// 		if (strChild.isLeaf()) {
	// 			strParent.leaf = true;
	// 			strParent.changeStr(strParent.val + strChild.val);
	// 			strParent.child.root.remove(strChild);
	// 		} else {
	// 			Iterator<TrieHashNode> iterator = strChild.child.root.iterator();
	// 			ArrayList<TrieStrHash> childArray = new ArrayList<TrieStrHash>(); 
	// 			if (iterator.hasNext()) {
	// 				childArray.add((TrieStrHash) iterator.next());
	// 			}
	// 			for (int i = 0; i < childArray.size(); i++) {
	// 				strParent.child.root.put(childArray.get(i));
	// 			}
	// 			strParent.changeStr(strParent.val + strChild.val);
	// 			strParent.child.root.remove(strChild);
	// 		}
	// 	}

	// }
	//*******END OLD*********//

	/**
	 * This method gets two strings as input and finds the
	 * index of the first character that is different between
	 * the two strings.
	 * @param first The first string
	 * @param second The second string
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

	//*********OLD********//
	// /**
	//  * This method gets two strings as input and finds the
	//  * index of the first character that is different between
	//  * the two strings
	//  * @param first The first string
	//  * @param second The second string
	//  * @return index of the character that is different 
	//  */
	// private int compress(String first, String second) {
	// 	int i = 0, j = 0;
	// 	boolean foundDiff = false;
	// 	String firstLetter = null, secondLetter = null;
	// 	while (!foundDiff) {
	// 		if (i < first.length()) {
	// 			firstLetter = first.charAt(i) + "";
	// 		}

	// 		if (j < second.length()) {
	// 			secondLetter = second.charAt(j) + "";
	// 		}

	// 		if (firstLetter == null || secondLetter == null){
	// 			foundDiff = true;
	// 			if (i > j) {
	// 				i = j;
	// 			}
	// 		} else if (!firstLetter.equals(secondLetter)) {
	// 			foundDiff = true;
	// 		} else {
	// 			i++;
	// 			j++;
	// 			firstLetter = null;
	// 			secondLetter = null;
	// 		}
	// 	}
	// 	return i;
	// }

	// //using ByteArrayCharSequence
	// private int compress(ByteArrayCharSequence first, ByteArrayCharSequence second) {
	// 	int i = 0, j = 0;
	// 	boolean foundDiff = false;
	// 	char charNull = '\u0000';
	// 	char firstLetter = charNull, secondLetter = charNull;
	// 	while (!foundDiff) {
	// 		if (i < first.length()) {
	// 			firstLetter = first.charAt(i);// + "";
	// 		}

	// 		if (j < second.length()) {
	// 			secondLetter = second.charAt(j);// + "";
	// 		}

	// 		if (firstLetter == charNull || secondLetter == charNull){
	// 			foundDiff = true;
	// 			if (i > j) {
	// 				i = j;
	// 			}
	// 		} else if (firstLetter != secondLetter) {
	// 			foundDiff = true;
	// 		} else {
	// 			i++;
	// 			j++;
	// 			firstLetter = charNull;
	// 			secondLetter = charNull;
	// 		}
	// 	}
	// 	return i;
	// }
	//******END OLD*******//

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

	//********OLD******//
	// /**
	//  * This is a generic search method. 
	//  * @param queryString The string to find
	//  * @return The list of entry objects that have been found from the query
	//  */
	// private List<Entry> genericSearch(String queryString) {
	// 	DoubleHashedHashMap<QueryEntry> map = new DoubleHashedHashMap<QueryEntry>();
	// 	ArrayList<Entry> idList = new ArrayList<Entry>();
	// 	String words[] = queryString.toLowerCase().split("\\s+");

	// 	/* insert individual words into trie */
	// 	for (int i = 0; i < words.length; i++) {
	// 		TrieStrHash temp = new TrieStrHash(words[i], null, null);
	// 		this.search(temp, map);
	// 	}

	// 	Iterator<QueryEntry> iterator = map.iterator();
	// 	while(iterator.hasNext()) {
	// 		QueryEntry tempEntry = iterator.next();
	// 		//checks if the entry is correct
	// 		if (tempEntry.getFrequency() == words.length) {
	// 			//limits the number of results returned
	// 			idList.add(tempEntry.getEntry());
	// 		}
	// 	}

	// 	return idList;
	// }
	//******END OLD*******//

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
			int[] boostValues = {1, 1, 1, 1};
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
	 * ing the map.
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

	//*******OLD*********//
	// /**
	//  * This method searches for the a String in the trie.
	//  * It fills the map with QueryEntry objects when it finds a node that should be
	//  * ing the map.
	//  * @param strNode The query node
	//  * @param map The map of QueryEntries
	//  */
	// private void search(TrieHashNode strNode, DoubleHashedHashMap<QueryEntry> map) {
	// 	TrieHashNode tempNode = this.root.get(strNode);
	// 	if (tempNode != null){
	// 		//Node exists
	// 		if (tempNode instanceof TrieStrHash && strNode instanceof TrieStrHash) {
	// 			TrieStrHash tempStrNode = (TrieStrHash) tempNode;
	// 			String value = tempStrNode.val;
	// 			TrieStrHash queryStrNode = (TrieStrHash) strNode;
				
	// 			if (!queryStrNode.val.equals(value) && queryStrNode.val.length() >= value.length()) {
	// 				//Check if we need to go down more levels
	// 				int index = compress(value, queryStrNode.val);
	// 				if (queryStrNode.getVal().length() > index) {
	// 					queryStrNode.changeStr(queryStrNode.getVal().substring(index));
	// 					tempStrNode.child.search(queryStrNode, map);
	// 				}
	// 			} else {
	// 				//We want the entries from this node
	// 				for (int i = 0; i < tempStrNode.entries.size(); i++) {
	// 					Entry tempEntry = (Entry)tempStrNode.entries.getVal(i);
	// 					QueryEntry tempQueryEntry = new QueryEntry(tempEntry,1);
	// 					QueryEntry prevQueryEntry = map.get(tempQueryEntry);
	// 					if (prevQueryEntry != null) {
	// 						prevQueryEntry.setFrequency(prevQueryEntry.getFrequency() + 1);
	// 					} else {
	// 						map.put(tempQueryEntry);
	// 					}
	// 				}
					
	// 			}
				
				
	// 		}
	// 	}
	// }
	//******END OLD********//

	/**
	 * Breadth first recursive traversal of trie.
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
		trie.insert6(e0);

		System.out.printf("\n0%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e1 = new Entry("e" + Integer.toString(1), 't', 10, words[1]);
		System.out.printf("ENTRY #%d\n", 1);
		trie.insert6(e1);
		
		System.out.printf("\n1%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e2 = new Entry("e" + Integer.toString(2), 'q', 10, words[2]);
		System.out.printf("ENTRY #%d\n", 2);
		trie.insert6(e2);
		
		System.out.printf("\n2%s\n", trie);
		trie.breadthFirstTraversal(trie);

		Entry e3 = new Entry("e" + Integer.toString(3), 'b', 10, words[3]);
		System.out.printf("ENTRY #%d\n", 3);
		trie.insert6(e3);

		System.out.printf("\n3%s\n", trie);
		trie.breadthFirstTraversal(trie);

		System.out.println("REMOVING STUFF");
		

		//normal search
//		System.out.println(trie.search(3, "b"));

		//weighted search, no boosts
//		System.out.println(trie.weightedSearch(2, "b", 0, null));
		
		//weighted search, boosts
		/*List<Boost> boostList = new ArrayList<Boost>();
		boostList.add(new Boost("board", 50));
		boostList.add(new Boost("question", 100));
		boostList.add(new Boost("e3", 51));
		System.out.println(trie.weightedSearch(4, "b", 3, boostList));*/
		//trie.remove2("e3"); //TODO - needs to be changed so just uses ID

		//System.out.printf("After remove...\n%s\n", trie);
		//trie.breadthFirstTraversal(trie);

		//testing ByteArrayCharSequence stuff
		ByteArrayCharSequence cseq = new ByteArrayCharSequence("byte array, yay!");
		System.out.println(cseq.subSequence(0,3).hashCode());
		ByteArrayCharSequence empty = new ByteArrayCharSequence("");
		System.out.println(empty.hashCode());
		ByteArrayCharSequence cseq_c = cseq.subSequence(0, cseq.length()-1);
		System.out.println(cseq.equals(cseq_c)); //should be true
		System.out.println(cseq.subSequence(1,4).subSequence(2)); //should print "e"
	}
}