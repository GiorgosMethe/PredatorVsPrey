package myPack;

public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		Integer[] manytimesdoingsteps = new Integer[101];
		double mean = 0.0;
		double squaredsum = 0.0;
		for (int i = 0; i < 100; i++) {
			manytimesdoingsteps[i] = 0;
			Environment env = new Environment();
			
			env.worldState.add(new Predator("predator", new Coordinate(0, 0)));
			env.worldState.add(new Prey("prey", new Coordinate(5, 5)));
			
			do{
				(manytimesdoingsteps[i])++;
				env.run();
				System.out.println(env.print());
				
			}while(!env.isDone());
			mean += manytimesdoingsteps[i];
			squaredsum += (manytimesdoingsteps[i] * manytimesdoingsteps[i]);
		}
		mean /= 100.0;
		squaredsum /= 100.0;
		
		System.out.println("mean over 100 runs: " + mean);
		System.out.println("stdev over 100 runs: " + Math.sqrt(squaredsum - (mean * mean)));
		
		System.exit(0);
				

	}

}
