package agentsPack;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;

import agentsPack.Agent;
import agentsPack.DefaultHashMap;

public class QTable extends DefaultHashMap<Vector<Agent>, Map<Coordinate, Double>> {
	
	/**
	 * This made Eclipse very happy.
	 */
	private static final long serialVersionUID = 8796256036751217618L;

	public QTable(Double initialValue) {
		super(new StateActionDefault(initialValue));
	}
	
	public QTable(Map<Coordinate, Double> initialValues) {
		super(initialValues);
	}

	public QTable(int initialCapacity, Double initialValue) {
		super(initialCapacity, new StateActionDefault(initialValue));
		// TODO Auto-generated constructor stub
	}

	public QTable(Map<Vector<Agent>, Map<Coordinate, Double>> copyTable, Double initialValue) {
		super(copyTable, new StateActionDefault(initialValue));
		// TODO Auto-generated constructor stub
	}

	public QTable(int initialCapacity, float loadFactor, Double initialValue) {
		super(initialCapacity, loadFactor, new StateActionDefault(initialValue));
		// TODO Auto-generated constructor stub
	}
	
	public Map<Coordinate, Double> get(Vector<Agent> worldState) {
//		if (worldState.size() <= 1 ||
//				Coordinate.compareCoordinates(worldState.get(0).position, worldState.get(1).position)) {
//			return new DefaultHashMap<Coordinate, Double>(0.0);
//		}
//		if (!this.containsKey(worldState)) {
//			Map<Coordinate, Double> defaultMap = new HashMap<Coordinate, Double>();
//			Agent predator = null;
//			for (Agent a : worldState) {
//				if (a instanceof Predator) {
//					predator = a;
//				}
//			}
//			for (Coordinate action : predator.possibleActions(worldState)) {
//				defaultMap.put(action, this.initialValue);
//			}
//			this.put(worldState, defaultMap);
//		}
		return super.get(worldState);
	}
	
	public Double get(Vector<Agent> worldState, Coordinate action) {
		return this.get(worldState).get(action);
	}
	
	public Double get(Pair<Vector<Agent>, Coordinate> stateAction) {
		return this.get(stateAction.getFirst(), stateAction.getSecond());
	}
	
	public Double put(Vector<Agent> worldState, Coordinate action, Double value) {
		Double old = this.get(worldState, action);
		this.get(worldState).put(action, value);
		return old;
	}
	
	public Double put(Pair<Vector<Agent>, Coordinate> stateAction, Double value) {
		return this.put(stateAction.getFirst(), stateAction.getSecond(), value);
	}
	
	public String getString(int representation) {
		if (representation == 11*11) {
			return "";
		}
		else if (representation == 6*6) {
			return "";
		}
		else if (representation == 21) {
			return "";
		}
		return "Unknown representation.";
	}
	
	public String toString() {
		return this.getString(11*11);
	}

}

class StateActionDefault implements DefaultHashMap.DefaultFactory<Vector<Agent>, Map<Coordinate, Double>> {
	public Double initialValue;
	
	public StateActionDefault(Double initialValue) {
		this.initialValue = initialValue;
	}
	@Override
	public Map<Coordinate, Double> getDefault(Vector<Agent> worldState) {
		if (worldState.size() <= 1 || Coordinate.compareCoordinates(worldState.get(0).position, worldState.get(1).position)) {
			return new DefaultHashMap<Coordinate, Double>(0.0);
		}
		Map<Coordinate, Double> defaultMap = new DefaultHashMap<Coordinate, Double>(this.initialValue);
		Agent predator = null;
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			}
		}
		for (Coordinate action : predator.possibleActions(worldState)) {
			defaultMap.put(action, this.initialValue);
		}
		return defaultMap;
	}	
}