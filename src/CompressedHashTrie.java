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
		protected DoubleHashedHashMap<TrieHashNode> child;
		
	//	TODO - make this list of references to entries
	//	TODO - make this a skip list data structure (JEFF SAID WE DONT NEED THIS...?)
		/* list of object references to unordered master Set */
		protected Set<Entry> eSet;
		
		/**
		 * Determine if node is leaf or not.
		 * @return true if leaf; false otherwise
		 */
		private boolean isLeaf() {
			return leaf;
		}

		private TrieHashNode(DoubleHashedHashMap<TrieHashNode> child, Entry e) {
			this.child = child;
			this.eSet.add(e);
		}

		/**
		 * Checks to see if first letter of val is equal
		 *
		 */
		//TODO - IS THERE A BETTER WAY TO PERFORM THIS CHECK?!!!? seems pretty expensive...
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
		private TrieStrHash(String val, DoubleHashedHashMap<TrieHashNode> 
				child, Entry e) {
			super(child, e);
			this.val = val;
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
		private TrieCharHash(char val, DoubleHashedHashMap<TrieHashNode> 
				child, Entry e) {
			super(child, e);
			this.val = val;
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
	public CompressedHashTrie() {
		//TODO - stuff (copy constructor, etc...)
	}
	
	/**
	 * Insert entry data into trie
	 * break up entry string into single words.
	 *
	 * @param e the entry to be inserted into the trie
	 */
	public void insert(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		String words[] = e.getDataStr().split("//s+");
		
		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			this.insert(words[i], e);
		}
	}

	/**
	 * Inserts a single string into the trie.
	 * 
	 * @param str the string to insert
	 * @param e the entry that the string references
	 * @return true if insert successful; false otherwise
	 */
	private boolean insert(String str, Entry e) {
		
		/* base case */
		if (this.root == null) {
			this.root = new DoubleHashedHashMap<TrieHashNode>();
			this.root.put(new TrieStrHash(str, null, e));
			return true;
		}

		//you know at least that none of the strings in the hash start with the same letter
		//nor do they have any suffixes that are the same... (maybe)
		//every bucket is hashed by a single character, but values can be chars or strings... :?
		//^^--> SO, need to find a way to hash new entry and check value of first char in buckets
 		//		if string (SOLVED with Overrides equal() in TrieHashNode)

		TrieStrHash in = new TrieStrHash(str, null, e);
		if (this.root.containsKey(in)) {
			;
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

	/**
	 * Test driver main method
	 */
	public static void main(String[] args) {
		System.out.println("testing compressed hash trie...");
		CompressedHashTrie trie = new CompressedHashTrie();
		Entry e = new Entry("q1", 'q', 10, "hello, world is this a test query?");
		trie.insert(e);
	}
}
