package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.lwjgl.opengl.Display;

import display.DisplayManager;
import entities.Player;
import entities.TileMap;
import input.Input;
import renderEngine.Renderable;
import renderEngine.Renderer;
import shaders.PlayerShader;
import shaders.TileMapShader;
import toolBox.Timer;

public class Game implements Observer{

	private Renderer renderer;
	private Player player;
	private List<Renderable> renderables;
	private Thread inputThread;

	private Input input;
	
	private boolean running = true;
	
	public Game(){
		DisplayManager.createDisplay();

		input = Input.getInput();
		inputThread = new Thread(input);

		TileMapShader shader = new TileMapShader();
		TileMap tileMap = new TileMap("res/entities/tileMap.xml", shader);

		PlayerShader playerShader = new PlayerShader();
		player = new Player("res/entities/knight.xml", playerShader, input);

		input.addObserver(player);
		input.addObserver(this);

		renderables = new ArrayList<Renderable>();
		renderables.add(tileMap);
		renderables.add(player);

		renderer = new Renderer();

		Timer.startFPS();
		inputThread.start();
	}
	
	public void gameLoop(){
		while(running){
			DisplayManager.clear();
			
			//input.checkInput();
			System.out.println("New frame");
			player.updatePosition(Timer.getDelta(), Timer.getTime());
			
			renderer.render(renderables);
			
			DisplayManager.updateDisplay();
			Timer.updateFPS();
			
			checkCloseRequest();
		}
		
		try{
			closeGame();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void checkCloseRequest() {
		if(Display.isCloseRequested())
			running = false;
	}
	
	private void setCloseRequest(){
		running = false;
	}

	private void closeGame() throws InterruptedException{
		input.terminate();
		inputThread.join();
		DisplayManager.closeDisplay();
		System.out.println("END");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == input){
			List<Integer> keyEvents = (List<Integer>) arg;
			for(int i=0; i<keyEvents.size(); i++)
				if(keyEvents.get(i) == Input.ESC){
					setCloseRequest();
				}
		}
	}

}
