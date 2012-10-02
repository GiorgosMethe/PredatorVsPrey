package agentsPack;

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
		super(new DefaultHashMap<Coordinate, Double>(5, initialValue));
		// TODO Auto-generated constructor stub
	}
	
	public QTable(Map<Coordinate, Double> initialValues) {
		super(initialValues);
	}

	public QTable(int initialCapacity, Double initialValue) {
		super(initialCapacity, new DefaultHashMap<Coordinate, Double>(5, initialValue));
		// TODO Auto-generated constructor stub
	}

	public QTable(Map<Vector<Agent>, Map<Coordinate, Double>> copyTable, Double initialValue) {
		super(copyTable, new DefaultHashMap<Coordinate, Double>(5, initialValue));
		// TODO Auto-generated constructor stub
	}

	public QTable(int initialCapacity, float loadFactor, Double initialValue) {
		super(initialCapacity, loadFactor, new DefaultHashMap<Coordinate, Double>(5, initialValue));
		// TODO Auto-generated constructor stub
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
