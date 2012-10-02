package agentsPack;

import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;
import environmentPack.SARSdata;

public class MCOnPredator extends LearningPredator {

	protected Map<Pair<Vector<Agent>, Coordinate>, Vector<Double>> returns;

	public MCOnPredator(String name, Coordinate p, Policy pi, Double initialQValue) {
		super(name, p, pi, initialQValue);
		this.returns = new DefaultHashMap<Pair<Vector<Agent>, Coordinate>, Vector<Double>>(new Vector<Double>());
	}

	public MCOnPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
	}

	@Override
	public boolean onPolicyLearning() {
		return true;
	}
	
	@Override
	public void updateQTable(SARSdata sars) {
		this.returns.get(sars.getStateAction()).add(sars.reward);
		double avg = 0;
		for (Double prevReward : this.returns.get(sars.getStateAction())) {
			avg += prevReward;
		}
		avg /= this.returns.get(sars.getStateAction()).size();
		this.qTable.put(sars.getStateAction(), avg);
	}

}
