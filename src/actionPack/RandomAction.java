package actionPack;

import environmentPack.Coordinate;

public class RandomAction {

	public double prob;
	public Coordinate coordinate;

	public RandomAction(double p, Coordinate c) {
		this.prob = p;
		this.coordinate = c;
	}
}