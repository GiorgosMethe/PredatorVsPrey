package myPack;

import java.util.Map;
import java.util.Vector;

public class SingleAgentRandom extends SingleAgentPolicy {

	@Override
	public void generateV() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented, sorry.");
	}

	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		return null;
	}

	@Override
	public Map<Coordinate, Double> getActions(Agent me, Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		return null;
	}
	
	@Override
	public Coordinate getOptimalAction(Agent me, Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		int random = (int) (Math.random() * 5);
		
		return possibleActions.get(random);
	}
}


