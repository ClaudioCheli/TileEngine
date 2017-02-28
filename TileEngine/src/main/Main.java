package main;

import java.util.ArrayList;
import java.util.List;

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

public class Main {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Input input = Input.getInput();
		Thread inputThread = new Thread(input);
		
		TileMapShader shader = new TileMapShader();
		TileMap tileMap = new TileMap("res/entities/tileMap.xml", shader);
		
		PlayerShader playerShader = new PlayerShader();
		Player player = new Player("res/entities/knight.xml", playerShader, input);
		
		input.addObserver(player);
		
		List<Renderable> renderables = new ArrayList<Renderable>();
		renderables.add(tileMap);
		renderables.add(player);
		
		Renderer renderer = new Renderer();
		
		Timer.startFPS();
		inputThread.start();
		while(!Display.isCloseRequested()){
			DisplayManager.clear();
			
			//input.checkInput();
			player.updatePosition(Timer.getDelta(), Timer.getTime());
			
			renderer.render(renderables);
			
			DisplayManager.updateDisplay();
			Timer.updateFPS();
		}
		
		inputThread.interrupt();
		DisplayManager.closeDisplay();
	}
	
}
