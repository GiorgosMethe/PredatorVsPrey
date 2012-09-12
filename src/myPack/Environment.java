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
			
		Action[] AgentActions = new Action[worldState.size()];
		int i=0;
		for (Agent a : worldState) {
			AgentActions[i++] = new Action(a,a.doAction(worldState));
		}
		updateWorldState(AgentActions);
		
	
	}
	
	public void updateWorldState(Action[] SelectedActions){
		
		for(int i=0;i<SelectedActions.length;i++){
				
				if(SelectedActions[i].agent instanceof Predator){
	
					SelectedActions[i].agent.position = SelectedActions[i].NewPosition;
					
					for(int j=0;j<SelectedActions.length;j++){
						
						if(SelectedActions[j].agent instanceof Prey){
							
							if(Coordinate.compareCoordinates(SelectedActions[j].NewPosition,
									SelectedActions[i].NewPosition)){
								
								SelectedActions[j].agent.kill();
								
							}
							
						}
						
					}
					
				}else{

					SelectedActions[i].agent.position = SelectedActions[i].NewPosition;
				}
				
			
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
