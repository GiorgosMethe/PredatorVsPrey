package valuePolicyIterationPack;

import java.util.Vector;

import actionPack.RandomAction;
import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class PolIter {

	public static void main(String[] args) {

		// Normal state space implementation
		PolicyIterationImpl(0.7, new Coordinate(0, 0));

		// First way to reduce the state space
		// Small World 6x6 matrix with prey standing always
		// at 0,0 position.
		//PolicyIterationImplSW(0.7, new Coordinate(0, 0));

		// Second way to reduce the state space
		// only the half of the matrix e.g Upper Triangular Matrix
		// Due to symmetric values we can implement this state space
		// as it was the 11x11 state space. There is also a static position
		// for the prey at 0,0 position.
		//PolicyIterationImplRSW(0.7, new Coordinate(0, 0));

	}

	public static void PolicyIterationImpl(double discountFactor, Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][][][] State = new double[11][11][11][11];
		Coordinate[][][][] policy = new Coordinate[11][11][11][11];
		double delta;
		double preValue;
		boolean policyStable = true;
		int algorithmSweeps = 0;
		int peSweep = 0;
		int instableActions = 0;
		Vector<Integer> peSweeps = new Vector<Integer>();

		do {
			algorithmSweeps++;
			// Policy Evaluation
			do {
				peSweep++;
				delta = 0;

				// for every possible state of the normal world
				for (int i = 0; i < 11; i++) {
					for (int j = 0; j < 11; j++) {
						for (int x = 0; x < 11; x++) {
							for (int y = 0; y < 11; y++) {

								// previous value of this world-state
								preValue = State[i][j][x][y];
								Environment env = new Environment();

								// a predator is generated into a specific position
								Predator P = new Predator("", new Coordinate(i, j), null);

								// ... and it is added to the worldState
								env.worldState.add(P);

								if (policy[i][j][x][y] == null) {
									// possible actions for the predator are computed
									// with respect to the current world state.
									Vector<RandomAction> PredAct = P
											.ProbabilityActions(env.worldState);
									double rand = Math.random();
									double begin = 0;
									for (RandomAction ra : PredAct) {
										begin += ra.prob;
										if (rand <= begin) {
											policy[i][j][x][y] = Coordinate.difference(ra.coordinate, P.position);
											break;
										}
									}
								}

								// predator and prey are located in the same
								// position
								// absorbing-terminal state and we initialize this
								// value to zero
								if (Coordinate.compareCoordinates(P.position,
										new Coordinate(x, y))) {

									State[i][j][x][y] = 0;

								} else {
									double newValue = 0;

									// world state is initialized again for
									// every action of the predator
									env.worldState.removeAllElements();

									// a new predator is created for every
									// action of the predator
									Predator PNew = new Predator("", policy[i][j][x][y], null);

									// a prey is a created
									Prey pNew = new Prey("", new Coordinate(x, y), null);
									env.worldState.add(PNew);
									env.worldState.add(pNew);

									// possible actions for the prey are
									// computed in respect to the current
									// worldstate after the predator's action
									Vector<RandomAction> PreyAct = pNew
											.ProbabilityActions(env.worldState);

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										// if the predator moves into the prey
										// there is
										// no way out
										// for it. An immediate reward is given
										// to the
										// predator
										double reward = 0;
										if (Coordinate.compareCoordinates(policy[i][j][x][y], pNew.position)) {
											reward = 10;
											newValue = reward;
											break;

										}

										double prob = PreyAct.elementAt(jj).prob;

										double discount = discountFactor
												* State[PNew.position.getX()]
														[PNew.position.getY()]
																[PreyAct.elementAt(jj).coordinate.getX()]
																		[PreyAct.elementAt(jj).coordinate.getY()];

										newValue += prob
												* (reward + discount);

									}

									State[i][j][x][y] = newValue;

									delta = Math.max(delta,
											Math.abs(preValue - newValue));

								}
							}
						}

					}
				}

			} while (delta > 0.01);
			peSweeps.add(peSweep);
			// Policy Improvement
			policyStable = true;
			instableActions = 0;
			
			// for every possible state of the normal world
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					for (int x = 0; x < 11; x++) {
						for (int y = 0; y < 11; y++) {
							Coordinate oldAction = policy[i][j][x][y];

							// previous value of this world-state
							Environment env = new Environment();

							// a predator is generated into a specific position
							Predator P = new Predator("", new Coordinate(i, j), null);

							// ... and it is added to the worldState
							env.worldState.add(P);

							// possible actions for the predator are computed
							// with respecto to the current worldstate
							Vector<RandomAction> PredAct = P.ProbabilityActions(env.worldState);

							// predator and prey are located in the same
							// position
							// absorbing-terminal state and we initialize this
							// action to "do nothing"
							if (Coordinate.compareCoordinates(P.position, new Coordinate(x, y))) {
								policy[i][j][x][y] = new Coordinate(0, 0);
							} else {
								double maxValue = Double.NEGATIVE_INFINITY;
								policy[i][j][x][y] = P.position;

								for (int ii = 0; ii < PredAct.size(); ii++) {
									double currentValue = 0;
									Coordinate currentAction = PredAct.get(ii).coordinate; 

									// world state is initialized again for
									// every action of the predator
									env.worldState.removeAllElements();

									// a new predator is created for every
									// action of the predator
									Predator PNew = new Predator("",
											PredAct.elementAt(ii).coordinate,
											null);

									// a prey is a created
									Prey pNew = new Prey("", new Coordinate(x,
											y), null);
									env.worldState.add(PNew);
									env.worldState.add(pNew);

									// possible actions for the prey are
									// computed in respect to the current
									// worldstate after the predator's action
									Vector<RandomAction> PreyAct = pNew
											.ProbabilityActions(env.worldState);

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										// check for terminal state
										// if terminal state, currentValue = Double.POSITIVE_INFINITY
										if (Coordinate.compareCoordinates(PredAct.get(ii).coordinate, pNew.position)) {
											currentValue = Double.POSITIVE_INFINITY;
											break;
										}
										// if the predator moves into the prey
										// there is
										// no way out
										// for it. An immediate reward is given
										// to the
										// predator
										double reward = 0;
										if (Coordinate
												.compareCoordinates(
														PredAct.elementAt(ii).coordinate,
														pNew.position)) {

											reward = 10;
											currentValue = reward;
											break;

										}

										double prob = PreyAct.elementAt(jj).prob;

										double discount = discountFactor
												* State[PNew.position.getX()]
														[PNew.position.getY()]
														[PreyAct.elementAt(jj).coordinate.getX()]
														[PreyAct.elementAt(jj).coordinate.getY()];

										currentValue += prob * (reward + discount);

									}

									if (maxValue < currentValue) {
										maxValue = currentValue;
										policy[i][j][x][y] = currentAction;
									}
								}
							}
							if (!Coordinate.compareCoordinates(oldAction, policy[i][j][x][y])) {
								policyStable = false;
								instableActions++;
							}
						}
					}
				}
			}
			System.out.println("" + algorithmSweeps + ": " + instableActions);
		} while (!policyStable);

		long end = System.currentTimeMillis();
		System.out.println("Normal 11x11 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		System.out.println("PE sweeps: " + peSweeps.toString());

	}
	
}