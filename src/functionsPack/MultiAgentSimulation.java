package functionsPack;

import agentsPack.Predator;
import agentsPack.Agent;
import agentsPack.Prey;
import agentsPack.Vector;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MultiAgentSimulation {


	
	public static void runSimulation(int no) {
		

		
	

		
		for (int i = 0; i < 5; i++) {
			
			Prey prey = new Prey("prey", new Coordinate(5,5), null);

			Vector<Agent> worldState = new Vector<Agent>();
			worldState.add(prey);
			
			for(int j =0;j<no;j++){
			Predator pred = new Predator("pred", new Coordinate((int)Math.abs(10*Math.random()),(int)Math.abs(10*Math.random())), null);

			worldState.add(pred);
			}
			
			
			System.out.println("Number of preds  :" +no+"   number of agents"+worldState.size());
			Environment env = new Environment();
			
			
			
			System.out.println("Episode No "+i+" has begun bitch.");
			boolean flag = false;
			do{
			
			for (Agent p : worldState){
				if(p instanceof Predator){
					p.position=((Predator) p).chooseRandomAction();
					System.out.println("Next position of "+ p.name +"   :    "+ p.position);
					}
				else {
					((Prey) p).position = ((Prey) p).chooseRandomAction();
					System.out.println("Next  position of prey    :     " + p.position);

					
					}
			}
			System.out.println(" ");
 
			if(env.checkCollision(worldState)){
				System.out.println("COLLISION!!");
				flag=true;
				}
			if(env.checkCaught(worldState)){
				System.out.println("YEAH ITS CAUGHT BITCH!");
				flag=true;
				}
			}while(!flag);
		}

}
}