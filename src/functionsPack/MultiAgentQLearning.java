package functionsPack;
import actionPack.StateActionPair;
import agentsPack.QPredator2;
import agentsPack.QPrey;
import agentsPack.Vector;
import environmentPack.Coordinate;
import environmentPack.Environment;
import agentsPack.Agent;

public class MultiAgentQLearning {

	private	QPredator2 qpred = new QPredator2("a",new Coordinate(0,0),null,0.1,0.7);
	
	private	QPrey qprey = new QPrey("p", new Coordinate(5,5), null, 0.1, 0.7);
	
	private Environment env = new Environment();
	Vector<Agent> worldState = new Vector<Agent>();

	
	
	
	public MultiAgentQLearning() {

		
		
	}

		
	public  void  run(){
		
		worldState.add(qpred);
		worldState.add(qprey);
		
		qpred.initializeQTable();
		qprey.initializeQTable();
		int steps =0;
		
		do{
			Coordinate predOldPosition = qpred.position;
			Coordinate preyOldPosition = qprey.position;
			
			StateActionPair predSAP = qpred.chooseEGreedyAction(0.1);
			StateActionPair preySAP = qprey.chooseEGreedyAction(0.1);
			
			qpred.position = predSAP.Action;
			qprey.position = preySAP.Action;
			
			if(env.checkCaught(worldState)){
				
				qpred.updateQTable(predOldPosition, predSAP, 10.0, true);
				qprey.updateQTable(preyOldPosition, preySAP, -10.0, true);
				qprey.kill();
				
			}
			else{

				qpred.updateQTable(predOldPosition, predSAP, 0.0, false);
				qprey.updateQTable(preyOldPosition, preySAP, 0.0, false);
				
			}
				
			steps++;
			
		}while (qprey.isAlive());
		
		System.out.println(steps);
		System.out.println(qpred.position);
		
		qpred.PrintQTable();
		System.out.println("--------------------");
		qprey.PrintQTable();
		
	}
	
	
	
}
