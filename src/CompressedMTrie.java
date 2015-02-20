/*
 * need to make a full write up on this
 * 
 * @author Michael Miller, Jeffrey Sham
 * 
 */
public class CompressedMTrie {
	
	private DoubleHashedHashMap<TrieNode> root;
	
	private final class TrieNode {
		private char val;
		private boolean end;
		private boolean str;
		private DoubleHashedHashMap<TrieNode> child;
		//TODO - make this point to an Entry
		private Set<Entry> eSet;
		
		private TrieNode(char val, boolean end, boolean str, DoubleHashedHashMap<TrieNode> child, Entry e) {
			this.val	= val;
			this.end	= end;
			this.str	= str;
			this.child	= child;
			
			this.eSet.add(e);
		}
		
	}
}
