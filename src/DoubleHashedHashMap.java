//TODO - make sure that hash size is always prime

import java.util.Iterator;
import java.lang.Iterable;
/**
 * This is a generic Double Hashed Hash Map. It
 * takes a parameter K.
 * 
 * @author Jeffrey Sham, Michael Miller
 *
 * @param <K>
 */
public class DoubleHashedHashMap<K> implements Iterable<K> {
	
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
	 * in the capacity.
	 * @param capacity the initial capacity
	 */
	public DoubleHashedHashMap(int capacity) {
		this.size = 0;
		
		if (capacity < 1) { //TODO - should this be < 1?
            throw new IllegalArgumentException();
        }
		
		this.loadFactor = DEFAULT_LOAD_FACTOR;
		this.initialCapacity = capacity;
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
			//System.out.println("Temp Load Factor: " + tempLoadFactor);
			if (tempLoadFactor >= this.loadFactor) {
				//System.out.println("Going to rehash");
				this.rehash();
			}
			
			return true;
		} else {
			//Item already in hash map
			return false;
		}

	}
	
	/**
	 * This method returns the key if it exists in
	 * the hash map, null otherwise.
	 * @param key The key to search for.
	 * @return The key, null otherwise.
	 */
	public K get(K key) {
		int index = this.findIndex(key);
		if (index != -1) {
			return this.hashMap[index];
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
	 *
	 */
	public boolean isEmpty() {
		return this.size == 0;
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

	//for TESTING
	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < initialCapacity; i++) {
			if (this.hashMap[i] != null) {
				str += this.hashMap[i].toString() + ",";
			}
		}
		str += "]";
		return str;
	}

	/**
	 * Iterator Class.
	 * issue: couldnt get anonymous class working...?
	 */
	public Iterator<K> iterator() {
		return new HashIterator();
	}

	private class HashIterator implements Iterator<K> {
		//return new Iterator<A> {
		private int index = 0;
		public boolean hasNext() {
			while (hashMap[index] == null) {
				index++;
			}
			return (index <= size);
		}
		public K next() {
			return hashMap[index++];
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
