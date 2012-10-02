package agentsPack;

import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;
import environmentPack.Coordinate;
import environmentPack.SARSdata;
import agentsPack.QTable;

public abstract class LearningPredator extends Predator {

	protected QTable qTable;
	protected Vector<Vector<SARSdata>> episodes;

	public LearningPredator(String name, Coordinate p, Policy pi, double initialQValue) {
		super(name, p, pi);
		this.qTable = new QTable(initialQValue);
		this.episodes = new Vector<Vector<SARSdata>>();
		this.episodes.add(new Vector<SARSdata>());
	}
	
	public LearningPredator(String name, Coordinate p, Policy pi) {
		this(name, p, pi, 0.0);
		// TODO Auto-generated constructor stub
	}

	public abstract boolean onPolicyLearning();
	public abstract void updateQTable(SARSdata sars);
	public abstract void updateQTable(Vector<SARSdata> episode);
	
	public void observe(SARSdata sars) {
		if (this.onPolicyLearning()) {
			this.updateQTable(sars);
		}
		else {
			this.episodes.lastElement().add(sars);
			if (sars.isAbsorbingState()) {
				this.updateQTable(this.episodes.lastElement());
				this.episodes.add(new Vector<SARSdata>());
			}
		}
	}	
	
	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		// Choose an action according to this.pi, and learn on-line
		return this.pi.chooseAction(worldState, qTable);
	}

	public void PrintQTable() {
		System.out.println(this.qTable.getString(6*6));
	}

	// Deactivate planning predator's methods
	@Override
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState,
			Coordinate action) {
		throw new UnsupportedOperationException(
				"Sorry, you don't know the reward function. Receive one from the environment.");
	}

	@Override
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, you should use ProbabilityActionsRSW(worldState).");
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

}
