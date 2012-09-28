package agentsPack;

import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;



public interface Policy {

	
public abstract  Coordinate chooseAction(Vector<Agent> worldState, Map<Vector<Agent>,Map<Coordinate, Double>> qTable);
	
	
	
}
