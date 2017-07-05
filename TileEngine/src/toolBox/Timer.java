package toolBox;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Timer {
	
	private static long lastFrame = 0;
	private static long lastFPS   = 0;
	private static int  fps       = 0;
	
	/**
	 * Get the time in milliseconds
	 * @return The system time in milliseconds
	 */
	public static long getTime(){
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static int getFrameTime(){
		long time = getTime();
		int delta = (int)(time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public static void startFPS(){
		lastFPS = getTime();
	}
	
	public static void updateFPS(){
		if(getTime() - lastFPS > 1000){
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

}
