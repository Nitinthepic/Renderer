import org.joml.Vector4f;

public class Material {
	private static final Vector4f DEFAULTCOLOR = new Vector4f(1f, 1f, 1f, 1f);
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;
	private float reflectance;
	private Texture texture;

	public Material(Vector4f colour, float reflectance) {
		this(colour, colour, colour, null, reflectance);
	}

	public Material(Texture texture) {
		this(DEFAULTCOLOR, DEFAULTCOLOR, DEFAULTCOLOR, texture, 0);
	}

	public Material(Texture texture, float reflectance) {
		this(DEFAULTCOLOR, DEFAULTCOLOR, DEFAULTCOLOR, texture, reflectance);
	}

	public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
		this.ambientColor = ambientColour;
		this.diffuseColor = diffuseColour;
		this.specularColor = specularColour;
		this.texture = texture;
		this.reflectance = reflectance;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean isTextured() {
		return this.texture != null;
	}
}
