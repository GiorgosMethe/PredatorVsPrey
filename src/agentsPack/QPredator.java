package agentsPack;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import actionPack.RandomAction;
import environmentPack.Coordinate;

public class QPredator extends Predator {
	
	protected Map<Vector<Agent>,Map<Coordinate, Double>> q;
	private Double alpha = 0.1;
	private Double gamma = 0.7;

	
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
		int count =0;
		for (int i=0;i<6;i++){
				for (int j=0;j<6;j++){
					
					if(j<=i){
						this.position.setX(i);
						this.position.setY(j);
						
						Vector<Agent> worldState = new Vector<Agent>();
						Map<Coordinate,Double> actionValuePair = new HashMap<Coordinate,Double>();
						worldState.add(this);
						worldState.add(prey);
						
							for(RandomAction ra : this.ProbabilityActionsRSW(worldState)){
								
								actionValuePair.put(ra.coordinate, 15.0);
								System.out.println("actionvalue size:  "+ actionValuePair.size());

							}
							
							q.put(worldState,actionValuePair);	
							System.out.println("SIZE OF QTABLE   " + q.size());
							
					count++;
					}
				}
		}
		
		System.out.println("function has finished.  No of states initialized:  " + count  );
				return q;
	}



public void qLearning (){

	q= this.initializeQ();
	
	System.out.println("SIZE OF Q TABLE" + q.entrySet().size());

	this.position.setX(5);
	this.position.setY(5);
	
	System.out.println("function Qlearning is running now.");
	
	for (int i=0 ; i<100; i++){
			
		Prey prey = new Prey("prey",new Coordinate(0,0), null);
		Vector<Agent> worldState = new Vector<Agent>();
		worldState.add(this);
		worldState.add(prey);

		do{
			Double reward = 0.0;
			
			//save the old position, we need it later.
			Coordinate oldPosition = this.position;
			
			//see what the next action will be (according to policy pi).
			Coordinate nextAction = this.pi.chooseAction(worldState, q);

		
			//update the predator's position
			this.position.setX(nextAction.getX());
			this.position.setY(nextAction.getY());
		
			// some checks not to exceed the limits of the
			// space
			if (this.position.getX() == 6)
				this.position.setX(5) ;
			if (this.position.getX() == -1)
				this.position.setX(1);
			if (this.position.getY() == 6)
				this.position.setY(5);
			if (this.position.getY() == -1)
				this.position.setY(1);

			// for positions under the diagonal of the
			// matrix we
			// convert them into the symmetrical position.
			// (i,j) --> (j,i)
			int NewPredPosXNor = this.position.getX();
			int NewPredPosYNor = this.position.getY();
			if (this.position.getY() < this.position.getX()) {

				this.position.setY(NewPredPosXNor);
				this.position.setX(NewPredPosYNor);

			}
			
			//new worldState (so i don't just add +2 agents to the old one)
			//this is only used in the reward function.			
			Vector<Agent> newWorldState = new Vector<Agent>();
						
			newWorldState.add(this);
			newWorldState.add(prey);
			
			//the reward for this particular move ??(the prey hasn't moved yet)
			if(Coordinate.compareCoordinates(this.position, prey.position)){
				reward = 10.0;
				prey.kill();
				}
			
			else{
			
			//Here, I am calculating the prey's possible actions
			Vector<RandomAction> preyActions = new Vector<RandomAction>();
			preyActions.addAll(prey.ProbabilityActionsRSW(worldState));
			
				//need a random [0,1] to see what the action will be
				Double rand = Math.random();
				
				//if rand  >0.2 , the prey stays.
				if (rand>0.2)
					break;
				
				//if the prob is <0.2
				else{
					
				Double step = 0.2/preyActions.size();
				
				//this is how i will get a random action. Not sure it's correct. 
				//but it seems correct ;)
				for (int k=0; k<preyActions.size();k++){
					if(rand < preyActions.get(k).prob){
						
						int x = preyActions.elementAt(k).coordinate.getX();
						if (x == 10)
							x = -1;
						int y = preyActions.elementAt(k).coordinate.getY();
						if (y == 10)
							y = -1;

						// in order to translate this action of the prey
						// into
						// a predator's next state we have to move the
						// predator
						// in relation to the movement of the prey
						int NewPredPosX = this.position.getX() - x;
						int NewPredPosY = this.position.getY() - y;

						// some checks not to exceed the limits of the
						// space
						if (NewPredPosX == 6)
							NewPredPosX = 5;
						if (NewPredPosX == -1)
							NewPredPosX = 1;
						if (NewPredPosY == 6)
							NewPredPosY = 5;
						if (NewPredPosY == -1)
							NewPredPosY = 1;

						// for positions under the diagonal of the
						// matrix we
						// convert them into the symmetrical position.
						// (i,j) --> (j,i)
						int NewPredPosXNor2 = NewPredPosX;
						int NewPredPosYNor2 = NewPredPosY;
						if (NewPredPosY < NewPredPosX) {

							NewPredPosXNor2 = NewPredPosY;
							NewPredPosYNor2 = NewPredPosX;

						}
						this.position.setX(NewPredPosXNor2);
						this.position.setY(NewPredPosYNor2);
						
						}
					
					else{
						//in order to reach the 4th possible move, it will have to
						//reduce by 0.05 3 times. 
						rand-=step;
						}
					}
				
				}
					
		
			}


			//new value for this state according to the update function.
			//remember, worldState is still the old one (before the Agents move)
			Double newValue = q.get(worldState).get(oldPosition)+
					(alpha*(reward+gamma*(q.get(worldState).get(this.position))
							-q.get(worldState).get(oldPosition)));
			
			//this is supposed to update the value of the state we WERE AT. 
			//not sure it works
			q.get(worldState).put(oldPosition, newValue);
			
			worldState.removeAllElements();
			worldState.add(this);
			worldState.add(prey);
			
			
		}while (prey.isAlive());
		

			
			System.out.println("The prey is caught!");
			System.out.println(" ");
		}
			
		
	}









}
