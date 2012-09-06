package myPack;

import java.util.Vector;

public class Environment {
	
	Vector<Agent> worldState = new Vector<Agent>();
	
	public Environment(){
		
	
	}
	
	public void addAgent(Agent x){
		
		worldState.add(x);
		
	}
	
    public boolean isDone(){
    	
    	for (int i = 0; i < worldState.size(); i++) {
    		if (worldState.get(i) instanceof Prey && worldState.get(i).isAlive()) {
    			return false;
    		}
    	}
		 	
		return true;
	}
	
    	
    
	public void run(){
			
		for (Agent a : worldState) {
			a = a.doAction(worldState);
		}
	
	}
	
	public String print() {
		String returnString = "";
		for (int i = 0; i < worldState.size(); i++) {
			returnString += worldState.get(i).print();
		}
		return returnString;
	}

}
