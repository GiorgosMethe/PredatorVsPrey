package agentsPack;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import agentsPack.Vector;
import matPack.MatFileGenerator;
import actionPack.RandomAction;
import actionPack.SAPair;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class MCOffPredator extends Predator {	
	

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> qTable[][] = (Vector<StateActionPair>[][]) new Vector[6][6];
	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> updatedQTable[][] = (Vector<StateActionPair>[][]) new Vector[6][6];
	private double numerator[][][] = new double[6][6][5];
	private double denominator[][][] = new double[6][6][5];
	
	private double gamma;

	public MCOffPredator(String name, Coordinate p, Policy pi,
			double gamma) {
		super(name, p, pi);
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
		this.qTable = initializeQTable(this.qTable);
		this.updatedQTable = initializeQTable(this.updatedQTable);
	}
	
	public Vector<StateActionPair>[][] initializeQTable(Vector<StateActionPair> qTable[][]) {
		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				qTable[i][j] = new Vector<StateActionPair>();
				MCOffPredator qP = new MCOffPredator("", new Coordinate(i, j), null, this.gamma);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(qP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : qP.ProbabilityActionsRSW(worldState)) {
					id++;
					qTable[i][j].add(new StateActionPair(new Coordinate(
							c.coordinate.getX(), c.coordinate.getY()), 15, id));

				}

			}
		}
		return qTable;
	}

	public void PrintQTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				System.out.println(new Coordinate(i, j));
				for (StateActionPair c : this.updatedQTable[i][j]) {

					System.out.println(c.Action + " " + c.Value);

				}
			}
		}
	}

	public StateActionPair chooseOptimalAction() {
		double maxValue = Double.NEGATIVE_INFINITY;
		StateActionPair maxA = null;
		for (StateActionPair s : this.updatedQTable[this.position.getX()][this.position.getY()]) {
			if (s.Value > maxValue) {
				maxValue = s.Value;
				maxA = s;
			}
		}
		return maxA;
	}
	public StateActionPair chooseMaxAction() {
		double maxValue = Double.NEGATIVE_INFINITY;
		StateActionPair maxA = null;
		for (StateActionPair s : this.qTable[this.position.getX()][this.position.getY()]) {
			if (s.Value > maxValue) {
				maxValue = s.Value;
				maxA = s;
			}
		}
		return maxA;
	}
	
	public StateActionPair chooseSoftMaxAction(double temperature) {
		return this.chooseMaxAction();
	}

	public StateActionPair chooseEGreedyAction(double epsilon) {
		return this.chooseMaxAction();
	}

	public static void RunMonteCarloOff(int number, double gamma,
			String policy, double policyParameter) {

		MCOffPredator qP = new MCOffPredator("qPredator", new Coordinate(5, 5), null, gamma);
		qP.initializeQTable();
		
		double[] output = new double[number];
		
		for (int i = 0; i < number; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			qP.position.setX(5);
			qP.position.setY(5);

			worldState.add(qP);
			worldState.add(prey);

			Vector<SAPair> SApairList = new Vector<SAPair>();
			
			// Training episode
			do {
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
				
				SApairList.add(new SAPair(oldPosition, action));

				if (Coordinate.compareCoordinates(qP.position, prey.position)) {
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
System.out.print(",");
			} while (prey.lives);
			
			// Update QTable
			Collections.reverse(SApairList);
			double Return = 10.0 / gamma;
			double w = 1.0;
			for(SAPair s : SApairList){
				Return *= gamma;
				w *= 1.0 / qP.probilityStateAction(s, policy, policyParameter);
				qP.numerator[s.State.getX()][s.State.getY()][s.Action.id] += (w * Return);
				qP.denominator[s.State.getX()][s.State.getY()][s.Action.id] += w;

				int actionID = -1;
				for(int j=0;j<qP.qTable[s.State.getX()][s.State.getY()].size();j++){
					if(qP.qTable[s.State.getX()][s.State.getY()].elementAt(j).id == s.Action.id){
						actionID = j;
						break;
					}
				}
				qP.qTable[s.State.getX()][s.State.getY()].elementAt(actionID).Value = qP.numerator[s.State.getX()][s.State.getY()][s.Action.id] / qP.denominator[s.State.getX()][s.State.getY()][s.Action.id];

				Coordinate optimalAction = null;
				double maxValue = Double.NEGATIVE_INFINITY;
				for (StateActionPair sap : qP.updatedQTable[s.State.getX()][s.State.getY()]) {
					if (sap.Value > maxValue) {
						maxValue = sap.Value;
						optimalAction = sap.Action;
					}
				}
				
				if (s.Action.Action != optimalAction) {
					break;
				}
			}
			
			// Show what you have learnt
			prey.lives = true;
			int steps = 0;
			do {

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
				
				SApairList.add(new SAPair(oldPosition, action));

				if (Coordinate.compareCoordinates(qP.position, prey.position)) {

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
System.out.print(".");
			} while (prey.lives);
		}
		
		qP.PrintQTable();

		try {
			MatFileGenerator.write(
					output,
					"MCoffLine-Learning" + "_"
							+ String.valueOf(gamma) + "_" + policy + "_"
							+ String.valueOf(policyParameter));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("An output .mat file has generated with the name: "
				+ "MCoffLine-Learning" + "_" 
				+ String.valueOf(gamma) + "_" + policy + "_"
				+ String.valueOf(policyParameter) + ".mat");

	}

	private double probilityStateAction(SAPair s, String policy, double policyParameter) {
		if (policy.equalsIgnoreCase("s")) {
			double sum = 0.0;
			for (StateActionPair sa : this.qTable[s.State.getX()][s.State.getY()]) {
				sum += Math.exp(sa.Value / policyParameter);
			}
			return Math.exp(s.Action.Value / policyParameter) / sum;
		}
		else if (policy.equalsIgnoreCase("e")) {
			for (StateActionPair sa : this.qTable[s.State.getX()][s.State.getY()]) {
				if (sa.Value > s.Action.Value) {
					return 0.25 * policyParameter;
				}
			}
			return 1 - policyParameter;
		}
		return 1.0;
	}
	
	public static void main(String[] args) {
		RunMonteCarloOff(1, 0.3, "e", 0.2);
	}
}
