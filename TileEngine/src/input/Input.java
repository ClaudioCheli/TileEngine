package input;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class Input extends Observable implements Runnable{
	
	private static Input instance = null;
	private volatile boolean running = true;
	
	public static final int W = 0;
	public static final int A = 1;
	public static final int S = 2;
	public static final int D = 3;
	public static final int ESC = 4;
	
	public static final int MAX_KEYS = 5;
	
	private List<Integer> keyEvents = new ArrayList<>();
	public boolean keyState[] = new boolean[MAX_KEYS];
	
	private Input(){
		for(int i=0; i<MAX_KEYS; i++)
			keyState[i] = false;
	}
	
	public static Input getInput(){
		if(instance == null)
			instance = new Input();
		return instance;
	}
	
	private boolean pressed = false;
	
	public synchronized boolean getKeyState(int key){
		return keyState[key];
	}
	
	public synchronized void setKeyState(int key, boolean state){
		keyState[key] = state;
	}
	
	public synchronized boolean[] getKeyStates(){
		return keyState;
	}
	
	public void terminate(){
		running = false;
	}
	
	private void checkInput(){
		while (Keyboard.next()) {
			if(Keyboard.getEventKeyState()){
				pressed = true;
			}else{
				pressed = false;
			}
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_W:
				setKeyState(W, pressed);
				keyEvents.add(W);
				setChanged();
				//System.out.println("Pressed: " + keyState[W] + ", Key: W");
				break;
			case Keyboard.KEY_A:
				setKeyState(A, pressed);
				keyEvents.add(A);
				setChanged();
				//System.out.println("Pressed: " + keyState[A] + ", Key: A");
				break;
			case Keyboard.KEY_S:
				setKeyState(S, pressed);
				keyEvents.add(S);
				setChanged();
				//System.out.println("Pressed: " + keyState[S] + ", Key: S");
				break;
			case Keyboard.KEY_D:
				setKeyState(D, pressed);
				keyEvents.add(D);
				setChanged();
				//System.out.println("Pressed: " + keyState[D] + ", Key: D");
				break;
			case Keyboard.KEY_ESCAPE:
				setKeyState(ESC, pressed);
				keyEvents.add(ESC);
				setChanged();
				break;
			}
			//System.out.print("Key W: " + keyState[W] + ", " + " Key S: " + keyState[S] + ", " + "Key A: " + keyState[A] + ", " + " Key D: " + keyState[D]);
			//System.out.println("");
		}
		notifyObservers(keyEvents);
		keyEvents.clear();
	}

	@Override
	public void run() {
		try {
			Keyboard.create();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		while(running){
			checkInput();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
}
