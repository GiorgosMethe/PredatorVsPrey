package functionsPack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import agentsPack.Vector;

import agentsPack.Agent;
import agentsPack.EGreedyPolicy;
import agentsPack.LearningPredator;
import agentsPack.MCOnPredator;
import agentsPack.Policy;
import agentsPack.Predator;
import agentsPack.Prey;
import agentsPack.SoftMaxPolicy;
import environmentPack.Coordinate;

public class MCOnPredSim {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		int choiceInt = 0;
		String choice = null;

		Policy policy = null;

		System.out.println("What kind of policy should be used?");
		System.out.println("  1. Epsilon-greedy");
		System.out.println("  2. Softmax");

		do {
			System.out.print("\nGive your choice (1-2):");
			try {
				choice = reader.readLine();
				choiceInt = Integer.parseInt(choice);
			} catch (Exception e) {
				System.out.println("No... That was wrong.");
			}
		} while (choiceInt < 1 || choiceInt > 2);

		if (choiceInt == 1) {
			Double epsilon = Double.NEGATIVE_INFINITY;
			do {
				System.out
						.println("Give a double value for epsilon (0 <= e <= 1): ");
				try {
					choice = reader.readLine();
					epsilon = Double.parseDouble(choice);
				} catch (Exception e) {
					System.out.println("No... That was wrong.");
				}
			} while (epsilon < 0 || epsilon > 1);
			policy = new EGreedyPolicy(epsilon);
		} else if (choiceInt == 2) {
			Double temperature = Double.NEGATIVE_INFINITY;
			do {
				System.out
						.println("Give a double value for the temperature (t > 0): ");
				try {
					choice = reader.readLine();
					temperature = Double.parseDouble(choice);
				} catch (Exception e) {
					System.out.println("No... That was wrong.");
				}
			} while (temperature <= 0);
			policy = new SoftMaxPolicy(temperature);
		}
		Double initialQValue = Double.NEGATIVE_INFINITY;
		do {
			System.out
					.println("Give a double value for the initial value of Q(s,a) for all s,a: ");
			try {
				choice = reader.readLine();
				initialQValue = Double.parseDouble(choice);
			} catch (Exception e) {
				System.out.println("No... That was wrong.");
			}
		} while (Double.isInfinite(initialQValue)
				|| Double.isNaN(initialQValue));

		LearningPredator predator = new MCOnPredator("", new Coordinate(5, 5),
				policy, initialQValue);
		Predator dummyCurrent = new Predator("", new Coordinate(5, 5), null);
		Predator dummyNext = new Predator("", new Coordinate(5, 5), null);
		Vector<Agent> currentState = new Vector<Agent>();
		Vector<Agent> nextState = new Vector<Agent>();
		currentState.add(dummyCurrent);
		nextState.add(dummyCurrent);

		int sumMoves = 0;
		final int EPISODE_COUNT = 1000;

		for (int i = 0; i < EPISODE_COUNT; i++) {
			Prey prey = new Prey("prey", new Coordinate(0, 5), null);
			Vector<Agent> worldState = new Vector<Agent>();
			predator.position.setX(5);
			predator.position.setY(5);

			worldState.add(predator);
			worldState.add(prey);

			int steps = 0;

			// Action selection according to the policy
			Coordinate predatorAction = null;
			Coordinate preyAction;

			// SoftMax action selection
			// StateActionPair action = sP.chooseSoftMaxAction(0.5);

			while (prey.lives) {
				dummyCurrent.position = new Coordinate(predator.position.getX(), predator.position.getY());

				predatorAction = predator.doAction(worldState).toSWCoordinate();

				predator.position.setX(predatorAction.getX());
				predator.position.setY(predatorAction.getY());

				if (Coordinate.compareCoordinates(predator.position, prey.position)) {
					dummyNext.position = new Coordinate(predator.position.getX(), predator.position.getY());
					predator.observe(currentState, predatorAction, nextState, 10.0);
					prey.kill();
				} else {

					preyAction = prey.doAction(worldState);

					int x = preyAction.getX();
					if (x == 10)
						x = -1;
					int y = preyAction.getY();
					if (y == 10)
						y = -1;

					int NewPredPosX = predator.position.getX() - x;
					int NewPredPosY = predator.position.getY() - y;

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
					predator.position.setX(NewPredPosXNor);
					predator.position.setY(NewPredPosYNor);

					dummyNext.position = new Coordinate(predator.position.getX(), predator.position.getY());

					predator.observe(currentState, predatorAction, nextState,
							0.0);
				}
				steps++;
			}
			predator.observe(nextState, null, null, 0.0, true);
			sumMoves += steps + 1;

		}
		System.out.println("the average is: "
				+ (sumMoves / (double) EPISODE_COUNT));

		predator.printQTable();

	}
}
