import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;


public class MouseInput {
	private final Vector2d previousPos;
	private final Vector2d currentPos;
	private final Vector2f displacementVector;

	private boolean inWindow = false;
	private boolean rightClick = false;
	private boolean leftClick = false;

	public MouseInput() {
		previousPos = new Vector2d(-1, -1);
		currentPos = new Vector2d(0, 0);
		displacementVector = new Vector2f();
	}

	public void init(Window window) {
		glfwSetCursorPosCallback(window.getWindow(),
				((window1, xpos, ypos) -> {
					currentPos.x = xpos;
					currentPos.y = ypos;
				}));
		glfwSetCursorEnterCallback(window.getWindow(),((window1, entered) -> {
			inWindow = entered;
		}));
		glfwSetMouseButtonCallback(window.getWindow(), ((window1, button,
														 action, mods) -> {
			leftClick = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			rightClick = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		}));
	}

	public void input(Window window) {
		displacementVector.x = 0;
		displacementVector.y = 0;

		if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
			double deltaX = currentPos.x - previousPos.x;
			double deltaY = currentPos.y - previousPos.y;
			boolean rotateX = deltaX != 0;
			boolean rotateY = deltaY != 0;
			if ((rotateX)) {
				displacementVector.y = (float) deltaX;
			}
			if (rotateY) {
				displacementVector.x = (float) deltaY;
			}
		}
		previousPos.x = currentPos.x;
		previousPos.y = currentPos.y;
	}

	public Vector2f getDisplacementVector() {
		return displacementVector;
	}

	public boolean isInWindow() {
		return inWindow;
	}

	public boolean isRightClick() {
		return rightClick;
	}

	public boolean isLeftClick() {
		return leftClick;
	}
}
