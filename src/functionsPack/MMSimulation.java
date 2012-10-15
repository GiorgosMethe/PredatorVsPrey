package functionsPack;

import environmentPack.Coordinate;
import agentsPack.Agent;
import agentsPack.MMPredator;
import agentsPack.MQPrey;
import agentsPack.Vector;

public class MMSimulation {

	public static void main(String[] Args){


	}
	
	public void RunMiniMaxQlearning(){
		Vector<Agent> worldstate = new Vector<Agent>();
		MMPredator a = new MMPredator("", new Coordinate(0, 0), new Coordinate(0, 0), null, 0.5, 0.7);
		MQPrey a1 = new MQPrey("", new Coordinate(5, 5), null, 0.5, 0.7);
		worldstate.add(a);
		worldstate.add(a1);
	}

}
