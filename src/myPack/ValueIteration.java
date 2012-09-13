package myPack;

import java.util.Map;
import java.util.Vector;

public class ValueIteration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] State = new double[11][11];
		
		//initialize the array
		for(int i=0;i<State.length;i++)
			for(int j=0;j<State.length;j++)
				State[i][j] = 0;
		
		
		
		for(int i=0;i<State.length;i++){
			for(int j=0;j<State.length;j++){
				
				Environment env = new Environment();
				Prey p = new Prey("prey", new Coordinate(5, 5), null);
				Predator P = new Predator("Predator", new Coordinate(i, j), null);
				env.addAgent(P);
				env.addAgent(p);
				
				Vector<RandomAction> PossiblePredatorActions = P.ProbabilityActions(env.worldState);
				Vector<RandomAction> PossiblePreyActions = p.ProbabilityActions(env.worldState);
				
				System.out.println("World:"+P.toString()+" "+p.toString());
				System.out.println("--------------------------------------");
				double Prob = 0;
				for(int ii=0;ii<PossiblePredatorActions.size();ii++){
					System.out.println("----");
					for(int jj=0;jj<PossiblePreyActions.size();jj++){
						
						System.out.println("Predator: "+PossiblePredatorActions.elementAt(ii).coordinate.toString());
						System.out.println("Prey: "+PossiblePredatorActions.elementAt(jj).coordinate.toString());
						Prob += PossiblePredatorActions.elementAt(ii).prob*
								PossiblePreyActions.elementAt(jj).prob;
						
						System.out.println("Probability: "+Prob);
						
					}
				}
				
				
			}
			
		}
				

	}

}
