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
}
