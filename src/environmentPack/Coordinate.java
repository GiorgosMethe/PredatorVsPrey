package environmentPack;

public class Coordinate {

	private int x, y;

	public Coordinate(int x, int y) {
		
		super();
		this.x = (x + 11) % 11;
		this.y = (y + 11) % 11;
		
	}	

	public static boolean CoordinateSW(Coordinate a) {
		
		if(a.x >=0 && a.x <= 5){
			if(a.y >=0 && a.y <= 5){
				return true;
			}
		}
		return false;
	
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = (x + 11) % 11;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = (y + 11) % 11;
	}

	public static boolean compareCoordinates(Coordinate c1, Coordinate c2) {

		return (c1.x == c2.x && c1.y == c2.y);

	}

	public static Coordinate difference(Coordinate c1, Coordinate c2) {
		return new Coordinate(c1.x - c2.x, c1.y - c2.y);
	}

	public Coordinate getNorth() {

		return new Coordinate(x - 1, y);

	}

	public Coordinate getSouth() {

		return new Coordinate(x + 1, y);

	}

	public Coordinate getEast() {

		return new Coordinate(x, y + 1);

	}

	public Coordinate getWest() {

		return new Coordinate(x, y - 1);

	}
	
	public int toIndex() {
		return 11 * x + y;
	}
	
	@Override
	public String toString() {
		return "<" + this.x + ", " + this.y + ">";
	}

}
