import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;


public class Renderer {
	private Shader shaderProg;
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_Near = 0.01f;
	private static final float Z_Far = 1000.f;
	private final Transformation transformation;

	public Renderer() {
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		shaderProg = new Shader();
		shaderProg.createVertexShader(Utils.loadResource("/resources/vertex" +
				".vs"));
		shaderProg.createFragmentShader(Utils.loadResource("/resources" +
				"/fragment.fs"));
		shaderProg.link();

		shaderProg.createUniform("projectionMatrix");
		shaderProg.createUniform("modelViewMatrix");
		shaderProg.createUniform("texture_sampler");
		shaderProg.createUniform("color");
		shaderProg.createUniform("useColor");
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void cleanup() {
		if (shaderProg != null) {
			shaderProg.cleanup();
		}
	}

	public void render(Window window, Camera camera, GameObj[] gameItems) {
		clear();
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shaderProg.bind();
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV,
				window.getWidth(), window.getHeight(), Z_Near, Z_Far);
		shaderProg.setUniform("projectionMatrix", projectionMatrix);

		Matrix4f viewMatrix = transformation.getViewMatrix(camera);

		shaderProg.setUniform("texture_sampler", 0);
		for (GameObj item : gameItems) {
			Mesh mesh = item.getMesh();
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);

			shaderProg.setUniform("modelViewMatrix", modelViewMatrix);
			shaderProg.setUniform("color", mesh.getColor());
			shaderProg.setUniform("useColor", mesh.isTextured() ? 0 : 1);

			mesh.render();
		}
		shaderProg.unbind();
	}


}
