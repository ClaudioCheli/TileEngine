package texture;

import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TilesetTexture {
	
	private Vector2f dimension;
	
	private Texture texture;
	
	/**
	 * Load the texture located at <code>imagePath</code> path
	 * @param imagePath The path of a png image
	 * @param dimension The texture's dimension, in org.lwjgl.util.vector.Vector2f
	 */
	public TilesetTexture(String imagePath, Vector2f dimension){
		this.dimension = new Vector2f(dimension);
		texture = null;
		try{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		texture.getTextureID();
		/*System.out.println(imagePath);
		System.out.println("width: " + dimension.x);
		System.out.println("height: " + dimension.y);*/
	}
	
	public Vector2f getDimension(){return new Vector2f(dimension);}
	
	public int getTextureID(){return texture.getTextureID();}
	
	public byte[] getData(){ return texture.getTextureData(); }

}
