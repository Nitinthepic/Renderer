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

	private static final int MAX_POINT_LIGHTS = 5;

	private static final int MAX_SPOT_LIGHTS = 5;

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
		shaderProg.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
		shaderProg.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
		shaderProg.createDirectionalLightUniform("directionalLight");
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
					   Vector3f ambientLight, PointLight[] pointLights,
					   SpotLight[] spotLights,
					   DirectionalLight directionalLight) {
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

		renderLights(viewMatrix, ambientLight, pointLights, spotLights, directionalLight);


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

	private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight,
							  PointLight[] pointLights,
							  SpotLight[] spotLights,
							  DirectionalLight directionalLight) {

		shaderProg.setUniform("ambientLight", ambientLight);
		shaderProg.setUniform("specularPower", specularPower);

		// Get a copy of the light object and transform its position to view coordinates
		int numLights = pointLights != null ? pointLights.length : 0;
		for (int i = 0; i < numLights; i++) {
			PointLight currPointLight = new PointLight(pointLights[i]);
			Vector3f lightPos = currPointLight.getPosition();
			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;
			shaderProg.setUniform("pointLights", currPointLight, i);
		}

		numLights = spotLights != null ? spotLights.length : 0;
		for (int i = 0; i < numLights; i++) {
			// Get a copy of the spot light object and transform its position and cone direction to view coordinates
			SpotLight currSpotLight = new SpotLight(spotLights[i]);
			Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
			dir.mul(viewMatrix);
			currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
			Vector3f lightPos = currSpotLight.getPointLight().getPosition();

			Vector4f aux = new Vector4f(lightPos, 1);
			aux.mul(viewMatrix);
			lightPos.x = aux.x;
			lightPos.y = aux.y;
			lightPos.z = aux.z;

			shaderProg.setUniform("spotLights", currSpotLight, i);
		}

		DirectionalLight currentDirectionLight =
				new DirectionalLight(directionalLight);
		Vector4f dir = new Vector4f(currentDirectionLight.getDirection(), 0);
		dir.mul(viewMatrix);
		currentDirectionLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		shaderProg.setUniform("directionalLight", currentDirectionLight);
	}
}
