package agentsPack;

import java.io.IOException;
import java.util.Map;

import matPack.MatFileGenerator;
import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class SarsaPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> sarsaTable[][] = new Vector[6][6];
	private double alpha;
	private double gamma;

	public SarsaPredator(String name, Coordinate p, Policy pi, double alpha,
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

	public void initializeSarsaTable() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				this.sarsaTable[i][j] = new Vector<StateActionPair>();
				SarsaPredator sP = new SarsaPredator("", new Coordinate(i, j),
						null, alpha, gamma);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(sP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : sP.ProbabilityActionsRSW(worldState)) {
					id++;
					this.sarsaTable[i][j].add(new StateActionPair(
							new Coordinate(c.coordinate.getX(), c.coordinate
									.getY()), 15, id));

				}

			}
		}

	}

	public void PrintSarsaTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {
				System.out.println(new Coordinate(i, j));
				for (StateActionPair c : this.sarsaTable[i][j]) {
					System.out.print(c.Action + " " + c.Value + "  ");
				}
				System.out.println("\n");
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
		Double maxValue = Double.NEGATIVE_INFINITY;
		Vector<StateActionPair> tempMaxVector = new Vector<StateActionPair>();
		int CountActions = 0;
		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
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
			for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
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

	public void updateSarsaTable(Coordinate stateOld,
			StateActionPair actionOld, StateActionPair actionNew,
			Coordinate State, double reward, boolean absorb) {

		int oldActionPosId = -1;
		for (int i = 0; i < this.sarsaTable[stateOld.getX()][stateOld.getY()]
				.size(); i++) {
			if (this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(i).id == actionOld.id) {
				oldActionPosId = i;
				break;
			}
		}

		int newActionPosId = -1;
		for (int i = 0; i < this.sarsaTable[actionOld.Action.getX()][actionOld.Action
				.getY()].size(); i++) {
			if (this.sarsaTable[actionOld.Action.getX()][actionOld.Action
					.getY()].elementAt(i).id == actionNew.id) {
				newActionPosId = i;
				break;
			}
		}

		if (absorb) {

			this.sarsaTable[stateOld.getX()][stateOld.getY()]
					.elementAt(oldActionPosId).Value = this.sarsaTable[stateOld
					.getX()][stateOld.getY()].elementAt(oldActionPosId).Value
					+ alpha
					* (reward + (gamma * 0) - this.sarsaTable[stateOld.getX()][stateOld
							.getY()].elementAt(oldActionPosId).Value);

		} else {

			this.sarsaTable[stateOld.getX()][stateOld.getY()]
					.elementAt(oldActionPosId).Value = this.sarsaTable[stateOld
					.getX()][stateOld.getY()].elementAt(oldActionPosId).Value
					+ alpha
					* (reward
							+ (gamma * this.sarsaTable[State.getX()][State
									.getY()].elementAt(newActionPosId).Value) - this.sarsaTable[stateOld
								.getX()][stateOld.getY()]
							.elementAt(oldActionPosId).Value);

		}

	}

	public static void RunSarsa(int number, double a, double gamma,
			String policy, double policyParameter) {

		SarsaPredator sP = new SarsaPredator("SarsaPredator", new Coordinate(5,
				5), null, a, gamma);
		sP.initializeSarsaTable();
		int sumMoves = 0;

		double[] output = new double[number];

		for (int i = 0; i < number; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			sP.position.setX(5);
			sP.position.setY(5);

			worldState.add(sP);
			worldState.add(prey);

			int steps = 0;

			Coordinate oldPosition = new Coordinate(sP.position.getX(),
					sP.position.getY());

			StateActionPair action = null;
			if (policy.equalsIgnoreCase("e")) {
				// e-Greedy action selection
				action = sP.chooseEGreedyAction(policyParameter);
			} else if (policy.equalsIgnoreCase("s")) {
				// SoftMax action selection
				action = sP.chooseSoftMaxAction(policyParameter);
			}

			while (prey.lives) {

				if (Coordinate.compareCoordinates(sP.position, prey.position)) {

					output[i] = (double) steps;
					prey.kill();
					break;

				} else {

					oldPosition = new Coordinate(sP.position.getX(),
							sP.position.getY());

					sP.position.setX(action.Action.getX());
					sP.position.setY(action.Action.getY());

					Coordinate preyAction = prey.doAction(worldState);

					int x = preyAction.getX();
					if (x == 10)
						x = -1;
					int y = preyAction.getY();
					if (y == 10)
						y = -1;

					int NewPredPosX = sP.position.getX() - x;
					int NewPredPosY = sP.position.getY() - y;

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

					// s = s'
					sP.position.setX(NewPredPosXNor);
					sP.position.setY(NewPredPosYNor);

					// a'
					StateActionPair newAction = sP.chooseEGreedyAction(0.1);

					double reward = 0.0;
					boolean absorb = false;
					if (Coordinate.compareCoordinates(sP.position,
							prey.position)) {
						reward = 10.0;
						absorb = true;

					}

					sP.updateSarsaTable(oldPosition, action, newAction,
							sP.position, reward, absorb);

					// a = a'
					if (policy.equalsIgnoreCase("e")) {
						// e-Greedy action selection
						action = sP.chooseEGreedyAction(policyParameter);
					} else if (policy.equalsIgnoreCase("s")) {
						// SoftMax action selection
						action = sP.chooseSoftMaxAction(policyParameter);
					}
					steps++;
				}
			}

		}
		System.out.println("the average is: " + (sumMoves / 10000));

		sP.PrintSarsaTable();

		try {
			MatFileGenerator.write(
					output,
					"Sarsa" + "_" + String.valueOf(a) + "_"
							+ String.valueOf(gamma) + "_" + policy + "_"
							+ String.valueOf(policyParameter));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("An output .mat file has generated with the name: "
				+ "Sarsa" + "_" + String.valueOf(a) + "_"
				+ String.valueOf(gamma) + "_" + policy + "_"
				+ String.valueOf(policyParameter) + ".mat");

	}

}
