package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

public abstract class Policy {
	protected Vector<Double> functionV = new Vector<Double>();

	// sets functionV with appropriate values
	public abstract void generateV();

	public abstract Integer stateIndex(Vector<Agent> worldState);

	public abstract Vector<SimpleEntry<Coordinate, Double>> getActions(
			Vector<Agent> worldState, Vector<Coordinate> possibleActions);

	public Coordinate getOptimalAction(Vector<Agent> worldState,
			Vector<Coordinate> possibleActions) {
		Vector<SimpleEntry<Coordinate, Double>> functionQ = getActions(worldState,
				possibleActions);
		Double maxValue = null;
		Coordinate maxAction = null;
		for (SimpleEntry<Coordinate, Double> actionValue : functionQ) {
			if (maxValue == null || actionValue.getValue() >= maxValue) {
				maxValue = actionValue.getValue();
				maxAction = actionValue.getKey();
			}
		}
		return maxAction;
	}
}
