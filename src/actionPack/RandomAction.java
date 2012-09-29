package actionPack;

import environmentPack.Coordinate;

public class RandomAction {

	public double prob;
	public Coordinate coordinate;

	public RandomAction(double p, Coordinate c) {
		this.prob = p;
		this.coordinate = c;
	}
	
	public String toString() {
		return "p(" + this.coordinate + ") = " + this.prob;
	}
	public Coordinate getCoordinate(){
		
		return coordinate;
		
	}
}