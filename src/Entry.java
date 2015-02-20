/*
 * 
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
}
