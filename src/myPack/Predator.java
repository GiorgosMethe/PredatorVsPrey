package myPack;

import java.util.Vector;

public class Predator extends Agent {

	public class RandomAction {
		public double prob;
		public Coordinate coordinate;

		public RandomAction(double p, Coordinate c) {
			this.prob = p;
			this.coordinate = c;
		}
	}

	public Predator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		// TODO Auto-generated constructor stub
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
