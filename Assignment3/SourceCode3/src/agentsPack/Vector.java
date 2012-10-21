package agentsPack;

import java.util.Collection;
import java.util.Collections;

import environmentPack.Coordinate;

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

	public int stateIndex(int observerIndex) {
		if (!(this.get(0) instanceof Agent)) {
			throw new RuntimeException(
					"This method only works on vectors of agents. Sorry for the bad place.");
		}
		int preyIndex = -1;
		Vector<Coordinate> reordered = new Vector<Coordinate>(this.size());
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i) instanceof Prey) {
				preyIndex = i;
				break;
			}
		}
		for (int i = 0; i < this.size(); i++) {
			if ((i == preyIndex) || (i == observerIndex)) {
				continue;
			}
			reordered.add(((Agent) this.get(i)).position);
		}
		Collections.sort(reordered, Collections.reverseOrder());
		if (observerIndex != preyIndex) {
			reordered.add(((Agent) this.get(observerIndex)).position);
		}
		reordered.add(((Agent) this.get(preyIndex)).position);
		// Collections.reverse(reordered);
		// we actually should do this (this is how we at first describe this
		// state space), but that requires more runtime...
		return reordered.hashCode();
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
			hashCode = 31 * hashCode
					+ (element == null ? 0 : element.hashCode());
		}
		return hashCode;
	}

	@Override
	public String toString() {
		String result = "[";
		for (int i = 0; i < this.size(); result += this.get(i).toString(), i++, result += i < this
				.size() ? ", " : "")
			;
		result += "]";
		return result;
	}
}
