
public class Boost implements Comparable<Boost>{
	private String type;
	private int boostValue;
	
	public Boost(String type, int boost) {
		this.type = type;
		this.boostValue = boost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boostValue;
		result = prime * result + type.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boost other = (Boost) obj;
		if (boostValue != other.boostValue)
			return false;
		if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public int compareTo(Boost obj) {
		if (getClass() == obj.getClass()) {
			Boost other = (Boost) obj; //TODO - warning here
			if (other.boostValue > this.boostValue) {
				return 1;
			} else if (other.boostValue < this.boostValue) {
				return -1;
			} else {
				return 0;
			}
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return "Boost [type=" + type + ", boostValue=" + boostValue + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBoostValue() {
		return boostValue;
	}

	public void setBoostValue(int boostValue) {
		this.boostValue = boostValue;
	}
	
	
}
