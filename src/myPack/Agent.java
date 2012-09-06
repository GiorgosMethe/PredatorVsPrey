package myPack;

import java.util.Vector;

public abstract class Agent {

	public boolean lives;
	public String name;
	Coordinate position;
	
	
	public Agent(String name, Coordinate p) {
		super();
		this.lives = true;
		this.name = name;
		this.position = p;
	}
	
	public boolean isAlive() {
		return this.lives;
	}
	
	public boolean isDead() {
		return !this.lives;
	}
	
	public void kill() {
		this.lives = false;
	}
	
	public abstract Agent doAction(Vector<Agent> worldState);
	
	public abstract boolean safePosition(Coordinate c, Vector<Agent> worldState);
	
	public abstract String print();
}
