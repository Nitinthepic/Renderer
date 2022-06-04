import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;


public class Texture {
	private final int id;
	private int width;
	private int height;

	public Texture(String fileName) throws IOException {
		this(loadTexture(fileName));
		PNGDecoder decoder = new PNGDecoder(
				Texture.class.getResourceAsStream(fileName));
		width = decoder.getWidth();
		height = decoder.getHeight();
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
