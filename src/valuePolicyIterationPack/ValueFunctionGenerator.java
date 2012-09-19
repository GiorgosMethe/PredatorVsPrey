package valuePolicyIterationPack;

import java.util.Map;

import agentsPack.Agent;

public interface ValueFunctionGenerator {
	public Map<? extends Number, Double> generateValueFunction(Agent agent,
			int maxStateIndex, double gamma, double theta);
}
