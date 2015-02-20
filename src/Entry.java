/*
 * This is a the entry data type that holds 
 * the value of the ADD commmand
 * 
 * @author Michael Miller, Jeffrey Sham
 */
public class Entry {
	
	/* Unique ID */
	private String id;
	
	/* Entry type (user | topic | question | board) */
	private char type;
	
	/* Score given to the entry to weight results */
	private float score;
	
	/* Constructor 
	 * @param id the unique id
	 * @param type (user | topic | question | board) 
	 * @param score used to weight results
	 */
	public Entry(String id, char type, float score) {
	//	initialize values
		this.id 	= id;
		this.type 	= type;
		this.score	= score;
	}
	
	public String getId() { 
		return this.id;
	}
	
	public char getType() {
		return this.type;
	}
	
	public float getScore() {
		return this.score;
	}
}
