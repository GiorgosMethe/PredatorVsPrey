package functionsPack;

import java.util.Collections;

import environmentPack.Coordinate;
import environmentPack.Environment;
import actionPack.StateActionPair;
import agentsPack.Agent;
import agentsPack.QPreyM;
import agentsPack.QPredatorM;
import agentsPack.Vector;

public class MultiAgentSimulation {
	final private static Coordinate[] PredatorPos = { new Coordinate(0, 0),
			new Coordinate(0, 10), new Coordinate(10, 0),
			new Coordinate(10, 10) };

	public static void main(String[] args) {
		MultiRun(2);
	}

	public static void MultiRun(int num) {
		
		boolean flag = false;
		boolean preyTrap = false;
		boolean absorbing = false;
		double reward = 0.0;
		Coordinate PreyPos = new Coordinate(5, 5);		
		// Table initialization
		Environment env = new Environment();
		QPreyM p = new QPreyM("prey", PreyPos, null, 0.5, 0.7);
		// env.worldState.add(p);
		for (int i = 0; i < num; i++) {
			QPredatorM q = new QPredatorM("Predator" + String.valueOf(i),
					PredatorPos[i],PredatorPos[i], null, 0.5, 0.7);
			env.worldState.add(q);
		}
		// Table initialization
		// for the predators
		for (Agent a : env.worldState) {
			((QPredatorM) a).initializeQtable(env.worldState);
		}
		// for the prey
		p.initializeQtable(env.worldState);
		Vector<Agent> oldState = new Vector<Agent>();
		StateActionPair[] Actions = new StateActionPair[num];
		
		for (int episode = 0; episode < 10000; episode++) {
			int j=0;
			for (Agent a : env.worldState) {
				if(j == 0){
					((QPredatorM) a).position.setX(0);
					((QPredatorM) a).position.setY(0);
					((QPredatorM) a).old.setX(0);
					((QPredatorM) a).old.setY(0);
				}
				if(j == 1){
					((QPredatorM) a).position.setX(10);
					((QPredatorM) a).position.setY(10);
					((QPredatorM) a).old.setX(10);
					((QPredatorM) a).old.setY(10);
				}
				j++;
			}
			System.out.println("\nepisode " + episode);
			int steps = 0;
			flag = false;
			do {
				steps++;
				for(Agent a : env.worldState){
					((QPredatorM) a).old = new Coordinate(a.position.getX(), a.position.getY());	
				}
				System.out.print(".");
				StateActionPair PreyAction = p.chooseEGreedyAction(0.1, env.worldState);
				preyTrap = (Math.random() < 0.2);
				j=0;
				for (Agent a : env.worldState) {
					StateActionPair PredAction = ((QPredatorM) a)
							.chooseEGreedyAction(0.1, env.worldState);
					Actions[j] = PredAction;
					j++;
					if(!preyTrap){
						a.position.setX(PredAction.Action.getX()-(PreyAction.Action.getX()-PreyPos.getX()));
						a.position.setY(PredAction.Action.getY()-(PreyAction.Action.getY()-PreyPos.getY()));
					}else{
						a.position.setX(PredAction.Action.getX());
						a.position.setY(PredAction.Action.getY());
					}
				}
				absorbing = false;
				reward = 0.0;
				if(env.checkCollision(env.worldState)){
					System.out.println("collision");
					reward = -10.0;
					absorbing = true;
					flag = true;
				}
				if(!flag && env.checkCaughtStaticPrey(env.worldState, PreyPos)){
					System.out.println("caught");
					reward = 10.0;
					absorbing = true;
					flag = true;
				}
				for(Agent a : env.worldState){
					((QPredatorM) a).updateQTable(env.worldState, Actions, reward, absorbing);
				}
			} while (!flag);
			System.out.println("steps = "+steps);
		}

	}
}
