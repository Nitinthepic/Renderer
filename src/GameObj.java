import org.joml.Vector3f;

public class GameObj {
	private Mesh mesh;
	private final Vector3f position;
	private float scale;
	private final Vector3f rotation;

	public GameObj(Mesh mesh) {
		this();
		this.mesh = mesh;
	}

	public GameObj() {
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(float x , float y , float z){
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
}
