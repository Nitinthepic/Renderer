import java.util.Arrays;

public class Vector {
	public int[] getCoordinates() {
		return coordinates;
	}

	private int[] coordinates;

	public Vector(int x, int y, int z){
		coordinates = new int[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}


	@Override
	public String toString() {
		return Arrays.toString(coordinates);
	}

	public Vector addVectorToVector(Vector addedVector){
		return new Vector(this.coordinates[0]+addedVector.getCoordinates()[0],
				this.coordinates[1]+addedVector.getCoordinates()[1],
				this.coordinates[2]+addedVector.getCoordinates()[2]);
	}

	public Vector subtractVectorFromVector(Vector addedVector){
		return new Vector(this.coordinates[0]-addedVector.getCoordinates()[0],
				this.coordinates[1]-addedVector.getCoordinates()[1],
				this.coordinates[2]-addedVector.getCoordinates()[2]);
	}

}
