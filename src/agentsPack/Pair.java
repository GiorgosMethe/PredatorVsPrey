package agentsPack;

public class Pair<T1, T2> {

	protected final T1 first;
	protected final T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair<T1, T2> other = (Pair<T1, T2>) o;
		return (this.getFirst() == null ?
				other.getFirst()==null : this.getFirst().equals(other.getFirst()))  &&
				(this.getSecond()==null ?
						other.getSecond()==null : this.getSecond().equals(other.getSecond()));
	}

	public int hashCode() {
	     return (this.getFirst() == null ? 0 : this.getFirst().hashCode()) ^
	    		 (this.getSecond() == null ? 0 : this.getSecond().hashCode());

	}
}
