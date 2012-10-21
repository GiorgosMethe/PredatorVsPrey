package functionsPack;

import actionPack.RandomAction;
import agentsPack.Predator;
import agentsPack.Prey;
import agentsPack.Vector;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class ValueIteration {

	public static void Run(double gamma, double theta, Coordinate prey) {

		// Normal state space implementation
		// ValueIterationImpl(gamma,theta, prey);

		// First way to reduce the state space
		// Small World 6x6 matrix with prey standing always
		// at 0,0 position.
		// ValueIterationImplSW(gamma,theta, prey);

		// Second way to reduce the state space
		// only the half of the matrix e.g Upper Triangular Matrix
		// Due to symmetric values we can implement this state space
		// as it was the 11x11 state space. There is also a static position
		// for the prey at 0,0 position.
		ValueIterationImplRSW(gamma, theta, prey);

	}

	public static void ValueIterationImpl(double discountFactor, double theta,
			Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][][][] State = new double[11][11][11][11];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

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
							Predator P = new Predator("", new Coordinate(i, j),
									null);

							// ... and it is added to the worldState
							env.worldState.add(P);

							// possible actions for the predator are computed
							// with respecto to the current worldstate
							Vector<RandomAction> PredAct = P
									.ProbabilityActions(env.worldState);

							// predator and prey are located in the same
							// position
							// absorbing-terminal state and we initialize this
							// value to zero
							if (Coordinate.compareCoordinates(P.position,
									new Coordinate(x, y))) {

								State[i][j][x][y] = 0;

							} else {

								double max = Double.NEGATIVE_INFINITY;

								for (int ii = 0; ii < PredAct.size(); ii++) {

									double currentValue = 0;

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
												* State[PNew.position.getX()][PNew.position
														.getY()][PreyAct
														.elementAt(jj).coordinate
														.getX()][PreyAct
														.elementAt(jj).coordinate
														.getY()];

										currentValue += prob
												* (reward + discount);

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

		} while (delta > theta);

		long end = System.currentTimeMillis();
		System.out.println("Normal 11x11 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		PrintValueIteration(State[Prey.getX()][Prey.getY()]);

	}

	public static void ValueIterationImplSW(double discountFactor,
			double theta, Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][] State = new double[6][6];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {

					// previous value of this state
					preValue = State[i][j];
					Environment env = new Environment();
					Predator P = new Predator("", new Coordinate(i, j), null);
					env.worldState.add(P);

					Vector<RandomAction> PredAct = P
							.ProbabilityActionsSW(env.worldState);

					// check if predator is in the same position as prey's
					// original position
					if (Coordinate.compareCoordinates(P.position,
							new Coordinate(0, 0))) {
						State[i][j] = 0;

					} else {

						double max = Double.NEGATIVE_INFINITY;

						for (int ii = 0; ii < PredAct.size(); ii++) {

							double currentValue = 0;

							// new world generation in respect
							// of the predator's action
							env.worldState.removeAllElements();
							Predator PNew = new Predator("",
									PredAct.elementAt(ii).coordinate, null);
							Prey pNew = new Prey("", new Coordinate(0, 0), null);
							env.worldState.add(PNew);
							env.worldState.add(pNew);

							// computation of every possible action for the prey
							// on the updated worldstate
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

								// prey's actions are always given from the
								// function
								// probability actions with respect to the
								// normal world.
								// here we translate them is this state space
								int x = PreyAct.elementAt(jj).coordinate.getX();
								if (x == 10)
									x = -1;
								int y = PreyAct.elementAt(jj).coordinate.getY();
								if (y == 10)
									y = -1;

								// in order to translate this action of the prey
								// into
								// a predator's next state we have to move the
								// predator
								// in relation to the movement of the prey
								int NewPredPosX = PNew.position.getX() - x;
								int NewPredPosY = PNew.position.getY() - y;

								// some checks for not to excede the limits of
								// our world
								if (NewPredPosX == 6)
									NewPredPosX = 5;
								if (NewPredPosX == -1)
									NewPredPosX = 1;
								if (NewPredPosY == 6)
									NewPredPosY = 5;
								if (NewPredPosY == -1)
									NewPredPosY = 1;

								double prob = PreyAct.elementAt(jj).prob;

								double discount = discountFactor
										* State[NewPredPosX][NewPredPosY];

								currentValue += prob * (reward + discount);

							}
							max = Math.max(max, currentValue);
						}

						State[i][j] = max;

						delta = Math.max(delta, Math.abs(preValue - max));

					}

				}

			}

		} while (delta > theta);

		long end = System.currentTimeMillis();
		System.out.println("\n\nSmall 6x6 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		QuarterMirroring(State, Prey);

	}

	public static void ValueIterationImplRSW(double discountFactor,
			double theta, Coordinate Prey) {

		long start = System.currentTimeMillis();

		double[][] State = new double[6][6];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 6; i++) {
				for (int j = i; j < 6; j++) {

					// previous value of the state
					preValue = State[i][j];
					// environment generation
					Environment env = new Environment();
					// predator generation
					Predator P = new Predator("", new Coordinate(i, j), null);
					env.worldState.add(P);

					// possible action for the predator given the worldstate
					Vector<RandomAction> PredAct = P
							.ProbabilityActionsRSW(env.worldState);

					// predator and prey are standing on the same position
					// absorbing state with value 0
					if (Coordinate.compareCoordinates(P.position,
							new Coordinate(0, 0))) {
						State[i][j] = 0;

					} else {

						double max = Double.NEGATIVE_INFINITY;

						for (int ii = 0; ii < PredAct.size(); ii++) {

							double currentValue = 0;

							// initialization of the worldstate removing all
							// agent from it
							env.worldState.removeAllElements();

							// a new predator is generated with a new position
							// depending on each action
							Predator PNew = new Predator("",
									PredAct.elementAt(ii).coordinate, null);

							// prey is generated always in position 0,0
							Prey pNew = new Prey("", new Coordinate(0, 0), null);
							env.worldState.add(PNew);
							env.worldState.add(pNew);

							// action for the prey given the worldstate are
							// generated
							Vector<RandomAction> PreyAct = pNew
									.ProbabilityActionsRSW(env.worldState);

							for (int jj = 0; jj < PreyAct.size(); jj++) {

								// if the predator moves into the prey there is
								// no way out
								// for it. An immediate reward is given to the
								// predator
								double reward = 0;
								if (Coordinate.compareCoordinates(
										PredAct.elementAt(ii).coordinate,
										new Coordinate(0, 0))) {

									reward = 10;
									currentValue = reward;
									break;

								}

								// probability of each action of the prey
								double prob = PreyAct.elementAt(jj).prob;

								// prey's actions are always given from the
								// function
								// probability actions with respect to the
								// normal world.
								// here we translate them is this state space
								int x = PreyAct.elementAt(jj).coordinate.getX();
								if (x == 10)
									x = -1;
								int y = PreyAct.elementAt(jj).coordinate.getY();
								if (y == 10)
									y = -1;

								// in order to translate this action of the prey
								// into
								// a predator's next state we have to move the
								// predator
								// in relation to the movement of the prey
								int NewPredPosX = PNew.position.getX() - x;
								int NewPredPosY = PNew.position.getY() - y;

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

								// for positions under the diagonal of the
								// matrix we
								// convert them into the symmetrical position.
								// (i,j) --> (j,i)
								int NewPredPosXNor = NewPredPosX;
								int NewPredPosYNor = NewPredPosY;
								if (NewPredPosY < NewPredPosX) {

									NewPredPosXNor = NewPredPosY;
									NewPredPosYNor = NewPredPosX;

								}

								// the next state of the predator will be
								// computed
								// by the above checks
								double discount = discountFactor
										* State[NewPredPosXNor][NewPredPosYNor];

								currentValue += prob * (reward + discount);

							}
							max = Math.max(max, currentValue);
						}

						State[i][j] = max;

						delta = Math.max(delta, Math.abs(preValue - max));

					}

				}

			}

		} while (delta > theta);

		long end = System.currentTimeMillis();
		System.out.println("\n\nLittle 21-states World");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		HalfQuarterMirroring(State, Prey);

	}

	// reward function return 10 for every time predator catches the prey
	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

	// printing function of each matrix
	public static void PrintValueIteration(double[][] StateValues) {

		System.out.println("\n");
		for (int i = 0; i < StateValues.length; i++) {

			for (int j = 0; j < StateValues.length; j++) {

				// System.out.printf(" %.4f |", StateValues[i][j]);

				System.out.printf(" %.4f |", StateValues[i][j]);

			}
			System.out.println("");
		}
	}

	// each Upper 6x6 Triangular Matrix is mirroring into a full 6x6 matrix with
	// non-zero elements in all positions
	public static void HalfQuarterMirroring(double[][] SmallWorld, Coordinate c) {

		PrintValueIteration(SmallWorld);

		for (int i = 0; i < SmallWorld.length; i++) {
			for (int j = 0; j < SmallWorld.length; j++) {
				if (j < i) {
					SmallWorld[i][j] = SmallWorld[j][i];
				} else {
					SmallWorld[i][j] = SmallWorld[i][j];
				}
			}
		}

		QuarterMirroring(SmallWorld, c);

	}

	// a 6x6 matrix is a quarter of the normal 11x11 grid world
	// this function convert 6x6 matrices to the normal state space
	// representation
	public static void QuarterMirroring(double[][] SmallWorld, Coordinate Prey) {

		PrintValueIteration(SmallWorld);
		double[][] State = new double[11][11];

		int currentPosX = Prey.getX();
		int currentPosY = Prey.getY();
		int xCur = -1;
		int yCur = -1;
		for (int x = Prey.getX(); x < (Prey.getX() + 6); x++) {
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for (int y = Prey.getY(); y < (Prey.getY() + 6); y++) {
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur = -1;
		for (int x = Prey.getX(); x > (Prey.getX() - 6); x--) {
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for (int y = Prey.getY(); y < (Prey.getY() + 6); y++) {
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur = -1;
		for (int x = Prey.getX(); x > (Prey.getX() - 6); x--) {
			currentPosX = (x + 11) % 11;
			xCur++;
			yCur = -1;
			for (int y = Prey.getY(); y > (Prey.getY() - 6); y--) {
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		xCur = -1;
		for (int x = Prey.getX(); x < (Prey.getX() + 6); x++) {
			xCur++;
			yCur = -1;
			currentPosX = (x + 11) % 11;
			for (int y = Prey.getY(); y > (Prey.getY() - 6); y--) {
				yCur++;
				currentPosY = (y + 11) % 11;
				State[currentPosX][currentPosY] = SmallWorld[xCur][yCur];
			}

		}

		PrintValueIteration(State);

	}

}
