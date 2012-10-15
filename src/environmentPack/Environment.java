package environmentPack;

import actionPack.Action;
import agentsPack.Agent;
import agentsPack.Predator;
import agentsPack.Prey;
import agentsPack.Vector;

public class Environment {

	// Class environment holds a vector with the agents.
	// this is our worldstate!
	public Vector<Agent> worldState = new Vector<Agent>();

	public Environment() {

	}

	// an agent is going to be added into the environment
	public void addAgent(Agent x) {

		worldState.add(x);

	}

	// this function inform us about the state of the worldstate
	// if predator(s) catch the prey(s)
	public boolean isDone() {

		for (int i = 0; i < worldState.size(); i++) {
			if (worldState.get(i) instanceof Prey
					&& worldState.get(i).isAlive()) {
				return false;
			}
		}

		return true;
	}

	// main function of the environment
	// this function calls all the agents to think about their next actions
	public void run() {

		Action[] AgentActions = new Action[worldState.size()];
		int i = 0;
		for (Agent a : worldState) {
			AgentActions[i++] = new Action(a, a.doAction(worldState));
		}
		updateWorldState(AgentActions);

	}

	// After the thinking process of the agents we have to update the worldstate
	// in respect to their desired actions
	public void updateWorldState(Action[] SelectedActions) {

		for (int i = 0; i < SelectedActions.length; i++) {

			// checking the move of a predator
			if (SelectedActions[i].agent instanceof Predator) {

				for (int j = 0; j < SelectedActions.length; j++) {

					if (i != j && SelectedActions[j].agent instanceof Prey) {

						// if there is a prey in the new position of the
						// predator :(
						if (Coordinate.compareCoordinates(
								SelectedActions[j].NewPosition,
								SelectedActions[i].NewPosition)) {

							SelectedActions[j].agent.kill();

						}

					} else if (i != j
							&& SelectedActions[j].agent instanceof Predator) {

						// if two predators select the same position to move
						if (Coordinate.compareCoordinates(
								SelectedActions[i].NewPosition,
								SelectedActions[j].NewPosition)) {

							SelectedActions[i].NewPosition = SelectedActions[i].agent.position;
							SelectedActions[j].NewPosition = SelectedActions[j].agent.position;

						}

					}

				}

				// checking the move of a prey
			} else {

				// We checked the move of the prey in the safe position function
				// in its class.

			}

			SelectedActions[i].agent.position = SelectedActions[i].NewPosition;

		}

	}

	public String toString() {
		return this.worldState.toString();
	}

	public boolean checkCollision(Vector<Agent> worldState) {

		for (int i = 0; i < worldState.size(); i++) {

			if (worldState.get(i) instanceof Predator) {

				for (int j = i + 1; j < worldState.size(); j++) {

					if (worldState.get(j) instanceof Predator
							&& worldState.get(i).position.equals(worldState
									.get(j).position))
						return true;

				}

			}

		}

		return false;
	}

	public boolean checkCaughtStaticPrey(Vector<Agent> worldState,
			Coordinate PreyPos) {
		for (Agent a : worldState) {
			if (Coordinate.compareCoordinates(PreyPos, a.position)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkCaught(Vector<Agent> worldState) {

		for (int i = 0; i < worldState.size(); i++) {

			if (worldState.get(i) instanceof Prey) {

				for (int j = 0; j < worldState.size(); j++) {

					if (worldState.get(i).position
							.equals(worldState.get(j).position)
							&& worldState.get(j) instanceof Predator)
						return true;

				}

			}

		}

		return false;

	}

}
