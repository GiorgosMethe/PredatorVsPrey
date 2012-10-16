package agentsPack;

import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import actionPack.MMStateActionPair;
import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class MMAgent extends Agent {

	private Vector<MMStateActionPair> qTable[];
	private Vector<StateActionPair> piTable[];
	private double vTable[];
	private double alpha;
	private double gamma;
	public Coordinate old;

	public MMAgent(String name, Coordinate p, Coordinate old, Policy pi,
			double alpha, double gamma) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
		this.old = old;

		// TODO Auto-generated constructor stub
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
	
	@SuppressWarnings("unchecked")
	public void initializeQtable(Vector<Agent> worldstate) {

		int QtableSize = (int) Math.pow(11, (2 * (worldstate.size())));
		this.qTable = (Vector<MMStateActionPair>[]) new Vector[QtableSize];
		this.piTable = (Vector<StateActionPair>[]) new Vector[QtableSize];
		this.vTable = new double[QtableSize];
		// find my position on the world state vector
		int mySelf = WhoAmI(worldstate);
		System.out.println("eimai to pred "+mySelf);
		for (int i = 0; i < this.qTable.length; i++) {
			// add my actions in this state
			// my position is given through the following:
			Coordinate MyState = IndexToMyPos(i, mySelf);
			Coordinate OtherAgentState;
			if(mySelf==0){
				OtherAgentState = IndexToMyPos(i, 1);
			}else{
				OtherAgentState = IndexToMyPos(i, 0);
			}
			//initialize pi table
			this.piTable[i] = new Vector<StateActionPair>();
			this.piTable[i].add(new StateActionPair(MyState.getEast(), 0.2, 1));
			this.piTable[i].add(new StateActionPair(MyState.getNorth(), 0.2, 2));
			this.piTable[i].add(new StateActionPair(MyState.getSouth(), 0.2, 3));
			this.piTable[i].add(new StateActionPair(MyState.getWest(), 0.2, 4));
			this.piTable[i].add(new StateActionPair(MyState, 0.2, 5));
			//initialize v table
			this.vTable[i] = new Double(1.0);
			//initialize q table
			Vector<StateActionPair> TempOtherAgentActions = new Vector<StateActionPair>();
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState.getEast(), 0.0, 1));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState.getNorth(), 0.0, 2));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState.getSouth(), 0.0, 3));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState.getWest(), 0.0, 4));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState, 0.0, 5));

			int j = 0;
			this.qTable[i] = new Vector<MMStateActionPair>();
			for(StateActionPair Aa : this.piTable[i]){
				for(StateActionPair oAa : TempOtherAgentActions){
					this.qTable[i].add(new MMStateActionPair(Aa, oAa, 1.0, j++));
				}
			}	
		}
	}
	
	public StateActionPair ChooseAction(Vector<Agent> worldState, double epsilon) {	
		boolean random = (Math.random() < epsilon);
		if(random){
			double rand = Math.random();
			double step = 1 / new Double(this.piTable[StateToIndex(worldState)].size());
			double counter = step;
			for (int ii = 0; ii < this.piTable[StateToIndex(worldState)].size(); ii++) {
				if (counter >= rand) {
					return this.piTable[StateToIndex(worldState)].elementAt(ii);
				}
				counter += step;
			}			
		}else{
			double rand = Math.random();
			double counter = new Double(this.piTable[StateToIndex(worldState)].elementAt(0).Value);
			for (int ii = 0; ii < this.piTable[StateToIndex(worldState)].size(); ii++) {
				if (counter >= rand) {
					return this.piTable[StateToIndex(worldState)].elementAt(ii);
				}
				counter += this.piTable[StateToIndex(worldState)].elementAt(ii+1).Value;
			}
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
			index += (((MMAgent) worldState.get(j)).old.getX())
					* Math.pow(11, power++)
					+ (((MMAgent) worldState.get(j)).old.getY())
					* Math.pow(11, power++);
		}
		return index;
	}
	
	public int WhoAmI(Vector<Agent> worldState) {
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
	
	public void UpdateMiniMax(Vector<Agent> worldState, StateActionPair myAction, StateActionPair otherAgentAction, double reward){
		
		int oldIndex = OldStateToIndex(worldState);
		for(int i=0;i<this.qTable[oldIndex].size();i++){
			if(this.qTable[oldIndex].elementAt(i).myAction.id == myAction.id){
				if(this.qTable[oldIndex].elementAt(i).otherAction.id == otherAgentAction.id){
					
				}
			}
		}
	}
}