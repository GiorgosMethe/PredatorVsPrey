package myPack;

import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;

import javax.xml.ws.Action;

public abstract class Policy {
	protected Vector<Double> functionV = new Vector<Double>();
	
	// sets functionV with appropriate values
	public abstract void generateV();
	
	public abstract int stateIndex(Agent me, Vector<Agent> worldState); 
	
	public abstract Vector<SimpleEntry<Action, Double>> getActions(Agent me, Vector<Agent> worldState, Vector<Action> possibleActions);
	
	public Action getOptimalAction(Agent me, Vector<Agent> worldState, Vector<Action> possibleActions) {
		Vector<SimpleEntry<Action, Double>> functionQ = getActions(me, worldState, possibleActions);
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
