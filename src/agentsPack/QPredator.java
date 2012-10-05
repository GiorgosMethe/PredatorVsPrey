package agentsPack;

import java.io.IOException;
import java.util.Map;

import matPack.MatFileGenerator;
import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class QPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> qTable[][] = (Vector<StateActionPair>[][]) new Vector[6][6];
	private double alpha;
	private double gamma;

	public QPredator(String name, Coordinate p, Policy pi, double alpha,
			double gamma) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
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

	public void initializeQTable() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				this.qTable[i][j] = new Vector<StateActionPair>();
				QPredator qP = new QPredator("", new Coordinate(i, j), null,
						alpha, gamma);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(qP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : qP.ProbabilityActionsRSW(worldState)) {
					id++;
					this.qTable[i][j].add(new StateActionPair(new Coordinate(
							c.coordinate.getX(), c.coordinate.getY()), 15, id));

				}

			}
		}

	}

	public void PrintQTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {
				System.out.println(new Coordinate(i, j));
				for (StateActionPair c : this.qTable[i][j]) {
					System.out.print(c.Action + " " + c.Value + "  ");
				}
				System.out.println("\n");
			}
		}
	}

	public StateActionPair chooseSoftMaxAction(double temperature) {

		double sum = 0;

		for (StateActionPair e : this.qTable[this.position.getX()][this.position
				.getY()]) {
			sum += Math.exp(e.Value / temperature);

		}

		double random = Math.random();
		double k = 0;

		for (StateActionPair e : this.qTable[this.position.getX()][this.position
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
		Double maxValue = Double.NEGATIVE_INFINITY;
		Vector<StateActionPair> tempMaxVector = new Vector<StateActionPair>();
		int CountActions = 0;
		for (StateActionPair e : this.qTable[this.position.getX()][this.position
				.getY()]) {
			if (e.Value >= maxValue) {
				if (e.Value == maxValue) {
					tempMaxVector.add(e);
				} else {
					tempMaxVector.removeAllElements();
					maxValue = e.Value;
					tempMaxVector.add(e);
				}
			}
			CountActions++;
		}

		double rand = Math.random();
		double step = 1 / tempMaxVector.size();
		double counter = step;
		int maxAct = 0;
		for (int ii = 0; ii < tempMaxVector.size(); ii++) {
			if (counter >= rand) {
				maxAct = ii;
				break;
			}
			counter += step;
		}

		if (r <= epsilon) {
			double step1 = epsilon / (CountActions - 1);
			double counter1 = epsilon - step1;
			for (StateActionPair e : this.qTable[this.position.getX()][this.position
					.getY()]) {
				if (!Coordinate.compareCoordinates(e.Action,
						tempMaxVector.elementAt(maxAct).Action)) {
					if (counter1 <= r) {
						return e;
					}
					counter1 -= step1;
				}
			}

		}
		return tempMaxVector.elementAt(maxAct);
	}

	public void updateQTable(Coordinate oldPosition, StateActionPair Action,
			double reward, boolean absorbing) {

		int actionPosId = -1;
		for (int i = 0; i < this.qTable[this.position.getX()][this.position
				.getY()].size(); i++) {
			if (this.qTable[this.position.getX()][this.position.getY()]
					.elementAt(i).id == Action.id) {
				actionPosId = i;
				break;
			}
		}

		if (!absorbing) {

			double actionMaxValue = Double.NEGATIVE_INFINITY;
			for (StateActionPair e : this.qTable[this.position.getX()][this.position
					.getY()]) {
				if (e.Value > actionMaxValue) {
					actionMaxValue = e.Value;
				}
			}

			this.qTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(actionPosId).Value = this.qTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(actionPosId).Value
					+ (alpha * (reward + (gamma * actionMaxValue) - this.qTable[oldPosition
							.getX()][oldPosition.getY()].elementAt(actionPosId).Value));

		} else {

			this.qTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(actionPosId).Value = this.qTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(actionPosId).Value
					+ alpha
					* (reward - this.qTable[oldPosition.getX()][oldPosition
							.getY()].elementAt(actionPosId).Value);

		}
	}

	public static void RunQLearning(int number, double a, double gamma,
			String policy, double policyParameter) {

		QPredator qP = new QPredator("qPredator", new Coordinate(5, 5), null,
				a, gamma);
		qP.initializeQTable();

		double[] output = new double[number];
		for (int i = 0; i < number; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			qP.position.setX(5);
			qP.position.setY(5);

			worldState.add(qP);
			worldState.add(prey);

			int steps = 0;
			boolean absorbingState = false;

			do {

				Double reward = 0.0;

				// save the old position, we need it later.
				Coordinate oldPosition = new Coordinate(qP.position.getX(),
						qP.position.getY());

				StateActionPair action = null;
				if (policy.equalsIgnoreCase("e")) {
					// e-Greedy action selection
					action = qP.chooseEGreedyAction(policyParameter);
				} else if (policy.equalsIgnoreCase("s")) {
					// SoftMax action selection
					action = qP.chooseSoftMaxAction(policyParameter);
				}

				// SoftMax action selection
				// StateActionPair action = qP.chooseSoftMaxAction(0.1);

				// update the predator's position
				qP.position.setX(action.Action.getX());
				qP.position.setY(action.Action.getY());

				if (Coordinate.compareCoordinates(qP.position, prey.position)) {

					output[i] = steps;
					reward = 10.0;
					prey.kill();
					absorbingState = true;

				} else {

					// Here, I am calculating the prey's possible actions
					Coordinate preyAction = prey.doAction(worldState);

					int x = preyAction.getX();
					if (x == 10)
						x = -1;
					int y = preyAction.getY();
					if (y == 10)
						y = -1;

					int NewPredPosX = qP.position.getX() - x;
					int NewPredPosY = qP.position.getY() - y;

					// some checks not to excede the limits of the
					// space
					if (NewPredPosX == 6)
						NewPredPosX = 5;
					if (NewPredPosX == -1)
						NewPredPosX = 1;
					if (NewPredPosY == 6)
						NewPredPosY = 5;
					if (NewPredPosY == -1)
						NewPredPosY = 1;

					int NewPredPosXNor = NewPredPosX;
					int NewPredPosYNor = NewPredPosY;

					if (NewPredPosY > NewPredPosX) {

						NewPredPosXNor = NewPredPosY;
						NewPredPosYNor = NewPredPosX;

					}

					qP.position.setX(NewPredPosXNor);
					qP.position.setY(NewPredPosYNor);

				}

				// new value for this state according to the update function.
				// remember, worldState is still the old one (before the Agents
				// move)
				qP.updateQTable(oldPosition, action, reward, absorbingState);

				steps++;

			} while (prey.lives);

		}

		qP.PrintQTable();

		try {
			MatFileGenerator.write(
					output,
					"Q-Learning" + "_" + String.valueOf(a) + "_"
							+ String.valueOf(gamma) + "_" + policy + "_"
							+ String.valueOf(policyParameter));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("An output .mat file has generated with the name: "
				+ "Q-Learning" + "_" + String.valueOf(a) + "_"
				+ String.valueOf(gamma) + "_" + policy + "_"
				+ String.valueOf(policyParameter) + ".mat");

	}

}
