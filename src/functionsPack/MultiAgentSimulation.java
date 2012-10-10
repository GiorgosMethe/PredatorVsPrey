package functionsPack;

import agentsPack.Predator;
import agentsPack.Agent;
import agentsPack.Prey;
import agentsPack.Vector;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MultiAgentSimulation {


	
	public static void runSimulation() {
		

		
	
		
		for (int i = 0; i < 5; i++) {
			
			Predator pred1 = new Predator("pred1", new Coordinate(0,0), null);
			Predator pred2 = new Predator("pred2", new Coordinate(10,0), null);
			Predator pred3 = new Predator("pred3", new Coordinate(0,10), null);
			Predator pred4 = new Predator("pred4", new Coordinate(10,10), null);
			Prey prey = new Prey("prey", new Coordinate(5,5), null);
			
			
			Vector<Agent> worldState = new Vector<Agent>();
			worldState.add(prey);
			worldState.add(pred1);
			worldState.add(pred2);
			worldState.add(pred3);
			worldState.add(pred4);
			
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