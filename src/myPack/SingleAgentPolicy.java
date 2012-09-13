package myPack;

import java.util.Map;
import java.util.Vector;

public abstract class SingleAgentPolicy extends Policy {

	protected Map<Integer, Double> reward = new DefaultHashMap<Integer, Double>(0.0);

	/**
	 * Corresponds with \pi(s, a) for multiple a.
	 * 
	 * @return A list of possible actions and the probability that \pi(s)
	 *         chooses those.
	 */
	@Override
	public Map<Coordinate, Double> getActions(Agent me, Vector<Agent> worldState, Vector<Coordinate> possibleActions) {
		double weightedValueSum = 0;
		double[] weightedValue = new double[5];
		for (int i = 0; i < possibleActions.size(); i++) {
			weightedValue[i] = 0;
			Map<Integer, Double> probabilities = me.functionP(worldState);
			for (Map.Entry<Integer, Double> pValue : probabilities.entrySet()) {
				weightedValue[i] += ((Double) pValue.getValue())
						* functionV.get((Integer) pValue.getKey());
			}
			weightedValueSum += weightedValue[i];
		}

		Map<Coordinate, Double> returnValue = new DefaultHashMap<Coordinate, Double>(0.0);

		for (int i = 0; i < possibleActions.size(); i++) {
			returnValue.put(possibleActions.get(i), weightedValue[i] / weightedValueSum);
		}
		return returnValue;
	}

}
