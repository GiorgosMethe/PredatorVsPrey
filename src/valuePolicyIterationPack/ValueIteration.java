package valuePolicyIterationPack;

import java.util.Vector;

import actionPack.RandomAction;
import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class ValueIteration {

	public static void main(String[] args) {

		ValueIterationImpl(new Coordinate(0, 0), 0.7);

	}

	public static void ValueIterationImpl(Coordinate PreyPosition,
			double discountFactor) {

		long start = System.currentTimeMillis();

		double[][][][] State = new double[11][11][11][11];
		double delta;
		double preValue;
		int algorithmSweeps = 0;

		do {

			algorithmSweeps++;

			delta = 0;

			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					for (int x = 0; x < 11; x++) {
						for (int y = 0; y < 11; y++) {

							preValue = State[i][j][x][y];
							Environment env = new Environment();
							Predator P = new Predator("", new Coordinate(i, j),
									null);
							Prey p = new Prey("", new Coordinate(x, y), null);
							env.worldState.add(P);
							env.worldState.add(p);

							Vector<RandomAction> PredAct = P
									.ProbabilityActions(env.worldState);
							Vector<RandomAction> PreyAct = p
									.ProbabilityActions(env.worldState);

							if (Coordinate.compareCoordinates(P.position,
									p.position)) {
								State[i][j][x][y] = 0;

							} else {

								double max = Double.NEGATIVE_INFINITY;
								
								for (int ii = 0; ii < PredAct.size(); ii++){
									
									double currentValue = 0;

//									double reward = 0;
//									if (Coordinate.compareCoordinates(
//											PredAct.elementAt(ii).coordinate,
//											p.position)) {
//										reward = 10;
//
//									}

									for (int jj = 0; jj < PreyAct.size(); jj++) {

										double prob = PreyAct.elementAt(jj).prob;
										
										double reward = rewardFunction(PredAct.elementAt(ii).coordinate,
												PreyAct.elementAt(jj).coordinate);
										
										double discount  = discountFactor *  State[PredAct.get(ii).coordinate
																					.getX()][PredAct
																								.get(ii).coordinate
																								.getY()][PreyAct
																								.get(jj).coordinate
																								.getX()][PreyAct
																								.get(jj).coordinate
																								.getY()];

										
										currentValue +=prob *(reward+discount); 
//										currentValue += (prob * (reward + discountFactor
//												* State[PredAct.get(ii).coordinate
//														.getX()][PredAct
//														.get(ii).coordinate
//														.getY()][PreyAct
//														.get(jj).coordinate
//														.getX()][PreyAct
//														.get(jj).coordinate
//														.getY()]));

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

		} while (delta > 0.0001);

		long end = System.currentTimeMillis();
		System.out.println("Normal 11x11 World Implementation");
		System.out.println("\nSweeps = " + algorithmSweeps);
		System.out.println("Execution time was " + (end - start) + "ms");
		PrintValueIteration(State);

	}

	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

	public static void PrintValueIteration(double[][][][] StateValues) {
		int cnt = 0;
		// for (int i = 0; i < StateValues.length; i++) {
		// System.out.println();
		// for (int j = 0; j < StateValues.length; j++) {
		for (int ii = 0; ii < StateValues.length; ii++) {
			System.out.println();
			for (int jj = 0; jj < StateValues.length; jj++) {

				System.out.print(" " + (float) StateValues[5][5][ii][jj] + " ");

			}
			// }

			// }
		}
		System.out.println("" + cnt);

	}

}