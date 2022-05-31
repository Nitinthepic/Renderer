import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture {
	private final int id;

	public Texture(String fileName) throws IOException {
		this(loadTexture(fileName));
	}

	public Texture(int id) {
		this.id = id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	private static int loadTexture(String fileName) throws IOException {
		PNGDecoder decoder = new PNGDecoder(
				Texture.class.getResourceAsStream(fileName));

		int width = decoder.getWidth();

		int height = decoder.getHeight();

		ByteBuffer imgBuffer;
		imgBuffer = ByteBuffer.allocateDirect(4 * width * height);

		decoder.decode(imgBuffer, 4 * width, PNGDecoder.Format.RGBA);
		imgBuffer.flip();
		int textureId = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		System.out.println("cat");
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
				GL_UNSIGNED_BYTE, imgBuffer);

		glGenerateMipmap(GL_TEXTURE_2D);


		return textureId;
	}

	public int getID() {
		return id;
	}

	public void cleanup() {
		glDeleteTextures(id);
	}
}
