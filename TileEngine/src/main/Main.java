package main;

import java.sql.Time;
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
		
		TileMapShader shader = new TileMapShader();
		TileMap tileMap = new TileMap("res/entities/tileMap.xml", shader);
		
		PlayerShader playerShader = new PlayerShader();
		Player player = new Player("res/entities/Player.xml", playerShader);
		
		
		List<Renderable> renderables = new ArrayList<Renderable>();
		renderables.add(tileMap);
		renderables.add(player);
		
		Renderer renderer = new Renderer();
		
		Timer.startFPS();
		while(!Display.isCloseRequested()){
			DisplayManager.clear();
			
			Input.checkInput();
			player.update(Timer.getDelta());
			
			renderer.render(renderables);
			
			DisplayManager.updateDisplay();
			Timer.updateFPS();
		}
		
		DisplayManager.closeDisplay();
	}
	
}
