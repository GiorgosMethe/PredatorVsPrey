package myPack;

public class Action {

	public Agent agent;
	public Coordinate NewPosition;

	public Action(Agent agent, Coordinate newPosition) {
		this.agent = agent;
		this.NewPosition = newPosition;
	}
	
	@Override
	public String toString() {
		try {
			return "Action{" + this.agent.toString() + ", " + this.NewPosition.toString() + "}";
		}
		catch (NullPointerException e) {
			return "Oh no! :<";
		}
	}

}
