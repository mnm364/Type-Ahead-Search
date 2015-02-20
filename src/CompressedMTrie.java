/*
 * need to make a full write up on this
 * 
 * @author Michael Miller, Jeffrey Sham
 * 
 */
public class CompressedMTrie {
	
	private DoubleHashedHashMap<TrieHashNode> root;
	
	private final class TrieHashNode {
		
		/* Character to be used in Trie. */
		private char val;
		
		/* Boolean to mark leaf nodes. */
		private boolean leaf;
		
		/* ??identify if compressed string node 
		 * This does not make sense to be here but a good reminder to deal with compression
		 */
		private boolean str;
		
		/* Pointer? to subsequent hash map. */
		private DoubleHashedHashMap<TrieHashNode> child;
		
		//TODO - make this list of pointers to entries
		/* list of pointers to unordered master Set */
		private Set<Entry> eSet;
		
		private TrieHashNode(char val, boolean leaf, boolean str, DoubleHashedHashMap<TrieHashNode> child, Entry e) {
			this.val	= val;
			this.leaf	= leaf;
			this.str	= str;
			this.child	= child;

			this.eSet.add(e);
		}
		
	}
	
	/**
	 * Inserts a string into the trie,
	 * 
	 * @param str the string to insert
	 * @return true if insert successful; false otherwise
	 */
	public boolean insert(String str) {
		//TODO - method stub
		return false;
	}
	
	/**
	 * Removes string from trie, two different uses.
	 * 	1) If string is non-unique or a prefix to another string, just remove id from ptr list.
	 * 	2) If string is unique (i.e. no other ptrs in list), remove entire string from trie.
	 * ISSUES: need to search through Set of Entries to find one to delete (linear search is not efficient, others take up more space)
	 * -maybe need a skip list structure...
	 * 
	 * @param str the str to remove
	 * @param id the id associated with the str
	 * @return 
	 */
	public boolean remove(String str, float id) {
		//TODO - method stub
		return false;
	}
	
	/**
	 * 
	 * 
	 * @return true if compression happened; otherwise, false
	 */
	private boolean compress() {
		//TODO - method stub
		return false;
	}
}
