import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class ByteArrayCharSequence implements CharSequence, Serializable {
	private static final long serialVersionUID = 042105;
	private static final String ENCODING = "US-ASCII";

	private final byte[] data;
	private final int offset;
	private final int end;

	public ByteArrayCharSequence(String str) {
		try {
			data = str.getBytes(ENCODING);
			offset = 0;
			end = data.length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected: US-ASCII not supported!");
		}
	}

	private ByteArrayCharSequence(byte[] data, int offset, int end) {
		this.data = data;
		this.offset = offset;
		this.end = end;
	}

	public ByteArrayCharSequence subSequence(int start, int end) {
		if (start < 0 || end >= (this.end-offset)) {
			throw new IllegalArgumentException("Illegal range " +
				start + "-" + end + " for sequence of length " + length());
		}
		return new ByteArrayCharSequence(data, start + offset, end + offset);
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

	@Override
	public int hashCode() {
		return data[0];
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ByteArrayCharSequence) {
			return true; //fix this
		}
		return false;

	}
}