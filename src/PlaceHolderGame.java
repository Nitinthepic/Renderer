import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;


public class PlaceHolderGame implements ILogic {


	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;

	private final Renderer renderer;

	private final Camera camera;

	private GameObj[] gameItems;

	private Vector3f ambientLight;

	private PointLight pointLight;

	private static final float CAMERA_POS_STEP = 0.05f;


	public PlaceHolderGame() {
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		float reflectance = 10f;
		Mesh mesh3 = OBJLoader.loadMesh("/models/shine.obj");
		Material material3 = new Material(new Vector4f(0.5f, 0.5f, 0.0f, 0),
				100f);
		GameObj gameObj3 = new GameObj(mesh3);
		mesh3.setMaterial(material3);
		gameObj3.setPosition(2, 0, -2);
		gameObj3.setScale(1f);


		Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
		Texture texture = new Texture("textures/grassblock.png");
		Material material = new Material(texture, reflectance);

		mesh.setMaterial(material);
		GameObj GameObj = new GameObj(mesh);
		GameObj.setScale(0.5f);
		GameObj.setPosition(0, 0, -2);

		Mesh mesh1 = OBJLoader.loadMesh("/models/cube.obj");
		Texture texture1 = new Texture("textures/grassblock.png");
		Material material1 = new Material(texture1, reflectance);

		mesh1.setMaterial(material1);
		GameObj GameObj1 = new GameObj(mesh);
		GameObj1.setScale(0.5f);
		GameObj1.setPosition(1, 0, -2);
		gameItems = new GameObj[]{GameObj, GameObj1, gameObj3};

		ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
		Vector3f lightColour = new Vector3f(1, 1, 1);
		Vector3f lightPosition = new Vector3f(0, 0, 1);
		float lightIntensity = 1.0f;
		pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
		PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
		pointLight.setAttenuation(att);
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

		float lightPos = pointLight.getPosition().z;
		if (window.isKeyPressed(GLFW_KEY_N)) {
			this.pointLight.getPosition().z = lightPos + 0.1f;
		} else if (window.isKeyPressed(GLFW_KEY_M)) {
			this.pointLight.getPosition().z = lightPos - 0.1f;
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
		renderer.render(window, camera, gameItems, ambientLight, pointLight);
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
