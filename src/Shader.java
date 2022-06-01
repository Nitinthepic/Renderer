import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	private final Map<String, Integer> uniforms;

	public Shader() throws Exception {
		uniforms = new HashMap<>();
		programId = glCreateProgram();
		if ((programId == 0))
			throw new Exception("Could not create Shader");
	}


	public void createPointLightUniform(String uniformName) throws Exception {
		createUniform(uniformName + ".color");
		createUniform(uniformName + ".position");
		createUniform(uniformName + ".intensity");
		createUniform(uniformName + ".att.constant");
		createUniform(uniformName + ".att.linear");
		createUniform(uniformName + ".att.exponent");
	}

	public void createMaterialUniform(String uniformName) throws Exception {
		createUniform(uniformName + ".ambient");
		createUniform(uniformName + ".diffuse");
		createUniform(uniformName + ".specular");
		createUniform(uniformName + ".hasTexture");
		createUniform(uniformName + ".reflectance");
	}

	public void createDirectionalLightUniform(String uniformName)
			throws Exception {
		createUniform(uniformName + ".color");
		createUniform(uniformName + ".direction");
		createUniform(uniformName + ".intensity");
	}

	public void createVertexShader(String code) throws Exception {
		vertexShaderId = shaderConstructor(code, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String code) throws Exception {
		fragmentShaderId = shaderConstructor(code, GL_FRAGMENT_SHADER);
	}

	private int shaderConstructor(String code, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Bad shader given");
		}

		glShaderSource(shaderId, code);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
			throw new Exception("Error compiling" + glGetShaderInfoLog(shaderId, 1024));

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLoc = glGetUniformLocation(programId, uniformName);
		if (uniformLoc < 0) {
			throw new Exception("ILLEGAL LOCATION!" + uniformName);
		}
		uniforms.put(uniformName, uniformLoc);
	}

	public void createSpotLightUniform(String uniformName) throws Exception {
		createPointLightUniform(uniformName + ".pointLight");
		createUniform(uniformName + ".coneDirection");
		createUniform(uniformName + ".range");
	}

	public void createPointLightListUniform(String uniformName, int size) throws Exception {
		for (int i = 0; i < size; i++) {
			createPointLightUniform(uniformName + "[" + i + "]");
		}
	}

	public void createSpotLightListUniform(String uniformName, int size) throws Exception {
		for (int i = 0; i < size; i++) {
			System.out.println(uniformName + "[" + i + "]");
			createSpotLightUniform(uniformName + "[" + i + "]");
		}
	}

	public void setUniform(String uniformName, Matrix4f transMatrix) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			transMatrix.get(buffer);
			glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
		}
	}

	public void setUniform(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}


	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, Vector4f value) {
		glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniformName, PointLight pointLight) {
		setUniform(uniformName + ".color", pointLight.getColor());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniform(uniformName + ".intensity", pointLight.getIntensity());
		PointLight.Attenuation att = pointLight.getAttenuation();
		setUniform(uniformName + ".att.constant", att.getConstant());
		setUniform(uniformName + ".att.linear", att.getLinear());
		setUniform(uniformName + ".att.exponent", att.getExponent());
	}


	public void setUniform(String uniformName, Material material) {
		setUniform(uniformName + ".ambient", material.getAmbientColor());
		setUniform(uniformName + ".diffuse", material.getDiffuseColor());
		setUniform(uniformName + ".specular", material.getSpecularColor());
		setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
		setUniform(uniformName + ".reflectance", material.getReflectance());
	}

	public void setUniform(String uniformName, DirectionalLight dirLight) {
		setUniform(uniformName + ".color", dirLight.getColor());
		setUniform(uniformName + ".direction", dirLight.getDirection());
		setUniform(uniformName + ".intensity", dirLight.getIntensity());
	}

	public void setUniform(String uniformName, PointLight[] pointLights) {
		int numLights = pointLights != null ? pointLights.length : 0;
		for (int i = 0; i < numLights; i++) {
			setUniform(uniformName, pointLights[i], i);
		}
	}

	public void setUniform(String uniformName, PointLight pointLight, int pos) {
		setUniform(uniformName + "[" + pos + "]", pointLight);
	}

	public void setUniform(String uniformName, SpotLight[] spotLights) {
		int numLights = spotLights != null ? spotLights.length : 0;
		for (int i = 0; i < numLights; i++) {
			setUniform(uniformName, spotLights[i], i);
		}
	}

	public void setUniform(String uniformName, SpotLight spotLight) {
		setUniform(uniformName + ".pointLight", spotLight.getPointLight());
		setUniform(uniformName + ".coneDirection",
				spotLight.getConeDirection());
		setUniform(uniformName + ".range", spotLight.getRange());
	}

	public void setUniform(String uniformName, SpotLight spotLight, int pos) {
		System.out.println(uniformName + "[" + pos + "]");
		setUniform(uniformName + "[" + pos + "]", spotLight);
	}

	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
			throw new Exception("Bad");
		if (vertexShaderId != 0)
			glDetachShader(programId, vertexShaderId);
		if (fragmentShaderId != 0)
			glDetachShader(programId, fragmentShaderId);

		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
			System.err.println("Bad time");
	}

	public void bind() {
		glUseProgram(programId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanup() {
		unbind();
		if (programId != 0)
			glDeleteProgram(programId);
	}
}
