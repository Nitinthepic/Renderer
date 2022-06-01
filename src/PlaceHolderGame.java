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

	private PointLight[] pointLightList;

	private SpotLight[] spotLightList;

	private DirectionalLight directionalLight;

	private float lightAngle;

	private static final float CAMERA_POS_STEP = 0.05f;

	private float spotAngle = 0;

	private float spotInc = 1;


	public PlaceHolderGame() {
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
		lightAngle = -90;
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);

		float reflectance = 1f;


		Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
		Texture texture = new Texture("textures/grassblock.png");
		Material material = new Material(texture, reflectance);

		mesh.setMaterial(material);
		GameObj gameItem = new GameObj(mesh);
		gameItem.setScale(0.5f);
		gameItem.setPosition(0, 0, -2);


		Mesh mesh1 = OBJLoader.loadMesh("models/shine.obj");
		Material material1 = new Material(new Vector4f(0.54f, 0.46f, 0f, 0f), 10f);
		mesh1.setMaterial(material1);
		GameObj gameObj = new GameObj(mesh1);

		gameObj.setScale(1f);
		gameObj.setPosition(8, 0, -4);

		gameItems = new GameObj[]{gameItem, gameObj};

		ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

		// Point Light
		Vector3f lightPosition = new Vector3f(0, 0, 1);
		float lightIntensity = 10.0f;
		PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
		PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
		pointLight.setAttenuation(att);
		pointLightList = new PointLight[]{pointLight};

		// Spot Light
		lightPosition = new Vector3f(0, 0.0f, 10f);
		pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
		att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
		pointLight.setAttenuation(att);
		Vector3f coneDir = new Vector3f(0, 0, -1);
		float cutoff = (float) Math.cos(Math.toRadians(140));
		SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
		spotLightList = new SpotLight[]{spotLight, new SpotLight(spotLight)};

		lightPosition = new Vector3f(-1, 0, 0);
		directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
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

		float lightPos = spotLightList[0].getPointLight().getPosition().z;
		if (window.isKeyPressed(GLFW_KEY_N)) {
			this.spotLightList[0].getPointLight().getPosition().z =
					lightPos + 0.1f;
		} else if (window.isKeyPressed(GLFW_KEY_M)) {
			this.spotLightList[0].getPointLight().getPosition().z = lightPos - 0.1f;
		}
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
		if (mouseInput.isRightClick()) {
			Vector2f rotVec = mouseInput.getDisplacementVector();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
		updateLight();
		System.out.println(camera.getPosition());
	}

	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems, ambientLight,
				pointLightList, spotLightList,
				directionalLight);
	}

	private void updateLight() {

		spotAngle += spotInc * 0.05f;
		if (spotAngle > 2) {
			spotInc = -1;
		} else if (spotAngle < -2) {
			spotInc = 1;
		}

		double spotAngleRad = Math.toRadians(spotAngle);
		Vector3f coneDir = spotLightList[0].getConeDirection();
		coneDir.y = (float) Math.sin(spotAngleRad);

		lightAngle += 1.1f;
		if (lightAngle > 90) {
			directionalLight.setIntensity(0);
			if (lightAngle >= 360) {
				lightAngle = -90;
			}
		} else if (lightAngle <= -80 || lightAngle >= 80) {
			float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10f;
			directionalLight.setIntensity(factor);
			directionalLight.getColor().y = Math.max(factor, 0.9f);
			directionalLight.getColor().z = Math.max(factor, 0.5f);
		} else {
			directionalLight.setIntensity(1);
			directionalLight.getColor().x = 1;
			directionalLight.getColor().y = 1;
			directionalLight.getColor().z = 1;
		}
		double angleInRadians = Math.toRadians(lightAngle);
		directionalLight.getDirection().x = (float) Math.sin(angleInRadians);
		directionalLight.getDirection().y = (float) Math.cos(angleInRadians);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		if (gameItems != null) {
			for (GameObj obj : gameItems) {
				obj.getMesh().cleanUp();
			}
		}
	}
}
