package agentsPack;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import environmentPack.Coordinate;

public class ArbitraryPolicy extends Policy {

	protected Map<Vector<Agent>, Coordinate> arbitraryActions;

	// Arbitrary = every time you look up the action for s, you pick the same
	// random action
	public ArbitraryPolicy() {
		this.arbitraryActions = new HashMap<Vector<Agent>, Coordinate>();
	}

	@Override
	public Coordinate chooseAction(Vector<Agent> worldState, QTable qTable) {
		if (!this.arbitraryActions.containsKey(worldState)) {
			Predator pred = null;
			for (Agent a : worldState) {
				if (a instanceof Predator) {
					pred = (Predator) a;
				}
			}
			if (pred == null) {
				throw new NullPointerException("No Predator in the worldState?");
			}
			Vector<Coordinate> possibleActions = pred
					.possibleActions(worldState);
			this.arbitraryActions.put(worldState, possibleActions
					.get((int) (Math.random() * possibleActions.size())));
		}
		return this.arbitraryActions.get(worldState);
	}

}
