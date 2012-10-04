package actionPack;

import environmentPack.Coordinate;

public class SAPair {

	public Coordinate State;
	public StateActionPair Action;
	
	public SAPair(Coordinate state, StateActionPair action) {
	
		State = state;
		Action = action;
	}
	public String toString() {
		return "{s=" + this.State + ", a=" + this.Action + "}";
	}
}
