package myPack;

import java.util.Vector;

public class Prey extends Agent {

	public class RandomAction {
		public double prob;
		public Coordinate coordinate;
		
		public RandomAction(double p, Coordinate c) {
			this.prob = p;
			this.coordinate = c;
		}
	}
	
	private Vector<RandomAction> actions = new Vector<RandomAction>();
	
	public Prey(String name, Coordinate p) {
		super(name, p);
		
		this.actions.addElement(new RandomAction(0.05, this.position.getNorth()));
		this.actions.addElement(new RandomAction(0.05, this.position.getEast()));
		this.actions.addElement(new RandomAction(0.05, this.position.getWest()));
		this.actions.addElement(new RandomAction(0.05, this.position.getSouth()));
		this.actions.addElement(new RandomAction(0.8, this.position));
		// TODO Auto-generated constructor stub
	}
	 

	@Override
	public void doAction(Vector<Agent> worldState) {
		// TODO Auto-generated method stub
		
		
		System.out.println("actionStarts");
		double prob = Math.random();
		double boundary = 0.0;
		for(int i=0;i<this.actions.size();i++){
			boundary += this.actions.get(i).prob;
			if (prob <= boundary) {
				if (this.safePosition(this.actions.get(i).coordinate, worldState)) {
					System.out.println("actionDecided");
					this.position = this.actions.get(i).coordinate;
					break;
				}
				else {
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
			if (a != this && Coordinate.compareCoordinates(c, a.position)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String print() {
		return "Prey(\"" + this.name + ", <" + this.position.getX() + ", " + this.position.getY() + ">)";
	}

}
