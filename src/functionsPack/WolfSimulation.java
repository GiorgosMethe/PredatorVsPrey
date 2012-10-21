package functionsPack;

import java.io.IOException;

import matPack.MatFileGenerator;
import actionPack.StateActionPair;
import agentsPack.MMAgent;
import agentsPack.WolfAgent;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class WolfSimulation {

	public static void RunWolflearning(double deltaLose,double deltaWin) {

		int numKati = 1;
		int numiter = 2000000;
		double[][] output = new double[numiter][numKati];
		for(int iterA=0;iterA<numKati;iterA++){
			
			Environment env = new Environment();
			WolfAgent Predator1 = new WolfAgent("", new Coordinate(10, 10),
					new Coordinate(10, 10), null, 0.7, 0.7, deltaLose, deltaWin);
			WolfAgent Predator = new WolfAgent("", new Coordinate(0, 0),
					new Coordinate(0, 0), null, 0.7, 0.7, deltaLose, deltaWin);
			WolfAgent Prey = new WolfAgent("", new Coordinate(5, 5), new Coordinate(5,
					5), null, 0.7, 0.7, deltaLose, deltaWin);
			env.worldState.add(Predator1);
			env.worldState.add(Predator);
			env.worldState.add(Prey);
			Predator1.initialize(env.worldState);
			Predator.initialize(env.worldState);
			Prey.initialize(env.worldState);
			System.out.println(iterA);

			int steps = 0;
			for (int episode = 0; episode < numiter; episode++) {

				System.out.println(episode);
				Prey.old.setX(5);
				Prey.old.setY(5);
				Prey.position.setX(5);
				Prey.position.setY(5);
				Predator.old.setX(0);
				Predator.old.setY(0);
				Predator.position.setX(0);
				Predator.position.setY(0);
				Predator1.old.setX(10);
				Predator1.old.setY(10);
				Predator1.position.setX(10);
				Predator1.position.setY(10);
				double reward = 0.0;
				boolean Flag = false;
				steps = 0;
				do {
					steps++;
					StateActionPair PreyAction = Prey.chooseAction(env.worldState);
					StateActionPair PredAction = Predator.chooseAction(env.worldState);
					StateActionPair Pred1Action = Predator1.chooseAction(env.worldState);
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
					
					Predator1.old.setX(Predator1.position.getX());
					Predator1.old.setY(Predator1.position.getY());
					Predator1.position.setX(Pred1Action.Action.getX());
					Predator1.position.setY(Pred1Action.Action.getY());
					
					if (Coordinate.compareCoordinates(Predator1.position,
							Predator.position)) {
						output[episode][iterA] = -steps;
							reward = -10.0;
						Flag = true;
					}
					
					if (!Flag && Coordinate.compareCoordinates(Prey.position,
							Predator.position)) {
						output[episode][iterA] = steps;
							reward = 10.0;
						Flag = true;
					}
					Predator1.updateWolf(env.worldState, Pred1Action, reward, Flag );
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
					"MultiAgentWolf3"+String.valueOf(deltaLose)+"-"+String.valueOf(deltaWin));
			System.out.println("Mat file created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}