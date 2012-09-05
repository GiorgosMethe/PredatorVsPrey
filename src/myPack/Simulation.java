package myPack;

public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Agent prey = new Prey("prey", new Coordinate(5, 5));
		Agent predator = new Predator("predator", new Coordinate(0, 0));
		
		
		Environment env = new Environment();
		
		env.worldState.add(predator);
		env.worldState.add(prey);
		
		do{
			
			env.run();
			System.out.println(env.print());
			
		}while(!env.isDone());
		
		System.exit(0);
				

	}

}
