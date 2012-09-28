package agentsPack;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;

import environmentPack.Coordinate;

public class QPredator extends Predator {
	
	protected Map<Vector<Agent>,Map<Coordinate, Double>> q;
	
	public QPredator(String name, Coordinate p, Policy pi, Map<Vector<Agent>,Map<Coordinate, Double>> qInitialization) {
		super(name, p, pi);
		if (qInitialization == null) {
			this.q = new DefaultHashMap<Vector<Agent>,Map<Coordinate, Double>>(new DefaultHashMap<Coordinate,Double>(Double.NEGATIVE_INFINITY));
		}
		else {
			this.q = qInitialization;
		}
		// TODO Auto-generated constructor stub
	}
	
	public Map<Coordinate, Double> possibleActionValues (Vector<Agent> worldState){
		return this.q.get(worldState);
	}
	
	
	@Override
	public Coordinate doAction(Vector<Agent> worldState) {
		// Choose an action according to this.pi, and learn on-line
		return null;
	}
	
	public void receiveReward(Double r) {
		// pass it to QLearning
	}
	
	@Override
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState, Coordinate action) {
		throw new UnsupportedOperationException("Sorry, you don't know the reward function. Receive one from the environment.");
	}
	
	@Override
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {
		throw new UnsupportedOperationException("Sorry, you should use ProbabilityActionsRSW(worldState).");
	}
	@Override
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		throw new UnsupportedOperationException("Sorry, we only use a really small world.");
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException("Sorry, we only use a really small world.");
	}
	public Map<Vector<Agent>,Map<Coordinate,Double>> initializeQ (){
		
		Map<Vector<Agent>, Map<Coordinate,Double>> q = new HashMap<Vector<Agent>,Map<Coordinate,Double>>();
		Prey prey = new Prey("prey", new Coordinate(0,0),null);
		for (int i=0;i<6;i++)
				for (int j=0;j<6;j++)
					if(i<=j){
						this.position.setX(i);
						this.position.setY(j);
						
						Vector<Agent> worldState = new Vector<Agent>();
						worldState.add(this);
						worldState.add(prey);
							for(Coordinate c : this.possibleActions(worldState)){
								
								Map<Coordinate,Double> actionValuePair = new HashMap<Coordinate,Double>();
								actionValuePair.put(c, 15.0);
								q.put(worldState,actionValuePair);	
							}
					
					}
					
				return q;
	}
}
