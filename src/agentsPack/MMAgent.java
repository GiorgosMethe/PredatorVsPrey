package agentsPack;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import actionPack.MMStateActionPair;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

@SuppressWarnings("deprecation")
public class MMAgent extends Predator {

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

	@SuppressWarnings("unchecked")
	public void initializeqTable(Vector<Agent> worldstate) {

		int qTableSize = (int) Math.pow(11, (2 * (worldstate.size())));
		this.qTable = (Vector<MMStateActionPair>[]) new Vector[qTableSize];
		this.piTable = (Vector<StateActionPair>[]) new Vector[qTableSize];
		this.vTable = new double[qTableSize];
		// find my position on the world state vector
		int mySelf = WhoAmI(worldstate);
		for (int i = 0; i < this.qTable.length; i++) {
			// add my actions in this state
			// my position is given through the following:
			Coordinate MyState = IndexToMyPos(i, mySelf);
			Coordinate OtherAgentState;
			OtherAgentState = IndexToMyPos(i, 1 - mySelf);
			// initialize pi table
			this.piTable[i] = new Vector<StateActionPair>();
			this.piTable[i].add(new StateActionPair(MyState.getEast(), 0.2, 1));
			this.piTable[i]
					.add(new StateActionPair(MyState.getNorth(), 0.2, 2));
			this.piTable[i]
					.add(new StateActionPair(MyState.getSouth(), 0.2, 3));
			this.piTable[i].add(new StateActionPair(MyState.getWest(), 0.2, 4));
			this.piTable[i].add(new StateActionPair(MyState, 0.2, 5));
			// initialize v table
			this.vTable[i] = new Double(1.0);
			// initialize q table
			Vector<StateActionPair> TempOtherAgentActions = new Vector<StateActionPair>();
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState
					.getEast(), 0.0, 1));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState
					.getNorth(), 0.0, 2));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState
					.getSouth(), 0.0, 3));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState
					.getWest(), 0.0, 4));
			TempOtherAgentActions.add(new StateActionPair(OtherAgentState, 0.0,
					5));

			int j = 0;
			this.qTable[i] = new Vector<MMStateActionPair>();
			for (StateActionPair Aa : this.piTable[i]) {
				for (StateActionPair oAa : TempOtherAgentActions) {
					this.qTable[i]
							.add(new MMStateActionPair(Aa, oAa, 1.0, j++));
				}
			}
		}
	}

	public StateActionPair ChooseAction(Vector<Agent> worldState, double epsilon) {

		boolean random = (Math.random() < epsilon);
		if (random) {
			double rand = Math.random();
			double step = 0.2;
			double counter = step;
			for (int ii = 0; ii < this.piTable[StateToIndex(worldState)].size(); ii++) {
				if (counter >= rand) {
					return this.piTable[StateToIndex(worldState)].elementAt(ii);
				}
				counter += step;
			}
		} else {
			double rand = Math.random();
			double counter = new Double(
					this.piTable[StateToIndex(worldState)].elementAt(0).Value);
			for (int ii = 0; ii < this.piTable[StateToIndex(worldState)].size(); ii++) {
				if (counter >= rand) {
					return this.piTable[StateToIndex(worldState)].elementAt(ii);
				}
				counter += this.piTable[StateToIndex(worldState)]
						.elementAt(ii + 1).Value;
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

	public void UpdateMiniMax(Vector<Agent> worldState,
			StateActionPair myAction, StateActionPair otherAgentAction,
			double reward, boolean b) {
		int oldStateIndex = OldStateToIndex(worldState);
		int newStateIndex = StateToIndex(worldState);
		int actionsId = -1;
		for (int i = 0; i < this.qTable[oldStateIndex].size(); i++) {
			if (this.qTable[oldStateIndex].elementAt(i).myAction.id == myAction.id) {
				if (this.qTable[oldStateIndex].elementAt(i).otherAction.id == otherAgentAction.id) {
					actionsId = i;
					break;
				}
			}
		}
		this.qTable[oldStateIndex].elementAt(actionsId).Value = (1 - this.alpha)
				* this.qTable[oldStateIndex].elementAt(actionsId).Value
				+ this.alpha
				* (reward + this.gamma * this.vTable[newStateIndex]);
		double[][] AV = new double[5][5];
		for (int i = 0; i < this.qTable[oldStateIndex].size(); i++) {
			AV[this.qTable[oldStateIndex].elementAt(i).myAction.id - 1][this.qTable[oldStateIndex]
					.elementAt(i).otherAction.id - 1] = this.qTable[oldStateIndex]
					.elementAt(i).Value;
		}
		LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {
				1, 0, 0, 0, 0, 0 }, 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 0, 1, 1, 1, 1, 1 },
				Relationship.EQ, 1));
		constraints.add(new LinearConstraint(new double[] { 0, 1, 0, 0, 0, 0 },
				Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 1, 0, 0, 0 },
				Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 1, 0, 0 },
				Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 1, 0 },
				Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 0, 1 },
				Relationship.GEQ, 0));
		for (int i = 0; i < 5; i++) {
			constraints.add(new LinearConstraint(new double[] { 1,
					-AV[0][i], -AV[1][i], -AV[2][i],
					-AV[3][i], -AV[4][i] }, Relationship.LEQ, 0));
		}
		boolean flag = false;
		RealPointValuePair solution = null;
		try {
			solution = new SimplexSolver().optimize(f, constraints,
					GoalType.MAXIMIZE, false);
		} catch (OptimizationException e) {
			// TODO Auto-generated catch block
			flag = true;
		}
		// get the solution
		if (!flag) {
			double[] max = solution.getPoint();
			for (int i = 0; i < this.piTable[oldStateIndex].size(); i++) {
				this.piTable[oldStateIndex].elementAt(i).Value = max[this.piTable[oldStateIndex]
						.elementAt(i).id];
			}
			this.vTable[oldStateIndex] = max[0];
		}
		this.alpha *= 0.99996;
	}
}
