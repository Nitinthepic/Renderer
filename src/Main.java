import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
	private static float movementY = 0;
	private static float movementX = 0;
	private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if(key==GLFW_KEY_ESCAPE&&action==GLFW_PRESS){
				glfwSetWindowShouldClose(window, true);
			}
			if(key==GLFW_KEY_W&&action==GLFW_PRESS){
				movementY += 0.1f;
			}
			if(key==GLFW_KEY_S&&action==GLFW_PRESS){
				movementY -= 0.1f;
			}
			if(key==GLFW_KEY_A&&action==GLFW_PRESS){
				movementX -= 0.1f;
			}
			if(key==GLFW_KEY_D&&action==GLFW_PRESS){
				movementX += 0.1f;
			}
		}
	};



	private static GLFWErrorCallback errorCallback =
			GLFWErrorCallback.createPrint(System.err);

	public static void main(String[] args){
		try {
			boolean vSync = true;
			ILogic gameLogic = new PlaceHolderGame();
			Engine gameEng = new Engine("GAME", 2560, 980, vSync, gameLogic);
			gameEng.run();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}




}
