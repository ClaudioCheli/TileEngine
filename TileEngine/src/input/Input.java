package input;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import entities.Player;

public class Input {
	
	public static final int W = 0;
	public static final int A = 1;
	public static final int S = 2;
	public static final int D = 3;
	
	public static final int MAX_KEYS = 4;
	
	public static boolean keyState[] = new boolean[MAX_KEYS];
	static{
		for(int i=0; i<MAX_KEYS; i++)
			keyState[i] = false;
	}
	
	private static boolean pressed = false;
	
	public static void checkInput(){
		while (Keyboard.next()) {
			if(Keyboard.getEventKeyState()){
				pressed = true;
			}else{
				pressed = false;
			}
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_W:
				keyState[W] = pressed; 
				//System.out.print("Pressed: " + keyState[W] + ", Key: W");
				break;
			case Keyboard.KEY_A:
				keyState[A] = pressed; 
				//System.out.print("Pressed: " + keyState[A] + ", Key: A");
				break;
			case Keyboard.KEY_S:
				keyState[S] = pressed;
				//System.out.print("Pressed: " + keyState[S] + ", Key: S");
				break;
			case Keyboard.KEY_D:
				keyState[D] = pressed; 
				//System.out.print("Pressed: " + keyState[D] + ", Key: D");
				break;
			}
			//System.out.print("Key W: " + keyState[W] + ", " + " Key S: " + keyState[S] + ", " + "Key A: " + keyState[A] + ", " + " Key D: " + keyState[D]);
			//System.out.println("");
		}
	}

}
