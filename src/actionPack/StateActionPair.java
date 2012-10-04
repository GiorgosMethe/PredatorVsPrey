package actionPack;

import environmentPack.Coordinate;

public class StateActionPair {

	public Coordinate Action;
	public double Value;
	public int id;

	public StateActionPair(Coordinate action, double value, int id) {

		this.Action = action;
		this.Value = value;
		this.id = id;

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof StateActionPair)) {
			return false;
		}
		StateActionPair that = (StateActionPair) o;

		return (that == null)
				|| ((this.Action == null ? that.Action == null : that.Action
						.equals(this.Action)) || (this.Value == that.Value) || (this.id == that.id));
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.Action != null) {
			hashCode += 11 * this.Action.getX() + this.Action.getY();
		}
		hashCode = id * ((int) this.Value) ^ hashCode;
		return hashCode;
	}

	public String toString() {
		return "(a=" + this.Action + ", v=" + this.Value + ", #=" + this.id
				+ ")";
	}
}
