package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

import javax.xml.ws.Action;

public abstract class SingleAgentPolicy extends Policy {

	public SingleAgentPolicy(Agent me) {
		super(me);
	}
	
	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		Agent predator = null;
		Agent prey = null;
		
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			}
			else if (a instanceof Prey) {
				prey = a;
			}
		}
		Coordinate c = Coordinate.difference(predator.position, prey.position);
		return (c.getX() * 11) + c.getY();
	}

	/**
	 * Computes the state transition probability of the Predator, given the Prey's current position. 
	 * @param worldState
	 * @return A table of state indices, and probabilities.
	 */
	protected Vector<SimpleEntry<Integer, Double>> functionP(Vector<Agent> worldState) {
		Vector<SimpleEntry<Integer, Double>> p = new Vector<SimpleEntry<Integer, Double>>();
		Vector<Vector<Agent>> possibleWorlds = new Vector<Vector<Agent>>();
		
		Agent prey = null;
		Agent predator = null;
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			}
			else if (a instanceof Prey) {
				prey = a;
			}
		}
		
		// Prey does not move.
		p.add(new SimpleEntry<Integer, Double>(this.stateIndex(worldState), 0.8));
		
		// Can prey move south?
		if (prey.safePosition(prey.position.getSouth(), worldState)) {
			prey.position = prey.position.getSouth();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move west?
		if (prey.safePosition(prey.position.getWest(), worldState)) {
			prey.position = prey.position.getWest();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move north?
		if (prey.safePosition(prey.position.getNorth(), worldState)) {
			prey.position = prey.position.getNorth();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move east?
		if (prey.safePosition(prey.position.getEast(), worldState)) {
			prey.position = prey.position.getEast();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}

		double prob = 0.2 / possibleWorlds.size();
		
		for (Vector<Agent> possibleWorld : possibleWorlds) {
			p.add(new SimpleEntry<Integer, Double>(this.stateIndex(possibleWorld), prob));
		}
		
		return p;
	}
	
	

	/**
	 * Corresponds with \pi(s, a) for multiple a.
	 * 
	 * @return A list of possible actions and the probability that \pi(s) chooses those.
	 */
	@Override
	public Vector<SimpleEntry<Action, Double>> getActions(Vector<Agent> worldState, Vector<Action> possibleActions) {
		double weightedValueSum = 0;
		double[] weightedValue = new double[5];
		for (int i = 0; i < possibleActions.size(); i++) {
			weightedValue[i] = 0;
			Vector<SimpleEntry<Integer, Double>> probabilities = this.functionP(worldState);
			for (SimpleEntry<Integer, Double> pValue : probabilities) {
				weightedValue[i] += ((Double) pValue.getValue()) * functionV.get((Integer) pValue.getKey());
			}
			weightedValueSum += weightedValue[i];
		}
		
		Vector<SimpleEntry<Action, Double>> returnValue = new Vector<SimpleEntry<Action, Double>>();
		
		for (int i = 0; i < possibleActions.size(); i++) {
			returnValue.add(new SimpleEntry<Action, Double>(possibleActions.get(i), weightedValue[i] / weightedValueSum));
		}
		return returnValue;
	}

}
