import org.joml.Vector3f;

public class SpotLight {
	private PointLight pointLight;
	private Vector3f coneDirection;
	private float range;

	public SpotLight(PointLight pointLight, Vector3f coneDirection,
					 float rangeAngle) {
		this.pointLight = pointLight;
		this.coneDirection = coneDirection;
		setRangeAngle(rangeAngle);
	}

	public SpotLight(SpotLight spotLight) {
		this(new PointLight(spotLight.getPointLight()),
				new Vector3f(spotLight.getConeDirection()), 0);
		setRange((spotLight.getRange()));
	}


	public PointLight getPointLight() {
		return pointLight;
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLight = pointLight;
	}

	public Vector3f getConeDirection() {
		return coneDirection;
	}

	public void setConeDirection(Vector3f coneDirection) {
		this.coneDirection = coneDirection;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public final void setRangeAngle(float cutOffAngle) {
		this.setRange((float) Math.cos(Math.toRadians(cutOffAngle)));
	}
}
