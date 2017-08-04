package test;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Error {
	
	/**
	 * Exit from the program if an OpenGL error occur
	 * @param errorMessage The message to be printed
	 */
	public static void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);

			if (Display.isCreated()) 
				Display.destroy();
			System.exit(-1);
		}
	}

}
