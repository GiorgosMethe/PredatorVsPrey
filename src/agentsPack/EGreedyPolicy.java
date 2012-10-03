package agentsPack;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import environmentPack.Coordinate;

public class EGreedyPolicy extends Policy {

	public double epsilon;

	public EGreedyPolicy(Double epsilon) {
		this.epsilon = epsilon;
	}

	@Override
	public Coordinate chooseAction(Vector<Agent> worldState, QTable qTable) {
		Double r = Math.random();

		Double maxValue = Double.NEGATIVE_INFINITY;
		Coordinate maxAction = null;

		Map<Double, Vector<Coordinate>> valueActionMap = new DefaultHashMap<Double, Vector<Coordinate>>(
				new Vector<Coordinate>());
		Vector<Coordinate> actions;

		for (Entry<Coordinate, Double> e : qTable.get(worldState).entrySet()) {
			actions = valueActionMap.get(e.getValue());
			actions.add(e.getKey());
			valueActionMap.put(e.getValue(), actions);
		}

		for (Double value : valueActionMap.keySet()) {
			if (value > maxValue) {
				maxValue = value;
			}
		}

		maxAction = valueActionMap.get(maxValue).get((int) (Math.random() * 5));

		if (r <= this.epsilon) {
			Map<Coordinate, Double> actionValueMap = new DefaultHashMap<Coordinate, Double>(
					qTable.get(worldState), 0.0);
			actionValueMap.remove(maxAction);
			Double step = (1 - this.epsilon) / actionValueMap.size();
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

	public double getEpsilon() {
		return this.epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
}