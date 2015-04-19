/**
 * This is a the entry data type that holds 
 * the value of the ADD commmand
 * 
 * @author Michael Miller, Jeffrey Sham
 */
public class Entry implements Comparable<Entry>{
	
	/** Unique ID for each entry */
	private String id;
	
	/** Entry type (user | topic | question | board) */
	private char type;
	
	/** Score given to the entry to weight results */
	private float score;
	
	private String dataStr;
	
	/** Constructor 
	 * @param id the unique id
	 * @param type (user | topic | question | board) 
	 * @param score used to weight results
	 */
	public Entry(String id, char type, float score, String dataStr) {
	//	initialize values
		this.id 	= id;
		this.type 	= type;
		this.score	= score;
		this.dataStr= dataStr;
	}
	
	/**
	 * ID getter method.
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
		//return "id:" + this.id + ";type:" + this.type + ";score:" + Float.toString(this.score) + ";dataStr:" + this.dataStr;
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
		return super.hashCode();
	}

	@Override
	public int compareTo(Entry obj) {
		if (this.getClass() == obj.getClass()) {
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
