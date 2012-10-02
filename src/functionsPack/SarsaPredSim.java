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

		for (int i = 0; i < 1000; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0), null);
			Vector<Agent> worldState = new Vector<Agent>();
			sP.position.setX(5);
			sP.position.setY(5);

			worldState.add(sP);
			worldState.add(prey);

			int steps = 0;

			Coordinate oldPosition = new Coordinate(sP.position.getX(),
					sP.position.getY());

			// e-Greedy action selection
			StateActionPair action = sP.chooseEGreedyAction(0.1);

			// SoftMax action selection
			// StateActionPair action = sP.chooseSoftMaxAction(0.5);

			while(prey.lives){
				
				if(Coordinate.compareCoordinates(sP.position, prey.position)){
					System.out.println("steps: "+steps);
					prey.kill();
					break;
				}else{
				
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
					if(Coordinate.compareCoordinates(sP.position,prey.position)){
						reward = 10.0;
						absorb = true;
						
					}
					
					sP.updateSarsaTable(oldPosition, action, newAction, sP.position, reward, absorb);
					
					// a = a'
					action = newAction;
					steps++;
				}
			}
			

			
		}
		System.out.println("the average is: " + (sumMoves / 10000));

		sP.printSarsaTable();

	}

}