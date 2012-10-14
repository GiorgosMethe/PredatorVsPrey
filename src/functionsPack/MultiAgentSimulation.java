package functionsPack;

import environmentPack.Coordinate;
import environmentPack.Environment;
import actionPack.StateActionPair;
import agentsPack.Agent;
import agentsPack.QPreyM;
import agentsPack.QPredatorM;

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
		// env.worldState.add(p);
		for (int i = 0; i < num; i++) {
			QPredatorM q = new QPredatorM("Predator" + String.valueOf(i),
					PredatorPos[i], null, 0.5, 0.7);
			env.worldState.add(q);
		}
		// Table initialization
		// for the predators
		for (Agent a : env.worldState) {
			((QPredatorM) a).initializeQtable(env.worldState);
		}
		// for the prey
		p.initializeQtable(env.worldState);
		Coordinate[] oldPosition = new Coordinate[num];
		StateActionPair[] Actions = new StateActionPair[num];
		int j=0;
		for (int episode = 0; episode < 20; episode++) {
			System.out.println("\nepisode " + episode);
			boolean flag = false;
//			for (int i=0;i<env.worldState.size();i++) {
//				env.worldState.get(i).position.setX(PredatorPos[i].getX());
//				env.worldState.get(i).position.setY(PredatorPos[i].getY());
//				System.out.println("#"+env.worldState.get(i).position+" %"+PredatorPos[i]);
//				oldPosition[i] = env.worldState.get(i).position;
//				
//			}
			env.worldState.get(0).position.setX(0);
			env.worldState.get(0).position.setX(0);
			env.worldState.get(1).position.setY(10);
			env.worldState.get(1).position.setY(10);
			do {
				System.out.print(".");
				// prey's action
				StateActionPair PreyAction = p.chooseEGreedyAction(0.1, env.worldState);
				System.out.print("prey:"+PreyAction.Action);
				boolean preyTrap = (Math.random() < 0.2);
				// predators' actions
				for (Agent a : env.worldState) {
					StateActionPair PredAction = ((QPredatorM) a)
							.chooseEGreedyAction(0.1, env.worldState);
					// positions are not the same as the action
					// prey action change them
					if(!preyTrap){
						a.position.setX(PredAction.Action.getX()-(PreyAction.Action.getX()-PreyPos.getX()));
						a.position.setY(PredAction.Action.getY()-(PreyAction.Action.getY()-PreyPos.getY()));
					}else{
						a.position.setX(PredAction.Action.getX());
						a.position.setY(PredAction.Action.getY());
					}
					System.out.print("pred:"+PredAction.Action);
				}
				System.out.println("");
				if(env.checkCollision(env.worldState)){
					j=0;
					for(Agent a : env.worldState){
						//((QPredatorM) a).updateQTable(oldPosition, Action, reward, absorbing)
					}
					System.out.println("collision");
					flag = true;
				}
				if(!flag && env.checkCaughtStaticPrey(env.worldState, PreyPos)){
					System.out.println("caught");
					flag = true;
				}
				
				
				
				j = 0;
				for (Agent a : env.worldState) {
					oldPosition[j] = a.position;
					j++;
				}
			} while (!flag);
		}

	}
}
