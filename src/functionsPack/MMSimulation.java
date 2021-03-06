package functionsPack;

import java.io.IOException;

import matPack.MatFileGenerator;
import actionPack.StateActionPair;
import agentsPack.MMAgent;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MMSimulation {

	public static void RunMiniMaxQlearning(double gamma) {

		int numKati = 5;
		int numiter = 20000;
		double[][] output = new double[numiter][numKati];
		
		
		for(int iterA=0;iterA<numKati;iterA++){
			
			System.out.println(iterA);
			Environment env = new Environment();
			MMAgent Predator = new MMAgent("", new Coordinate(0, 0),
					new Coordinate(0, 0), null, 1, 0.7);
			MMAgent Prey = new MMAgent("", new Coordinate(5, 5), new Coordinate(5,
					5), null, 1, 0.7);
			env.worldState.add(Predator);
			env.worldState.add(Prey);
			Predator.initializeqTable(env.worldState);
			Prey.initializeqTable(env.worldState);
			System.out.println();
			int steps = 0;
			for (int episode = 0; episode < numiter; episode++) {
				if((episode+1)%2000 == 0){
					System.out.print(".");
				}
				
				Prey.old.setX(5);
				Prey.old.setY(5);
				Prey.position.setX(5);
				Prey.position.setY(5);
				Predator.old.setX(0);
				Predator.old.setY(0);
				Predator.position.setX(0);
				Predator.position.setY(0);
				steps = 0; 
				double reward = 0.0;
				boolean Flag = false;
				do {
					steps++;
					StateActionPair PreyAction = Prey.ChooseAction(env.worldState, 0.1);
					StateActionPair PredAction = Predator.ChooseAction(env.worldState, 0.1);
					Prey.old.setX(Prey.position.getX());
					Prey.old.setY(Prey.position.getY());	

					if (Math.random() > 0.2) { // tripping probability
						Prey.position.setX(PreyAction.Action.getX());
						Prey.position.setY(PreyAction.Action.getY());
					}
					Predator.old.setX(Predator.position.getX());
					Predator.old.setY(Predator.position.getY());
					Predator.position.setX(PredAction.Action.getX());
					Predator.position.setY(PredAction.Action.getY());

					reward = 0.0;
					if (Coordinate.compareCoordinates(Prey.position, Predator.position)) {
						output[episode][iterA] = steps;
						reward = 10.0;
						Flag = true;
					}

					Predator.UpdateMiniMax(env.worldState, PredAction, PreyAction, reward, false);
					Prey.UpdateMiniMax(env.worldState, PreyAction, PredAction, -reward, false);
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
					"MultiAgentMiniMaxQdecay"+String.valueOf(gamma));
			System.out.println("Mat file created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
