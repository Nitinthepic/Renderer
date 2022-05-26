import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Map;


public class Renderer {
	private Shader shaderProg;
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_Near = 0.01f;
	private static final float Z_Far = 1000.f;

	private Matrix4f projectionMatrix;


	public void init(Window window) throws Exception {
		shaderProg = new Shader();
		shaderProg.createVertexShader(Utils.loadResource("/resources/vertex" +
				".vs"));
		shaderProg.createFragmentShader(Utils.loadResource("/resources" +
				"/fragment.fs"));
		shaderProg.link();

		float aspectRatio = (float) window.getWidth() / window.getHeight();
		projectionMatrix = new Matrix4f().perspective(Renderer.FOV,
				aspectRatio, Renderer.Z_Near, Renderer.Z_Far);
		shaderProg.createUniform("projectionMatrix");
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void cleanup() {
		if (shaderProg != null) {
			shaderProg.cleanup();
		}
	}

	public void render(Window window, Mesh mesh) {
		clear();
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shaderProg.bind();
		shaderProg.setUniform("projectionMatrix", projectionMatrix);

		glBindVertexArray(mesh.getVaoID());


		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		System.out.println("woof");

		glBindVertexArray(0);

		shaderProg.unbind();
	}
}
