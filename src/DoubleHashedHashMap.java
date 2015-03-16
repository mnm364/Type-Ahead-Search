//TODO - 'K' should be changed to 'T'
//TODO - make initial capacity definable in constructor (want to make it small for Trie)

import java.io.Serializable;

/**
 * This is a generic Double Hashed Hash Map. It
 * takes a parameter K.
 * 
 * @author Jeffrey Sham, Michael Miller
 *
 * @param <K>
 */
public class DoubleHashedHashMap<K> {
	
	/** The rehash multiplying factor.
	 */
	private static final int REHASH_MULTIPYING_FACTOR = 2;
	
	/** The default initial capacity of the hash map.
	 */
	private static final int DEFAULT_INITIAL_CAPACITY = 11;

	
	/** The default load factor of the hash map.
	 */
	private static final double DEFAULT_LOAD_FACTOR = .75;
	
	/** The size of the hash map.
	 */
	private int size;
	
	/** The load factor of the hash map.
	 */
	private double loadFactor;
	
	/** The initial capacity of the hash map.
	 */
	private int initialCapacity;
	
	/** The hash map.
	 */
	private K[] hashMap;
	
	/** The default constructor of the DoubleHashedHashMap.
	 */
	public DoubleHashedHashMap() {
		this.size = 0;
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		this.initialCapacity = DEFAULT_INITIAL_CAPACITY;
		this.makeHash(this.initialCapacity);
	}
	
	/**
	 * The constructor for the DoubleHashedHashMap that takes
	 * in the load factor and the capacity.
	 * @param loadFactor load factor of the hash map
	 * @param capacity initial capacity
	 */
	public DoubleHashedHashMap(double loadFactor, int capacity) {
		this.size = 0;
		
		if (loadFactor <= 0 || capacity < 0) {
            throw new IllegalArgumentException();
        }
		
		this.loadFactor = loadFactor;
		this.initialCapacity = capacity;
		this.makeHash(this.initialCapacity);
	}
	
	/**
	 * This method inputs a key into the hash map.
	 * 
	 * @param key the key to input
	 * @return true if the key was inputed, false if it already exists
	 */
	public boolean put(K key) {
		int indexOfItem = this.findIndex(key);
		if (indexOfItem == -1) {
			//Item not in hash map
			int hashValue = key.hashCode() % this.hashMap.length;
			
			if (hashValue < 0) {
				hashValue *= -1;
			}
			
			boolean inserted = false;
			
			int index = hashValue;
			
			while(!inserted) {
				K tempKey = this.hashMap[index];
				
				if (tempKey == null) {
					this.hashMap[index] = key;
					this.size++;
					inserted = true;
				} else {
					index = (index + secondHashFunction(tempKey)) % this.hashMap.length;
				}
			}
			
			double tempLoadFactor = (double) this.size / this.hashMap.length;
			System.out.println("Temp Load Factor: " + tempLoadFactor);
			if (tempLoadFactor >= this.loadFactor) {
				System.out.println("Going to rehash");
				this.rehash();
			}
			
			return true;
		} else {
			//Item already in hash map
			return false;
		}

	}
	
	/*
	 * Don't think we need to make a get method...
	 * yes we do -MNM :D
	 * its because we need to be able to return a TrieHashNode if the character already exists in 
	 * ^the hash. Meaning that there is already that char and we need to move to the next node. To
	 * ^move to the next node, we need to be able to move to the child of the TrieHashNode and,
	 * ^thereby we need to return the K key if it exists so as to get that child node.
	 */
	public K get(K key) {
		int index = this.findIndex(key);
		if (index != -1) {
			return hashMap[index];
		}
		return null;
	}
	
	/**
	 * This method removes an object from a key.
	 * @param key the object to remove
	 * @return true if removed, false otherwise
	 */
	public boolean remove(K key) {
		int index = this.findIndex(key);
		
		if (index != -1) {
			this.hashMap[index] = null;
            this.size--;
            return true;
		}
		return false;
		
	}
	
	/**
	 * This method determines if the hash map contains
	 * a specified key.
	 * @param key the specified key
	 * @return true if the key exists, false otherwise
	 */
	public boolean containsKey(K key) {
		int index = this.findIndex(key);
		System.out.println("Key: " + key + ". Index: " + index);
		return !(index == -1);
	}
	
