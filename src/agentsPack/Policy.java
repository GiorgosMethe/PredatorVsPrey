package agentsPack;

import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;

public abstract class Policy {
	protected Vector<Double> functionV = new Vector<Double>();

	// sets functionV with appropriate values
	public abstract void generateV();

	public abstract Integer stateIndex(Vector<Agent> worldState);

	public abstract Map<Coordinate, Double> getActions(Agent me,
			Vector<Agent> worldState, Vector<Coordinate> possibleActions);

	public Coordinate getOptimalAction(Agent me, Vector<Agent> worldState,
			Vector<Coordinate> possibleActions) {
		Map<Coordinate, Double> functionQ = getActions(me, worldState,
				possibleActions);
		Double maxValue = null;
		Coordinate maxAction = null;
		for (Map.Entry<Coordinate, Double> actionValue : functionQ.entrySet()) {
			if (maxValue == null || actionValue.getValue() >= maxValue) {
				maxValue = actionValue.getValue();
				maxAction = actionValue.getKey();
			}
		}
		return maxAction;
	}
}
