package environmentPack;

import java.util.Vector;

import agentsPack.Agent;
import agentsPack.Pair;

public class SARSdata {

	public Vector<Agent> currentState;
	public Coordinate action;
	public Vector<Agent> nextState;
	public Double reward;
	private boolean isAbsorbingState;
	
	public SARSdata(Vector<Agent> currentState, Coordinate action, Vector<Agent> nextState, Double reward) {
		this(currentState, action, nextState, reward, (currentState.size() <= 1));
	}
	
	public SARSdata(Vector<Agent> currentState, Coordinate action, Vector<Agent> nextState, Double reward, boolean isAbsorbingState) {
		this.currentState = currentState;
		this.action = action;
		this.nextState = nextState;
		this.reward = reward;
		this.isAbsorbingState = isAbsorbingState;
	}
	
	public boolean isAbsorbingState() {
		return isAbsorbingState;
	}
	
	public Pair<Vector<Agent>, Coordinate> getStateAction() {
		return new Pair<Vector<Agent>, Coordinate>(this.currentState, this.action);
	}

}
