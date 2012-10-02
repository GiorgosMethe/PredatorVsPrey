package agentsPack;

import java.util.Map;
import java.util.Vector;

import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class SarsaPredator extends Predator {

	@SuppressWarnings("unchecked")
	private Vector<StateActionPair> sarsaTable[][] = new Vector[6][6];
	private double alpha = 0.1;
	private double gamma = 0.7;

	public SarsaPredator(String name, Coordinate p, Policy pi) {
		super(name, p, pi);
		// TODO Auto-generated constructor stub
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
	public Double reward(Vector<Agent> currState, Vector<Agent> nextState,
			Coordinate action) {
		throw new UnsupportedOperationException(
				"Sorry, you don't know the reward function. Receive one from the environment.");
	}

	@Override
	public Map<Integer, Double> functionP(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, you should use ProbabilityActionsRSW(worldState).");
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

	public void initializeSarsaTable() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j <= i; j++) {

				this.sarsaTable[i][j] = new Vector<StateActionPair>();
				SarsaPredator sP = new SarsaPredator("", new Coordinate(i, j),
						null);
				Vector<Agent> worldState = new Vector<Agent>();
				worldState.add(sP);
				worldState.add(prey);
				int id = 0;

				for (RandomAction c : sP.ProbabilityActionsRSW(worldState)) {
					id++;
					this.sarsaTable[i][j].add(new StateActionPair(
							new Coordinate(c.coordinate.getX(), c.coordinate
									.getY()), 15, id));

				}

			}
		}

	}

	public void printSarsaTable() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (j <= i) {
					System.out.println(new Coordinate(i, j));
					for (StateActionPair c : this.sarsaTable[i][j]) {

						System.out.println(c.Action + " " + c.Value);

					}
				}
			}
		}
	}

	public StateActionPair chooseSoftMaxAction(double temperature) {

		double sum = 0;

		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {
			sum += Math.exp(e.Value / temperature);

		}

		double random = Math.random();
		double k = 0;

		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {

			k += Math.exp(e.Value / temperature) / sum;

			if (random <= k) {
				return e;
			}
		}
		return null;
	}

	public StateActionPair chooseEGreedyAction(double epsilon) {

		Double r = Math.random();
		StateActionPair maxAction = null;
		Double maxValue = Double.NEGATIVE_INFINITY;

		int CountActions = 0;
		for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
				.getY()]) {
			if (e.Value > maxValue) {
				maxValue = e.Value;
				maxAction = e;
			}
			CountActions++;
		}

		if (r <= epsilon) {
			Double step = epsilon / (CountActions - 1);
			Double counter = step;
			for (StateActionPair e : this.sarsaTable[this.position.getX()][this.position
					.getY()]) {
				if (!Coordinate.compareCoordinates(e.Action, maxAction.Action)) {
					if (counter <= r) {
						return e;
					}
					counter -= step;
				}
			}
		}
		return maxAction;

	}

	public void updateSarsaTable(Coordinate stateOld,
			StateActionPair actionOld, StateActionPair actionNew,double reward, boolean absorb) {

		int oldActionPosId = -1;
		for (int i = 0; i < this.sarsaTable[stateOld.getX()][stateOld.getY()]
				.size(); i++) {
			if (this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(i).id == actionOld.id) {
				oldActionPosId = i;
				break;
			}
		}

		int newActionPosId = -1;
		for (int i = 0; i < this.sarsaTable[actionOld.Action.getX()][actionOld.Action
				.getY()].size(); i++) {
			if (this.sarsaTable[actionOld.Action.getX()][actionOld.Action
					.getY()].elementAt(i).id == actionNew.id) {
				newActionPosId = i;
				break;
			}
		}

		if(absorb){
			
			this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value = 
					this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value +
					alpha * (reward + ( gamma * 0 ) - this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value );
		
		}else{
			
			this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value = 
					this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value +
					alpha * (reward + ( gamma * this.sarsaTable[actionOld.Action.getX()][actionOld.Action.getY()]
							.elementAt(newActionPosId).Value ) - this.sarsaTable[stateOld.getX()][stateOld.getY()].elementAt(oldActionPosId).Value );
		
		}
		
		
	}

}
