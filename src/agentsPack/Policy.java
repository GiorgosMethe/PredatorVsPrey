package agentsPack;

import java.util.Vector;

import environmentPack.Coordinate;

public interface Policy {

	public abstract Coordinate chooseAction(Vector<Agent> worldState,
			QTable qTable);

}
