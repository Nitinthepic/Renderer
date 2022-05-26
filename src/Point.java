import java.awt.*;
import java.util.Arrays;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Point {
	public int[] getCoordinates() {
		return coordinates;
	}

	private int[] coordinates;

	public Point(int x, int y, int z) {
		coordinates = new int[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}

	public Point addVectorToPoint(Vector addedVector) {
		return new Point(this.coordinates[0] + addedVector.getCoordinates()[0],
				this.coordinates[1] + addedVector.getCoordinates()[1],
				this.coordinates[2] + addedVector.getCoordinates()[2]);
	}

	public Point subtractVectorFromPoint(Vector subtractedVector) {
		return new Point(this.coordinates[0] - subtractedVector.getCoordinates()[0],
				this.coordinates[1] - subtractedVector.getCoordinates()[1],
				this.coordinates[2] - subtractedVector.getCoordinates()[2]);
	}

	public Vector subtractPointFromPoint(Point subtractedPoint) {
		return new Vector(this.coordinates[0] - subtractedPoint.getCoordinates()[0],
				this.coordinates[1] - subtractedPoint.getCoordinates()[1],
				this.coordinates[2] - subtractedPoint.getCoordinates()[2]);
	}

	public void SetPointToPoint(Point newPoint) {
		this.coordinates = newPoint.coordinates.clone();
	}

	public void drawPoint() {
		glPointSize(10f);
		glBegin(GL_POINTS);
		glVertex3iv(coordinates);
		glEnd();
	}

	@Override
	public String toString() {
		return Arrays.toString(coordinates);
	}
}
