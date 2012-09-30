package functionsPack;

import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class RandomSimulation {

	/*
	 * This is the main function of the simulation.It performs 100 hundred steps
	 * each one of them ends when the predator with the random policy manages to
	 * catch the prey
	 */
	public static void runSimulation() {

		int[] manytimesdoingsteps = new int[100];

		double mean = 0.0;
		double squaredsum = 0.0;

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < 100; i++) {

			manytimesdoingsteps[i] = 0;

			// Generation of the environment
			Environment env = new Environment();

			// Generation of the agent-Predator. Predator is going to be added
			// in the worldstate
			env.worldState.add(new Predator("predator", new Coordinate(0, 0),
					null));

			// Generation of the agent-Prey. Prey is going to be added
			// in the worldstate
			env.worldState.add(new Prey("prey", new Coordinate(5, 5), null));

			do {

				manytimesdoingsteps[i]++;

				// Run the simulation
				env.run();

			} while (!env.isDone());

			mean += manytimesdoingsteps[i];
			squaredsum += (manytimesdoingsteps[i] * manytimesdoingsteps[i]);

			if (manytimesdoingsteps[i] < min)
				min = manytimesdoingsteps[i];

			if (manytimesdoingsteps[i] > max)
				max = manytimesdoingsteps[i];

		}
		mean /= 100.0;
		squaredsum /= 100.0;

		System.out.println("\n\nResults over 100 runs:\n");
		System.out.println("Max # of steps: " + max + "\nMin # of steps: "
				+ min);
		System.out.println("Mean over 100 runs: " + mean);
		System.out.println("Stdev over 100 runs: "
				+ Math.sqrt(squaredsum - (mean * mean)));

	}

}
