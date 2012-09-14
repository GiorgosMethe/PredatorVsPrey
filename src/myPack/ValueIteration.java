package myPack;

import java.util.Map;
import java.util.Vector;

public class ValueIteration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] State = new double[11][11];

		// initialize the array
		for (int i = 0; i < State.length; i++)
			for (int j = 0; j < State.length; j++)
				State[i][j] = 0.0;

		double gamma = 0.9;
		double delta;
		int cnt = 0;
		

		do {
			
			delta = 0;
			cnt++;
			for (int i = 0; i < State.length; i++) {
				for (int j = 0; j < State.length; j++) {

					double ValuePrevious = State[i][j];

					Environment env = new Environment();
					Predator P = new Predator("Predator", new Coordinate(i, j),
							null);
					Prey p = new Prey("prey", new Coordinate(0, 0), null);
					env.addAgent(P);
					env.addAgent(p);

					Vector<RandomAction> PossiblePredatorActions = P
							.ProbabilityActions(env.worldState);
					Vector<RandomAction> PossiblePreyActions = p
							.ProbabilityActions(env.worldState);

					double max = 0;
					double ValueCurrent = 0;
					double finalValue = 0;
					
					for (int ii = 0; ii < PossiblePredatorActions.size(); ii++) {
						
						for (int jj = 0; jj < PossiblePreyActions.size(); jj++) {

							// probability of each s-prime state
							double Prob = PossiblePredatorActions.elementAt(ii).prob
									* PossiblePreyActions.elementAt(jj).prob;

							double Reward = rewardFunction(
									PossiblePredatorActions.elementAt(ii).coordinate,
									PossiblePreyActions.elementAt(jj).coordinate);

							double Discount = gamma
									* State[PossiblePredatorActions
											.elementAt(ii).coordinate.getX()][PossiblePredatorActions
											.elementAt(ii).coordinate.getY()];

							ValueCurrent = Prob * (Reward + Discount);

							max = Math.max(max, ValueCurrent);

						}

					}

					State[i][j] = max;

					delta = Math.max(delta,Math.abs(max - ValuePrevious));

				}
			}

		} while (delta > 0.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001);

		for (int i = 0; i < State.length; i++) {
			System.out.println();
			for (int j = 0; j < State.length; j++) {
				System.out.print((float) State[i][j] + " ");
			}
		}

		System.out.println("\nDelta = " + delta);
		System.out.println("\ncnt "+cnt);
	}

	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

}
