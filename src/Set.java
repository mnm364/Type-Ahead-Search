//TODO - should be called a Bag if duplicates are allowed!

public class Set<T> {

	private T[] theSet;
	private int size; //# of items in Set
	private static final int START_SIZE = 5; 

	@SuppressWarnings("unchecked")
	public Set() {
		theSet = (T[]) new Object[START_SIZE]; //declare array of type <T>, causes warning
		size = 0;
	}

	/** Find out how many elements are in the set.
	   @return the number
	*/
	public final int size() {
		return this.size;
	}

	/** See if the set is empty.
	   @return true if empty, false otherwise
	*/
	public final boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/** Add an item to the set, don't care about and DO check for duplicates.
	 * @param o the item to add
	 * @return true if added, false otherwise (was duplicate)
	*/
	@SuppressWarnings("unchecked")
	public final boolean add(final Object o) {
		if (!this.contains(o)) {
			//doubles size of array if full
			if (size == this.theSet.length) {
				//create temp array to hold value of theSet[]
				T[] temp = (T[]) new Object[this.theSet.length * 2];
				for (int i = 0; i < size; i++) {
					temp[i] = this.theSet[i];
				}
				this.theSet = temp;
			}
			//cast "o" and add to end of array
			theSet[size++] = (T) o;
			return true;
		} else {
			return false;
		}
	}

	/** Remove an item from the set if it's there.
	 * @param o the item to remove
	 * @return true if removed, false if not found
	*/
	public final boolean remove(final Object o) {
		if (this.isEmpty()) {
			return false;
		}
		int index = this.search(o);
		if (index == -1) { //not found
			return false;
		}
		//move last to spot to overwrite and decrement size
		this.theSet[index] = this.theSet[--this.size];
        return true;
	}

	/** Search for an item in the set.
	   @param o the item to search for
	   @return true if found, false otherwise
	*/
	public final boolean contains(final Object o) {
		return (this.search(o) != -1);
	}

	/** Create the union of two sets, no duplicates.
	 * @param that the other set to union with this
	 * @return a new set which contains all the elements in this and that
	*/
	public final Set<T> union(final Set<T> that) {
		Set<T> union = new Set <T>();
		Set<T> intersect = this.intersect(that);
		Set<T> cross = that;

		//copy "this" array into union to start
		for (int i = 0; i < this.size; i++) {
			union.add(this.theSet[i]);
		}
		//copy all values that are not the same into "union"
		for (int i = 0; i < cross.size(); i++) {
			if (!intersect.contains(cross.theSet[i])) {
				union.add(cross.theSet[i]);
			}
		}

		return union;
	}

	/** Create the intersection of two sets, which is every item that
	 * appears in both sets.  For example, if this is {2, 3, 1, 4} and
	 * that is {3, 10, 2} then the intersection is {2, 3}.
	 * @param that the other set to union with this
	 * @return a new set which is (this intersect that)
	*/
	public final Set<T> intersect(final Set<T> that) {
		//store values of intersection
		Set<T> intersect = new Set <T>();
		//cast "that" to MyJHUSet<T>
		Set<T> cross = that;

		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < cross.size(); j++) {
				//compare value of cross and this
				if (this.theSet[i].equals(cross.theSet[j])) {
					//make sure "intersect" doesn't already have value
					if (!intersect.contains(this.theSet[i])) {
						intersect.add(this.theSet[i]);
					}
				}
			}
		}

		return intersect;
	}

	/**
	 * HELPER METHOD that searches for "Object o".
	 * @param o
	 * @return index of object being searched for
	 */
	private int search(final Object o) {
        for (int i = 0; i < size; i++) {
            if (this.theSet[i].equals(o)) {
                return i;
            }
        }
        return -1; //nothing found
	}

	@Override
	public String toString() {
		if (this.size == 0) {
			return "{}";
		} else {
			String str = "{";
	    	for (int i = 0; i < this.size - 1; i++) {
	    		str = str + this.theSet[i] + ", ";
	    	}
	    	str = str + this.theSet[this.size - 1] + "}";
	    	return str;
		}
	}

	/**
	 * can access the array from outside the private field variables.
	 * @param i is the index of value to get
	 * @return Object of desire
	 */
	public final Object getVal(final int i) {
		Object o = null;
		if (i <= this.size) {
			o = this.theSet[i];
		}
		return o;
	}
}
