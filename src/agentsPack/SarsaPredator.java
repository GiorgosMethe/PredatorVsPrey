package agentsPack;

import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class SarsaPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> sarsaTable[][] = new Vector[6][6];
	private double alpha = 0.1;
	private double gamma = 0.7;

	public SarsaPredator(String name, Coordinate p, Policy pi) {
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
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

	public void initializeSarsaTable() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				this.sarsaTable[i][j] = new Vector<StateActionPair>();
				SarsaPredator sP = new SarsaPredator("", new Coordinate(i, j),
						null);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(sP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : sP.ProbabilityActionsRSW(worldState)) {
					id++;
					this.sarsaTable[i][j].add(new StateActionPair(
							new Coordinate(c.coordinate.getX(), c.coordinate
									.getY()), 0, id));

				}

			}
		}

	}

	public void printSarsaTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (j <= i) {
					System.out.println(new Coordinate(i, j));
					for (StateActionPair c : this.sarsaTable[i][j]) {

						System.out.println(c.Action + " " + c.Value);

					}
				}
			}
		}
	}

	public StateActionPair chooseSoftMaxAction(double temperature) {

		double sum = 0;

		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {
			sum += Math.exp(e.Value / temperature);

		}

		double random = Math.random();
		double k = 0;

		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {

			k += Math.exp(e.Value / temperature) / sum;

			if (random <= k) {
				return e;
			}
		}
		return null;
	}

	public StateActionPair chooseEGreedyAction(double epsilon) {

		Double r = Math.random();
		StateActionPair maxAction = null;
		Double maxValue = Double.NEGATIVE_INFINITY;

		int CountActions = 0;
		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {
			if (e.Value > maxValue) {
				maxValue = e.Value;
				maxAction = e;
			}
			CountActions++;
		}

		if (r <= epsilon) {
			Double step = epsilon / (CountActions - 1);
			Double counter = step;
			for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
					.getY()]) {
				if (!Coordinate.compareCoordinates(e.Action, maxAction.Action)) {
					if (counter <= r) {
						return e;
					}
					counter -= step;
				}
			}
		}
		return maxAction;

	}

	public void updateSarsaTable(Coordinate oldPosition,
			StateActionPair oldAction, StateActionPair action, double reward,
			boolean absorbing) {

		int oldActionPosId = -1;
		for (int i = 0; i < this.sarsaTable[oldPosition.getX()][oldPosition
				.getY()].size(); i++) {
			if (this.sarsaTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(i).id == oldAction.id) {
				oldActionPosId = i;
				break;
			}
		}

		int actionPosId = -1;
		for (int j = 0; j < this.sarsaTable[oldAction.Action.getX()][oldAction.Action
				.getY()].size(); j++) {
			if (this.sarsaTable[oldAction.Action.getX()][oldAction.Action
					.getY()].elementAt(j).id == action.id) {
				actionPosId = j;
				break;
			}
		}

		if (!absorbing) {

			this.sarsaTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(oldActionPosId).Value = this.sarsaTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(oldActionPosId).Value
					+ alpha
					* (reward
							+ gamma
							* this.sarsaTable[action.Action.getX()][action.Action
									.getY()].elementAt(actionPosId).Value - this.sarsaTable[oldPosition
								.getX()][oldPosition.getY()]
							.elementAt(oldActionPosId).Value);

		} else {

			this.sarsaTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(oldActionPosId).Value = this.sarsaTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(oldActionPosId).Value
					+ alpha
					* (reward - this.sarsaTable[oldPosition.getX()][oldPosition
							.getY()].elementAt(oldActionPosId).Value);

		}
	}

}
