package agentsPack;

import java.util.Map;
import java.util.Vector;

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

	/**
	 * Computes the estimated immediate reward of the Predator.
	 * 
	 * @param worldState
	 * @return
	 */
	protected Map<Integer, Double> reward(Vector<Agent> worldState) {
		return this.reward;
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

	@Override
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();
		actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		actions.addElement(new RandomAction(0.2, this.position.getEast()));
		actions.addElement(new RandomAction(0.2, this.position.getWest()));
		actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		actions.addElement(new RandomAction(0.2, this.position));

		for (int i = 0; i < actions.size(); i++) {
			if (!this.safePosition(actions.elementAt(i).coordinate, worldState)) {
				for (int j = 0; j < actions.size(); j++) {
					if (i != j) {
						actions.elementAt(j).prob += actions.elementAt(i).prob
								/ (actions.size() - 1);
					}
				}
			}
		}
		return actions;
	}
	
	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {

		Vector<RandomAction> actions = new Vector<RandomAction>();
		actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		actions.addElement(new RandomAction(0.2, this.position.getEast()));
		actions.addElement(new RandomAction(0.2, this.position.getWest()));
		actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		actions.addElement(new RandomAction(0.2, this.position));
		
		Vector<RandomAction> safeActions = new Vector<RandomAction>();

		double probSum = 0;
		for (int i = 0; i < actions.size(); i++) {
			if (Coordinate.CoordinateSW(actions.elementAt(i).coordinate)) {
			
				safeActions.add(actions.elementAt(i));
				probSum += actions.elementAt(i).prob;
			
			}
		}	
		for (int j = 0; j < safeActions.size(); j++) {
			safeActions.elementAt(j).prob += (1-probSum)/safeActions.size(); 
		}
		return safeActions;
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
