package agentsPack;

import java.util.Map;
import agentsPack.Vector;

import actionPack.RandomAction;
import environmentPack.Coordinate;

public abstract class Agent {

	public boolean lives;
	public String name;
	public Coordinate position;
	public Policy pi;

	// agents include a string id, a coordinate (x,y) and a policy
	public Agent(String name, Coordinate p, Policy pi) {
		super();
		this.lives = true;
		this.name = name;
		this.position = p;
		this.pi = pi;
	}

	// we are using this function to inform us if an agent is alive or not. It
	// is currently in use only for the preys
	public boolean isAlive() {
		return this.lives;
	}

	public boolean isDead() {
		return !this.lives;
	}

	// This is how we kill a prey
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
	 * Generates a typical state from a state index.
	 * 
	 * @param stateIndex
	 *            A value returned by @{stateIndex}.
	 * @return A state, that is, a vector of agents.
	 */
	public abstract Vector<Agent> typicalState(int stateIndex);

	/**
	 * The reward function. Corresponds to R_{s, s'}^{a}.
	 * 
	 */
	public abstract Double reward(Vector<Agent> currState,
			Vector<Agent> nextState, Coordinate action);

	public Double reward(int currState, int nextState, Coordinate action) {
		return this.reward(this.typicalState(currState),
				this.typicalState(nextState), action);
	}

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

	public Map<Integer, Double> functionP(int stateIndex) {
		return this.functionP(this.typicalState(stateIndex));
	}

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

	public Vector<Coordinate> possibleActions(int stateIndex) {
		return this.possibleActions(this.typicalState(stateIndex));
	}

	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the normal state space world.
	 */
	public abstract Vector<RandomAction> ProbabilityActions(
			Vector<Agent> worldState);

	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the small 6x6 state space world.
	 */
	public abstract Vector<RandomAction> ProbabilityActionsSW(
			Vector<Agent> worldState);

	/*
	 * This function return all possible actions that a prey or a predator can
	 * be able to do in relation to the current worldstate. This function is
	 * used for the little 21 state space world.
	 */
	public abstract Vector<RandomAction> ProbabilityActionsRSW(
			Vector<Agent> worldState);

	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Agent)) {
			return false;
		}
		Agent that = (Agent) o;
		
		return (that == null ? this == null : 
			(that.lives == this.lives)
				&& (that.name == null ? this.name == null : that.name.equals(this.name))
				&& (that.position == null ? this.position == null : that.position.equals(this.position))
				/*&& (that.pi == null ? this.pi == null : that.pi.equals(this.pi))*/);
	}
}
