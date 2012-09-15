package valuePolicyIterationPack;

import java.util.Vector;

import actionPack.RandomAction;
import agentsPack.Predator;
import agentsPack.Prey;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class ValueIteration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ValueIteration11NEw();

	}

	public static void ValueIteration11NEw() {
		// TODO Auto-generated method stub
		double[][] State = new double[11][11];

		// initialize the array
		for (int i = 0; i < State.length; i++)
			for (int j = 0; j < State.length; j++)
				State[i][j] = 0.0;

		double gamma = 0.7;
		double delta;
		double ValuePrevious;
		int cnt = 0;

		do {

			delta = 0;
			cnt++;
			System.out.println();
			System.out.println("--new swift");
			System.out.println();
			for (int i = 0; i < State.length; i++) {
				for (int j = 0; j < State.length; j++) {
					
					//System.out.println("++new state");

					ValuePrevious = State[i][j];

					Environment env = new Environment();
					Predator P = new Predator("Predator", new Coordinate(i, j),
							null);
					Prey p = new Prey("prey", new Coordinate(5, 5), null);
					env.addAgent(P);
					env.addAgent(p);

					Vector<RandomAction> PossiblePredatorActions = P
							.ProbabilityActions(env.worldState);
					Vector<RandomAction> PossiblePreyActions = p
							.ProbabilityActions(env.worldState);
					
					if (rewardFunction(P.position, p.position) > 0) {

						State[i][j] = rewardFunction(P.position, p.position);

					} else {

						double max = 0;
						for (int ii = 0; ii < PossiblePredatorActions.size(); ii++) {
							double ValueCurrent = 0;
							for (int jj = 0; jj < PossiblePreyActions.size(); jj++) {

								double prob = PossiblePredatorActions
										.elementAt(ii).prob
										* PossiblePreyActions.elementAt(jj).prob;

								double reward = rewardFunction(
										PossiblePredatorActions.elementAt(ii).coordinate,
										PossiblePreyActions.elementAt(jj).coordinate);

								double discount = gamma
										* State[PossiblePredatorActions
												.elementAt(ii).coordinate
												.getX()][PossiblePredatorActions
												.elementAt(ii).coordinate
												.getY()];

								ValueCurrent += prob * (reward + discount);
							}

							max = Math.max(max, ValueCurrent);
							
							
						}
						
						State[i][j] = max;
						delta = Math.max(delta, Math.abs(max-ValuePrevious));
						
						
					}					
				}
			}
			
			System.out.println(delta);

		} while (delta > Math.pow(10, -4));

		for (int i = 0; i < State.length; i++) {
			System.out.println();
			for (int j = 0; j < State.length; j++) {
				System.out.print((float) State[i][j] + " ");
			}
		}

		System.out.println("\nDelta = " + delta);
		System.out.println("\ncnt " + cnt);
	}

	public static double rewardFunction(Coordinate a1, Coordinate a2) {

		if ((Coordinate.compareCoordinates(a1, a2))) {
			return 10;
		}

		return 0;

	}

}
