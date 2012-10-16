package actionPack;


public class MMStateActionPair {

	public StateActionPair myAction;
	public StateActionPair otherAction;
	public double Value;
	public int id;

	public MMStateActionPair(StateActionPair aa, StateActionPair oAa,
			double value, int id) {
		this.myAction = aa;
		this.otherAction = oAa;
		this.Value = value;
		this.id = id;
	}
}
