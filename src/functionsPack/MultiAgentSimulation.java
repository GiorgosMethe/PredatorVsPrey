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
		MultiRun(2);
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
		
		for (Agent a : env.worldState) {
			if (a instanceof QPredatorM)
				((QPredatorM) a).chooseEGreedyAction(0.1, env.worldState);
			
		}

	}
	

}
