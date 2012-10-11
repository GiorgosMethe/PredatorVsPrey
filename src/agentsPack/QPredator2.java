package agentsPack;

import java.util.Map;

import actionPack.RandomAction;
import actionPack.StateActionPair;
import environmentPack.Coordinate;

public class QPredator2 extends Predator { 
	


	@SuppressWarnings("unchecked")
	private  Vector<StateActionPair> qTable[][] = (Vector<StateActionPair>[][]) new Vector[11][11];
	private double alpha;
	private double gamma;

	public QPredator2(String name, Coordinate p, Policy pi, double alpha,
			double gamma) {
		super(name, p, pi);
		this.alpha = alpha;
		this.gamma = gamma;
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

	public void initializeQTable() {

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j<11; j++) {

				this.qTable[i][j] = new Vector<StateActionPair>();
				QPredator2 q2 = new QPredator2("q",new Coordinate(i,j),null,this.alpha,this.gamma);
		
					this.qTable[i][j].add(new StateActionPair(q2.position.getEast(),15.0,0));
					this.qTable[i][j].add(new StateActionPair(q2.position.getWest(),15.0,1));
					this.qTable[i][j].add(new StateActionPair(q2.position.getNorth(),15.0,2));
					this.qTable[i][j].add(new StateActionPair(q2.position.getSouth(),15.0,3));
					this.qTable[i][j].add(new StateActionPair(q2.position,15.0,4));
				

			}
		}

	}

	public  void PrintQTable() {
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j<11; j++) {
				System.out.println(new Coordinate(i, j));
				for (StateActionPair c : this.qTable[i][j]) {
					System.out.print(c.Action + " " + c.Value + "  ");
				}
				System.out.println("\n");
			}
		}
	}

	public StateActionPair chooseSoftMaxAction(double temperature) {

		double sum = 0;

		for (StateActionPair e : this.qTable[this.position.getX()][this.position
				.getY()]) {
			sum += Math.exp(e.Value / temperature);

		}

		double random = Math.random();
		double k = 0;

		for (StateActionPair e : this.qTable[this.position.getX()][this.position
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
		Double maxValue = Double.NEGATIVE_INFINITY;
		Vector<StateActionPair> tempMaxVector = new Vector<StateActionPair>();
		int CountActions = 0;
		for (StateActionPair e : this.qTable[this.position.getX()][this.position
				.getY()]) {
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
			for (StateActionPair e : this.qTable[this.position.getX()][this.position
					.getY()]) {
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

	
	
	
	public void updateQTable(Coordinate oldPosition, StateActionPair Action,
			double reward, boolean absorbing) {

		int actionPosId = -1;
		for (int i = 0; i < this.qTable[this.position.getX()][this.position
				.getY()].size(); i++) {
			if (this.qTable[this.position.getX()][this.position.getY()]
					.elementAt(i).id == Action.id) {
				actionPosId = i;
				break;
			}
		}

		if (!absorbing) {

			double actionMaxValue = Double.NEGATIVE_INFINITY;
			for (StateActionPair e : this.qTable[this.position.getX()][this.position
					.getY()]) {
				if (e.Value > actionMaxValue) {
					actionMaxValue = e.Value;
				}
			}

			this.qTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(actionPosId).Value = this.qTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(actionPosId).Value
					+ (alpha * (reward + (gamma * actionMaxValue) - this.qTable[oldPosition
							.getX()][oldPosition.getY()].elementAt(actionPosId).Value));

		} else {

			this.qTable[oldPosition.getX()][oldPosition.getY()]
					.elementAt(actionPosId).Value = this.qTable[oldPosition
					.getX()][oldPosition.getY()].elementAt(actionPosId).Value
					+ alpha
					* (reward - this.qTable[oldPosition.getX()][oldPosition
							.getY()].elementAt(actionPosId).Value);

		}
	}

	
}
