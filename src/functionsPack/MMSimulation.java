package functionsPack;

import java.util.ArrayList;
import java.util.Collection;


import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import actionPack.StateActionPair;
import agentsPack.MMAgent;
import environmentPack.Coordinate;
import environmentPack.Environment;

public class MMSimulation {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void main(String[] Args) throws OptimizationException {
		
		
		double[] min =new double[3];
		
		
		LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {1, 1, 1}, 0);
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 1, 1, 1}, Relationship.EQ, 1));
		constraints.add(new LinearConstraint(new double[] { 1, 0, 0}, Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 1, 0}, Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 1}, Relationship.GEQ, 0));

		// create and run the solver
		RealPointValuePair solution = new SimplexSolver().optimize(f, constraints, GoalType.MAXIMIZE, false);
		// get the solution
		double[] max1 = solution.getPoint();
		double max = solution.getValue();
		System.out.println(max1[0]);
		System.out.println(max1[1]);
		System.out.println(max1[2]);

	}

	public static void RunMiniMaxQlearning() {
		Environment env = new Environment();
		MMAgent Predator = new MMAgent("", new Coordinate(0, 0),
				new Coordinate(0, 0), null, 0.5, 0.7);
		MMAgent Prey = new MMAgent("", new Coordinate(5, 5),
				new Coordinate(5, 5), null, 0.5, 0.7);
		env.worldState.add(Predator);
		env.worldState.add(Prey);
		Predator.initializeQtable(env.worldState);
		Prey.initializeQtable(env.worldState);

		for (int episode = 0; episode < 100; episode++) {
			Prey.old.setX(5);
			Prey.old.setY(5);
			Prey.position.setX(5);
			Prey.position.setY(5);
			Predator.old.setX(0);
			Predator.old.setY(0);
			Predator.position.setX(0);
			Predator.position.setY(0);
			
			int steps=0;
			double reward = 0.0;
			boolean Flag = false;
			do{
				steps++;
				StateActionPair PreyAction = Prey.ChooseAction(env.worldState,
						0.1);
				Prey.old.setX(Prey.position.getX());
				Prey.old.setY(Prey.position.getY());
				if (Math.random() < 0.2) {
					Prey.position.setX(PreyAction.Action.getX());
					Prey.position.setY(PreyAction.Action.getY());
				}
				Predator.old.setX(Predator.position.getX());
				Predator.old.setY(Predator.position.getY());
				StateActionPair PredAction = Predator.ChooseAction(
						env.worldState, 0.1);
				Predator.position.setX(PredAction.Action.getX());
				Predator.position.setY(PredAction.Action.getY());

				if (Coordinate.compareCoordinates(Prey.position,
						Predator.position)) {
					System.out.println(steps);
					reward = 10.0;
					Flag = true;
				}
				Predator.UpdateMiniMax(env.worldState,PredAction,PreyAction,reward);
				
				
				
				
			}while(!Flag);

		}
	}

}
