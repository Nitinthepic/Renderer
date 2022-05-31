import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;


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

		Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
		Texture texture = new Texture("textures/grassblock.png");
		mesh.setTexture(texture);
		GameObj gameItem = new GameObj(mesh);
		gameItem.setScale(0.5f);
		gameItem.setPosition(0, 0, -2);

		Mesh mesh1 = OBJLoader.loadMesh("/models/bunny.obj");
		GameObj gameItem1 = new GameObj(mesh1);
		gameItem.setScale(1.5f);
		gameItem.setPosition(0, 0, -4);

		gameItems = new GameObj[]{gameItem, gameItem1};
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {
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
		System.out.println(camera.getPosition());
	}

	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		if(gameItems!=null) {
			for (GameObj obj : gameItems) {
				obj.getMesh().cleanUp();
			}
		}
	}
}
