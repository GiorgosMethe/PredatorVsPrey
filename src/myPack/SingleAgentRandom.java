package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

public class SingleAgentRandom extends SingleAgentPolicy {

	@Override
	public void generateV() {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		return null;
	}

	@Override
	public Vector<SimpleEntry<Coordinate, Double>> getActions(Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		return null;
	}
	
	@Override
	public Coordinate getOptimalAction(Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		int random = (int) (Math.random() * 5);
		
		return possibleActions.get(random);
	}
}


