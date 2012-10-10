package agentsPack;

import java.util.Map;

import actionPack.RandomAction;
import environmentPack.Coordinate;

public class Predator extends Agent {

	protected Map<Integer, Double> reward;

	public Predator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		this.reward = new DefaultHashMap<Integer, Double>(0.0);
		this.reward.put(0, 0.8 * 10);
		for (int i = 1; i < 11 * 11; i++) {
			this.reward.put(i, 0.0);
		}
	}

	/**
	 * Converts the state (position of 2 agents) to an integer representing the
	 * difference vector of positions.
	 * 
	 * @param worldState
	 * @return
	 */
	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		Agent prey = null;

		for (Agent a : worldState) {
			if (a instanceof Prey) {
				prey = a;
			}
		}
		Coordinate c = Coordinate.difference(this.position, prey.position);
		return (c.getX() * 11) + c.getY();
	}

	public Vector<Agent> typicalState(int stateIndex) {
		Vector<Agent> state = new Vector<Agent>();
		state.add(new Predator("typical predator", new Coordinate(0, 0), null));
		state.add(new Prey("typical prey", new Coordinate(stateIndex / 11,
				stateIndex % 11), null));
		return state;
	}

	/**
	 * Computes the estimated immediate reward of the Predator.
	 * 
	 * @param worldState
	 * @return
	 */
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState,
			Coordinate action) {
		return this.reward.get(this.stateIndex(nextState));
	}

	/**
	 * Computes the state transition probability of the Predator, given the
	 * Prey's current position.
	 * 
	 * @param worldState
	 * @return A table of state indices, and probabilities.
	 */
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {

		Map<Integer, Double> p = new DefaultHashMap<Integer, Double>(0.0);
		Vector<Vector<Agent>> possibleWorlds = new Vector<Vector<Agent>>();

		Agent prey = null;
		Agent predator = null;
		for (Agent a : worldState) {
			if (a instanceof Predator) {
				predator = a;
			} else if (a instanceof Prey) {
				prey = a;
			}
		}

		// Prey does not move.
		p.put(this.stateIndex(worldState), 0.8);

		// Can prey move south?
		if (prey.safePosition(prey.position.getSouth(), worldState)) {
			prey.position = prey.position.getSouth();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move west?
		if (prey.safePosition(prey.position.getWest(), worldState)) {
			prey.position = prey.position.getWest();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move north?
		if (prey.safePosition(prey.position.getNorth(), worldState)) {
			prey.position = prey.position.getNorth();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}
		// Can prey move east?
		if (prey.safePosition(prey.position.getEast(), worldState)) {
			prey.position = prey.position.getEast();
			Vector<Agent> nextWorld = new Vector<Agent>();
			nextWorld.add(predator);
			nextWorld.add(prey);
			possibleWorlds.add(nextWorld);
		}

		double prob = 0.2 / possibleWorlds.size();

		for (Vector<Agent> possibleWorld : possibleWorlds) {
			p.put(this.stateIndex(possibleWorld), prob);
		}

		return p;
	}

	public Coordinate chooseRandomAction(){
		

		Vector<RandomAction> actions = new Vector<RandomAction>();
		actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		actions.addElement(new RandomAction(0.2, this.position.getEast()));
		actions.addElement(new RandomAction(0.2, this.position.getWest()));
		actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		actions.addElement(new RandomAction(0.2, this.position));

		
		double prob = Math.random();
		double step = 0.2;
		Coordinate newPosition = null;
		
		for (int i=0; i<actions.size();i++){
			
			if(prob<=step){
				newPosition=actions.get(i).getCoordinate();	
				break;
				}
				else 
				step += 0.2;
			}
		return newPosition;
		
	}
	
	
	@Override
	public Coordinate doAction(Vector<Agent> worldState) {

		// TODO Auto-generated method stub
		Vector<RandomAction> actions = new Vector<RandomAction>();
		actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		actions.addElement(new RandomAction(0.2, this.position.getEast()));
		actions.addElement(new RandomAction(0.2, this.position.getWest()));
		actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		actions.addElement(new RandomAction(0.2, this.position));


		double prob = Math.random();
		double boundary = 0.0;
		Coordinate NewPosition = this.position;

		for (int i = 0; i < actions.size(); i++) {

			boundary += actions.elementAt(i).prob;
			if (prob <= boundary) {
				if (this.safePosition(actions.elementAt(i).coordinate,
						worldState)) {

					NewPosition = actions.elementAt(i).coordinate;
					break;

				} else {

					this.doAction(worldState);
					break;
				}
			}
		}

		return NewPosition;
	}

	
	
	
	/*
	 * This function return all possible action that a prey or a predator can be
	 * able to do in relation to the current worldstate. This function is used
	 * for the normal state space world.
	 */
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		Vector<RandomAction> actions = new Vector<RandomAction>();

		actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		actions.addElement(new RandomAction(0.2, this.position.getEast()));
		actions.addElement(new RandomAction(0.2, this.position.getWest()));
		actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		actions.addElement(new RandomAction(0.2, this.position));

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

		return safeActions;

	}

	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();
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
		return safeActions;
	}

	/*
	 * This function return all possible actions that a prey or a predator can
	 * be able to do in relation to the current worldstate. This function is
	 * used for the little 21 state space world.
	 */
	public Vector<RandomAction> ProbabilityActionsRSW(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();

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
			if (actions.elementAt(i).coordinate.getY() > actions.elementAt(i).coordinate
					.getX()) {
				int x = actions.elementAt(i).coordinate.getX();
				int y = actions.elementAt(i).coordinate.getY();
				actions.elementAt(i).coordinate.setX(y);
				actions.elementAt(i).coordinate.setY(x);
			}

		}

		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		double probSum = 0;

		for (int i = 0; i < actions.size(); i++) {
			if (this.safePosition(actions.elementAt(i).coordinate, worldState)) {

				safeActions.add(actions.elementAt(i));
				probSum += actions.elementAt(i).prob;

			}
		}

		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob += (1 - probSum) / safeActions.size();
		}
		return safeActions;
	}

	// this function is used to inform us if a possible coordinate is safe for
	// the predator
	@Override
	public boolean safePosition(Coordinate c, Vector<Agent> worldState) {

		for (int i = 0; i < worldState.size(); i++) {
			Agent a = worldState.elementAt(i);
			if (a != this && a instanceof Predator
					&& Coordinate.compareCoordinates(c, a.position)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "Predator(\"" + this.name + "\", " + this.position + ")";
	}

}
