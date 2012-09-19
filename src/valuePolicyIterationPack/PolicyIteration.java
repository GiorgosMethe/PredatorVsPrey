package valuePolicyIterationPack;

import java.util.Map;

import agentsPack.Agent;
import agentsPack.DefaultHashMap;
import environmentPack.Coordinate;

/*
 * Implementation of Policy Iteration, according to Sutton and Barto, p.98, using some dirty tricks to speed it all up.
 */
public class PolicyIteration implements ValueFunctionGenerator {
	public PolicyIteration() {
		// TODO Auto-generated constructor stub
	}

	public Map<? extends Number, Double> generateValueFunction(Agent agent,
			int maxStateIndex, double discountFactor, double maxError) {
		Map<Integer, Double> valueFunction = new DefaultHashMap<Integer, Double>(
				0.0);
		Map<Integer, Coordinate> policy = new DefaultHashMap<Integer, Coordinate>(
				new Coordinate(-1, 0));
		boolean policyStable;
		double error, updatedValue, optimalActionValue;
		int intermediateState;
		Coordinate optimalAction;
		Map<Integer, Double> stateTransitionProb;
		/*
		 * Initialization V(s) = 0, for all s pi(s) =
		 * agent.posibleActions(S0).getFirst(), for all s
		 */

		do {
			/*
			 * Policy Evaluation
			 */
			error = Double.NEGATIVE_INFINITY;
			do {
				for (int s = 0; s < maxStateIndex; s++) {
					updatedValue = 0;
					intermediateState = s; // where Predator did a move, but
											// Prey hasn't
					if (policy.containsKey(s)) {
						intermediateState -= policy.get(s).toIndex();
					}
					stateTransitionProb = agent.functionP(intermediateState);
					for (int sprime = 0; sprime < maxStateIndex; sprime++) {
						updatedValue += stateTransitionProb.get(sprime)
								* (agent.reward(s, sprime, policy.get(s)) + discountFactor
										* valueFunction.get(sprime));
					}
					error = Math.max(
							error,
							Math.abs(valueFunction.put(s, updatedValue)
									- valueFunction.get(s)));
				}
			} while (error < maxError);

			/*
			 * Policy Improvement
			 */
			policyStable = true;
			for (int s = 0; s < maxStateIndex; s++) {
				optimalAction = null;
				optimalActionValue = Double.NEGATIVE_INFINITY;
				updatedValue = 0.0;
				// select optimal action
				for (Coordinate action : agent.possibleActions(s)) {
					intermediateState = s; // where Predator did a move, but
											// Prey hasn't
					if (policy.containsKey(s)) {
						intermediateState -= policy.get(s).toIndex();
					}
					stateTransitionProb = agent.functionP(intermediateState);
					for (int sprime = 0; sprime < maxStateIndex; sprime++) {
						updatedValue += stateTransitionProb.get(sprime)
								* (agent.reward(s, sprime, action) + discountFactor
										* valueFunction.get(sprime));
					}
					if (updatedValue >= optimalActionValue) {
						optimalActionValue = updatedValue;
						optimalAction = action;
					}
				}
				if (!Coordinate.compareCoordinates(
						policy.put(s, optimalAction), policy.get(s))) {
					policyStable = false;
				}
			}
		} while (!policyStable);
		return valueFunction;
	}

}
