import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glViewport;

public class PlaceHolderGame implements ILogic {

	private int displxInc = 0;

	private int displyInc = 0;

	private int displzInc = 0;

	private int scaleInc = 0;
	private float color = 0.0f;

	private final Renderer renderer;
	private GameObj[] gameItems;
	private Mesh mesh;

	public PlaceHolderGame() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		float[] positions = new float[] {
				// VO
				-0.5f,  0.5f,  0.5f,
				// V1
				-0.5f, -0.5f,  0.5f,
				// V2
				0.5f, -0.5f,  0.5f,
				// V3
				0.5f,  0.5f,  0.5f,
				// V4
				-0.5f,  0.5f, -0.5f,
				// V5
				0.5f,  0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f,
		};
		int[] indices = new int[] {
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				4, 0, 3, 5, 4, 3,
				// Right face
				3, 2, 7, 5, 3, 7,
				// Left face
				6, 1, 0, 6, 0, 4,
				// Bottom face
				2, 1, 6, 2, 6, 7,
				// Back face
				7, 6, 4, 7, 4, 5,
		};
		float[] colors = new float[]{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		mesh = new Mesh(positions, colors, indices);
		GameObj item = new GameObj(mesh);
		item.setPosition(0,0,-2);
		gameItems = new GameObj[]{item};
	}

	@Override
	public void input(Window window) {
		displyInc = 0;
		displxInc = 0;
		displzInc = 0;
		scaleInc = 0;
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			displyInc = 1;
		} else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			displyInc = -1;
		} else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			displxInc = -1;
		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			displxInc = 1;
		} else if (window.isKeyPressed(GLFW_KEY_A)) {
			displzInc = -1;
		} else if (window.isKeyPressed(GLFW_KEY_Q)) {
			displzInc = 1;
		} else if (window.isKeyPressed(GLFW_KEY_Z)) {
			scaleInc = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			scaleInc = 1;
		}
	}

	@Override
	public void update(float interval) {
		for (GameObj gameItem : gameItems) {
			// Update position
			Vector3f itemPos = gameItem.getPosition();
			float posx = itemPos.x + displxInc * 0.01f;
			float posy = itemPos.y + displyInc * 0.01f;
			float posz = itemPos.z + displzInc * 0.01f;
			gameItem.setPosition(posx, posy, posz);

			// Update scale
			float scale = gameItem.getScale();
			scale += scaleInc * 0.05f;
			if ( scale < 0 ) {
				scale = 0;
			}
			gameItem.setScale(scale);

			// Update rotation angle
			float rotation = gameItem.getRotation().x + 1.5f;
			if ( rotation > 360 ) {
				rotation = 0;
			}
			gameItem.setRotation(rotation, rotation, rotation);
		}
	}

	@Override
	public void render(Window window) {
		window.setClearColor(color, color, color, 0.0f);
		renderer.render(window, gameItems );
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		mesh.cleanUp();
	}
}
