package texture;

import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TilesetTexture {
	
	private Vector2f dimension;
	private int textureID;
	
	/**
	 * Load the texture located at <code>imagePath</code> path
	 * @param imagePath The path of a png image
	 * @param dimension The texture's dimension, in org.lwjgl.util.vector.Vector2f
	 */
	public TilesetTexture(String imagePath, Vector2f dimension){
		dimension = new Vector2f(dimension.x, dimension.y);
		
		Texture texture = null;
		try{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		textureID = texture.getTextureID();
	}
	
	public Vector2f getDimendion(){return new Vector2f(dimension.x, dimension.y);}
	
	public int getTextureID(){return textureID;}

}
