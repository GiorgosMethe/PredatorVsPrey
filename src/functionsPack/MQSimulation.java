package functionsPack;

import java.io.IOException;

import matPack.MatFileGenerator;
import actionPack.StateActionPair;
import agentsPack.Agent;
import agentsPack.MQPredator;
import agentsPack.MQPrey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MQSimulation {
	final private static int[] PredatorPos = { 0, 0, 10, 10, 10, 0, 0, 10 };

	public static void MultiRun(int num) {

		double[] output = new double[1000000];
		boolean flag = false;
		boolean preyTrap = false;
		boolean absorbing = false;
		double reward = 0.0;
		int caught = 0;
		int collision = 0;
		int sumsteps = 0;
		Coordinate PreyPos = new Coordinate(5, 5);
		// Table initialization
		Environment env = new Environment();
		MQPrey p = new MQPrey("prey", PreyPos, null, 0.5, 0.7);
		// env.worldState.add(p);
		for (int i = 0; i < num; i++) {
			MQPredator q = new MQPredator("Predator" + String.valueOf(i),
					new Coordinate(0, 0), new Coordinate(0, 0), null, 0.5, 0.7);
			env.worldState.add(q);
		}
		// Table initialization
		// for the predators
		for (Agent a : env.worldState) {
			((MQPredator) a).initializeQtable(env.worldState);
		}
		// for the prey
		p.initializeQtable(env.worldState);
		StateActionPair[] Actions = new StateActionPair[num];
		for (int episode = 0; episode < 1000000; episode++) {

			int steps = 0;
			// System.out.println("------------");
			int j = 0;
			for (Agent a : env.worldState) {
				((MQPredator) a).position.setX(PredatorPos[j]);
				((MQPredator) a).position.setY(PredatorPos[j + 1]);
				((MQPredator) a).old.setX(PredatorPos[j]);
				((MQPredator) a).old.setY(PredatorPos[j + 1]);
				j += 2;
			}
			flag = false;
			do {
				steps++;

				for (Agent a : env.worldState) {
					((MQPredator) a).old = new Coordinate(a.position.getX(),
							a.position.getY());
				}
				// double nonFixedE = (1-(100000/(episode+1)));
				StateActionPair PreyAction = p.chooseEGreedyAction(0.1,
						env.worldState);
				preyTrap = (Math.random() < 0.2);
				j = 0;
				for (Agent a : env.worldState) {
					StateActionPair PredAction = ((MQPredator) a)
							.chooseEGreedyAction(0.1, env.worldState);
					Actions[j] = PredAction;
					j++;
					if (!preyTrap) {
						a.position.setX(PredAction.Action.getX()
								- (PreyAction.Action.getX() - PreyPos.getX()));
						a.position.setY(PredAction.Action.getY()
								- (PreyAction.Action.getY() - PreyPos.getY()));
					} else {
						a.position.setX(PredAction.Action.getX());
						a.position.setY(PredAction.Action.getY());
					}
				}
				absorbing = false;
				reward = 0.0;
				if (env.checkCollision(env.worldState)) {
					reward = -10.0;
					collision++;
					output[episode] = -steps;
					absorbing = true;
					flag = true;
				}
				if (!flag && env.checkCaughtStaticPrey(env.worldState, PreyPos)) {
					caught++;
					output[episode] = steps;
					reward = 10.0;
					absorbing = true;
					flag = true;
				}
				for (Agent a : env.worldState) {
					((MQPredator) a).updateQTable(env.worldState, Actions,
							reward, absorbing);
				}
				p.updateQTable(env.worldState, PreyAction, (-1) * reward,
						absorbing);

			} while (!flag);
			sumsteps += steps;
		}
		for (Agent a : env.worldState) {
			System.out.println("agent" + a.name
					+ " univisited state action pairs:"
					+ ((MQPredator) a).UnvisitedStateActions());
		}
		System.out.println("collisions:" + collision);
		System.out.println("caught:" + caught);
		System.out.println("avg steps:" + (sumsteps / 1000000));

		try {
			MatFileGenerator.write(output,
					"MultiAgentQ" + "_" + String.valueOf(num));
			System.out.println("Mat file created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
