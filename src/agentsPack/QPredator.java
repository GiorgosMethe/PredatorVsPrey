package agentsPack;

import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;
import environmentPack.Coordinate;

public class QPredator extends Predator {

	private double qTable[][] = new double[6][6];
	private double alpha = 0.1;
	private double gamma = 0.7;

	public QPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		// Choose an action according to this.pi, and learn on-line
		return null;
	}

	public void receiveReward(Double r) {
		// pass it to QLearning
	}

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
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

	public void PrintQTable() {
		for (int i = 0; i < 6; i++) {
			System.out.println();
			for (int j = 0; j < 6; j++) {
				System.out.print(this.qTable[i][j] + " ");
			}
		}
		System.out.println();
	}

	public void initializeQTable() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (j <= i) {

					this.position.setX(i);
					this.position.setY(j);

					Vector<Agent> worldState = new Vector<Agent>();
					worldState.add(this);
					worldState.add(prey);

					for (RandomAction c : this
							.ProbabilityActionsRSW(worldState)) {
						this.qTable[c.coordinate.getX()][c.coordinate.getY()] = 15;
					}

				}
			}
		}

		this.qTable[0][0] = 10.0;

	}

	public void updateQTable(Coordinate oldPosition, Coordinate NewPosition,
			double reward) {

		this.qTable[oldPosition.getX()][oldPosition.getY()] = this.qTable[oldPosition
				.getX()][oldPosition.getY()]
				+ (alpha * (reward
						+ gamma
						* (this.qTable[this.position.getX()][this.position
								.getY()]) - this.qTable[oldPosition.getX()][oldPosition
						.getY()]));

	}

	public Coordinate chooseEGreedyAction(Agent agent,
			Vector<Agent> worldState, double epsilon) {

		Double r = Math.random();
		Coordinate maxAction = null;
		Double maxValue = Double.NEGATIVE_INFINITY;

		for (RandomAction e : agent.ProbabilityActionsRSW(worldState)) {
			if (qTable[e.coordinate.getX()][e.coordinate.getY()] > maxValue) {
				maxValue = qTable[e.coordinate.getX()][e.coordinate.getY()];
				maxAction = e.coordinate;
			}
		}

		if (r <= epsilon) {
			Double step = epsilon
					/ (agent.ProbabilityActionsRSW(worldState).size() - 1);
			Double counter = step;
			for (RandomAction e : agent.ProbabilityActionsRSW(worldState)) {
				if (!Coordinate.compareCoordinates(e.coordinate, maxAction)) {
					if (counter <= r) {
						return e.coordinate;
					}
					counter -= step;
				}
			}
		}
		return maxAction;

	}

}
