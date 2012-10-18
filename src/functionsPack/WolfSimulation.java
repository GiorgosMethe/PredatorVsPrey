package functionsPack;

import java.io.IOException;

import matPack.MatFileGenerator;
import actionPack.StateActionPair;
import agentsPack.MMAgent;
import agentsPack.WolfAgent;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class WolfSimulation {

	public static void main(String[] Args) {
		RunWolflearning();
	}


	public static void RunWolflearning() {


		Environment env = new Environment();
		WolfAgent Predator = new WolfAgent("", new Coordinate(0, 0),
				new Coordinate(0, 0), null, 0.5, 0.7, 0.1, 0.2);
		WolfAgent Prey = new WolfAgent("", new Coordinate(5, 5), new Coordinate(5,
				5), null, 0.5, 0.7, 0.1, 0.2);
		env.worldState.add(Predator);
		env.worldState.add(Prey);
		Predator.initialize(env.worldState);
		Prey.initialize(env.worldState);


		double reward = 0.0;
		boolean Flag = false;
		int steps = 0;
		do {
			steps++;
			StateActionPair PreyAction = Prey.chooseAction(env.worldState);
			StateActionPair PredAction = Predator.chooseAction(env.worldState);
			Prey.old.setX(Prey.position.getX());
			Prey.old.setY(Prey.position.getY());	
			if (Math.random() < 0.2) {
				Prey.position.setX(PreyAction.Action.getX());
				Prey.position.setY(PreyAction.Action.getY());
			}
			Predator.old.setX(Predator.position.getX());
			Predator.old.setY(Predator.position.getY());
			Predator.position.setX(PredAction.Action.getX());
			Predator.position.setY(PredAction.Action.getY());

			if (Coordinate.compareCoordinates(Prey.position,
					Predator.position)) {
				System.out.println(steps);
				reward = 10.0;
				Flag = true;
			}

			Predator.updateWolf(env.worldState, PredAction, reward, Flag );


		} while (!Flag);

	}

}