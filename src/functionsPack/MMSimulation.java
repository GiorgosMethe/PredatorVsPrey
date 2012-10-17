package functionsPack;

import actionPack.StateActionPair;
import agentsPack.MMAgent;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MMSimulation {

	public static void main(String[] Args) {

		RunMiniMaxQlearning();
	}

	public static void RunMiniMaxQlearning() {
		Environment env = new Environment();
		MMAgent Predator = new MMAgent("", new Coordinate(0, 0),
				new Coordinate(0, 0), null, 0.5, 0.7);
		MMAgent Prey = new MMAgent("", new Coordinate(5, 5), new Coordinate(5,
				5), null, 0.5, 0.7);
		env.worldState.add(Predator);
		env.worldState.add(Prey);
		Predator.initializeQtable(env.worldState);
		Prey.initializeQtable(env.worldState);
		int steps = 0;
		int numiter = 100000;
		for (int episode = 0; episode < numiter; episode++) {
			Prey.old.setX(5);
			Prey.old.setY(5);
			Prey.position.setX(5);
			Prey.position.setY(5);
			Predator.old.setX(0);
			Predator.old.setY(0);
			Predator.position.setX(0);
			Predator.position.setY(0);

			double reward = 0.0;
			boolean Flag = false;
			do {
				steps++;
				StateActionPair PreyAction = Prey.ChooseAction(env.worldState,
						0.1);
				Prey.old.setX(Prey.position.getX());
				Prey.old.setY(Prey.position.getY());
				if (Math.random() < 0.2) {
					Prey.position.setX(PreyAction.Action.getX());
					Prey.position.setY(PreyAction.Action.getY());
				}
				Predator.old.setX(Predator.position.getX());
				Predator.old.setY(Predator.position.getY());
				StateActionPair PredAction = Predator.ChooseAction(
						env.worldState, 0.1);
				Predator.position.setX(PredAction.Action.getX());
				Predator.position.setY(PredAction.Action.getY());

				if (Coordinate.compareCoordinates(Prey.position,
						Predator.position)) {
					// System.out.println(steps);
					reward = 10.0;
					Flag = true;
				}
				Predator.UpdateMiniMax(env.worldState, PredAction, PreyAction,
						reward);
				Prey.UpdateMiniMax(env.worldState, PreyAction, PredAction, -1
						* reward);

			} while (!Flag);

		}
		System.out.println(steps / numiter);
	}

}
