package valuePolicyIterationPack;

import java.util.Vector;

import actionPack.RandomAction;
import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class PolicyIteration {

	public static void Run(double gamma, double theta) {

		// Normal state space implementation
		System.out.println("\n\nNormal 11^4-state World");
		PolicyIterationImpl(gamma, theta);

		// First way to reduce the state space
		// Small World 6x6 matrix with prey standing always
		// at 0,0 position.
		System.out.println("\n\nSmall 6^2-state World");
		PolicyIterationImplSW(gamma, theta);

		// Second way to reduce the state space
		// only the half of the matrix e.g Upper Triangular Matrix
		// Due to symmetric values we can implement this state space
		// as it was the 11x11 state space. There is also a static position
		// for the prey at 0,0 position.
		System.out.println("\n\nLittle 21-state World");
		PolicyIterationImplRSW(gamma, theta);

	}

	public static void PolicyIterationImpl(double discountFactor, double theta) {

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
									Vector<RandomAction> PredAct = P.ProbabilityActions(env.worldState);
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
								if (Coordinate.compareCoordinates(P.position, new Coordinate(x, y))) {
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

			} while (delta > theta);
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
									Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate, null);

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
										if (Coordinate.compareCoordinates(PredAct.elementAt(ii).coordinate, pNew.position)) {
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
			System.out.println("Iteration " + algorithmSweeps + ": " + instableActions+" Instable actions");
		} while (!policyStable);

		long end = System.currentTimeMillis();
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");;
	}



	public static void PolicyIterationImplSW(double discountFactor, double theta) {

		long start = System.currentTimeMillis();

		double[][] State = new double[6][6];
		Coordinate[][] policy = new Coordinate[6][6];
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

				// for every possible state of the small world
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 6; j++) {

						// previous value of this state
						preValue = State[i][j];
						Environment env = new Environment();
						Predator P = new Predator("", new Coordinate(i, j), null);
						env.worldState.add(P);

						if (policy[i][j] == null) {
							// possible actions for the predator are computed 
							// with respect to the current world state.
							Vector<RandomAction> PredAct = P.ProbabilityActionsSW(env.worldState);
							double rand = Math.random();
							double begin = 0;
							for (RandomAction ra : PredAct) {
								begin += ra.prob;
								if (rand <= begin) {
									policy[i][j] = Coordinate.difference(ra.coordinate, P.position).toSWCoordinate();
									break;
								}
							}
						}

						// check if predator is in the same position as prey's
						// original position; if so, we have reached the absorbing state.
						if (Coordinate.compareCoordinates(P.position, new Coordinate(0, 0))) {
							State[i][j] = 0;
						} else {
							double newValue = 0;

							// new world generation in respect
							// of the predator's action
							env.worldState.removeAllElements();
							Predator PNew = new Predator("", policy[i][j], null);
							Prey pNew = new Prey("", new Coordinate(0, 0), null);
							env.worldState.add(PNew);
							env.worldState.add(pNew);

							// computation of every possible action for the prey
							// on the updated worldstate
							Vector<RandomAction> PreyAct = pNew
									.ProbabilityActionsSW(env.worldState);

							for (int jj = 0; jj < PreyAct.size(); jj++) {

								double reward = 0;
								if (Coordinate.compareCoordinates(policy[i][j], new Coordinate(0, 0))) {
									reward = 10;
									newValue = reward;
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

								double discount = discountFactor * State[NewPredPosX][NewPredPosY];

								newValue += prob * (reward + discount);

							}

							State[i][j] = newValue;

							delta = Math.max(delta, Math.abs(preValue - newValue));

						}

					}

				}
			} while (delta > theta);
      peSweeps.add(peSweep);

      // Policy Improvement
      policyStable = true;
      instableActions = 0;

      // for every possible state of the world
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
          Coordinate oldAction = policy[i][j];
					Environment env = new Environment();

					Predator P = new Predator("", new Coordinate(i, j), null);
					env.worldState.add(P);

					Vector<RandomAction> PredAct = P.ProbabilityActionsSW(env.worldState);

					// check if predator is in the same position as prey's
					// original position
					if (Coordinate.compareCoordinates(P.position, new Coordinate(0, 0))) {
            policy[i][j] = new Coordinate(0, 0);
					} else {

						double maxValue = Double.NEGATIVE_INFINITY;
            policy[i][j] = P.position;

						for (int ii = 0; ii < PredAct.size(); ii++) {
							double currentValue = 0;
              Coordinate currentAction = PredAct.get(ii).coordinate;

							// new world generation in respect
							// of the predator's action
							env.worldState.removeAllElements();

							Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate, null);
							Prey pNew = new Prey("", new Coordinate(0, 0), null);
							env.worldState.add(PNew);
							env.worldState.add(pNew);

							// computation of every possible action for the prey
							// on the updated worldstate
							Vector<RandomAction> PreyAct = pNew.ProbabilityActionsSW(env.worldState);

							for (int jj = 0; jj < PreyAct.size(); jj++) {

                if (Coordinate.compareCoordinates(PNew.position, pNew.position)) {
                  currentValue = Double.POSITIVE_INFINITY;
                  break;
                }

                // If the predator moves into the prey, there is no way out
                // of this state. An immediate reward is given to the predator.
								double reward = 0;
								if (Coordinate.compareCoordinates(PredAct.elementAt(ii).coordinate, new Coordinate(0, 0))) {
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
							if (maxValue < currentValue) {
								maxValue = currentValue;
								policy[i][j] = currentAction;
							}
						}
          }
          if (!Coordinate.compareCoordinates(oldAction, policy[i][j])) {
            policyStable = false;
            instableActions++;
          }
        }
      }
      System.out.println("Iteration " + algorithmSweeps + ": " + instableActions+" Instable actions");
    } while (!policyStable);

    long end = System.currentTimeMillis();
    System.out.println("\nSweeps = " + algorithmSweeps);
    System.out.println("Execution time was " + (end - start) + "ms");

  }

	public static void PolicyIterationImplRSW(double discountFactor, double theta) {

		long start = System.currentTimeMillis();

		double[][] State = new double[6][6];
    Coordinate[][] policy = new Coordinate[6][6];
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

        for (int i = 0; i < 6; i++) {
          for (int j = i; j < 6; j++) {

            // previous value of the state
            preValue = State[i][j];
            // environment generation
            Environment env = new Environment();
            // predator generation
            Predator P = new Predator("", new Coordinate(i, j), null);
            env.worldState.add(P);

            if (policy[i][j] == null) {
              // possible action for the predator are computed
              // with respect to the current world state
              Vector<RandomAction> PredAct = P.ProbabilityActionsRSW(env.worldState);
              double rand = Math.random();
              double begin = 0;
              for (RandomAction ra : PredAct) {
                begin += ra.prob;
                if (rand <= begin) {
                  policy[i][j] = Coordinate.difference(ra.coordinate, P.position).toRSWCoordinate();
                  break;
                }
              }
            }

            // predator and prey are standing on the same position
            // absorbing state with value 0
            if (Coordinate.compareCoordinates(P.position, new Coordinate(0, 0))) {
              State[i][j] = 0;

            } else {
              double newValue = 0;

              // initialization of the worldstate removing all
              // agent from it
              env.worldState.removeAllElements();

              // a new predator is generated with a new position
              // depending on each action
              Predator PNew = new Predator("", policy[i][j], null);

              // prey is generated always in position 0,0
              Prey pNew = new Prey("", new Coordinate(0, 0), null);
              env.worldState.add(PNew);
              env.worldState.add(pNew);

              // action for the prey given the worldstate are
              // generated
              Vector<RandomAction> PreyAct = pNew.ProbabilityActionsRSW(env.worldState);

              for (int jj = 0; jj < PreyAct.size(); jj++) {

                // if the predator moves into the prey there is
                // no way out
                // for it. An immediate reward is given to the
                // predator
                double reward = 0;
                if (Coordinate.compareCoordinates(policy[i][j], new Coordinate(0, 0))) {
                  reward = 10;
                  newValue = reward;
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

                // probability of each action of the prey
                double prob = PreyAct.elementAt(jj).prob;

                // the next state of the predator will be
                // computed
                // by the above checks
                double discount = discountFactor * State[NewPredPosXNor][NewPredPosYNor];

                newValue += prob * (reward + discount);

              }
              State[i][j] = newValue;

              delta = Math.max(delta, Math.abs(preValue - newValue));

            }

          }

        }

      } while (delta > theta);
      peSweeps.add(peSweep);

      // Policy Improvement
      policyStable = true;
      instableActions = 0;

      // for every possible state of the world
			for (int i = 0; i < 6; i++) {
				for (int j = i; j < 6; j++) {
          Coordinate oldAction = policy[i][j];
					// environment generation
					Environment env = new Environment();
					// predator generation
					Predator P = new Predator("", new Coordinate(i, j), null);
					env.worldState.add(P);

					// possible action for the predator given the worldstate
					Vector<RandomAction> PredAct = P.ProbabilityActionsRSW(env.worldState);

					// predator and prey are standing on the same position
					// absorbing state with value 0
					if (Coordinate.compareCoordinates(P.position, new Coordinate(0, 0))) {
            policy[i][j] = new Coordinate(0, 0);
					} else {
						double maxValue = Double.NEGATIVE_INFINITY;
            policy[i][j] = P.position;

						for (int ii = 0; ii < PredAct.size(); ii++) {
							double currentValue = 0;
              Coordinate currentAction = PredAct.get(ii).coordinate;

							// initialization of the worldstate removing all
							// agent from it
							env.worldState.removeAllElements();

							// a new predator is generated with a new position
							// depending on each action
							Predator PNew = new Predator("", PredAct.elementAt(ii).coordinate, null);

							// prey is generated always in position 0,0
							Prey pNew = new Prey("", new Coordinate(0, 0), null);
							env.worldState.add(PNew);
							env.worldState.add(pNew);

							// action for the prey given the worldstate are
							// generated
							Vector<RandomAction> PreyAct = pNew.ProbabilityActionsRSW(env.worldState);

							for (int jj = 0; jj < PreyAct.size(); jj++) {

                if (Coordinate.compareCoordinates(PNew.position, pNew.position)) {
                  currentValue = Double.POSITIVE_INFINITY;
                  break;
                }

								// if the predator moves into the prey there is
								// no way out
								// for it. An immediate reward is given to the
								// predator
								double reward = 0;
								if (Coordinate.compareCoordinates(PredAct.elementAt(ii).coordinate, new Coordinate(0, 0))) {

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

								// probability of each action of the prey
								double prob = PreyAct.elementAt(jj).prob;

								// the next state of the predator will be
								// computed
								// by the above checks
								double discount = discountFactor
										* State[NewPredPosXNor][NewPredPosYNor];

								currentValue += prob * (reward + discount);

							}
              if (maxValue < currentValue) {
                maxValue = currentValue;
                policy[i][j] = currentAction;
              }
						}
					}
          if (!Coordinate.compareCoordinates(oldAction, policy[i][j])) {
            policyStable = false;
            instableActions++;
          }
				}
			}
			System.out.println("Iteration " + algorithmSweeps + ": " + instableActions+" Instable actions");
    } while (!policyStable);

		long end = System.currentTimeMillis();
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");

	}

	// reward function return 10 for every time predator catches the prey
	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

	// printing function of each matrix
	public static void PrintPolicyIteration(double[][] StateValues) {

		System.out.println("\n");
		for (int i = 0; i < StateValues.length; i++) {

			for (int j = 0; j < StateValues.length; j++) {

				System.out.printf(" %.4f |", StateValues[i][j]);

			}
			System.out.println("\n");
		}
	}

	// each Upper 6x6 Triangular Matrix is mirroring into a full 6x6 matrix with
	// non-zero elements in all positions
	public static void HalfQuarterMirroring(double[][] SmallWorld, Coordinate c) {

		PrintPolicyIteration(SmallWorld);

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
	// represantation
	public static void QuarterMirroring(double[][] SmallWorld, Coordinate Prey) {

		PrintPolicyIteration(SmallWorld);
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

		PrintPolicyIteration(State);

	}

}
