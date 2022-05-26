import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public class Mesh {
	private final int vaoID;
	private final int vboID;

	private final int idxVboID;
	private final int colorVboID;
	private final int vertexCount;

	public Mesh(float[] positions, float[] colors, int[] indices){
		FloatBuffer verticesBuffer = null;
		IntBuffer indicesBuffer = null;
		FloatBuffer colorBuffer = null;
		try{

			vertexCount =indices.length;

			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);

			vboID=glGenBuffers();
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			verticesBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER,vboID);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			colorVboID = glGenBuffers();
			colorBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colorVboID);
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1,3,GL_FLOAT, false,0,0);

			idxVboID = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,idxVboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,GL_STATIC_DRAW);


			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		}finally {
			if(verticesBuffer != null){
				MemoryUtil.memFree(verticesBuffer);
			}
			if(indicesBuffer!=null){
				MemoryUtil.memFree(indicesBuffer);
			}
			if(colorBuffer!=null){
				MemoryUtil.memFree(colorBuffer);
			}
		}
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVboID() {
		return vboID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanUp(){
		glDisableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER,0);

		glDeleteBuffers(vboID);
		glDeleteBuffers(idxVboID);

		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}

	public void render() {
		// Draw the mesh
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
}
