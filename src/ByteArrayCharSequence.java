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

	public ByteArrayCharSequence subSequence(int start, int end) {
		if (start < 0 || end > (this.end-offset)) {
			throw new IllegalArgumentException("Illegal range " +
				start + "-" + end + " for sequence of length " + length());
		}
		return new ByteArrayCharSequence(data, start + offset, end + offset);
	}

	public ByteArrayCharSequence subSequence(int start) {
		if (start < 0 || start > (this.end - offset)) {
			throw new IllegalArgumentException("Illegal range " +
				end + "-" + (this.end - offset) + " for sequence of length " + length()); 
		}
		return new ByteArrayCharSequence(data, start + offset, this.end + offset);
	}

	public char charAt(int index) {
		int ix = index + offset;
		if (ix >= end) {
			throw new StringIndexOutOfBoundsException("Invalid index" + 
				index + "length " + length());
		}
		return (char) (data[ix] & 0xff);
	}

	public int length() {
		return end - offset;
	}

	@Override
	public String toString() {
		try {
			return new String(data, offset, end - offset, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected: US-ASCII not supported");
		}
	}

	//not really needed...
	@Override
	public int hashCode() {
		if (this.length() == 0) {
			return 0;
		}
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ByteArrayCharSequence) { //should I use getClass() instead?
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
}