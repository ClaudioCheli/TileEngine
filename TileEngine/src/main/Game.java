package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import camera.Camera;
import display.DisplayManager;
import entities.Player;
import entities.TileMap;
import gameObjectBuilder.EntityCreationDirector;
import gameObjectBuilder.PlayerBuilder;
import gameObjectBuilder.TileMapBuilder;
import input.Input;
import renderEngine.Entity;
import renderEngine.Renderable;
import renderEngine.Renderer;
import toolBox.Timer;

public class Game implements Observer{

	private Renderer renderer;
	
	private List<Renderable> renderables	= new ArrayList<>();
	private List<Entity> physical 			= new ArrayList<>();
	private TileMap tileMap;
	private Player player;
	private Camera camera;
	private Vector2f playerPosition = new Vector2f();
	
	private Thread inputThread;

	private Input input;
	
	private boolean running = true;
	
	public Game(){
		DisplayManager.createDisplay();
		try {
			Keyboard.create();
		} catch (LWJGLException e) {e.printStackTrace();}
		input = Input.getInput();
		inputThread = new Thread(input);
	
		try {
			TileMapBuilder tileMapBuilder = new TileMapBuilder("island.xml", "terrain_def.xml", 
												"tileMapVertexShader.vs", "tileMapFragmentShader.fs");
			tileMapBuilder.createEntity();
			//tileMapBuilder.createTileset();
			tileMapBuilder.createTileLevels();
			tileMapBuilder.createShader();
			tileMapBuilder.bindBuffers();
			renderables.add(tileMapBuilder.getEntity());
			tileMap = (TileMap) tileMapBuilder.getEntity();
		} catch (Exception e) {e.printStackTrace();}
		
		EntityCreationDirector director = new EntityCreationDirector();	
		try {
			director.setEntityBuilder(new PlayerBuilder("knight.xml", "knight_def.xml",
											"playerVertexShader.vs", "playerFragmentShader.fs"));
			director.createEntity();
			renderables.add(director.getEntity());
			physical.add(director.getEntity());
			player = (Player) director.getEntity();
		} catch (Exception e) {e.printStackTrace();}
		
		input.addObserver(player);
		input.addObserver(this);
		
		camera = new Camera();
		
		renderer = new Renderer();

		Timer.startFPS();
		inputThread.start();
	}
	
	public void gameLoop(){
		while(running){
			DisplayManager.clear();
			
			playerPosition = player.getPosition();

			camera.move(new Vector2f(playerPosition.x-DisplayManager.WIDTH/2,
					playerPosition.y-DisplayManager.HEIGHT/2));
			
			for(Renderable renderable : renderables)
				renderable.handleInput();
			
			renderer.render(renderables, camera);
			
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
