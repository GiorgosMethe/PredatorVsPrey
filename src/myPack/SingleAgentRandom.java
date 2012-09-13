package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

public class SingleAgentRandom extends SingleAgentPolicy {

	public SingleAgentRandom(Agent me) {

		super(me);

	}

	@Override
	public void generateV() {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		return null;
	}

	protected Vector<SimpleEntry<Integer, Double>> functionP(
			Vector<Agent> worldState) {

		Vector<SimpleEntry<Integer, Double>> p = new Vector<SimpleEntry<Integer, Double>>();
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
			p.add(new SimpleEntry<Integer, Double>(this
					.stateIndex(possibleWorld), prob));
		}

		return p;
	}

	@Override
	public Vector<SimpleEntry<Coordinate, Double>> getActions(Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		final Vector<SimpleEntry<Coordinate, Double>> returnValue = new Vector<SimpleEntry<Coordinate, Double>>();
		for (Coordinate a : possibleActions) {
			returnValue.add(new SimpleEntry<Coordinate, Double>(a, new Double(1/possibleActions.size())));
		}
		return returnValue;
	}
}


