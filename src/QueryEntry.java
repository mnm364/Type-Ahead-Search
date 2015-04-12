
public class QueryEntry {
	private String id;
	private int frequency;
	
	public QueryEntry(String id, int frequency) {
		this.id = id;
		this.frequency = frequency;
	}
	
	public QueryEntry(String id) {
		this.id = id;
		this.frequency = 0;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o instanceof QueryEntry) {
			QueryEntry temp = (QueryEntry) o;
			return this.id.equals(temp.id);
		} else if (o instanceof String) {
			String data = (String) o;
			return this.id.equals(data);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return this.id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
