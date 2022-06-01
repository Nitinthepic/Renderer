import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {
	private Shader shaderProg;
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_Near = 0.01f;
	private static final float Z_Far = 1000.f;
	private final Transformation transformation;
	private float specularPower;

	public Renderer() {
		transformation = new Transformation();
		specularPower = 10f;
	}

	public void init(Window window) throws Exception {
		shaderProg = new Shader();
		shaderProg.createVertexShader(Utils.loadResource("/resources/vertex" +
				".vs"));
		shaderProg.createFragmentShader(Utils.loadResource("/resources" +
				"/fragment.fs"));
		shaderProg.link();

		// Create uniforms for modelView and projection matrices and texture
		shaderProg.createUniform("projectionMatrix");
		shaderProg.createUniform("modelViewMatrix");
		shaderProg.createUniform("texture_sampler");
		// Create uniform for material
		shaderProg.createMaterialUniform("material");
		// Create lighting related uniforms
		shaderProg.createUniform("specularPower");
		shaderProg.createUniform("ambientLight");
		shaderProg.createPointLightUniform("pointLight");
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void cleanup() {
		if (shaderProg != null) {
			shaderProg.cleanup();
		}
	}

	public void render(Window window, Camera camera, GameObj[] gameItems,
					   Vector3f ambientLight, PointLight pointLight) {
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

		shaderProg.setUniform("ambientLight", ambientLight);
		shaderProg.setUniform("specularPower", specularPower);
		// Get a copy of the light object and transform its position to view coordinates
		PointLight currPointLight = new PointLight(pointLight);
		Vector3f lightPos = currPointLight.getPosition();
		Vector4f aux = new Vector4f(lightPos, 1);
		aux.mul(viewMatrix);
		lightPos.x = aux.x;
		lightPos.y = aux.y;
		lightPos.z = aux.z;
		shaderProg.setUniform("pointLight", currPointLight);

		shaderProg.setUniform("texture_sampler", 0);
		for (GameObj item : gameItems) {
			Mesh mesh = item.getMesh();
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);

			shaderProg.setUniform("modelViewMatrix", modelViewMatrix);
			shaderProg.setUniform("material", mesh.getMaterial());

			mesh.render();
		}
		shaderProg.unbind();
	}


}
