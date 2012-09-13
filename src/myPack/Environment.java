package myPack;

import java.util.Vector;

public class Environment {

	Vector<Agent> worldState = new Vector<Agent>();

	public Environment() {

	}

	public void addAgent(Agent x) {

		worldState.add(x);

	}

	public boolean isDone() {

		for (int i = 0; i < worldState.size(); i++) {
			if (worldState.get(i) instanceof Prey
					&& worldState.get(i).isAlive()) {
				return false;
			}
		}

		return true;
	}

	public void run() {

		Action[] AgentActions = new Action[worldState.size()];
		int i = 0;
		for (Agent a : worldState) {
			AgentActions[i++] = new Action(a, a.doAction(worldState));
		}
		updateWorldState(AgentActions);

	}

	public void updateWorldState(Action[] SelectedActions) {

		for (int i = 0; i < SelectedActions.length; i++) {

			//checking the move of a predator
			if (SelectedActions[i].agent instanceof Predator) {

				for (int j = 0; j < SelectedActions.length; j++) {

					
					
					if (i!=j && SelectedActions[j].agent instanceof Prey) {

						//if there is a prey in the new position of the predator :(
						if (Coordinate.compareCoordinates(
								SelectedActions[j].NewPosition,
								SelectedActions[i].NewPosition)) {

							SelectedActions[j].agent.kill();

						}

					}else if (i!=j && SelectedActions[j].agent instanceof Predator) {
						
						
						//if two predators select the same position to move
						if (Coordinate.compareCoordinates(
								SelectedActions[i].NewPosition,
								SelectedActions[j].NewPosition)) {

							SelectedActions[i].NewPosition = SelectedActions[i].agent.position;
							SelectedActions[j].NewPosition = SelectedActions[j].agent.position;

						}
						
						
						
					}

				}

			//checking the move of a prey
			} else {
				
				//We checked the move of the prey in the safe position function in its class.

			}
			
			SelectedActions[i].agent.position = SelectedActions[i].NewPosition;

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
