package functionsPack;

import java.util.ArrayList;

import agentsPack.Predator;
import agentsPack.Agent;
import agentsPack.Prey;
import agentsPack.Vector;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MultiAgentSimulation {

	
	public static void addPredator(ArrayList<Coordinate> list, Vector<Agent> ws){
		
		
	Predator pred = new Predator("pred",
			new Coordinate((int)Math.abs(10*Math.random()),
					(int)Math.abs(10*Math.random())), null);
	
	if (list.contains(pred.position))
		addPredator(list, ws);
		
	else{
		list.add(pred.position);
		ws.add(pred);
	}
		
		
	}
	
	
	
	public static void runSimulation(int no) {
		
		int collisions =0;
		int catches= 0;
		int sumCatchSteps =0;
		
		for (int i = 0; i < 500; i++) {
			int steps=0;


			Prey prey = new Prey("prey", new Coordinate(5,5), null);

			Vector<Agent> worldState = new Vector<Agent>();
			worldState.add(prey);
			ArrayList<Coordinate> occupied = new ArrayList<Coordinate>();
			occupied.add(prey.position);
			
			for(int j =0;j<no;j++){
				
				addPredator(occupied,worldState);	
				
				}
			Environment env = new Environment();
			
			
			boolean flag = false;
			do{
			
			for (Agent p : worldState){
				if(p instanceof Predator){
					p.position=((Predator) p).chooseRandomAction();
					}
				else {
					((Prey) p).position = ((Prey) p).chooseRandomAction();

					
					}
			}
 
			if(env.checkCollision(worldState)){
				collisions++;
				flag=true;
				}
			if(env.checkCaught(worldState)){
				catches++;
				sumCatchSteps+=steps;
				flag=true;
				}
			
			steps++;
			}while(!flag);
			
		
			System.out.println(steps);
			
		}
		System.out.println("Collisions:  "+ collisions + "   Catches:   "+ catches);

		System.out.println("Average catch steps  :   " + sumCatchSteps/catches);
}

	
	
	
}