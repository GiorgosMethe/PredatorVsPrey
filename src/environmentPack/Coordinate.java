package environmentPack;

public class Coordinate {

	protected int divisor, x, y;

	protected Coordinate(int divisor, int x, int y) {
		super();
		this.divisor = 11;
		this.setX(x);
		this.setY(y);
	}

	public Coordinate(int x, int y) {
		this(11, x, y);
	}

	public Coordinate(Coordinate c) {
		this(11, c.getX(), c.getY());
	}

	public static boolean CoordinateSW(Coordinate a) {

		if (a.x >= 0 && a.x <= 5) {
			if (a.y >= 0 && a.y <= 5) {
				return true;
			}
		}

		return false;
	}
	
	public Coordinate toSWCoordinate() {
		if (Coordinate.CoordinateSW(this)) {
			return this;
		}
		return new Coordinate((this.x + 6) % 6, (this.y + 6) % 6);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = (x + this.divisor) % this.divisor;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = (y + this.divisor) % this.divisor;
	}

	public static boolean compareCoordinates(Coordinate c1, Coordinate c2) {

		return (c1.x == c2.x && c1.y == c2.y);

	}

	public static Coordinate difference(Coordinate c1, Coordinate c2) {
		return new Coordinate(c1.x - c2.x, c1.y - c2.y);
	}
	
	public static Coordinate sum(Coordinate c1, Coordinate c2) {
		return new Coordinate(c1.x + c2.x, c1.y + c2.y);
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
		return this.divisor * x + y;
	}

	@Override
	public String toString() {
		return "<" + this.x + ", " + this.y + ">";
	}

}
