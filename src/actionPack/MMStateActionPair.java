package actionPack;

import environmentPack.Coordinate;

public class MMStateActionPair {

	public Coordinate myAction;
	public Coordinate otherAction;
	public double Value;
	public int id;

	public MMStateActionPair(Coordinate myAction, Coordinate otherAction,
			double value, int id) {
		this.myAction = myAction;
		this.otherAction = otherAction;
		this.Value = value;
		this.id = id;
	}
}
