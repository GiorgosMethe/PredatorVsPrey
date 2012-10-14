package agentsPack;

import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class QPreyM extends Prey {

	private Vector<StateActionPair> qTable[];
	private double alpha;
	private double gamma;

	public QPreyM(String name, Coordinate p, Policy pi, double alpha,
			double gamma) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
		// TODO Auto-generated constructor stub
	}
	
	public void initializeQtable(Vector<Agent> worldstate) {

		int QtableSize = (int) Math.pow(11, (2 * (worldstate.size()-1)));
		this.qTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		long start = System.currentTimeMillis();
		int mySelf = 0;
		for (int i = 0; i < this.qTable.length; i++) {

			//add my actions in this state
			//my position is given through the following:
			Coordinate MyState = new Coordinate(5, 5);
			//given my position i know the coordinates of my actions
			//every action initialized
			this.qTable[i] = new Vector<StateActionPair>();
			this.qTable[i].add(new StateActionPair(MyState.getEast(), 15, 1));
			this.qTable[i].add(new StateActionPair(MyState.getNorth(), 15, 2));
			this.qTable[i].add(new StateActionPair(MyState.getSouth(), 15, 3));
			this.qTable[i].add(new StateActionPair(MyState.getWest(), 15, 4));
			this.qTable[i].add(new StateActionPair(MyState, 15, 5));
		}
		long end = System.currentTimeMillis();
		System.out.println("I am agent "+mySelf+" it took me "+((end-start)/1000)+" sec to initialize my Qtable");
	}
	public StateActionPair chooseEGreedyAction(double epsilon,Vector<Agent> worldstate) {

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
	public int StateToIndex(Vector<Agent> worldState){
		int index = -1;
		for (int j = 0; j < worldState.size(); j++) {
			if(worldState.get(j) instanceof QPredatorM)
				index += Math.pow(worldState.get(j).position.getX()+1,j) + 
				Math.pow(worldState.get(j).position.getX()+1,j+1);
		}
		return index;
	}





}
