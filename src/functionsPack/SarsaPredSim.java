package functionsPack;

import java.util.Vector;

import agentsPack.Agent;
import agentsPack.Prey;
import agentsPack.QPredator;
import environmentPack.Coordinate;

public class SarsaPredSim {
	
	public static void main(String[] args) {

		Run();

	}

	public static void Run() {

		QPredator qP = new QPredator("qPredator", new Coordinate(5, 5), null);
		qP.initializeQTable();
		int sumMoves = 0;

		for (int i = 0; i < 10000; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			qP.position.setX(5);
			qP.position.setY(5);

			worldState.add(qP);
			worldState.add(prey);

			int steps = 0;

			do {

				Double reward = 0.0;

				// save the old position, we need it later.
				Coordinate oldPosition = new Coordinate(qP.position.getX(),
						qP.position.getY());

				// e-Greedy action selection
				// Coordinate nextAction = qP.chooseEGreedyAction(qP,
				// worldState,
				// 0.01);

				// SoftMax action selection
				Coordinate nextAction = qP.chooseSoftMaxAction(qP, worldState,
						0.1);

				// update the predator's position
				qP.position.setX(nextAction.getX());
				qP.position.setY(nextAction.getY());

				if (Coordinate.compareCoordinates(qP.position, prey.position)) {

					System.out.println("killed in " + steps + " steps");
					sumMoves += steps;
					reward = 10.0;
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

					if (NewPredPosY < NewPredPosX) {

						NewPredPosXNor = NewPredPosY;
						NewPredPosYNor = NewPredPosX;

					}

					qP.position.setX(NewPredPosXNor);
					qP.position.setY(NewPredPosYNor);

				}

				// new value for this state according to the update function.
				// remember, worldState is still the old one (before the Agents
				// move)
				//qP.updateQTable(oldPosition, qP.position, reward);

				steps++;
			} while (prey.lives);

		}

		System.out.println("the average is" + (sumMoves / 10000));

		qP.PrintQTable();

	}

}

