package agentsPack;
import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;

import environmentPack.Coordinate;

public class QPredator extends Predator {
	
	protected Map<Vector<Agent>,Map< Coordinate, Double>> q;
	
	public QPredator(String name, Coordinate p, Policy pi, Map<Vector<Agent>,Map<Coordinate, Double>> qInitialization) {
		super(name, p, pi);
		if (qInitialization == null) {
			this.q = new DefaultHashMap<Vector<Agent>,Map<Coordinate, Double>>(new DefaultHashMap<Coordinate, Double>(Double.NEGATIVE_INFINITY));
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
}
