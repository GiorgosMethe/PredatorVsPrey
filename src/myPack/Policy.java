package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

import javax.xml.ws.Action;

public abstract class Policy {
	protected Vector<Double> functionV = new Vector<Double>();
	protected Agent me;

	public Policy(Agent me) {
		this.me = me;
	}

	// sets functionV with appropriate values
	public abstract void generateV();

	public abstract Integer stateIndex(Vector<Agent> worldState);

	public abstract Vector<SimpleEntry<Action, Double>> getActions(
			Vector<Agent> worldState, Vector<Action> possibleActions);

	public Action getOptimalAction(Vector<Agent> worldState,
			Vector<Action> possibleActions) {
		Vector<SimpleEntry<Action, Double>> functionQ = getActions(worldState,
				possibleActions);
		Double maxValue = Double.MIN_VALUE;
		Action maxAction = null;
		for (SimpleEntry<Action, Double> actionValue : functionQ) {
			if (actionValue.getValue() >= maxValue) {
				maxValue = actionValue.getValue();
				maxAction = actionValue.getKey();
			}
		}
		return maxAction;
	}
}
