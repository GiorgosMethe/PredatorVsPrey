package agentsPack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;
import environmentPack.SARSdata;

public class MCOffPredator extends LearningPredator {

	protected Map<Pair<Vector<Agent>, Coordinate>, Double> numerator;
	protected Map<Pair<Vector<Agent>, Coordinate>, Double> denominator;

	public MCOffPredator(String name, Coordinate p, Policy pi,
			double initialQValue) {
		super(name, p, new ArbitraryPolicy(), initialQValue);
		this.numerator = new DefaultHashMap<Pair<Vector<Agent>, Coordinate>, Double>(
				0.0);
		this.denominator = new DefaultHashMap<Pair<Vector<Agent>, Coordinate>, Double>(
				0.0);
	}

	public MCOffPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
	}

	public MCOffPredator(String name, Coordinate p) {
		super(name, p, new ArbitraryPolicy());
	}

	@Override
	public boolean onPolicyLearning() {
		return false;
	}

	@Override
	public void updateQTable(Vector<SARSdata> episode) {
		int tau = 0;
		for (SARSdata sars : episode) {
			if (sars.action.equals(this.pi.chooseAction(sars.currentState,
					this.qTable))) {
				break;
			}
			tau++;
		}
		List<SARSdata> sublist = episode.subList(tau, episode.size() - 1);
		Collections.reverse(sublist);
		Double w = 0.0;
		Pair<Vector<Agent>, Coordinate> s_a;
		for (SARSdata sars : sublist) {
			w *= 1 / this.pi.actionProbability(sars.currentState, sars.action,
					qTable);
			s_a = sars.getStateAction();
			this.numerator.put(s_a, this.numerator.get(s_a) + w * sars.reward);
			this.denominator.put(s_a, this.denominator.get(s_a) + w);
			this.qTable.put(s_a,
					this.numerator.get(s_a) / this.denominator.get(s_a));
		}
	}
}