	/**
	 * This method clears the hash map of all its contents
	 */
	public void clear() {
		this.makeHash(this.hashMap.length);
		this.size = 0;
	}
	
	/**
	 * This method returns the number of items in the hash map.
	 * @return number of items in hash map
	 */
	public int size(){
		return this.size;
	}
	
	/**
	 * This method finds the index of the key.
	 * @param key the specified key
	 * @return index of the key, -1 otherwise
	 */
	private int findIndex(K key) {
		int value = 0;
		
		if (key != null) {
			value = key.hashCode() % this.hashMap.length;
			
			if (value < 0) {
				value *= -1;
			}
		}
		
		int index = value;
		
		do {
			K tempKey = this.hashMap[index];
			
			if (tempKey == null) {
				return -1;
			} else if (key.equals(tempKey)) {
				return index;
			}
			
			index = (index + secondHashFunction(tempKey)) % this.hashMap.length;
			
		} while (index != value);
		return -1;
	}
	
	/**
	 * This is the second hash function for the class.
	 * @param key the key 
	 * @return new integer hash code
	 */
	private int secondHashFunction(K key) {
		int result = (key.hashCode() / 10) % (this.hashMap.length / 2);
		if (result < 0) {
			result *= -1;
		}
		return (result * 2) + 1;	
	}
	
	/**
	 * This method creates the hash map.
	 * @param capacity the capacity of the hash map
	 */
	@SuppressWarnings("unchecked")
	private void makeHash(int capacity) {
		this.hashMap = (K[]) new Object[capacity];
	}
	
	/**
	 * This method rehashes the hash map to 2 times its
	 * previous length and then adds the contents of the
	 * old hash map to the new hash map.
	 */
	private void rehash() {
		K[] tempHash = this.hashMap;
		this.makeHash(this.hashMap.length * REHASH_MULTIPYING_FACTOR);
		
		this.size = 0;
		
		for (int i = 0; i < tempHash.length; i++) {
			K tempKey = tempHash[i];
			
			if (tempKey == null) {
				continue;
			} else {
				this.put(tempKey);
			}
		}
	}
	
	/**
	 * Object to test the map with.
	 *
	 */
	public static class Node {
		Node next;
		int id;
		
		public Node() {
			next = null;
			id = 0;
		}
		
		public Node(Node next, int id) {
			this.next = next;
			this.id = id;
		}
		
		@Override
		public String toString() {
			if (next != null) {
				return "id :" +id + ". Next: " + next.id;
			}
			return "id :" +id;
		}
		
		@Override
		public int hashCode(){
			return id * 5 + 29 * 3 - 12;
		}
	}
	
	/**
	 * This test shows that the hash map's objects can change without
	 * explicitly changing them in the hash map.
	 * @param args
	 */
	public static void main(String[] args) {
		DoubleHashedHashMap<Node> map = new DoubleHashedHashMap<>();
		Node n1 = new Node();
		Node n2 = new Node(n1, 10);
		Node n3 = new Node();
		n1.next = n3;
		n1.id = 5;
		map.put(n1);
		map.put(n2);
		map.put(n3);
		
		//Pointer Test
		System.out.println(map.containsKey(n1));
		System.out.println(map.containsKey(n2));
		System.out.println(map.containsKey(n3));
		n1.next = null;
		System.out.println(map.containsKey(n1));
		System.out.println(map.containsKey(n2));
		System.out.println(map.containsKey(n3));
		
		Node[] array = new Node[50];
		//Testing rehashing of map
		for (int i = 0; i < 7; i++) {
			Node tempNode = new Node();
			tempNode.id = i + 1;
			array[i] = tempNode;
			if (i != 0) {
				tempNode.next = array[i - 1];
			}
			map.put(tempNode);
		}
		
		for (int i = 0; i < 7; i++) {
			map.containsKey(array[i]);
		}
		
		//testing remove method
		for (int i = 0; i < 7; i+=2) {
			map.remove(array[i]);
		}
		
		for (int i = 0; i < 7; i++) {
			map.containsKey(array[i]);
		}
		
	}
	
}
