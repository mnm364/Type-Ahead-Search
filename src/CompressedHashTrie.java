/* NOTES
 * -do all the checks to see if node is char or string justify the overhead if everything was a
 * ^^string?
 * -midstring compression is not implemented yet
 * ^^b/c of this, inputting same string in twice will make the whole string single chars.. :(
 */

//import java.util.Queue

/**
 * need to make a full write up on this
 * 
 * @author Michael Miller, Jeffrey Sham
 * 
 */
public class CompressedHashTrie {
	
	/* Root of the trie. */
	private DoubleHashedHashMap<TrieHashNode> root;
	
	/**
	 * 
	 *
	*/
	private class TrieHashNode {
		/* Boolean to mark leaf nodes. */
		protected boolean leaf;
		
		/* Pointer to subsequent hash map. */
		protected CompressedHashTrie child;
		
	//	TODO - make this list of references to entries
	//	TODO - make this a skip list data structure (JEFF SAID WE DONT NEED THIS...?)
		/* list of object references to unordered master Set */
		protected Set<Entry> entries;
		
		/**
		 * Determine if node is leaf or not.
		 * @return true if leaf; false otherwise
		 */
		private boolean isLeaf() {
			return leaf;
		}

		private TrieHashNode(CompressedHashTrie child, Entry e) {
			if (this.entries == null) {
				this.entries = new Set<Entry>();
			}
			
			if (this.child == null) {
				this.child = new CompressedHashTrie();
			}
			
			if (child != null) {
				//System.out.println(child.toString());
				this.child = child;
			}
			//System.out.println(e.toString());
			
			if (e != null) {
				this.entries.add(e);
			}
		}

		public void addEntry(Entry e) {
			this.entries.add(e);
		}

		/**
		 * Checks to see if first letter of val is equal
		 *
		 */
		//TODO - IS THERE A BETTER WAY TO PERFORM THIS CHECK?!!!? seems pretty expensive...
		//^^yes... make DoubleHashedHashMap not generic so it can deal with this directly...?
		//@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			//TrieHashNode that = (TrieHashNode) o;
			if (this instanceof TrieStrHash) {
				TrieStrHash _this = (TrieStrHash) this;
				if (o instanceof TrieStrHash) {
					TrieStrHash that = (TrieStrHash) o;
					return _this.val.charAt(0) == that.val.charAt(0);
				} else if (o instanceof TrieCharHash) {
					TrieCharHash that = (TrieCharHash) o;
					return _this.val.charAt(0) == that.val;
				} else {
					//dont care...
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
				} else {
					//dont care...
					return false;
				}
			} else {
				//dont care...
				return false;
			}
		}

