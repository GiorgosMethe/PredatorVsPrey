package valuePolicyIterationPack;

import java.util.Vector;

import actionPack.RandomAction;
import agentsPack.Agent;
import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class ValueIteration {

	public static void main(String[] args) {

		//ValueIterationImpl(0.7, new Coordinate(0, 0));
		ValueIterationImplRSW(0.7, new Coordinate(5, 5));

	}

	public static void ValueIterationImplRSW(double discountFactor, Coordinate Prey) {

		long start = System.currentTimeMillis();

		Vector<Vector<Agent>> stateSpace = new Vector<Vector<Agent>>(21);
		Vector<Agent> state;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; (j+i) < 6; j++) {
				state = new Vector<Agent>(2);
				state.add(new Predator(null, new Coordinate(i, 6 - j), null));
				state.add(new Prey(null, new Coordinate(0, 0), null));
				stateSpace.add(state);
			}
		}
		
		double[][] State = new double[6][6];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 6; i++) {
				for (int j = 0; (j+i) < 6; j++){

							preValue = State[i][j];
							Environment env = new Environment();
							Predator P = (Predator) stateSpace.get(i+j).firstElement();
							
							env.worldState.add(P);

							Vector<RandomAction> PredAct = P
									.ProbabilityActionsRSW(env.worldState);
							

							if (Coordinate.compareCoordinates(P.position,
									new Coordinate(0, 0))) {
								State[i][j] = 0;

							} else {

								double max = Double.NEGATIVE_INFINITY;

								for (int ii = 0; ii < PredAct.size(); ii++){

									double currentValue = 0;
									
									env.worldState.removeAllElements();
									Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate,
											null);
									Prey pNew = new Prey("", new Coordinate(0, 0), null);
									env.worldState.add(PNew);
									env.worldState.add(pNew);
									
									Vector<RandomAction> PreyAct = pNew
											.ProbabilityActionsRSW(env.worldState);

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										double reward = 0;
										if (Coordinate.compareCoordinates(
												PredAct.elementAt(ii).coordinate,
												new Coordinate(0, 0))) {

											reward = 10;
											currentValue = reward; 
											break;

										}


										double prob = PreyAct.elementAt(jj).prob;
																				
										double discount  = discountFactor *  State[PNew.position.getX()%6][PNew.position.getY()%6];

										currentValue +=prob *(reward+discount); 

									}
									max = Math.max(max, currentValue);
								}

								State[i][j] = max;

								delta = Math.max(delta,
										Math.abs(preValue - max));

							}
							
						}
				
			}
								
		} while (delta > 0.0001);

		long end = System.currentTimeMillis();
		System.out.println("\n\nNormal 6x6 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		ValuesMirroring(State, Prey);

	}

	
	public static void ValueIterationImplSW(double discountFactor, Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][] State = new double[6][6];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j <6; j++){

							preValue = State[i][j];
							Environment env = new Environment();
							Predator P = new Predator("", new Coordinate(i, j),
									null);
							env.worldState.add(P);

							Vector<RandomAction> PredAct = P
									.ProbabilityActionsSW(env.worldState);
							

							if (Coordinate.compareCoordinates(P.position,
									new Coordinate(0, 0))) {
								State[i][j] = 0;

							} else {

								double max = Double.NEGATIVE_INFINITY;

								for (int ii = 0; ii < PredAct.size(); ii++){

									double currentValue = 0;
									
									env.worldState.removeAllElements();
									Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate,
											null);
									Prey pNew = new Prey("", new Coordinate(0, 0), null);
									env.worldState.add(PNew);
									env.worldState.add(pNew);
									
									Vector<RandomAction> PreyAct = pNew
											.ProbabilityActionsSW(env.worldState);

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										double reward = 0;
										if (Coordinate.compareCoordinates(
												PredAct.elementAt(ii).coordinate,
												new Coordinate(0, 0))) {

											reward = 10;
											currentValue = reward; 
											break;

										}


										double prob = PreyAct.elementAt(jj).prob;
																				
										double discount  = discountFactor *  State[PNew.position.getX()][PNew.position.getY()];

										currentValue +=prob *(reward+discount); 

									}
									max = Math.max(max, currentValue);
								}

								State[i][j] = max;

								delta = Math.max(delta,
										Math.abs(preValue - max));

							}
							
						}
				
			}
								
		} while (delta > 0.0001);

		long end = System.currentTimeMillis();
		System.out.println("\n\nNormal 6x6 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		ValuesMirroring(State, Prey);

	}

	public static void ValueIterationImpl(double discountFactor, Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][][][] State = new double[11][11][11][11];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 11; i++) {
				for (int j = 0; j <11; j++) {
					for (int x = 0; x < 11; x++) {
						for (int y = 0; y < 11; y++) {

							preValue = State[i][j][x][y];
							Environment env = new Environment();
							Predator P = new Predator("", new Coordinate(i, j),
									null);
							env.worldState.add(P);

							Vector<RandomAction> PredAct = P
									.ProbabilityActions(env.worldState);
							

							if (Coordinate.compareCoordinates(P.position,
									new Coordinate(x, y))) {
								State[i][j][x][y] = 0;

							} else {

								double max = Double.NEGATIVE_INFINITY;

								for (int ii = 0; ii < PredAct.size(); ii++){

									double currentValue = 0;
									
									env.worldState.removeAllElements();
									Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate,
											null);
									Prey pNew = new Prey("", new Coordinate(x, y), null);
									env.worldState.add(PNew);
									env.worldState.add(pNew);
									
									Vector<RandomAction> PreyAct = pNew
											.ProbabilityActions(env.worldState);

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										double reward = 0;
										if (Coordinate.compareCoordinates(
												PredAct.elementAt(ii).coordinate,
												pNew.position)) {

											reward = 10;
											currentValue = reward; 
											break;

										}


										double prob = PreyAct.elementAt(jj).prob;

										double discount  = discountFactor *  State[PNew.position.getX()][PNew.position.getY()]
												[PreyAct.elementAt(jj).coordinate.getX()][PreyAct.elementAt(jj).coordinate.getY()];


										currentValue +=prob *(reward+discount); 

									}


									max = Math.max(max, currentValue);
								}

								State[i][j][x][y] = max;

								delta = Math.max(delta,
										Math.abs(preValue - max));

							}
						}
					}

				}
			}

		} while (delta > 0);

		long end = System.currentTimeMillis();
		System.out.println("Normal 11x11 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		PrintValueIteration(State[Prey.getX()][Prey.getY()]);

	}

	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

	public static void PrintValueIteration(double[][] StateValues) {
		System.out.println("\n\n\n");
		System.out.println("\n-----------------------------------------------------------------------------");
		for (int i = 0; i < StateValues.length; i++) {
			
			for (int j = 0; j < StateValues.length; j++) {

				System.out.printf(" %.6f |", StateValues[i][j] );

			}
			System.out.println("\n-----------------------------------------------------------------------------");
		}
	}

	public static void ValuesMirroring(double[][] SmallWorld, Coordinate Prey) {

		PrintValueIteration(SmallWorld);
		double[][] State = new double[11][11];

		int currentPosX = Prey.getX();
		int currentPosY = Prey.getY();
		int xCur=-1;
		int yCur=-1;
		for(int x=Prey.getX();x < (Prey.getX()+6);x++){
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for(int y=Prey.getY();y < (Prey.getY()+6);y++){
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur=-1;
		for(int x=Prey.getX();x > (Prey.getX()-6);x--){
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for(int y=Prey.getY();y < (Prey.getY()+6);y++){
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur=-1;
		for(int x=Prey.getX();x > (Prey.getX()-6);x--){
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for(int y=Prey.getY();y > (Prey.getY()-6);y--){
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur=-1;
		for(int x=Prey.getX();x < (Prey.getX()+6);x++){
			xCur++;
			yCur = -1;
			currentPosX = (x + 11) % 11;
			for(int y=Prey.getY();y > (Prey.getY()-6);y--){
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		PrintValueIteration(State);


	}

}