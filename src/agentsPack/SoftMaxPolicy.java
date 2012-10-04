package agentsPack;

import java.util.Map;

import environmentPack.Coordinate;

public class SoftMaxPolicy extends Policy {

	public Double temperature;

	public SoftMaxPolicy(Double temperature) {
		this.temperature = temperature;
	}

	@Override
	public Coordinate chooseAction(Vector<Agent> worldState, QTable qTable) {
		double sum = 0;

		for (Double actionValue : qTable.get(worldState).values()) {
			sum += Math.exp(actionValue / this.temperature);
		}

		double random = Math.random();
		double k = 0;

		for (Map.Entry<Coordinate, Double> actionValue : qTable.get(worldState)
				.entrySet()) {
			k += Math.exp(actionValue.getValue() / temperature) / sum;

			if (random <= k) {
				return actionValue.getKey();
			}
		}
		return null;
	}
}
