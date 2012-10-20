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

		int numKati = 20;
		int numiter = 100000;
		double[][] output = new double[numiter][numKati];
		for(int iterA=0;iterA<numKati;iterA++){
			System.out.println(iterA);
			Environment env = new Environment();
			WolfAgent Predator = new WolfAgent("", new Coordinate(0, 0),
					new Coordinate(0, 0), null, 0.7, 0.7, 0.1, 0.01);
			WolfAgent Prey = new WolfAgent("", new Coordinate(5, 5), new Coordinate(5,
					5), null, 0.7, 0.7, 0.1, 0.01);
			env.worldState.add(Predator);
			env.worldState.add(Prey);
			Predator.initialize(env.worldState);
			Prey.initialize(env.worldState);


			int steps = 0;
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
				steps = 0;
				do {
					steps++;
					StateActionPair PreyAction = Prey.chooseAction(env.worldState);
					StateActionPair PredAction = Predator.chooseAction(env.worldState);
					Prey.old.setX(Prey.position.getX());
					Prey.old.setY(Prey.position.getY());	
					if (Math.random() > 0.2) {
						Prey.position.setX(PreyAction.Action.getX());
						Prey.position.setY(PreyAction.Action.getY());
					}
					Predator.old.setX(Predator.position.getX());
					Predator.old.setY(Predator.position.getY());
					Predator.position.setX(PredAction.Action.getX());
					Predator.position.setY(PredAction.Action.getY());
					if (Coordinate.compareCoordinates(Prey.position,
							Predator.position)) {
						output[episode][iterA] = steps;
							reward = 10.0;
						Flag = true;
					}
					Predator.updateWolf(env.worldState, PredAction, reward, Flag );
					Prey.updateWolf(env.worldState, PreyAction, -reward, Flag );
				} while (!Flag);
			}
		}
		double[] kati = new double[numiter];
		for(int i=0;i<numiter;i++){
			int sum=0;
			for(int j=0;j<numKati;j++){
				sum+=output[i][j];
			}
			kati[i] = sum/numKati;
		}
		try {
			MatFileGenerator.write(kati,
					"MultiAgentWolf2"+String.valueOf(07-07-01-001));
			System.out.println("Mat file created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}