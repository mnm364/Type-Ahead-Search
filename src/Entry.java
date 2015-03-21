/**
 * This is a the entry data type that holds 
 * the value of the ADD commmand
 * 
 * @author Michael Miller, Jeffrey Sham
 */
public class Entry {
	
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

	@Override
	public String toString() {
		return this.id; //for testing
		//return "id:" + this.id + ";type:" + this.type + ";score:" + Float.toString(this.score) + ";dataStr:" + this.dataStr;
	}
}
