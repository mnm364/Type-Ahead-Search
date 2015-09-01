//TODO use ByteArrayCharSequence throughtout this DS

/**
 * This is a the entry data type that holds 
 * the value of the ADD commmand
 * 
 * @author Michael Miller, Jeffrey Sham
 */
public class Entry implements Comparable<Entry>{
	
	/** Unique ID for each entry. */
	private String id;
	// TODO ^^ can make this a charArray???
	
	/** Entry type (user | topic | question | board). */
	private char type;
	
	/** Score given to the entry to weight results. */
	private float score;

	/** Index of entry to be used in event of score tie. */
	private int index;
	
	/** Data string associated with entry. */
	private String dataStr;
	
	/** 
	 * Constructor for Entry object
	 * @param id the unique id
	 * @param type (user | topic | question | board) 
	 * @param score used to weight results
	 * @param index index of entry to be used in even of score tie
	 * @param dataStr string to be associated with entry
	 */
	public Entry(String id, char type, float score, int index, String dataStr) {
		//	initialize values
		this.id 	= id;
		this.type 	= type; //TODO error detection (u,t,q,b)
		this.score	= score;
		this.index	= index;
		this.dataStr= dataStr; //TODO make ByteArrayCharSequence
	}
	public Entry(String id, char type, float score, String dataStr) {
		this(id, type, score, 0, dataStr);
	}
	public Entry(String id) {
		this(id, 'a', 0, 0, null);
	}

	/**
	 * id getter method.
	 * @return the id
	 */
	public String getId() { 
		return this.id;
	}

	/**
	 * Type getter method.
	 * @return the type
	 */
	public char getType() {
		return this.type;
	}
	
	/**
	 * Score getter method.
	 * @return the score
	 */
	public float getScore() {
		return this.score;
	}
	
	/** 
	 * Index getter method.
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Data string getter method.
	 * @return the data string
	 */
	public String getDataStr() {
		return dataStr;
	}
	
	public void setScore(float score) {
		this.score *= score;
	}

	@Override
	public String toString() {
		return this.id; //for testing
		// return "id:" + this.id + ";type:" + this.type + ";score:" + Float.toString(this.score) + ";dataStr:" + this.dataStr;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Entry) {
			Entry theObject = (Entry) o;
			return this.id.equals(theObject.id);
		} else if (o instanceof String) {
			String theObject = (String) o;
			return this.id.equals(theObject);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode(); //TODO changed this, make sure still works well :)
	}

	@Override
	public int compareTo(Entry obj) {
		if (this.getClass() == obj.getClass()) { //TODO why not instanceof?
			Entry other = (Entry) obj; //TODO - warning here
			if (other.score > this.score) {
				return 1;
			} else if (other.score < this.score) {
				return -1;
			} else {
				return 0;
			}
		}
		return 0;
	}
}