		//not ever used, but needed for polymorphism
		@Override
		public int hashCode() {
			return this.hashCode();
		}
		
	}
	
	/**
	 *
	 */
	private class TrieStrHash extends TrieHashNode {
		
		/* String to be used in Trie. */
		private String val;
		
		/**
		 * Constructor for TrieStrHash
		 * @param val value of the character to be put in trie
		 * @param child the next node in the trie
		 * @param e reference to the entry from which being inserted
		 */
		private TrieStrHash(String val, CompressedHashTrie child, Entry e) {
			super(child, e);
			this.val = val;
		}

		public String getVal() {
			return val;
		}

		public void changeStr(String str) {
			this.val = str;
		}

		@Override
		public int hashCode() {
			Character c = new Character(val.charAt(0));
			return c.hashCode();
		}
	}
	
	/**
	 *
	 */
	private final class TrieCharHash extends TrieHashNode {
		
		/* Character to be used in Trie and hashed into TrieHashNode. */
		private char val;
		
		/**
		 * Constructor for TrieCharHash
		 * @param val value of the character to be put in trie
		 * @param child the next node in the trie
		 * @param e reference to the entry from which being inserted
		 */
		private TrieCharHash(char val, CompressedHashTrie child, Entry e) {
			super(child, e);
			this.val = val;
		}
		
		public char getVal() {
			return val;
		}

		@Override
		public int hashCode() {
			Character c = new Character(val);
			return c.hashCode();
		}
	}

	/**
	 * Constructor for CompressedHashTrie
	 */
	public CompressedHashTrie(DoubleHashedHashMap<TrieHashNode> root) {
		this.root = root;
		//TODO - stuff (copy constructor, etc...)
	}
	public CompressedHashTrie() {
		this(null); //was NULL
	}
	
	/**
	 * Insert entry data into trie
	 * break up entry string into single words.
	 *
	 * @param e the entry to be inserted into the trie
	 */
	public void insert(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		String words[] = e.getDataStr().toLowerCase().split("//s+");
		
		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			this.insert(new TrieStrHash(words[i], null, e));
		}
	}

	/**
	 * Inserts a single string into the trie.
	 * 
	 * @param str the string to insert
	 * @param e the entry that the string references
	 * @return true if insert updated trie; false if failed or string already in trie
	 */
	private boolean insert(TrieStrHash strNode) {
		/* base case */
		if (this.root == null) {
			this.root = new DoubleHashedHashMap<TrieHashNode>();
			this.root.put(strNode);
			return true;
		}

		//you know at least that none of the strings in the hash start with the same letter
		//nor do they have any suffixes that are the same... (maybe) <-- NOT IMPLEMENTED
		//every bucket is hashed by a single character, but values can be chars or strings... :?
		//^^--> SO, need to find a way to hash new entry and check value of first char in buckets
 		//		if string (SOLVED with Overrides equal() in TrieHashNode)

		TrieHashNode node = this.root.get(strNode);
		/* check to see if already in hashmap */
		if (node != null) {
			/* char or first char in string in hash */
			/* OPTIONS
			 * 1) If node is a TrieStrHash, then need to break up string and move it down one level
			 * ^also need to take away first char in string, then pass back to insert. (2 ops)
			 * 2) If node is a TrieCharHash, then need to move down one level and take away first
			 * ^char in string, then pass back to insert.
			 */
			//TODO - need to get away from using instanceof so much!
			if (node instanceof TrieStrHash) {
				TrieStrHash tempStrNode = (TrieStrHash) node;
				//roundabout way of moving string down a level, but should be more efficient
				TrieCharHash tempCharNode = new TrieCharHash(tempStrNode.val.charAt(0), node.child,
					null);
				tempCharNode.entries = node.entries; //copy over entries to char node
				tempStrNode.val = tempStrNode.val.substring(1); //take away first char of string
				node.child.insert(tempStrNode); //add edited string node to level below
				this.root.remove(node); //remove string node from current level of trie
				this.root.put(tempCharNode); //add char node into current level of trie
				//TODO - At this point dont have to deal with children of string nodes, but keep in 
				//^mind that this code does not deal with that if implemented in future
			}
			strNode.val = strNode.val.substring(1);
			node.child.insert(strNode); //recursion
		} else {
			if (strNode.val.length() > 1) {
				/* insert full UNIQUE string into single trie hash node */ 
				this.root.put(strNode);
				//System.out.print(this.root.toString()); //testing
				return true;
			} else if (strNode.val.length() == 1) {
				/* string is only one char, so insert just char hash node */
				//TODO - is the overhead of these checks less than the overhead of just using
				//		^strings?
				//		^Maybe can make TrieStrHash convert itself to TrieCharHash when needed..?
				this.root.put(new TrieCharHash(strNode.val.charAt(0), strNode.child, 
					(Entry) strNode.entries.getVal(0)));
				System.out.print(this.root.toString()); //testing
				return true;
			} else {
				/* end of string, therefore, string already in trie. Just add entry to Set */
				node.entries.add((Entry) strNode.entries.getVal(0));
				//System.out.print(this.root.toString()); //testing
				return false;
			}
		}

		return false;
	}
	
	/**
	 * Removes string from trie, two different uses.
	 * 	1) If string is non-unique or a prefix to another string, just remove id from ref list.
	 * 	2) If string is unique (i.e. no other ref in list), remove entire string from trie.
	 * ISSUES: need to search through Set of Entries to find one to delete (linear search is not 
	 * 	efficient, others take up more space)
	 * -maybe need a skip list structure...
	 * 
	 * @param str the str to remove
	 * @param id the id associated with the str
	 * @return true if removed string; false otherwise
	 */
	public boolean remove(String str, float id) {
	//	TODO - method stub
		return false;
	}
	
	/**
	 * 
	 */
	//public Set
	
	/**
	 * 
	 * 
	 * @return true if compression happened; otherwise, false
	 */
	private boolean compress() {
		//TODO - method stub
		return false;
	}

/*
	private DoubleHashedHashMap<TrieHashNode> breadthFirstTraversal(CompressedHashTrie trie) {
		//Breadth first traversal
		Queue<DoubleHashedHashMap<TrieHashNode>> queue = new Queue<>();
		for (int i = 0; i < trie.root.size(); i++) {
			if (trie.root.) {
				queue.add();
			}
		}
	}
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
		String[] words = {"sap b","soda","bob","bp sap sap"};
		for (int i = 0; i < 3; i++) {
			Entry e = new Entry("e" + Integer.toString(i), 'b', 10, words[i]);
			trie.insert(e);
		}
	}
}