package input;

import java.util.Observable;

import org.lwjgl.input.Keyboard;

public class Input extends Observable implements Runnable{
	
	private static Input instance = null;
	
	public static final int W = 0;
	public static final int A = 1;
	public static final int S = 2;
	public static final int D = 3;
	
	public static final int MAX_KEYS = 4;
	
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
	
	private synchronized void checkInput(){
		while (Keyboard.next()) {
			if(Keyboard.getEventKeyState()){
				pressed = true;
			}else{
				pressed = false;
			}
			switch (Keyboard.getEventKey()) {
			case Keyboard.KEY_W:
				setKeyState(W, pressed);
				setChanged();
				//System.out.print("Pressed: " + keyState[W] + ", Key: W");
				break;
			case Keyboard.KEY_A:
				setKeyState(A, pressed);
				setChanged();
				//System.out.print("Pressed: " + keyState[A] + ", Key: A");
				break;
			case Keyboard.KEY_S:
				setKeyState(S, pressed);
				setChanged();
				//System.out.print("Pressed: " + keyState[S] + ", Key: S");
				break;
			case Keyboard.KEY_D:
				setKeyState(D, pressed);
				setChanged();
				//System.out.print("Pressed: " + keyState[D] + ", Key: D");
				break;
			}
			//System.out.print("Key W: " + keyState[W] + ", " + " Key S: " + keyState[S] + ", " + "Key A: " + keyState[A] + ", " + " Key D: " + keyState[D]);
			//System.out.println("");
		}
		notifyObservers();
	}

	@Override
	public void run() {
		while(true){
			checkInput();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
}
