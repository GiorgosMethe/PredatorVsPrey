package agentsPack;

import agentsPack.Vector;

import environmentPack.Coordinate;

public abstract class Policy {

	public abstract Coordinate chooseAction(Vector<Agent> worldState,
			QTable qTable);

	public Double actionProbability(Vector<Agent> worldState,
			Coordinate action, QTable qTable) {
		Double denominator = 0.0;
		for (Double value : qTable.get(worldState).values()) {
			denominator += value;
		}
		return qTable.get(worldState, action) / denominator;
	}

}
