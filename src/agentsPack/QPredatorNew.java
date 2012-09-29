package agentsPack;

import java.util.Map;
import java.util.Vector;


import actionPack.RandomAction;
import environmentPack.Coordinate;

public class QPredatorNew extends Predator {

	protected double qTable[][] = new double[6][6];
	private double alpha = 0.1;
	private double gamma = 0.7;

	public QPredatorNew(String name, Coordinate p, Policy pi) {
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
	public Vector<RandomAction> ProbabilityActions(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}

	@Override
	public Vector<RandomAction> ProbabilityActionsSW(Vector<Agent> worldState) {
		throw new UnsupportedOperationException(
				"Sorry, we only use a really small world.");
	}
	
	public void PrintQTable() {
		for (int i = 0; i < 6; i++) {
			System.out.println();
			for (int j = 0; j < 6; j++) {
				System.out.print(this.qTable[i][j]+" ");
			}
		}
		System.out.println();
	}
	public void initializeQ() {

		Prey prey = new Prey("prey", new Coordinate(0, 0), null);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (j <= i) {

					this.position.setX(i);
					this.position.setY(j);

					Vector<Agent> worldState = new Vector<Agent>();
					worldState.add(this);
					worldState.add(prey);

					for (RandomAction c : this
							.ProbabilityActionsRSW(worldState)) {
						this.qTable[c.coordinate.getX()][c.coordinate.getY()] = 15;
					}

				}
			}
		}

	}
	
	public Coordinate chooseAction(Agent agent, Vector<Agent> worldState, double qTable[][],double epsilon){
		
		Double r = Math.random();
		Coordinate maxAction = null;
		Double maxValue = Double.NEGATIVE_INFINITY;
		
		for(RandomAction e : agent.ProbabilityActionsRSW(worldState)){
			if(qTable[e.coordinate.getX()][e.coordinate.getY()] > maxValue) {
				maxValue = qTable[e.coordinate.getX()][e.coordinate.getY()];
				maxAction = e.coordinate;
			}
		}
		
		if (r <= epsilon) {

			Double step = epsilon / (agent.ProbabilityActionsRSW(worldState).size()-1);
			Double counter = step;
			for(RandomAction e : agent.ProbabilityActionsRSW(worldState)){
				if(!Coordinate.compareCoordinates(e.coordinate,maxAction)){
					if (counter <= r) {
						return e.coordinate;
					}
					counter -= step;
				}	
			}
		}
		return maxAction;
		
	}

	public void qLearning() {

		this.initializeQ();

		for (int i = 0; i < 10000; i++) {

			Prey prey = new Prey("prey", new Coordinate(0, 0),null);
			Vector<Agent> worldState = new Vector<Agent>();
			this.position.setX(5);
			this.position.setY(5);
			
			worldState.add(this);
			worldState.add(prey);

			int steps =0;
			
			do {
				
				Double reward = 0.0;

				// save the old position, we need it later.
				Coordinate oldPosition = new Coordinate(this.position.getX(), this.position.getY());
			
				
				// see what the next action will be (according to policy pi).
				Coordinate nextAction = this.chooseAction(this, worldState,this.qTable,0.3);
				
			
				// update the predator's position
				this.position.setX(nextAction.getX());
				this.position.setY(nextAction.getY());

				if (Coordinate.compareCoordinates(this.position, prey.position)) {
					
					System.out.println("killed in "+steps+" steps");
					
					reward = 10.0;
					prey.kill();
					
				}else {

					// Here, I am calculating the prey's possible actions
					Coordinate preyAction  = prey.doAction(worldState);

					int x = preyAction.getX();
					if (x == 10)
						x = -1;
					int y = preyAction.getY();
					if (y == 10)
						y = -1;
					
					int NewPredPosX = this.position.getX() - x;
					int NewPredPosY = this.position.getY() - y;

					// some checks not to excede the limits of the
					// space
					if (NewPredPosX == 6)
						NewPredPosX = 5;
					if (NewPredPosX == -1)
						NewPredPosX = 1;
					if (NewPredPosY == 6)
						NewPredPosY = 5;
					if (NewPredPosY == -1)
						NewPredPosY = 1;
					
					int NewPredPosXNor = NewPredPosX;
					int NewPredPosYNor = NewPredPosY;
					
					if (NewPredPosY < NewPredPosX) {

						NewPredPosXNor = NewPredPosY;
						NewPredPosYNor = NewPredPosX;

					}
					this.position.setX(NewPredPosXNor);
					this.position.setY(NewPredPosYNor);								
				}
				
				
				// new value for this state according to the update function.
				// remember, worldState is still the old one (before the Agents
				// move)
				this.qTable[oldPosition.getX()][oldPosition.getY()] = this.qTable[oldPosition.getX()][oldPosition.getY()]
						+ (alpha * (reward + gamma
								* (this.qTable[this.position.getX()][this.position.getY()]) -
										this.qTable[oldPosition.getX()][oldPosition.getY()]));
				

				steps++;
			}while(prey.lives);
		
		
		}
		
		this.PrintQTable();
		
	}
}
