
public class Boost implements Comparable<Boost>{
	private char type;
	private int boostValue;
	
	public Boost(char type, int boost) {
		this.type = type;
		this.boostValue = boost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + boostValue;
		result = prime * result + type;
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
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Boost obj) {
		if (getClass() == obj.getClass()) {
			Boost other = (Boost) obj;
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

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getBoostValue() {
		return boostValue;
	}

	public void setBoostValue(int boostValue) {
		this.boostValue = boostValue;
	}
	
	
}
