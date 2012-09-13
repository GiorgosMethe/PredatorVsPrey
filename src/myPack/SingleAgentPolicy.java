package myPack;

import java.util.Map;
import java.util.Vector;

public abstract class SingleAgentPolicy extends Policy {

	protected Map<Integer, Double> reward = new DefaultHashMap<Integer, Double>(0.0);
	
	public SingleAgentPolicy() {
		this.reward.put(0, 0.8 * 10);
		for (int i = 1; i < 11*11; i++) {
			this.reward.put(i, 0.0);
		}
	}
	
	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		Agent predator = null;
		Agent prey = null;

		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			} else if (a instanceof Prey) {
				prey = a;
			}
		}
		Coordinate c = Coordinate.difference(predator.position, prey.position);
		return (c.getX() * 11) + c.getY();
	}

	/**
	 * Computes the estimated immediate reward of the Predator.
	 * 
	 * @param worldState
	 * @return
	 */
	protected Map<Integer, Double> reward(Vector<Agent> worldState) {
		return this.reward;
	}
	
	/**
	 * Computes the state transition probability of the Predator, given the
	 * Prey's current position.
	 * 
	 * @param worldState
	 * @return A table of state indices, and probabilities.
	 */
	protected Map<Integer, Double> functionP(
			Vector<Agent> worldState) {
		Map<Integer, Double> p = new DefaultHashMap<Integer, Double>(0.0);
		Vector<Vector<Agent>> possibleWorlds = new Vector<Vector<Agent>>();

		Agent prey = null;
		Agent predator = null;
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			} else if (a instanceof Prey) {
				prey = a;
			}
		}

		// Prey does not move.
		p.put(this.stateIndex(worldState), 0.8);

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
			p.put(this.stateIndex(possibleWorld), prob);
		}

		return p;
	}

	/**
	 * Corresponds with \pi(s, a) for multiple a.
	 * 
	 * @return A list of possible actions and the probability that \pi(s)
	 *         chooses those.
	 */
	@Override
	public Map<Coordinate, Double> getActions(Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		double weightedValueSum = 0;
		double[] weightedValue = new double[5];
		for (int i = 0; i < possibleActions.size(); i++) {
			weightedValue[i] = 0;
			Map<Integer, Double> probabilities = this.functionP(worldState);
			for (Map.Entry<Integer, Double> pValue : probabilities.entrySet()) {
				weightedValue[i] += ((Double) pValue.getValue())
						* functionV.get((Integer) pValue.getKey());
			}
			weightedValueSum += weightedValue[i];
		}

		Map<Coordinate, Double> returnValue = new DefaultHashMap<Coordinate, Double>(0.0);

		for (int i = 0; i < possibleActions.size(); i++) {
			returnValue.put(possibleActions.get(i), weightedValue[i] / weightedValueSum);
		}
		return returnValue;
	}

}
