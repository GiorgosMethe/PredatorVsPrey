package agentsPack;

import java.util.Map;

import actionPack.RandomAction;
import environmentPack.Coordinate;

public class Prey extends Agent {

	public Prey(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		if (this.isDead()) {
			return this.position;
		}
		// TODO Auto-generated method stub
		Vector<RandomAction> actions = new Vector<RandomAction>();
		actions.addElement(new RandomAction(0.05, this.position.getNorth()));
		actions.addElement(new RandomAction(0.05, this.position.getEast()));
		actions.addElement(new RandomAction(0.05, this.position.getWest()));
		actions.addElement(new RandomAction(0.05, this.position.getSouth()));
		actions.addElement(new RandomAction(0.8, this.position));

		double prob = Math.random();
		double boundary = 0.0;
		Coordinate NewPosition = this.position;

		for (int i = 0; i < actions.size(); i++) {
			boundary += actions.get(i).prob;
			if (prob <= boundary) {
				if (this.safePosition(actions.get(i).coordinate, worldState)) {

					NewPosition = actions.get(i).coordinate;
					break;
				} else {
					this.doAction(worldState);
					break;
				}
			}
		}

		return NewPosition;

	}

	// this function is used to inform us if a possible coordinate is safe for
	// the prey
	@Override
	public boolean safePosition(Coordinate c, Vector<Agent> worldState) {

		for (int i = 0; i < worldState.size(); i++) {
			Agent a = worldState.elementAt(i);
			if (a instanceof Predator
					&& Coordinate.compareCoordinates(c, a.position)) {
				return false;
			} else if (a != this && a instanceof Prey
					&& Coordinate.compareCoordinates(c, a.position) && a.lives) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "Prey(\"" + this.name + "\", " + this.position + ")";
	}

	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		Agent predator = null;

		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			}
		}
		Coordinate c = Coordinate.difference(predator.position, this.position);
		return (c.getX() * 11) + c.getY();
	}

	@Override
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState,
			Coordinate action) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("No P function for prey");
	}

	@Override
	public Vector<Agent> typicalState(int stateIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the normal state space world.
	 */
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		Vector<RandomAction> maybeSafeActions = new Vector<RandomAction>();

		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getNorth()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getEast()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getWest()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getSouth()));

		// a new vector is created to get only the safe actions
		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		// sum of the probability of the safe actions
		for (int i = 0; i < maybeSafeActions.size(); i++) {
			if (this.safePosition(maybeSafeActions.elementAt(i).coordinate,
					worldState)) {
				safeActions.add(maybeSafeActions.elementAt(i));
			}
		}

		// the (1-sum) probability is lost due to unsafe moves. It will be
		// distributed uniformly to the safe actions
		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob += (1 - 0.8) / safeActions.size();
		}

		safeActions.addElement(new RandomAction(0.8, this.position));

		return safeActions;
	}

	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the small 6x6 state space world.
	 */
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {

		Vector<RandomAction> maybeSafeActions = new Vector<RandomAction>();
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getNorth()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getEast()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getWest()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getSouth()));

		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		// every action of the prey has to be returned as a static movement
		// e.g same position(0,0), move east (1,0)
		for (int i = 0; i < maybeSafeActions.size(); i++) {
			int realCoordx = maybeSafeActions.elementAt(i).coordinate.getX();
			int realCoordy = maybeSafeActions.elementAt(i).coordinate.getY();

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
			Coordinate RealCoordinate = new Coordinate(realCoordx, realCoordy);

			if (this.safePosition(RealCoordinate, worldState)) {
				safeActions.add(maybeSafeActions.elementAt(i));
			}
		}

		// the (1-sum) probability is lost due to unsafe moves. It will be
		// distributed uniformly to the safe actions
		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob += (1 - 0.8) / safeActions.size();
		}

		safeActions.addElement(new RandomAction(0.8, this.position));
		return safeActions;
	}

	/*
	 * This function return all possible actions that a prey or a predator can
	 * be able to do in relation to the current worldstate. This function is
	 * used for the little 21 state space world.
	 */
	public Vector<RandomAction> ProbabilityActionsRSW(Vector<Agent> worldState) {

		Vector<RandomAction> maybeSafeActions = new Vector<RandomAction>();

		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getNorth()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getEast()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getWest()));
		maybeSafeActions.addElement(new RandomAction(0.05, this.position
				.getSouth()));

		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		for (int i = 0; i < maybeSafeActions.size(); i++) {
			int realCoordx = maybeSafeActions.elementAt(i).coordinate.getX();
			int realCoordy = maybeSafeActions.elementAt(i).coordinate.getY();

			if (realCoordx == 6)
				realCoordx = 5;
			if (realCoordx == -1)
				realCoordx = 1;
			if (realCoordy == 6)
				realCoordy = 5;
			if (realCoordy == -1)
				realCoordy = 1;

			Coordinate RealCoordinate = new Coordinate(realCoordx, realCoordy);

			if (this.safePosition(RealCoordinate, worldState)) {
				safeActions.add(maybeSafeActions.elementAt(i));
			}
		}
		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob = (1 - 0.8) / safeActions.size();
		}

		safeActions.addElement(new RandomAction(0.8, this.position));

		System.out.println(safeActions);
		return safeActions;
	}

}
