package myPack;

import java.util.Vector;



public class Predator extends Agent {

	public class RandomAction {
		public double prob;
		public Coordinate coordinate;
		
		public RandomAction(double p, Coordinate c) {
			this.prob = p;
			this.coordinate = c;
		}
	}
	
	private Vector<RandomAction> actions = new Vector<RandomAction>();
	
	public Predator(String name, Coordinate p) {
		super(name, p);
		
		this.actions.addElement(new RandomAction(0.2, this.position.getNorth()));
		this.actions.addElement(new RandomAction(0.2, this.position.getEast()));
		this.actions.addElement(new RandomAction(0.2, this.position.getWest()));
		this.actions.addElement(new RandomAction(0.2, this.position.getSouth()));
		this.actions.addElement(new RandomAction(0.2, this.position));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doAction(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		
		double prob = Math.random();
		double boundary = 0.0;
		System.out.println("actionStarts with prob: " + prob);
		for(int i=0;i<this.actions.size();i++){
			System.out.println("itnernal i"+ i) ;
			boundary += this.actions.get(i).prob;
			System.out.println("prob  and boundary "+this.actions.get(i).prob +"  "+ boundary);
			if (prob <= boundary) {
				if (this.safePosition(this.actions.get(i).coordinate, worldState)) {
					this.position = this.actions.get(i).coordinate;
					for (int j = 0; j < worldState.size(); i++) {
						if (Coordinate.compareCoordinates(worldState.get(j).position, this.position) && worldState.get(j) instanceof Prey) {
							worldState.removeElementAt(j);
						}
					}
					break;
				}
				else {
					System.out.println("actionDecided");
					this.doAction(worldState);
					break;
				}
			}
		}

		
	}
	
	
	@Override
	public boolean safePosition(Coordinate c, Vector<Agent> worldState) {
		
		for(int i=0;i<worldState.size();i++){
			Agent a = worldState.elementAt(i);
			if (a != this && a instanceof Predator && Coordinate.compareCoordinates(c, a.position)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String print() {
		return "Predator(\"" + this.name + ", <" + this.position.getX() + ", " + this.position.getY() + ">)";
	}
}
