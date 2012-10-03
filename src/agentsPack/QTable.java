package agentsPack;

import java.util.Map;
import agentsPack.Vector;

import environmentPack.Coordinate;

public class QTable extends
		DefaultHashMap<Vector<Agent>, Map<Coordinate, Double>> {

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

	public QTable(Map<Vector<Agent>, Map<Coordinate, Double>> copyTable,
			Double initialValue) {
		super(copyTable, new StateActionDefault(initialValue));
		// TODO Auto-generated constructor stub
	}

	public QTable(int initialCapacity, float loadFactor, Double initialValue) {
		super(initialCapacity, loadFactor, new StateActionDefault(initialValue));
		// TODO Auto-generated constructor stub
	}

	public Map<Coordinate, Double> get(Vector<Agent> worldState) {
		// if (worldState.size() <= 1 ||
		// Coordinate.compareCoordinates(worldState.get(0).position,
		// worldState.get(1).position)) {
		// return new DefaultHashMap<Coordinate, Double>(0.0);
		// }
		// if (!this.containsKey(worldState)) {
		// Map<Coordinate, Double> defaultMap = new HashMap<Coordinate,
		// Double>();
		// Agent predator = null;
		// for (Agent a : worldState) {
		// if (a instanceof Predator) {
		// predator = a;
		// }
		// }
		// for (Coordinate action : predator.possibleActions(worldState)) {
		// defaultMap.put(action, this.initialValue);
		// }
		// this.put(worldState, defaultMap);
		// }
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
		return this.getString(representation, new Coordinate(0,0));
	}
	
	public String getString(int representation, Coordinate preyPosition) {
		Vector<Agent> worldState = new Vector<Agent>(2);
		Coordinate predatorPosition = new Coordinate(0, 0);
		worldState.add(new Predator("", predatorPosition, null));
		worldState.add(new Prey("", preyPosition, null));
		String result = "";
		
		switch (representation) {
		case 21:
			for (int i = 0; i < 6; i++) {
				predatorPosition.setX(i);
				for (int j = 0; j <= i; j++) {
					predatorPosition.setY(j);
					result += predatorPosition + ": "; 
					for (Map.Entry<Coordinate, Double> actionValue : this.get(worldState).entrySet()) {
						result += "[" + actionValue.getKey() + ": " + actionValue.getValue() + "]  ";
					}
					result += "\n";
				}
			}
			break;
		case 11*11:
		case 6*6:
			int maxcoord = (int) Math.sqrt(representation);
			for (int i = 0; i < maxcoord; i++) {
				predatorPosition.setX(i);
				for (int j = 0; j < maxcoord; j++) {
					predatorPosition.setY(j);
					result += predatorPosition + ": ";
					for (Map.Entry<Coordinate, Double> actionValue : this.get(worldState).entrySet()) {
						result += "[" + actionValue.getKey() + ": " + actionValue.getValue() + "]  ";
					}
					result += "\n";
				}
			}
			break;
		default:
			for (Map.Entry<Vector<Agent>, Map<Coordinate, Double>> entry : this.entrySet()) {
				Agent p = null;
				for (Agent a : entry.getKey()) {
					if (a instanceof Prey) {
						p = a;
					}
				}
				result += p.position + ": ";
				//result += entry.getKey() + ": ";
				for (Map.Entry<Coordinate, Double> actionEntry : entry.getValue().entrySet()) { 
					result += "[" + actionEntry.getKey() + ": " + actionEntry.getValue() + "]  ";
				}
				result += "\n";
			}
		}
		return result + "\n\n" + this.size();
	}

	public String toString() {
		return this.getString(-1);
	}

}

class StateActionDefault implements
		DefaultHashMap.DefaultFactory<Vector<Agent>, Map<Coordinate, Double>> {
	public Double initialValue;

	public StateActionDefault(Double initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public Map<Coordinate, Double> getDefault(Vector<Agent> worldState) {
		if (worldState.size() <= 1
				|| Coordinate.compareCoordinates(worldState.get(0).position,
						worldState.get(1).position)) {
			return new DefaultHashMap<Coordinate, Double>(0.0);
		}
		Map<Coordinate, Double> defaultMap = new DefaultHashMap<Coordinate, Double>(
				this.initialValue);
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
