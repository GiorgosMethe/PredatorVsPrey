package agentsPack;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import matPack.MatFileGenerator;
import actionPack.RandomAction;
import actionPack.SAPair;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class MCOnPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> qTable[][] = (Vector<StateActionPair>[][]) new Vector[6][6];
	private double gamma;

	public MCOnPredator(String name, Coordinate p, Policy pi,
			double gamma) {
		super(name, p, pi);
		this.gamma = gamma;
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		MCOnPredator.RunMonteCarloLearning(1000, 0.7, "e", 0.01);
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
				MCOnPredator mcOffP = new MCOnPredator("", new Coordinate(i, j), null, gamma);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(mcOffP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : mcOffP.ProbabilityActionsRSW(worldState)) {
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
					System.out.print(c.Action + " " + c.Value+"  ");
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

	public void updateQTable() {
		
	}

	public static void RunMonteCarloLearning(int number, double gamma,
			String policy, double policyParameter) {

		MCOnPredator mcOffP = new MCOnPredator("mcOffPredator", new Coordinate(5, 5), null,
				 gamma);
		mcOffP.initializeQTable();

		double[] output = new double[number];
		for (int i = 0; i < number; i++) {

			Vector<SAPair> episode = new Vector<SAPair>();
			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			mcOffP.position.setX(5);
			mcOffP.position.setY(5);

			worldState.add(mcOffP);
			worldState.add(prey);

			int steps = 0;

			do {

				Coordinate oldposition = new Coordinate(mcOffP.position.getX(), mcOffP.position.getY());
				StateActionPair action = null;
				if (policy.equalsIgnoreCase("e")) {
					// e-Greedy action selection
					action = mcOffP.chooseEGreedyAction(policyParameter);
				} else if (policy.equalsIgnoreCase("s")) {
					// SoftMax action selection
					action = mcOffP.chooseSoftMaxAction(policyParameter);
				}

				// SoftMax action selection
				// StateActionPair action = mcOffP.chooseSoftMaxAction(0.1);

				// update the predator's position
				mcOffP.position.setX(action.Action.getX());
				mcOffP.position.setY(action.Action.getY());

				if (Coordinate.compareCoordinates(mcOffP.position, prey.position)) {

					System.out.println("killed in :"+steps);
					output[i] = steps;
					prey.kill();

				} else {

					// Here, I am calculating the prey's possible actions
					Coordinate preyAction = prey.doAction(worldState);

					int x = preyAction.getX();
					if (x == 10)
						x = -1;
					int y = preyAction.getY();
					if (y == 10)
						y = -1;

					int NewPredPosX = mcOffP.position.getX() - x;
					int NewPredPosY = mcOffP.position.getY() - y;

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

					mcOffP.position.setX(NewPredPosXNor);
					mcOffP.position.setY(NewPredPosYNor);

				}
				episode.add(new SAPair(oldposition, action));
				steps++;

			} while (prey.lives);
			@SuppressWarnings("unchecked")
			Vector<Double> R[][][] = (Vector<Double>[][][]) new Vector[6][6][5];
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < 6; k++) {
					for (int l = 0; l < 5; l++) {
						R[j][k][l] = new Vector<Double>();
					}
				}
			}
			Collections.reverse(episode);
			double Return = 10.0 / gamma;
			for(SAPair s : episode){
				Return *= gamma;
				R[s.State.getX()][s.State.getY()][s.Action.id - 1].add(Return);
				
				double sum = 0.0;
				for(Double r : R[s.State.getX()][s.State.getY()][s.Action.id - 1]){
					sum += r;
				}
				
				double average = sum/R[s.State.getX()][s.State.getY()][s.Action.id - 1].size();
				
				int actionID = -1;
				for(int j=0;j<mcOffP.qTable[s.State.getX()][s.State.getY()].size();j++){
					if(mcOffP.qTable[s.State.getX()][s.State.getY()].elementAt(j).id == s.Action.id){
						actionID = j;
						break;
					}
				}
				
				mcOffP.qTable[s.State.getX()][s.State.getY()].elementAt(actionID).Value = average;
				
					
			}
			
			episode.clear();

		}

		mcOffP.PrintQTable();

		try {
			MatFileGenerator.write(
					output,
					"Q-Learning" + "_"
							+ String.valueOf(gamma) + "_" + policy + "_"
							+ String.valueOf(policyParameter));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("An output .mat file has generated with the name: "
				+ "Q-Learning" + "_"
				+ String.valueOf(gamma) + "_" + policy + "_"
				+ String.valueOf(policyParameter) + ".mat");

	}

}
