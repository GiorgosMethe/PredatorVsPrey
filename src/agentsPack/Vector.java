package agentsPack;

import java.util.Collection;

public class Vector<T> extends java.util.Vector<T> {

	/**
	 * This made Eclipse really happy. Yes, again.
	 */
	private static final long serialVersionUID = -5394473854835359847L;

	public Vector() {
		super();
	}
	
	public Vector(Collection<? extends T> c) {
		super(c);
	}
	
	public Vector(int initialCapacity) {
		super(initialCapacity);
	}
	
	public Vector(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (!(o instanceof Vector)) {
			return false;
		}
		Vector<Object> that = (Vector<Object>) o;
		if (that.size() != this.size()) {
			return false;
		}
		
		for (int i = 0; i < this.size(); i++) {
			if (!this.get(i).equals(that.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		for (T element : this) {
			hashCode = 31*hashCode + (element==null ? 0 : element.hashCode());
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		String result = "[";
		for (int i = 0; i < this.size(); result += this.get(i).toString(), i++, result += i < this.size() ? ", " : "");
		result += "]";
		return result;
	}
}
