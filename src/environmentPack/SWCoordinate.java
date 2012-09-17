package environmentPack;

public class SWCoordinate extends Coordinate {

	public SWCoordinate(int x, int y) {
		super(6, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public SWCoordinate(Coordinate c) {
		super(6, c.getX(), c.getY());
	}

}
