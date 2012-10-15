package agentsPack;

import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class MQPredator extends Predator {

	private Vector<StateActionPair> qTable[];
	private double alpha;
	private double gamma;
	public Coordinate old;

	public MQPredator(String name, Coordinate p, Coordinate old, Policy pi,
			double alpha, double gamma) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
		this.old = old;

		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public void initializeQtable(Vector<Agent> worldstate) {

		int QtableSize = (int) Math.pow(11, (2 * (worldstate.size())));
		this.qTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		long start = System.currentTimeMillis();
		// find my position on the world state vector
		int mySelf = WhoAmI(worldstate);
		for (int i = 0; i < this.qTable.length; i++) {
			// add my actions in this state
			// my position is given through the following:
			Coordinate MyState = IndexToMyPos(i, mySelf);
			// given my position i know the coordinates of my actions
			// every action initialized
			this.qTable[i] = new Vector<StateActionPair>();
			this.qTable[i].add(new StateActionPair(MyState.getEast(), 15, 1));
			this.qTable[i].add(new StateActionPair(MyState.getNorth(), 15, 2));
			this.qTable[i].add(new StateActionPair(MyState.getSouth(), 15, 3));
			this.qTable[i].add(new StateActionPair(MyState.getWest(), 15, 4));
			this.qTable[i].add(new StateActionPair(MyState, 15, 5));
		}
		long end = System.currentTimeMillis();
		System.out.println("I am predator " + mySelf + " it took me "
				+ ((end - start))
				+ " ms to initialize my Qtable, Number of states: "
				+ this.qTable.length);
	}

	public StateActionPair chooseEGreedyAction(double epsilon,
			Vector<Agent> worldstate) {
		Double r = Math.random();
		Double maxValue = Double.NEGATIVE_INFINITY;
		Vector<StateActionPair> tempMaxVector = new Vector<StateActionPair>();
		int CountActions = 0;
		for (StateActionPair e : this.qTable[StateToIndex(worldstate)]) {
			if (e.Value >= maxValue) {
				if (e.Value == maxValue) {
					tempMaxVector.add(e);
				} else {
					tempMaxVector.removeAllElements();
					maxValue = e.Value;
					tempMaxVector.add(e);
				}
			}
			CountActions++;
		}
		double rand = Math.random();
		double step = 1 / tempMaxVector.size();
		double counter = step;
		int maxAct = 0;
		for (int ii = 0; ii < tempMaxVector.size(); ii++) {
			if (counter >= rand) {
				maxAct = ii;
				break;
			}
			counter += step;
		}

		if (r <= epsilon) {
			double step1 = epsilon / (CountActions - 1);
			double counter1 = epsilon - step1;
			for (StateActionPair e : this.qTable[StateToIndex(worldstate)]) {
				if (!Coordinate.compareCoordinates(e.Action,
						tempMaxVector.elementAt(maxAct).Action)) {
					if (counter1 <= r) {
						return e;
					}
					counter1 -= step1;
				}
			}

		}
		return tempMaxVector.elementAt(maxAct);
	}
	public int StateToIndex(Vector<Agent> worldState) {
		int index = 0;
		int power = 0;
		for (int j = 0; j < worldState.size(); j++) {
			index += (worldState.get(j).position.getX())
					* Math.pow(11, power++)
					+ (worldState.get(j).position.getY())
					* Math.pow(11, power++);
		}
		return index;
	}
	public int OldStateToIndex(Vector<Agent> worldState) {
		int index = 0;
		int power = 0;
		for (int j = 0; j < worldState.size(); j++) {
			index += (((MQPredator) worldState.get(j)).old.getX())
					* Math.pow(11, power++)
					+ (((MQPredator) worldState.get(j)).old.getY())
					* Math.pow(11, power++);
		}
		return index;
	}
	public int WhoAmI(Vector<Agent> worldState) {
		int jj = -1;
		int mySelf = 0;
		for (int j = 0; j < worldState.size(); j++) {
			if (worldState.elementAt(j) instanceof MQPredator)
				jj++;
			if (this == worldState.elementAt(j)) {
				mySelf = jj;
				break;
			}
		}
		return mySelf;
	}

	public Coordinate IndexToMyPos(int index, int mySelf) {
		mySelf *= 2;
		Coordinate MyState = new Coordinate(((index % (int) Math.pow(11,
				mySelf + 1)) / (int) Math.pow(11, mySelf)),
				((index % (int) Math.pow(11, mySelf + 2)) / (int) Math.pow(11,
						mySelf + 1)));
		return MyState;
	}

	public void updateQTable(Vector<Agent> worldState,
			StateActionPair[] actions, double reward, boolean absorbing) {

		int mySelf = WhoAmI(worldState);
		int OldStateIndex = OldStateToIndex(worldState);
		int NewStateIndex = StateToIndex(worldState);

		int actionPosId = -1;
		for (int i = 0; i < this.qTable[OldStateIndex].size(); i++) {
			if (this.qTable[OldStateIndex].elementAt(i).id == actions[mySelf].id) {
				actionPosId = i;
				break;
			}
		}

		if (!absorbing) {

			double actionMaxValue = Double.NEGATIVE_INFINITY;
			for (StateActionPair e : this.qTable[NewStateIndex]) {
				if (e.Value > actionMaxValue) {
					actionMaxValue = e.Value;
				}
			}

			this.qTable[OldStateIndex].elementAt(actionPosId).Value = this.qTable[OldStateIndex]
					.elementAt(actionPosId).Value
					+ (alpha * (reward + (gamma * actionMaxValue) - this.qTable[OldStateIndex]
							.elementAt(actionPosId).Value));

		} else {

			this.qTable[OldStateIndex].elementAt(actionPosId).Value = this.qTable[OldStateIndex]
					.elementAt(actionPosId).Value
					+ alpha
					* (reward - this.qTable[OldStateIndex]
							.elementAt(actionPosId).Value);

		}
	}

	public int UnvisitedStateActions() {
		int counter = 0;
		for (int i = 0; i < this.qTable.length; i++) {
			for (StateActionPair a : this.qTable[i])
				if (a.Value == 15.0)
					counter++;
		}
		return counter;
	}
}
