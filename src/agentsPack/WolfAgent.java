package agentsPack;

import java.util.Map;

import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class WolfAgent extends Agent {

	private Vector<StateActionPair> qTable[];
	private long cTable[];
	private Vector<StateActionPair> piTable[];
	private Vector<StateActionPair> piaTable[];
	private double alpha;
	private double gamma;
	private double deltaLose, deltaWin, delta;
	public Coordinate old;

	public WolfAgent(String name, Coordinate p, Coordinate old, Policy pi,
			double alpha, double gamma, double deltaLose, double deltaWin) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
		this.old = old;
		this.deltaWin = deltaWin;
		this.deltaLose = deltaLose;

		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public void initialize(Vector<Agent> worldstate) {

		this.delta = this.deltaLose;
		int QtableSize = (int) Math.pow(11, (2 * (worldstate.size())));
		this.qTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		this.cTable = (long[]) new long[QtableSize];
		this.piTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		this.piaTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		int mySelf = WhoAmI(worldstate);
		System.out.println(mySelf);

		for (int i = 0; i < this.qTable.length; i++) {
			Coordinate MyState = IndexToMyPos(i, mySelf);
			this.qTable[i] = new Vector<StateActionPair>();
			this.qTable[i].add(new StateActionPair(MyState.getEast(), 15, 1));
			this.qTable[i].add(new StateActionPair(MyState.getNorth(), 15, 2));
			this.qTable[i].add(new StateActionPair(MyState.getSouth(), 15, 3));
			this.qTable[i].add(new StateActionPair(MyState.getWest(), 15, 4));
			this.qTable[i].add(new StateActionPair(MyState, 15, 5));

			this.piTable[i] = new Vector<StateActionPair>();
			this.piTable[i].add(new StateActionPair(MyState.getEast(), 0.2, 1));
			this.piTable[i]
					.add(new StateActionPair(MyState.getNorth(), 0.2, 2));
			this.piTable[i]
					.add(new StateActionPair(MyState.getSouth(), 0.2, 3));
			this.piTable[i].add(new StateActionPair(MyState.getWest(), 0.2, 4));
			this.piTable[i].add(new StateActionPair(MyState, 0.2, 5));

			this.piaTable[i] = new Vector<StateActionPair>();
			this.piaTable[i]
					.add(new StateActionPair(MyState.getEast(), 0.2, 1));
			this.piaTable[i]
					.add(new StateActionPair(MyState.getNorth(), 0.2, 2));
			this.piaTable[i]
					.add(new StateActionPair(MyState.getSouth(), 0.2, 3));
			this.piaTable[i]
					.add(new StateActionPair(MyState.getWest(), 0.2, 4));
			this.piaTable[i].add(new StateActionPair(MyState, 0.2, 5));

			this.cTable[i] = 0;

		}
	}

	public StateActionPair chooseAction(Vector<Agent> worldstate) {
		double rand = Math.random();
		double counter = new Double(
				this.piTable[StateToIndex(worldstate)].elementAt(0).Value);
		for (int ii = 0; ii < this.piTable[StateToIndex(worldstate)].size(); ii++) {
			if (counter >= rand) {
				return this.piTable[StateToIndex(worldstate)].elementAt(ii);
			}
			if(ii > 3){
				for (int jj = 0; jj < this.piTable[StateToIndex(worldstate)].size(); jj++) {
					System.out.println("fuck" + this.piTable[StateToIndex(worldstate)].elementAt(jj).Value);
				}
			}
			counter += this.piTable[StateToIndex(worldstate)].elementAt(ii + 1).Value;
		}
		return null;
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
			index += (((WolfAgent) worldState.get(j)).old.getX())
					* Math.pow(11, power++)
					+ (((WolfAgent) worldState.get(j)).old.getY())
					* Math.pow(11, power++);
		}
		return index;
	}

	public int WhoAmI(Vector<Agent> worldState) {
		int jj = -1;
		int mySelf = 0;
		for (int j = 0; j < worldState.size(); j++) {
			if (this == worldState.elementAt(j)) {
				mySelf = j;
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

	public void updateWolf(Vector<Agent> worldState,
			StateActionPair predAction, double reward, boolean absorbing) {

		// index for last state
		int oldState = OldStateToIndex(worldState);
		// index for current state
		int newState = StateToIndex(worldState);
		// find the max value over all action to the current state
		int ActID = -1;
		for (int i = 0; i < this.qTable[oldState].size(); i++) {
			if (this.qTable[oldState].elementAt(i).id == predAction.id) {
				ActID = i;
				break;
			}
		}
		// find the max value over all action to the current state
		int maxActID = -1;
		double maxValue = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < this.qTable[newState].size(); i++) {
			if (this.qTable[newState].elementAt(i).Value > maxValue) {
				maxValue = this.qTable[newState].elementAt(i).Value;
				maxActID = i;
			}
		}
		// update value for the q table
		if (!absorbing) {
			this.qTable[oldState].elementAt(ActID).Value = (1 - this.alpha)
					* this.qTable[oldState].elementAt(ActID).Value
					+ this.alpha
					* (reward + this.gamma
							* this.qTable[newState].elementAt(maxActID).Value);
		} else {
			this.qTable[oldState].elementAt(ActID).Value = (1 - this.alpha)
					* this.qTable[oldState].elementAt(ActID).Value + this.alpha
					* reward;
		}
		// update estimate of avg. policy
		this.cTable[oldState]++;
		for (int i = 0; i < this.piaTable[oldState].size(); i++) {
			this.piaTable[oldState].elementAt(i).Value = this.piaTable[oldState]
					.elementAt(i).Value
					+ 1
					/ this.cTable[oldState]
					* (this.piTable[oldState].elementAt(i).Value - this.piaTable[oldState]
							.elementAt(i).Value);
		}
		// update policy
		boolean optAction = true;
		for (StateActionPair a : this.qTable[oldState]) {
			if (this.qTable[oldState].elementAt(ActID).Value < a.Value) {
				optAction = false;
				break;
			}
		}
		if (optAction) {
			this.piTable[oldState].elementAt(ActID).Value += this.delta;
		} else {
			this.piTable[oldState].elementAt(ActID).Value -= this.delta
					/ (this.piTable[oldState].size() - 1);
		}
		if(this.piTable[oldState].elementAt(ActID).Value < 0.0){
			this.piTable[oldState].elementAt(ActID).Value = 0;
		}
		
		// sum of the probabilities must sum up to one
		double sum = 0.0;
		for (StateActionPair a : this.piTable[oldState]) {
			sum += a.Value;
		}
		for (StateActionPair a : this.piTable[oldState]) {
			a.Value /= sum;
		}
		// which delta should i use in the next step?
		double sumAvg = 0.0;
		double sumCur = 0.0;
		for(int i=0;i<this.qTable[oldState].size();i++){
			sumAvg += this.piaTable[oldState].elementAt(i).Value * this.qTable[oldState].elementAt(i).Value;
			sumCur += this.piTable[oldState].elementAt(i).Value * this.qTable[oldState].elementAt(i).Value;
		}
		if(sumCur > sumAvg){
			this.delta = this.deltaWin;
		}else{
			this.delta = this.deltaLose;
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

	@Override
	public Integer stateIndex(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Agent> typicalState(int stateIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState,
			Coordinate action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean safePosition(Coordinate c, Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsRSW(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		return null;
	}
}
