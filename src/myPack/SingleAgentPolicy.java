package myPack;

import java.util.AbstractMap.SimpleEntry;
import java.util.Vector;

import javax.xml.ws.Action;

public class SingleAgentPolicy extends Policy {

	@Override
	public void generateV() {
		// TODO Auto-generated method stub

	}

	@Override
	public int stateIndex(Agent me, Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		Agent predator = null;
		Agent prey = null;
		
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			}
			else if (a instanceof Prey) {
				prey = a;
			}
		}
		Coordinate c = Coordinate.difference(predator.position, prey.position);
		return (c.getX() * 11) + c.getY();
	}

	@Override
	public Vector<SimpleEntry<Action, Double>> getActions(Agent me,
			Vector<Agent> worldState, Vector<Action> possibleActions) {
		
		
		return null;
		
	}

}
