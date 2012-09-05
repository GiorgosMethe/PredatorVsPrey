package myPack;

import java.util.Vector;

public abstract class Agent {
	
	public String name;
	Coordinate position;
	
	
	public Agent(String name, Coordinate p) {
		super();
		this.name = name;
		this.position = p;
	}
	
	public abstract void doAction(Vector<Agent> worldState);
	
	public abstract boolean safePosition(Coordinate c, Vector<Agent> worldState);
	
	public abstract String print();
}
