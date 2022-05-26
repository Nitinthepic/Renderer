import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11C.glViewport;

public class PlaceHolderGame implements ILogic {
	private int direction = 0;

	private float color = 0.0f;

	private final Renderer renderer;
	private Mesh mesh;

	public PlaceHolderGame() {
		renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		float[] positions = new float[]{
				-0.5f,  0.5f, -100.5f,
				-0.5f, -0.5f, -100.5f,
				0.5f, -0.5f, -100.5f,
				0.5f,  0.5f, -100.5f,
		};
		int[] indices = new int[]{0, 1, 3, 3, 1, 2};
		float[] colors = new float[]{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		mesh = new Mesh(positions, colors, indices);
	}

	@Override
	public void input(Window window) {
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			direction = 1;
		} else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			direction = -1;
		} else {
			direction = 0;
		}
	}

	@Override
	public void update(float interval) {
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		} else if (color < 0) {
			color = 0.0f;
		}
	}

	@Override
	public void render(Window window) {
		window.setClearColor(color, color, color, 0.0f);
		System.out.println("dog");
		renderer.render(window, mesh);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		mesh.cleanUp();
	}
}
