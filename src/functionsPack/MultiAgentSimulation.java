package functionsPack;

import environmentPack.Coordinate;
import environmentPack.Environment;
import actionPack.StateActionPair;
import agentsPack.Agent;
import agentsPack.QPreyM;
import agentsPack.QPredatorM;
import agentsPack.Vector;

public class MultiAgentSimulation {

	public static void main(String[] args) {
		MultiRun(3);
	}

	public static void MultiRun(int num) {
		Coordinate PreyPos = new Coordinate(5, 5);
		Coordinate[] PredatorPos = { new Coordinate(0, 0),
				new Coordinate(0, 10), new Coordinate(10, 0),
				new Coordinate(10, 10) };
		// Table initialization
		Environment env = new Environment();
		QPreyM p = new QPreyM("prey", PreyPos, null, 0.5, 0.7);
		//env.worldState.add(p);
		for (int i = 0; i < num; i++) {
			QPredatorM q = new QPredatorM("Predator" + String.valueOf(i),
					PredatorPos[i], null, 0.5, 0.7);
			env.worldState.add(q);
		}
		// Table initialization
		for (Agent a : env.worldState) {
			if (a instanceof QPredatorM)
				((QPredatorM) a).initializeQtable(env.worldState);
			//			if (a instanceof QPreyM)
			//				((QPreyM) a).initializeQtable(env.worldState);
		}
		for(int episode = 0 ; episode < 20 ; episode ++){
			System.out.println("\nepisode "+episode);
			boolean flag = false;
			int j=0;
			for (Agent a : env.worldState) {
				a.position.setX(PredatorPos[j].getX());
				a.position.setY(PredatorPos[j].getY());
				j++;
			}
			do{
				for (Agent a : env.worldState){
					if (a instanceof QPredatorM){
						StateActionPair PredAction = ((QPredatorM) a).chooseEGreedyAction(0.1, env.worldState);
						a.position.setX(PredAction.Action.getX());
						a.position.setY(PredAction.Action.getY());
						System.out.print(".");
					}
				}
				flag = env.checkCollision(env.worldState);
			}while(!flag);
		}

	}


}
