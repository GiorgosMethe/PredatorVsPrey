package functionsPack;

import java.util.Vector;

import actionPack.StateActionPair;
import agentsPack.Agent;
import agentsPack.Prey;
import agentsPack.SarsaPredator;
import environmentPack.Coordinate;

public class SarsaPredSim {

	public static void main(String[] args) {

		Run();

	}

	public static void Run() {

		SarsaPredator sP = new SarsaPredator("SarsaPredator", new Coordinate(5,
				5), null);
		sP.initializeSarsaTable();
		int sumMoves = 0;

		for (int i = 0; i < 10000; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			sP.position.setX(5);
			sP.position.setY(5);
			
			worldState.add(sP);
			worldState.add(prey);

			int steps = 0;

			Coordinate oldPosition;

			// e-Greedy action selection
			StateActionPair action = sP.chooseEGreedyAction(0.1);

			// SoftMax action selection
			// StateActionPair action = sP.chooseSoftMaxAction(0.5);

			boolean absorbingState = false;

			do {

				oldPosition = new Coordinate(sP.position.getX(), sP.position.getY());
				
				sP.position.setX(action.Action.getX());
				sP.position.setY(action.Action.getY());
				
				if(Coordinate.compareCoordinates(sP.position,prey.position)){
					break;
				}else{
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
				steps++;
			}while(prey.lives);
		}
	}

}
