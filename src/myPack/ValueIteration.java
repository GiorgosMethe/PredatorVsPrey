package myPack;

import java.util.Vector;

public class ValueIteration {

	/**
	 * @param args
	 */
	public static Double Value[] = new Double[121];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		for(int i=0;i<11;i++){
			for(int j=0;j<11;j++){
				
				int stateIndex = (i * 11) + j;
				Value[stateIndex] = (double) 0;
				Coordinate PredatorPosition = new Coordinate(i, j);
				Coordinate PreyPosition = new Coordinate(5, 5);
				Environment env = new Environment();
				env.worldState.add(new Predator("Predator", PredatorPosition, null));
				env.worldState.add(new Prey("Prey", PreyPosition, null));
				
				VIterationComp(env.worldState);		
				
			}
		}

	}

	
	public static double VIterationComp(Vector<Agent> worldState){
		
		
		
		
		
		
		return 0;
		
	}
}
