package agentsPack;

import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;
import environmentPack.Coordinate;

public abstract class Agent {

	public boolean lives;
	public String name;
	public Coordinate position;
	public Policy pi;

	public Agent(String name, Coordinate p, Policy pi) {
		super();
		this.lives = true;
		this.name = name;
		this.position = p;
		this.pi = pi;
	}

	public boolean isAlive() {
		return this.lives;
	}

	public boolean isDead() {
		return !this.lives;
	}

	public void kill() {
		this.lives = false;
	}

	public void setPolicy(Policy pi) {
		this.pi = pi;
	}

	public Policy getPolicy() {
		return this.pi;
	}

	/**
	 * Assign an integer 0 <= i < 11^2 to a state.
	 * 
	 * @param worldState
	 *            Contains only one predator and one prey.
	 * @return The integer is computed as dotprod(((prey.position -
	 *         predator.position) + <10,10> % <11,11>), <11, 1>)
	 */
	public abstract Integer stateIndex(Vector<Agent> worldState);

	/**
	 * The reward function. Corresponds to R_{s, s'}^{a}.
	 * 
	 */
	protected abstract Map<Integer, Double> reward(Vector<Agent> worldState);

	/**
	 * The probabilistic transition function, given the intermediate state,
	 * right after doing the action.
	 * 
	 * This corresponds to P_{s, s'}^{a} with s and a fixed.
	 * 
	 * @param worldState
	 *            The intermediate state (after the "current" world state, when
	 *            you have taken an action, but before the "next" world state,
	 *            when the prey has moved).
	 * @return A mapping from an integer describing the possible next state s',
	 *         and the probability that it will occur.
	 */
	public abstract Map<Integer, Double> functionP(Vector<Agent> worldState);

	/**
	 * Return the next position after your chosen action.
	 * 
	 */
	public abstract Coordinate doAction(Vector<Agent> worldState);

	/**
	 * Is it safe for this agent to move to a new spot c?
	 * 
	 */
	public abstract boolean safePosition(Coordinate c, Vector<Agent> worldState);
	
	/**
	 * Computes a list of possible next positions, given the current state.
	 * 
	 */
	public Vector<Coordinate> possibleActions(Vector<Agent> worldState) {

		Vector<Coordinate> PossiblePosition = new Vector<Coordinate>();
		PossiblePosition.add(this.position.getNorth());
		PossiblePosition.add(this.position.getEast());
		PossiblePosition.add(this.position.getWest());
		PossiblePosition.add(this.position.getSouth());
		PossiblePosition.add(this.position);

		for (int i = 0; i < PossiblePosition.size(); i++) {
			if (!this.safePosition(PossiblePosition.elementAt(i), worldState)) {
				PossiblePosition.removeElementAt(i);
			}
		}
		return PossiblePosition;
	}
	

	public abstract Vector<RandomAction> ProbabilityActions(
			Vector<Agent> worldState);
	
	public abstract Vector<RandomAction> ProbabilityActionsSW(
			Vector<Agent> worldState);

}
