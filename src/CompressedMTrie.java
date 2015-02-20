/*
 * need to make a full write up on this
 * 
 * @author Michael Miller, Jeffrey Sham
 * 
 */
public class CompressedMTrie {
	
	private DoubleHashedHashMap<TrieNode> root;
	
	private final class TrieNode {
		
		/* character to be used in Trie. */
		private char val;
		
		/* mark leaf nodes. */
		private boolean leaf;
		
		/* ??identify if compressed string node 
		 * This does not make sense to be here but a good reminder to deal with compression
		 */
		private boolean str;
		
		/* pointer? to subsequent hash map */
		private DoubleHashedHashMap<TrieNode> child;
		
		//TODO - make this point to an Entry
		/* list of pointers to unordered master Set */
		private Set<Entry> eSet;
		
		private TrieNode(char val, boolean leaf, boolean str, DoubleHashedHashMap<TrieNode> child, Entry e) {
			this.val	= val;
			this.leaf	= leaf;
			this.str	= str;
			this.child	= child;
			
			this.eSet.add(e);
		}
		
	}
	
	/**
	 * 
	 * @param str the string to insert
	 * @return true if insert successful; false otherwise
	 */
	public boolean insert(String str) {
		//TODO - method stub
		return false;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public boolean remove(String str, float id) {
		//TODO - method stub
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean compress() {
		//TODO - method stub
		return false;
	}
}
