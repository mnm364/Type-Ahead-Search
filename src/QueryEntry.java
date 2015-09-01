//What does this do?

public class QueryEntry {
	private Entry entry;
	private int frequency;
	
	public QueryEntry(Entry entry, int frequency) {
		this.entry = entry;
		this.frequency = frequency;
	}
	
	public QueryEntry(Entry entry) {
		this.entry = entry;
		this.frequency = 0;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o instanceof QueryEntry) {
			QueryEntry temp = (QueryEntry) o;
			return this.entry.equals(temp.entry);
		} else if (o instanceof String) {
			String data = (String) o;
			return this.entry.equals(data);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return entry.getId().hashCode();
	}
	
	@Override
	public String toString() {
		return "QueryEntry [entry=" + entry + ", frequency=" + frequency + "]";
	}

	public Entry getEntry() {
		return this.entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}
