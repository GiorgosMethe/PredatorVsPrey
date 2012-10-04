package agentsPack;

import java.io.IOException;
import java.util.Map;
import agentsPack.Vector;

import matPack.MatFileGenerator;
import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class MCOnPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> qTable[][] = (Vector<StateActionPair>[][]) new Vector[6][6];

	@SuppressWarnings("unchecked")
	protected Vector<Double> returns[][][] = (Vector<Double>[][][]) new Vector[6][6][5];

	public MCOnPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < 5; k++) {
					returns[i][j][k] = new Vector<Double>();
				}
			}
		}
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
				MCOnPredator qP = new MCOnPredator("", new Coordinate(i, j), null);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(qP);
				worldState.add(prey);
				int id = 0;
				double reward = 15;

				if (i == 0 && j == 0) {
					reward = 0;
				}
				for (RandomAction c : qP.ProbabilityActionsRSW(worldState)) {
					id++;
					this.qTable[i][j].add(new StateActionPair(new Coordinate(
							c.coordinate.getX(), c.coordinate.getY()), reward, id));

				}

			}
		}

	}

	public void PrintQTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				System.out.println(new Coordinate(i, j));
				for (StateActionPair c : this.qTable[i][j]) {

					System.out.println(c.Action + " " + c.Value);

				}
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
		StateActionPair maxAction = null;
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

	public void updateQTable(Coordinate oldPosition, StateActionPair action, double reward, boolean absorbing) {

		int actionPosId = -1;
		for (int i = 0; i < this.qTable[this.position.getX()][this.position
				.getY()].size(); i++) {
			if (this.qTable[this.position.getX()][this.position.getY()]
					.elementAt(i).id == action.id) {
				actionPosId = i;
				break;
			}
		}

		if (!absorbing) {

			double actionMaxValue = Double.NEGATIVE_INFINITY;
			for (StateActionPair e : this.qTable[this.position.getX()][this.position.getY()]) {
				if (e.Value > actionMaxValue) {
					actionMaxValue = e.Value;
				}
			}
			this.returns[oldPosition.getX()][oldPosition.getY()][actionPosId].add(reward);
			
			double avgReturn = 0;
			for (Double returnedValue : this.returns[oldPosition.getX()][oldPosition.getY()][actionPosId]) {
				avgReturn += returnedValue;
			}
			avgReturn = avgReturn /= this.returns[oldPosition.getX()][oldPosition.getY()][actionPosId].size();

			this.qTable[oldPosition.getX()][oldPosition.getY()].elementAt(actionPosId).Value 
				= avgReturn;

		} else {

			this.qTable[oldPosition.getX()][oldPosition.getY()].elementAt(actionPosId).Value 
				= 0;
		}
	}

	public static void RunMonteCarlo(int number, String policy, double policyParameter) {

		MCOnPredator mcP = new MCOnPredator("qPredator", new Coordinate(5, 5), null);
		mcP.initializeQTable();

		double[] output = new double[number];
		for (int i = 0; i < number; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			mcP.position.setX(5);
			mcP.position.setY(5);

			worldState.add(mcP);
			worldState.add(prey);

			int steps = 0;
			boolean absorbingState = false;

			do {

				Double reward = 0.0;

				// save the old position, we need it later.
				Coordinate oldPosition = new Coordinate(mcP.position.getX(),
						mcP.position.getY());

				StateActionPair action = null;
				if (policy.equalsIgnoreCase("e")) {
					// e-Greedy action selection
					action = mcP.chooseEGreedyAction(policyParameter);
				} else if (policy.equalsIgnoreCase("s")) {
					// SoftMax action selection
					action = mcP.chooseSoftMaxAction(policyParameter);
				}

				// SoftMax action selection
				// StateActionPair action = qP.chooseSoftMaxAction(0.1);

				// update the predator's position
				mcP.position.setX(action.Action.getX());
				mcP.position.setY(action.Action.getY());

				if (Coordinate.compareCoordinates(mcP.position, prey.position)) {

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

					int NewPredPosX = mcP.position.getX() - x;
					int NewPredPosY = mcP.position.getY() - y;

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

					mcP.position.setX(NewPredPosXNor);
					mcP.position.setY(NewPredPosYNor);

				}

				// new value for this state according to the update function.
				// remember, worldState is still the old one (before the Agents
				// move)
				mcP.updateQTable(oldPosition, action, reward, absorbingState);

				steps++;

			} while (prey.lives);

		}

		mcP.PrintQTable();

		try {
			MatFileGenerator.write(
					output,
					"MonteCarlo_on-policy_" + policy + "_" + String.valueOf(policyParameter));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("An output .mat file has generated with the name: "
				+ "MonteCarlo_on-policy_" + policy + "_" + String.valueOf(policyParameter));

	}

}
