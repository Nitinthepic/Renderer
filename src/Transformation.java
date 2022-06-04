import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	private final Matrix4f projectionMatrix;
	private final Matrix4f modelViewMatrix;
	private final Matrix4f viewMatrix;
	private final Matrix4f orthoMatrix;

	public Transformation() {
		modelViewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		orthoMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return projectionMatrix;
	}

	public final Matrix4f getModelViewMatrix(GameObj obj, Matrix4f viewMatrix) {
		Vector3f rotation = obj.getRotation();
		modelViewMatrix.identity().translate(obj.getPosition()).
				rotateX((float) Math.toRadians(-rotation.x)).
				rotateY((float) Math.toRadians(-rotation.y)).
				rotateZ((float) Math.toRadians(-rotation.z)).scale(obj.getScale());
		Matrix4f viewCurrent = new Matrix4f(viewMatrix);
		return viewCurrent.mul(modelViewMatrix);
	}

	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f cameraPosition = camera.getPosition();
		Vector3f rotation = camera.getRotation();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0
				, 0)).rotate((float) Math.toRadians(rotation.y), new Vector3f(0,
				1, 0));
		viewMatrix.translate(-cameraPosition.x, -cameraPosition.y,
				-cameraPosition.z);
		return viewMatrix;
	}

	public final Matrix4f getOrthoProjectionMatrix(float left, float right,
												   float bottom, float top) {
		orthoMatrix.identity();
		orthoMatrix.setOrtho2D(left, right, bottom, top);
		return orthoMatrix;
	}

	public final Matrix4f getOrthoProjModelMatrix(GameObj obj,
												  Matrix4f orthoMatrix) {
		Vector3f rotation = obj.getRotation();
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.identity().translate(obj.getPosition()).
				rotateX((float) Math.toRadians(-rotation.x)).
				rotateY((float) Math.toRadians(-rotation.y)).
				rotateZ((float) Math.toRadians(-rotation.z)).scale(obj.getScale());
		Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
		orthoMatrixCurr.mul(modelMatrix);
		return orthoMatrixCurr;
	}
}
