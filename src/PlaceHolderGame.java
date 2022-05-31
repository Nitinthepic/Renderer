import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glViewport;

public class PlaceHolderGame implements ILogic {


	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;
	private final Camera camera;


	private static final float CAMERA_POS_STEP = 0.05f;

	private final Renderer renderer;
	private GameObj[] gameItems;


	public PlaceHolderGame() {
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		// Create the Mesh
		float[] positions = new float[] {
				// V0
				-0.5f, 0.5f, 0.5f,
				// V1
				-0.5f, -0.5f, 0.5f,
				// V2
				0.5f, -0.5f, 0.5f,
				// V3
				0.5f, 0.5f, 0.5f,
				// V4
				-0.5f, 0.5f, -0.5f,
				// V5
				0.5f, 0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f,

				// For text coords in top face
				// V8: V4 repeated
				-0.5f, 0.5f, -0.5f,
				// V9: V5 repeated
				0.5f, 0.5f, -0.5f,
				// V10: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V11: V3 repeated
				0.5f, 0.5f, 0.5f,

				// For text coords in right face
				// V12: V3 repeated
				0.5f, 0.5f, 0.5f,
				// V13: V2 repeated
				0.5f, -0.5f, 0.5f,

				// For text coords in left face
				// V14: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V15: V1 repeated
				-0.5f, -0.5f, 0.5f,

				// For text coords in bottom face
				// V16: V6 repeated
				-0.5f, -0.5f, -0.5f,
				// V17: V7 repeated
				0.5f, -0.5f, -0.5f,
				// V18: V1 repeated
				-0.5f, -0.5f, 0.5f,
				// V19: V2 repeated
				0.5f, -0.5f, 0.5f,
		};
		float[] textCoords = new float[]{
				0.0f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.5f, 0.0f,

				0.0f, 0.0f,
				0.5f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,

				// For text coords in top face
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.0f, 1.0f,
				0.5f, 1.0f,

				// For text coords in right face
				0.0f, 0.0f,
				0.0f, 0.5f,

				// For text coords in left face
				0.5f, 0.0f,
				0.5f, 0.5f,

				// For text coords in bottom face
				0.5f, 0.0f,
				1.0f, 0.0f,
				0.5f, 0.5f,
				1.0f, 0.5f,
		};
		int[] indices = new int[]{
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				8, 10, 11, 9, 8, 11,
				// Right face
				12, 13, 7, 5, 12, 7,
				// Left face
				14, 15, 6, 4, 14, 6,
				// Bottom face
				16, 18, 19, 17, 16, 19,
				// Back face
				4, 6, 7, 5, 4, 7,};
		Texture texture = new Texture("textures/morb.png");
		Mesh mesh = new Mesh(positions, textCoords, indices,texture);
		GameObj GameObj1 = new GameObj(mesh);
		GameObj1.setScale(0.5f);
		GameObj1.setPosition(0, 0, -2);
		GameObj GameObj2 = new GameObj(mesh);
		GameObj2.setScale(0.5f);
		GameObj2.setPosition(0.5f, 0.5f, -2);
		GameObj GameObj3 = new GameObj(mesh);
		GameObj3.setScale(0.5f);
		GameObj3.setPosition(0, 0, -2.5f);
		GameObj GameObj4 = new GameObj(mesh);
		GameObj4.setScale(0.5f);
		GameObj4.setPosition(0.5f, 0, -2.5f);
		gameItems = new GameObj[]{GameObj1, GameObj2, GameObj3, GameObj4};
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0,0,0);
		if(window.isKeyPressed(GLFW_KEY_W)){
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if(window.isKeyPressed(GLFW_KEY_A)){
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if(window.isKeyPressed(GLFW_KEY_Z)){
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			cameraInc.y = 1;
		}
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
		if (mouseInput.isRightClick()) {
			Vector2f rotVec = mouseInput.getDisplacementVector();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for (GameObj obj: gameItems) {
			obj.getMesh().cleanUp();
		}
	}
}
