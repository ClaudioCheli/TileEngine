package main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import display.DisplayManager;
import entities.TileMap;
import renderEngine.Renderable;
import renderEngine.Renderer;
import shaders.TileMapShader;

public class Main {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		TileMapShader shader = new TileMapShader();
		TileMap tileMap = new TileMap("res/entities/tileMap.xml", shader);
		
		/*PlayerShader playerShader = new PlayerShader();
		Player player = new Player("res/entities/Player.xml", playerShader);
		*/
		
		List<Renderable> renderables = new ArrayList<Renderable>();
		renderables.add(tileMap);
		
		Renderer renderer = new Renderer();
		
		
		while(!Display.isCloseRequested()){
			DisplayManager.clear();
			
			renderer.render(renderables);
			
			DisplayManager.updateDisplay();
			
		}
		
		DisplayManager.closeDisplay();
	}
	
}
