import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

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
			throw new Exception("Error compiling");

		glAttachShader(programId, shaderId);

		return shaderId;
	}

	public void createUniform(String uniformName) throws Exception{
		int uniformLoc = glGetUniformLocation(programId, uniformName);
		if(uniformLoc < 0 ){
			throw new Exception("ILLEGAL LOCATION!");
		}
		uniforms.put(uniformName,uniformLoc);
	}

	public void setUniform(String uniformName, Matrix4f transMatrix){
		try(MemoryStack stack = MemoryStack.stackPush()){
			FloatBuffer buffer = stack.mallocFloat(16);
			transMatrix.get(buffer);
			glUniformMatrix4fv(uniforms.get(uniformName),false,buffer);
		}
	}
	public void setUniform(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
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
