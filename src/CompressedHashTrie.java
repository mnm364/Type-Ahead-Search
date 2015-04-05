/* NOTES
 * -do all the checks to see if got is char or string justify the overhead if everything was a
 * ^^string?
 * -midstring compression is not implemented yet
 * ^^b/c of this, inputting same string in twice will make the whole string single chars.. :(
 */

import java.util.ArrayList;
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

	/* Root of the trie. */
	private DoubleHashedHashMap<TrieHashNode> root;

	private Set<Entry> allEntries;
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
		 * Determine if got is leaf or not.
		 * @return true if leaf; false otherwise
		 */
		protected boolean isLeaf() {
			return leaf;
		}

		private TrieHashNode(CompressedHashTrie child, Entry e) {
			child = new CompressedHashTrie();
			entries = new Set<Entry>();

			this.child = child;
			this.entries.add(e);
		}

		protected void addEntry(Entry e) {
			this.entries.add(e);
		}

		protected Set<Entry> getEntries() {
			return entries;
		}

		protected Entry getFirstEntry() {
			return (Entry) entries.getVal(0);
		}

		/**
		 * Checks to see if first letter of val is equal
		 *
		 */
		//TODO - IS THERE A BETTER WAY TO PERFORM THIS CHECK?!!!? seems pretty expensive...
		//^^yes... make DoubleHashedHashMap not generic so it can deal with this directly...?
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

		private void setChildNull() {
			this.child = null;
		}

		private String getVal() {
			return val;
		}

		private void changeStr(String str) {
			this.val = str;
		}

		@Override
		public String toString() {
			return "(" + this.val + ":" + entries + ")" + "isLeaf:" + this.leaf;
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

		private char getVal() {
			return val;
		}

		@Override
		public String toString() {
			return "(" + this.val +  ":" + entries + ")";
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
		this.allEntries = new Set<Entry>();
		//TODO - stuff (copy constructor, etc...)
	}
	public CompressedHashTrie() {
		this(new DoubleHashedHashMap<TrieHashNode>(5));//was NULL
		this.allEntries = new Set<Entry>();
	}

	/**
	 * Insert entry data into trie
	 * break up entry string into single words.
	 *
	 * @param e the entry to be inserted into the trie
	 */
	public void insert(Entry e) {
		//TODO - split at commas, punctuation marks, etc.
		String words[] = e.getDataStr().toLowerCase().split("\\s+");

		/* insert individual words into trie */
		for (int i = 0; i < words.length; i++) {
			System.out.printf("- insert %s:{%s}\n", words[i],e);
			allEntries.add(e);
			this.insert4(new TrieStrHash(words[i], null, e));
		}
	}

	private boolean insert4(TrieHashNode strNode) {
		//boolean putSuccess = this.root.put(strNode);
		TrieHashNode tempNode = this.root.get(strNode);

		if (tempNode != null) { //!putSuccess) {
			//The first letter of strNode is already in the hash map
			//TrieHashNode tempNode = this.root.get(strNode);

			if (tempNode.child == null) {
				tempNode.child = new CompressedHashTrie();
			}

			if (tempNode instanceof TrieStrHash && strNode instanceof TrieStrHash) {
				TrieStrHash tempStrHash = (TrieStrHash) tempNode;
				String value = tempStrHash.getVal();
				tempStrHash.leaf = false;
				TrieStrHash newStrNode = (TrieStrHash) strNode;

				int index = compress(value, newStrNode.val);

				tempStrHash.changeStr(value.substring(0, index));

				Set<Entry> entries = newStrNode.entries;
				for (int i = 0; i < entries.size(); i++) {
					tempStrHash.addEntry((Entry)entries.getVal(i));
				}

				if (value.length() > index) {
					TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(index), null, 
							tempStrHash.getFirstEntry());
					tempStrHashShorter.leaf = true;
					tempStrHash.child.insert4(tempStrHashShorter);	
				}

				if (newStrNode.getVal().length() > index) {
					newStrNode.changeStr(newStrNode.getVal().substring(index));
					newStrNode.leaf = true;
					tempStrHash.child.insert4(newStrNode);
				}
			}
		} else {
			//doesnt exist in hash map yet
			this.root.put(strNode);
		}

		return true;
	}




	/**
	 * This method inserts a new TrieStrHash object into the CompressedHashTrie.
	 * It also almost correctly keeps track of the entries that are in the root node.
	 * It converts the TrieStrHash to a TrieCharHash if there are duplicate letters
	 * in the hash map.
	 * @param strNode The TrieHashNode to insert
	 * @return true
	 */
	private boolean insert3(TrieHashNode strNode) {
		//boolean putSuccess = this.root.put(strNode);
		TrieHashNode tempNode = this.root.get(strNode);

		if (tempNode != null) { //!putSuccess) {
			//The first letter of strNode is already in the hash map
			//TrieHashNode tempNode = this.root.get(strNode);

			if (tempNode.child == null) {
				tempNode.child = new CompressedHashTrie();
			}

			if (tempNode instanceof TrieStrHash && strNode instanceof TrieStrHash) {
				TrieStrHash tempStrHash = (TrieStrHash) tempNode;
				String value = tempStrHash.getVal();

				TrieCharHash tempCharHash = new TrieCharHash(value.charAt(0),
						new CompressedHashTrie(), tempStrHash.getFirstEntry());

				//System.out.printf("%s - %s\n", tempStrHash, tempStrHash.entries);

				//tempCharHash.entries = tempStrHash.entries;

				//tempCharHash.addEntry(tempStrHash.getFirstEntry());
				//System.out.printf("%s - %s\n", tempCharHash, tempCharHash.entries);

				this.root.remove(tempStrHash);
				this.root.put(tempCharHash);

				TrieStrHash newStrNode = (TrieStrHash) strNode;

				Set<Entry> entries = newStrNode.entries;
				for (int i = 0; i < entries.size(); i++) {
					tempCharHash.addEntry((Entry)entries.getVal(i));
				}

				if (value.length() > 1) {
					TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(1), null, 
							null);
					tempStrHashShorter.entries = tempCharHash.entries;
					tempCharHash.child.insert3(tempStrHashShorter);	
				}

				if (newStrNode.getVal().length() > 1) {
					newStrNode.changeStr(newStrNode.getVal().substring(1));
					tempCharHash.child.insert3(newStrNode);
				}
				//System.out.printf("%s\n%s --> %s\n", this.root, tempStrHash, 
				//	tempStrHash.child.root);

			} else if(tempNode instanceof TrieCharHash && strNode instanceof TrieStrHash) {
				TrieCharHash tempCharHash = (TrieCharHash) tempNode;

				TrieStrHash newStrNode = (TrieStrHash) strNode;

				Set<Entry> entries = newStrNode.entries;
				for (int i = 0; i < entries.size(); i++) {
					tempCharHash.addEntry((Entry)entries.getVal(i));
				}

				if (newStrNode.getVal().length() > 1) {
					newStrNode.changeStr(newStrNode.getVal().substring(1));
					tempCharHash.child.insert3(newStrNode);
				}
				//System.out.printf("%s\n%s --> %s\n", this.root, tempCharHash, 
				//	tempCharHash.child.root);

			}
		} else {
			//doesnt exist in hash map yet
			this.root.put(strNode);
		}

		return true;
	}

	private boolean insert2(TrieStrHash strNode) {
		boolean putSuccess = this.root.put(strNode);

		if (!putSuccess) {
			//The first letter of strNode is already in the hash map
			TrieHashNode tempNode = this.root.get(strNode);

			if (tempNode.child == null) {
				tempNode.child = new CompressedHashTrie();
			}

			if (tempNode instanceof TrieStrHash) {
				TrieStrHash tempStrHash = (TrieStrHash) tempNode;
				String value = tempStrHash.getVal();
				tempStrHash.changeStr(value.charAt(0) + "");

				if (value.length() > 1) {
					TrieStrHash tempStrHashShorter = new TrieStrHash(value.substring(1), null,
							null);
					tempStrHashShorter.entries = tempStrHash.entries;
					//tempStrHash.addEntry(strNode.getFirstEntry()); //update entry list
					tempStrHash.child.insert2(tempStrHashShorter);	
				}

				if (strNode.getVal().length() > 1) {
					strNode.changeStr(strNode.getVal().substring(1));
					tempStrHash.child.insert2(strNode);
				}

				System.out.printf("%s\n%s --> %s\n", this.root, tempStrHash, 
						tempStrHash.child.root);
			}
		} else {
			System.out.printf("%s\n", this.root); //for testing
		}

		return false;
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

		//System.out.printf("search for: %s\n", strNode);
		TrieHashNode got = this.root.get(strNode);
		/* check to see if already in hashmap */
		if (got != null) {
			//System.out.printf("return on search: %s\n", got);
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
				TrieCharHash tempCharNode = new TrieCharHash(tempStrNode.val.charAt(0), new CompressedHashTrie(),
						null); //TODO - giving me issues with "got.child"
				tempStrNode.child = new CompressedHashTrie();
				//System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
				tempCharNode.entries = got.entries; //copy over entries to char got
				//System.out.printf("<%s\nremove attempt: %s \n",this.root, got);
				this.root.remove(got); //remove string got from current level of trie
				//System.out.printf("-->%s\n",this.root);
				tempStrNode.val = tempStrNode.val.substring(1); //take away first char of string
				//System.out.printf("inserting %s to child of %s\n", tempStrNode, tempCharNode);
				tempCharNode.child.insert(tempStrNode); //add edited string got to level below
				//System.out.printf("child of %s --> %s\n", tempCharNode, tempCharNode.child.root);
				//System.out.printf("putting char: %s\n", tempCharNode);
				this.root.put(tempCharNode); //add char got into current level of trie
				//System.out.printf("-->%s\n", this.root);	

				//TODO - At this point dont have to deal with children of string nodes, but keep in 
				//^mind that this code does not deal with that if implemented in future
			}

			if (strNode.val.length() > 1) {
				strNode.val = strNode.val.substring(1);
				//System.out.printf("curr level: %s\n", this.root);
				//System.out.printf("recurse with %s\n", strNode);
				//System.out.printf("Child of %s --> %s\n", got, got.child.root);
				got.child.insert(strNode); //recursion
				//System.out.printf("Child of %s --> %s\n", got, got.child.root);
			}

		} else {
			//System.out.printf("return on search: NULL\n");
			if (strNode.val.length() > 1) {
				/* insert full UNIQUE string into single trie hash got */ 
				//System.out.printf("<%s\n", this.root);
				//System.out.printf("putting {STR}: %s\n", strNode);
				this.root.put(strNode);
				//System.out.printf("-->%s\n",this.root); //testing
				return true;
			} else if (strNode.val.length() == 1) {
				/* string is only one char, so insert just char hash got */
				//TODO - is the overhead of these checks less than the overhead of just using
				//		^strings?
				//		^Maybe can make TrieStrHash convert itself to TrieCharHash when needed..?
				//System.out.printf("<%s\n", this.root);
				//System.out.printf("putting {CHAR}: %s\n", strNode);
				this.root.put(new TrieCharHash(strNode.val.charAt(0), strNode.child, 
						(Entry) strNode.entries.getVal(0))); //questionable entry logic...?
				//System.out.printf("-->%s\n",this.root); //testing
				return true;
			} else {
				/* end of string, therefore, string already in trie. Just add entry to Set */
				got.entries.add((Entry) strNode.entries.getVal(0));
				//System.out.println(this.root); //testing
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
	public boolean remove(String str, String id) {
		//	TODO - method stub
		String words[] = str.toLowerCase().split("\\s+");

		for (int i = 0; i < words.length; i++) {
			TrieHashNode tempNode = this.root.get(new TrieStrHash(words[i], null, null));
			if (tempNode != null) {
				this.remove(tempNode, words[i], id);
			}
		}
		return false;
	}

	public boolean remove2(String id) {
		int index = this.allEntries.search(id);

		if (index != -1) {
			Entry tempEntry = (Entry) this.allEntries.getVal(index);
			String words[] = tempEntry.getDataStr().toLowerCase().split("\\s+");
			for (int i = 0; i < words.length; i++) {
				TrieHashNode tempNode = this.root.get(new TrieStrHash(words[i], null, null));
				if (tempNode != null) {
					this.remove2(tempNode, words[i], id);
				}
			}
			return true;
		}
		return false;
	}

	private boolean remove2(TrieHashNode tempNode, String str, String id) {

		if (tempNode instanceof TrieStrHash) {
			TrieStrHash tempStrNode = (TrieStrHash) tempNode;
			Set<Entry> entries = tempStrNode.entries;

			if (tempStrNode.child != null) {
				String value = tempStrNode.getVal();

				int index = compress(value, str);

				str = str.substring(index);
				TrieHashNode tempHashNode = null;
				if (str.length() > 0) {
					tempHashNode = tempStrNode.child.root.get(new TrieStrHash(str, null, null));
				}

				if (tempHashNode != null) {
					if (tempHashNode.isLeaf()){
						tempStrNode.child.root.remove(tempHashNode);
						if (tempStrNode.child.root.size() == 0) {
							tempStrNode.leaf = true;
						}
					}
					this.remove2(tempHashNode, str, id);
				}
			}

			entries.remove(id);

		} 

		return false;
	}

	private void pullNodesUp(TrieHashNode parent, TrieHashNode child) {

		if (parent instanceof TrieStrHash && child instanceof TrieStrHash) {
			TrieStrHash strParent = (TrieStrHash) parent;
			TrieStrHash strChild = (TrieStrHash) child;
			int indexOfWord = this.allEntries.search(strChild.getFirstEntry().getId());
			Entry tempEntry = (Entry)this.allEntries.getVal(indexOfWord);
			String words[] = tempEntry.getDataStr().toLowerCase().split("\\s+");
			ArrayList<TrieStrHash> childArray = new ArrayList<TrieStrHash>();
			for (int i = 0; i < words.length; i++) {
				//childStrNode = (TrieStrHash) this.root.get(new TrieStrHash(words[i], null, null));
				int indexOfLetter = words[i].indexOf(strChild.val);
				if (indexOfLetter != -1 && indexOfLetter != words[i].length()-1) {
					TrieStrHash childStrNode = (TrieStrHash)strChild.child.root.get(new TrieStrHash(words[i].substring(indexOfLetter+1),null,null));
					if (childStrNode != null) {
						childArray.add(childStrNode);
					}
				}
			}

			for (int i = 0; i < childArray.size(); i++) {
				strParent.child.root.put(childArray.get(i));
			}
		}

	}

	private boolean remove(TrieHashNode tempNode, String str, String id) {

		if (tempNode instanceof TrieStrHash) {
			TrieStrHash tempStrNode = (TrieStrHash) tempNode;
			Set<Entry> entries = tempStrNode.entries;

			if (entries.size() > 1) {
				if (tempStrNode.child != null && str.length() > 1) {
					String value = tempStrNode.getVal();
					str = str.substring(str.indexOf(value.charAt(value.length()-1)));

					TrieHashNode tempHashNode = tempStrNode.child.root.get(new TrieStrHash(str, null, null));
					if (tempHashNode != null) {
						if (tempHashNode.entries.size() == 1) {
							tempStrNode.child.root.remove(tempHashNode);
						}
						this.remove(tempHashNode, str, id);
					}
				}
			}
			entries.remove(id);

		} else {
			TrieCharHash tempCharNode = (TrieCharHash) tempNode;

			Set<Entry> entries = tempCharNode.entries;

			if (entries.size() > 1) {
				if (str.length() > 1) {
					str = str.substring(1);
				}

				if (tempCharNode.child != null) {
					TrieHashNode tempHashNode = tempCharNode.child.root.get(new TrieStrHash(str, null, null));
					if (tempHashNode != null) {
						if (tempHashNode.entries.size() == 1) {
							tempCharNode.child.root.remove(tempHashNode);
						}
						this.remove(tempHashNode, str, id);
					}
				}
			}
			entries.remove(id);

		}

		return false;
	}

	/**
	 * 
	 * 
	 * @return true if compression happened; otherwise, false
	 */
	private int compress(String first, String second) {
		int i = 0, j = 0;
		boolean foundDiff = false;
		String firstLetter = null, secondLetter = null;
		while (!foundDiff) {
			if (i < first.length()) {
				firstLetter = first.charAt(i) + "";
			}

			if (j < second.length()) {
				secondLetter = second.charAt(j) + "";
			}

			if (firstLetter == null || secondLetter == null){
				foundDiff = true;
				if (i > j) {
					i = j;
				}
			} else if (!firstLetter.equals(secondLetter)) {
				foundDiff = true;
			} else {
				i++;
				j++;
				firstLetter = null;
				secondLetter = null;
			}
		}
		return i;
	}

	//Breadth first traversal of trie
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
		for (int i = 0; i < 4; i++) {
			Entry e = new Entry("e" + Integer.toString(i), 'b', 10, words[i]);
			System.out.printf("ENTRY #%d\n", i);
			trie.insert(e);
		}

		System.out.printf("\n%s\n", trie);
		trie.breadthFirstTraversal(trie);

		trie.remove2("e3"); //TODO - needs to be changed so just uses ID

		System.out.printf("After remove...\n%s\n", trie);
		trie.breadthFirstTraversal(trie);




	}
}