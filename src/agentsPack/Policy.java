package agentsPack;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import environmentPack.Coordinate;

public interface Policy {
	public Map<Coordinate, Double> getActions(Agent me, 
			Vector<Agent> worldState, 
			Vector<Coordinate> possibleActions, 
			Map<Entry<Vector<Agent>, Coordinate>, Double> qTable);
}
