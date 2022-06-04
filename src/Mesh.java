import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class Mesh {
	private static final Vector3f DEFAULTCOLOR = new Vector3f(1f, 1f, 1f);
	private final int vaoID;
	private final List<Integer> vboIDList;

	private Material material;
	private final int vertexCount;
	private Vector3f color;

	public Mesh(float[] positions, float[] textCoordinates,
				float[] normals, int[] indices) {
		FloatBuffer verticesBuffer = null;
		IntBuffer indicesBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		FloatBuffer normalsBuffer = null;
		try {
			color = Mesh.DEFAULTCOLOR;
			vertexCount = indices.length;
			vboIDList = new ArrayList<>();

			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);

			int vboID = glGenBuffers();
			vboIDList.add(vboID);
			verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
			verticesBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			vboID = glGenBuffers();
			vboIDList.add(vboID);
			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoordinates.length);
			textCoordsBuffer.put(textCoordinates).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			vboID = glGenBuffers();
			vboIDList.add(vboID);
			normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			normalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

			vboID = glGenBuffers();
			vboIDList.add(vboID);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);


			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if (normalsBuffer != null) {
				MemoryUtil.memFree(normalsBuffer);
			}
		}
	}

	public int getVaoID() {
		return vaoID;
	}


	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboID : vboIDList) {
			glDeleteBuffers(vboID);
		}
		Texture texture = material.getTexture();
		if (texture != null)
			texture.cleanup();

		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}


	public void deleteBuffers() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : vboIDList) {
			glDeleteBuffers(vboId);
		}

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}

	public void render() {
		Texture texture = material.getTexture();
		if (texture != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getID());
		}
		// Draw the mesh
		glBindVertexArray(getVaoID());


		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public List<Integer> getVboIDList() {
		return vboIDList;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Vector3f getColor() {
		return color;
	}


	public void setColor(Vector3f color) {
		this.color = color;
	}
}
