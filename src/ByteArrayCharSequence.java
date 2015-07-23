import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class ByteArrayCharSequence implements CharSequence, Serializable {
	private static final long serialVersionUID = 042015;
	private static final String ENCODING = "US-ASCII";

	private final byte[] data;
	private final int offset;
	private final int end;

	/**
	 * Constructor to create ByteArrayCharSequence from String.
	 * @param str string to create ByteArrayCharSequence from
	 */
	public ByteArrayCharSequence(String str) {
		try {
			data = str.getBytes(ENCODING);
			offset = 0;
			end = data.length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected: US-ASCII not supported!");
		}
	}

	/**
	 * Constructor to create ByteArrayCharSequence from byte array.
	 * @param data the byte array
	 * @param offset start index in array to create sequence
	 * @param end end index in array to create sequence
	 */
	private ByteArrayCharSequence(byte[] data, int offset, int end) {
		this.data = data;
		this.offset = offset;
		this.end = end;
	}

	/**
	 * Create subsequence of byte array from given start to given end.
	 * @param start start index
	 * @param end end index
	 * @return the subsequence
	 */
	public ByteArrayCharSequence subSequence(int start, int end) {
		if (start < 0 || end > (this.end-offset)) {
			throw new IllegalArgumentException("Illegal range " +
				start + "-" + end + " for sequence of length " + length());
		}
		return new ByteArrayCharSequence(data, start + offset, end + offset);
	}

	/**
	 * Create subsequence of byte array from given start to end.
	 * @param start start index
	 * @return the subsequence
	 */
	public ByteArrayCharSequence subSequence(int start) {
		//return this.subSequence(start, this.end);
		if (start < 0 || start > (end - offset)) {
			throw new IllegalArgumentException("Illegal range " +
				end + "-" + (this.end - offset) + " for sequence of length " + length()); 
		}
		return new ByteArrayCharSequence(data, start + offset, this.end);
	}

	public ByteArrayCharSequence append(ByteArrayCharSequence bar) {
		byte[] n = new byte[this.length() + bar.length()];
		System.arraycopy(this.data, 0, n, 0, this.length());
		System.arraycopy(bar.getData(), 0, n, 0, bar.length() + this.length()); //TODO, why is the destination at index 0?
		return new ByteArrayCharSequence(n, this.offset, this.end + bar.length());
	}

	/**
	 * Get char at given index from byte array.
	 * @param index the index to get the char from
	 * @return the char at the indicated index
	 */
	public char charAt(int index) {
		int ix = index + offset;
		if (ix >= end) {
			throw new StringIndexOutOfBoundsException("Invalid index" + 
				index + " length " + length());
		}
		return (char) (data[ix] & 0xff);
	}

	/**
	 * Get length of the subsequence.
	 * @return the length
	 */
	public int length() {
		return end - offset;
	}

	public byte[] getData() {
		return this.data;
	}

	@Override
	public String toString() {
		try {
			return new String(data, offset, end - offset, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected: US-ASCII not supported");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ByteArrayCharSequence) { //TODO - should I use getClass() instead?
			ByteArrayCharSequence that = (ByteArrayCharSequence) o;
			if (this.length() == that.length()) {
				for (int i = 0; i < this.length(); i++) {
					if (this.charAt(i) != that.charAt(i)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	//not really needed...
	@Override
	public int hashCode() {
		if (this.length() == 0) {
			return 0;
		}
		return this.toString().hashCode();
	}
}