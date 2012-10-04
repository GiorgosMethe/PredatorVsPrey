package actionPack;

import environmentPack.Coordinate;

public class SAPair {

	public Coordinate State;
	public StateActionPair Action;
	
	public SAPair(Coordinate state, StateActionPair action) {
	
		State = state;
		Action = action;
	}

}
