package myPack;

import java.util.Vector;

public class PolicyPredator extends Predator {

	public PolicyPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		return this.pi.getOptimalAction(this, worldState, this.possibleActions(worldState));
	}

}
