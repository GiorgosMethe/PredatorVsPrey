package myPack;

public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] manytimesdoingsteps = new int[101];
		double mean = 0.0;
		double squaredsum = 0.0;
		int min = 100000;
		int max = 0;
		for (int i = 0; i < 100; i++) {
			manytimesdoingsteps[i] = 0;
			Environment env = new Environment();

			// This is not really nice coding. It's only a quick fix; needs redesign?
			env.worldState.add(new PolicyPredator("predator", new Coordinate(0, 0),
					null));
			env.worldState.lastElement().pi = new SingleAgentRandom(env.worldState.lastElement());
			env.worldState.add(new Prey("prey", new Coordinate(5, 5), null));

			do {
				(manytimesdoingsteps[i])++;
				env.run();
				System.out.println(env.print());

			} while (!env.isDone());
			mean += manytimesdoingsteps[i];
			squaredsum += (manytimesdoingsteps[i] * manytimesdoingsteps[i]);

			if (manytimesdoingsteps[i] < min) {
				min = manytimesdoingsteps[i];
			}

			if (manytimesdoingsteps[i] > max) {
				max = manytimesdoingsteps[i];
			}
		}
		mean /= 100.0;
		squaredsum /= 100.0;

		System.out
				.println("max # of steps: " + max + " min # of steps: " + min);
		System.out.println("mean over 100 runs: " + mean);
		System.out.println("stdev over 100 runs: "
				+ Math.sqrt(squaredsum - (mean * mean)));

		System.exit(0);

	}

}
