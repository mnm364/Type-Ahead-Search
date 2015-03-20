/* NOTES
 * -do all the checks to see if got is char or string justify the overhead if everything was a
 * ^^string?
 * -midstring compression is not implemented yet
 * ^^b/c of this, inputting same string in twice will make the whole string single chars.. :(
 */

import java.util.Queue;
import java.util.LinkedList;

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
		protected CompressedHashTrie child = new CompressedHashTrie();
		
	//	TODO - make this list of references to entries
	//	TODO - make this a skip list data structure (JEFF SAID WE DONT NEED THIS...?)
		/* list of object references to unordered master Set */
		protected Set<Entry> entries = new Set<>();
		
		/**
		 * Determine if got is leaf or not.
		 * @return true if leaf; false otherwise
		 */
		private boolean isLeaf() {
			return leaf;
		}

		private TrieHashNode(CompressedHashTrie child, Entry e) {
			this.child = child;
			this.entries.add(e);
		}

		public void addEntry(Entry e) {
			this.entries.add(e);
		}

		public Set<Entry> getEntries() {
			return entries;
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
		 * @param child the next got in the trie
		 * @param e reference to the entry from which being inserted
		 */
		private TrieStrHash(String val, CompressedHashTrie child, Entry e) {
			super(child, e);
			this.val = val;
		}

		public void setChildNull() {
			this.child = null;
		}

		public String getVal() {
			return val;
		}

		public void changeStr(String str) {
			this.val = str;
		}

		@Override
		public String toString() {
			return "(" + this.val + ")";
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
		 * @param child the next got in the trie
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
		public String toString() {
			return "(" + this.val + ")";
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
		this(new DoubleHashedHashMap<TrieHashNode>()); //was NULL
	}
	
	/**
	 * Insert entry data into trie
	 * break up entry string into single words.
	 *
	 * @param e the entry to be inserted into the trie
	 */
	public void insert(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		System.out.printf("- phrase: %s\n", e.getDataStr().toLowerCase());
		String words[] = e.getDataStr().toLowerCase().split("\\s+");
		
		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			System.out.printf("- insert word[%d]: %s\n", i, words[i]);
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
		/* base case
		if (this.root.isEmpty()) {
			this.root = new DoubleHashedHashMap<TrieHashNode>();
			this.root.put(strNode);
			System.out.printf("new level: %s\n", strNode);
			System.out.printf("-->%s\n",this.root); //testing
			return true;
		}*/

		//you know at least that none of the strings in the hash start with the same letter
		//nor do they have any suffixes that are the same... (maybe) <-- NOT IMPLEMENTED
		//every bucket is hashed by a single character, but values can be chars or strings... :?
		//^^--> SO, need to find a way to hash new entry and check value of first char in buckets
 		//		if string (SOLVED with Overrides equal() in TrieHashNode)

		System.out.printf("search for: %s\n", strNode);
		TrieHashNode got = this.root.get(strNode);
		/* check to see if already in hashmap */
		if (got != null) {
			System.out.printf("return on search: %s\n", got);
			/* char or first char in string in hash */
			/* OPTIONS
			 * 1) If got is a TrieStrHash, then need to break up string and move it down one level
			 * ^also need to take away first char in string, then pass back to insert. (2 ops)
			 * 2) If got is a TrieCharHash, then need to move down one level and take away first
			 * ^char in string, then pass back to insert.
			 */
			//TODO - need to get away from using instanceof so much!
			if (got.child == null){
				//System.out.printf("child of %s --> NEW\n", got);
				got.child = new CompressedHashTrie();
			} else {
				//System.out.printf("child of %s --> %s\n", got, got.child.root);
			}
			if (got instanceof TrieStrHash) {
				TrieStrHash tempStrNode = (TrieStrHash) got;
				
				//roundabout way of moving string down a level, but should be more efficient
				TrieCharHash tempCharNode = new TrieCharHash(tempStrNode.val.charAt(0), got.child,
					null); //TODO - giving me issues with "got.child"
				System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
				tempCharNode.entries = got.entries; //copy over entries to char got
				System.out.printf("<%s\nremove attempt: %s \n",this.root, got);
				this.root.remove(got); //remove string got from current level of trie
				System.out.printf("-->%s\n",this.root);
				tempStrNode.val = tempStrNode.val.substring(1); //take away first char of string
				System.out.printf("inserting %s to child of %s\n", tempStrNode, tempCharNode);
				tempCharNode.child.insert(tempStrNode); //add edited string got to level below
				System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
				System.out.printf("putting char: %s\n", tempCharNode);
				this.root.put(tempCharNode); //add char got into current level of trie
				System.out.printf("-->%s\n", this.root);				
				//TODO - At this point dont have to deal with children of string nodes, but keep in 
				//^mind that this code does not deal with that if implemented in future
			}
			
			
			//3/20/15 ADDED a check for the length of the string because it was throwing an error before 
			
			if (strNode.val.length() > 1) {
				strNode.val = strNode.val.substring(1);
				System.out.printf("curr level: %s\n", this.root);
				System.out.printf("recurse with %s\n", strNode);
				System.out.printf("Child of %s --> %s\n", got, got.child.root);
				got.child.insert(strNode); //recursion
				System.out.printf("Child of %s --> %s\n", got, got.child.root);
			} else if (strNode.val.length() == 1) {
					/* string is only one char, so insert just char hash got */
					//TODO - is the overhead of these checks less than the overhead of just using
					//		^strings?
					//		^Maybe can make TrieStrHash convert itself to TrieCharHash when needed..?
					System.out.printf("<%s\n", this.root);
					System.out.printf("putting {CHAR}: %s\n", strNode);
					this.root.put(new TrieCharHash(strNode.val.charAt(0), strNode.child, 
						(Entry) strNode.entries.getVal(0))); //questionable entry logic...?
					System.out.printf("-->%s\n",this.root); //testing
					return true;
				
			}
			
			

		} else {
			System.out.printf("return on search: NULL\n");
			if (strNode.val.length() > 1) {
				/* insert full UNIQUE string into single trie hash got */ 
				System.out.printf("<%s\n", this.root);
				System.out.printf("putting {STR}: %s\n", strNode);
				this.root.put(strNode);
				System.out.printf("-->%s\n",this.root); //testing
				return true;
			} else if (strNode.val.length() == 1) {
				/* string is only one char, so insert just char hash got */
				//TODO - is the overhead of these checks less than the overhead of just using
				//		^strings?
				//		^Maybe can make TrieStrHash convert itself to TrieCharHash when needed..?
				System.out.printf("<%s\n", this.root);
				System.out.printf("putting {CHAR}: %s\n", strNode);
				this.root.put(new TrieCharHash(strNode.val.charAt(0), strNode.child, 
					(Entry) strNode.entries.getVal(0))); //questionable entry logic...?
				System.out.printf("-->%s\n",this.root); //testing
				return true;
			} else {
				/* end of string, therefore, string already in trie. Just add entry to Set */
				got.entries.add((Entry) strNode.entries.getVal(0));
				System.out.println(this.root); //testing
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

/* work in progress...
	private CompressedHashTrie breadthFirstTraversal(CompressedHashTrie trie) {
		//Breadth first traversal
		if (trie == null) {
			return 
		}
		Queue<TrieHashNode> queue = new LinkedList<>();
		for (int i = 0; trie.root.iterator().hasNext(); i++) {
			queue.add(trie.root.iterator().next());
		}
		while (queue.peek() != null) {
			breadthFirstTraversal(queue.remove().child);
		}
	}

	@Override
	public String toString() {
		return this.root.toString();
	}
*/
	/**
	 * Test driver main method
	 */
	public static void main(String[] args) {
		System.out.println("testing compressed hash trie...");
		CompressedHashTrie trie = new CompressedHashTrie();
		String[] words = {"sap","b soda","bob","by sad sap"};
		for (int i = 0; i < 4; i++) {
			Entry e = new Entry("e" + Integer.toString(i), 'b', 10, words[i]);
			System.out.printf("ENTRY #%d\n", i);
			trie.insert(e);
			//TrieStrHash strHash = new TrieStrHash("sap",null,e);
			//trie.root.get(strHash).getEntries().toString();
			//trie.root.toString();
		}
	}
}