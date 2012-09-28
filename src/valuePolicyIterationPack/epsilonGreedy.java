package valuePolicyIterationPack;

import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import agentsPack.Agent;
import agentsPack.DefaultHashMap;
import environmentPack.Coordinate;

public class epsilonGreedy {
	
public double epsilon;


public epsilonGreedy (Double epsilon) {
	this.epsilon=epsilon;
}
public Coordinate chooseAction( Vector<Agent> worldState, Map<Vector<Agent>,Map<Coordinate, Double>> qTable){
	
	Double r = Math.random();
	Coordinate maxAction = null;
	Double maxValue = Double.NEGATIVE_INFINITY;
	for(Entry<Coordinate, Double> e : qTable.get(worldState).entrySet()){
		if(e.getValue() > maxValue) {
			maxValue = e.getValue();
			maxAction = e.getKey();
			}
	}
	
	if (r <= epsilon) {
		Map<Coordinate, Double> actionValueMap = new DefaultHashMap<Coordinate, Double>(qTable.get(worldState), 0.0);
		actionValueMap.remove(maxAction);
		Double step = (1 - epsilon) / actionValueMap.size();
		Double counter = step;
		for (Coordinate a : actionValueMap.keySet()) {
			if (counter <= r) {
				return a;
			}
			counter += step;
		}
	}
	return maxAction;
	
}

}
