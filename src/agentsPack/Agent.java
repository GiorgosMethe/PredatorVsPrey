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
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		Vector<RandomAction> actions = new Vector<RandomAction>();

		// if this is a prey
		if (this instanceof Prey) {
			actions.addElement(new RandomAction(0.05, this.position.getNorth()));
			actions.addElement(new RandomAction(0.05, this.position.getEast()));
			actions.addElement(new RandomAction(0.05, this.position.getWest()));
			actions.addElement(new RandomAction(0.05, this.position.getSouth()));
			actions.addElement(new RandomAction(0.8, this.position));

			// if this is a predator
		} else {

			actions.addElement(new RandomAction(0.2, this.position.getNorth()));
			actions.addElement(new RandomAction(0.2, this.position.getEast()));
			actions.addElement(new RandomAction(0.2, this.position.getWest()));
			actions.addElement(new RandomAction(0.2, this.position.getSouth()));
			actions.addElement(new RandomAction(0.2, this.position));
		}

		// a new vector is created to get only the safe actions
		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		// sum of the probability of the safe actions
		double probSum = 0;
		for (int i = 0; i < actions.size(); i++) {
			if (this.safePosition(actions.elementAt(i).coordinate, worldState)) {

				safeActions.add(actions.elementAt(i));
				probSum += actions.elementAt(i).prob;

			}
		}

		// the (1-sum) probability is lost due to unsafe moves. It will be
		// distributed uniformly to the safe actions
		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob += (1 - probSum) / safeActions.size();
		}
		return actions;

	}

	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the small 6x6 state space world.
	 */
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();

		if (this instanceof Prey) {

			actions.addElement(new RandomAction(0.05, this.position.getNorth()));
			actions.addElement(new RandomAction(0.05, this.position.getEast()));
			actions.addElement(new RandomAction(0.05, this.position.getWest()));
			actions.addElement(new RandomAction(0.05, this.position.getSouth()));
			actions.addElement(new RandomAction(0.8, this.position));

			Vector<RandomAction> safeActions = new Vector<RandomAction>();

			double probSum = 0;
			// every action of the prey has to be returned as a static movement
			// e.g same position(0,0), move east (1,0)
			for (int i = 0; i < actions.size(); i++) {
				int realCoordx = actions.elementAt(i).coordinate.getX();
				int realCoordy = actions.elementAt(i).coordinate.getY();

				// after this we have to check if each actions is safe
				// but with translated coordinates
				if (realCoordx == 6)
					realCoordx = 5;
				if (realCoordx == -1)
					realCoordx = 1;
				if (realCoordy == 6)
					realCoordy = 5;
				if (realCoordy == -1)
					realCoordy = 1;

				// this coordinate is the translated coordinate, with which we
				// can check for safety
				Coordinate RealCoordinate = new Coordinate(realCoordx,
						realCoordy);

				if (this.safePosition(RealCoordinate, worldState)) {

					safeActions.add(actions.elementAt(i));
					probSum += actions.elementAt(i).prob;

				}
			}

			// the (1-sum) probability is lost due to unsafe moves. It will be
			// distributed uniformly to the safe actions
			for (int j = 0; j < safeActions.size(); j++) {
				safeActions.elementAt(j).prob += (1 - probSum)
						/ safeActions.size();
			}
			return safeActions;

		} else {

			actions.addElement(new RandomAction(0.2, this.position.getNorth()));
			actions.addElement(new RandomAction(0.2, this.position.getEast()));
			actions.addElement(new RandomAction(0.2, this.position.getWest()));
			actions.addElement(new RandomAction(0.2, this.position.getSouth()));
			actions.addElement(new RandomAction(0.2, this.position));

			// for predator the above actions corresponds to the 11x11 grid
			// space. We have to convert them into coordinates which are in the
			// limits of the new 6x6 state space
			for (int i = 0; i < actions.size(); i++) {
				if (actions.elementAt(i).coordinate.getX() == 10)
					actions.elementAt(i).coordinate.setX(1);
				if (actions.elementAt(i).coordinate.getY() == 10)
					actions.elementAt(i).coordinate.setY(1);
				if (actions.elementAt(i).coordinate.getY() == 6)
					actions.elementAt(i).coordinate.setY(5);
				if (actions.elementAt(i).coordinate.getX() == 6)
					actions.elementAt(i).coordinate.setX(5);
			}

			Vector<RandomAction> safeActions = new Vector<RandomAction>();

			double probSum = 0;
			for (int i = 0; i < actions.size(); i++) {
				if (this.safePosition(actions.elementAt(i).coordinate,
						worldState)) {

					safeActions.add(actions.elementAt(i));
					probSum += actions.elementAt(i).prob;

				}
			}

			// the (1-sum) probability is lost due to unsafe moves. It will be
			// distributed uniformly to the safe actions
			for (int j = 0; j < safeActions.size(); j++) {
				safeActions.elementAt(j).prob += (1 - probSum)
						/ safeActions.size();
			}
			return safeActions;
		}

	}

	/*
	 * This function return all possible actions that a prey or a predator can
	 * be able to do in relation to the current worldstate. This function is
	 * used for the little 21 state space world.
	 */
	public Vector<RandomAction> ProbabilityActionsRSW(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();

		if (this instanceof Prey) {

			actions.addElement(new RandomAction(0.05, this.position.getNorth()));
			actions.addElement(new RandomAction(0.05, this.position.getEast()));
			actions.addElement(new RandomAction(0.05, this.position.getWest()));
			actions.addElement(new RandomAction(0.05, this.position.getSouth()));
			actions.addElement(new RandomAction(0.8, this.position));

			Vector<RandomAction> safeActions = new Vector<RandomAction>();

			double probSum = 0;
			for (int i = 0; i < actions.size(); i++) {
				int realCoordx = actions.elementAt(i).coordinate.getX();
				int realCoordy = actions.elementAt(i).coordinate.getY();

				if (realCoordx == 6)
					realCoordx = 5;
				if (realCoordx == -1)
					realCoordx = 1;
				if (realCoordy == 6)
					realCoordy = 5;
				if (realCoordy == -1)
					realCoordy = 1;

				Coordinate RealCoordinate = new Coordinate(realCoordx,
						realCoordy);

				if (this.safePosition(RealCoordinate, worldState)) {

					safeActions.add(actions.elementAt(i));
					probSum += actions.elementAt(i).prob;

				}
			}
			for (int j = 0; j < safeActions.size(); j++) {
				safeActions.elementAt(j).prob += (1 - probSum)
						/ safeActions.size();
			}
			return safeActions;

		} else {

			actions.addElement(new RandomAction(0.2, this.position.getNorth()));
			actions.addElement(new RandomAction(0.2, this.position.getEast()));
			actions.addElement(new RandomAction(0.2, this.position.getWest()));
			actions.addElement(new RandomAction(0.2, this.position.getSouth()));
			actions.addElement(new RandomAction(0.2, this.position));

			for (int i = 0; i < actions.size(); i++) {
				if (actions.elementAt(i).coordinate.getX() == 10)
					actions.elementAt(i).coordinate.setX(1);
				if (actions.elementAt(i).coordinate.getY() == 10)
					actions.elementAt(i).coordinate.setY(1);
				if (actions.elementAt(i).coordinate.getY() == 6)
					actions.elementAt(i).coordinate.setY(5);
				if (actions.elementAt(i).coordinate.getX() == 6)
					actions.elementAt(i).coordinate.setX(5);

				// for actions which results in coordinates in the lower
				// triangular matrix
				// we transpose this element to fit into the upper triangular
				// matrix we are using for the representation of the 21 states
				// space
				if (actions.elementAt(i).coordinate.getY() < actions
						.elementAt(i).coordinate.getX()) {
					int x = actions.elementAt(i).coordinate.getX();
					int y = actions.elementAt(i).coordinate.getY();
					actions.elementAt(i).coordinate.setX(y);
					actions.elementAt(i).coordinate.setY(x);
				}

			}

			Vector<RandomAction> safeActions = new Vector<RandomAction>();

			double probSum = 0;

			for (int i = 0; i < actions.size(); i++) {
				if (this.safePosition(actions.elementAt(i).coordinate,
						worldState)) {

					safeActions.add(actions.elementAt(i));
					probSum += actions.elementAt(i).prob;

				}
			}

			for (int j = 0; j < safeActions.size(); j++) {
				safeActions.elementAt(j).prob += (1 - probSum)
						/ safeActions.size();
			}
			return safeActions;
		}

	}

}
