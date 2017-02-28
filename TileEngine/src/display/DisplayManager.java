package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	public static final int WIDTH  = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "TileEngine";
	
	/**
	 * Create a window of <code>WIDTH</code> * <code>HEIGHT</code> pixels and <code>TITLE</code> title
	 */
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(4, 3).withForwardCompatible(true).withProfileCore(true);
		
		try{
			DisplayMode mode = new DisplayMode(WIDTH, HEIGHT);
			Display.setDisplayMode(mode);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	/**
	 * Clear color and depth buffers
	 */
	public static void clear(){
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Cap framerate to 60 and update the Display 
	 */
	public static void updateDisplay(){
		Display.sync(60);
		Display.update();
	}
	
	/**
	 * Destroy the Display
	 */
	public static void closeDisplay(){
		Display.destroy();
	}
 
}
