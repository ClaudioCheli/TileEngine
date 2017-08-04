package tileMap;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;
import texture.TilesetTexture;
import toolBox.BoundingBox;

public class Tileset {

	private int textureWidth, textureHeight, tilesNumber, tileWidth,
	tileHeight, firstTileID, lastTileID, columns, rows;
	private String tilesetName;
	private TilesetTexture texture;
	private Map<Integer, BoundingBox> boundingBoxes;

	public Tileset(){}

	public Vector2f getTileDimension(){return new Vector2f(tileWidth, tileHeight);}
	public void setTileDimension(Vector2f dimension) { tileWidth = (int) dimension.x; tileHeight = (int) dimension.y; }
	public String getName(){return tilesetName;}
	public void setName(String name){this.tilesetName = name;}
	public int getNumberOfRows(){return rows;}
	public void setNumberOfRows(int rows){this.rows = rows;}
	public int getNumberOfColumns(){return columns;}
	public void setNumberOfColumns(int columns){this.columns = columns;}
	public TilesetTexture getTexture(){return texture;}
	public void setTexture(TilesetTexture texture){this.texture = texture;}
	public int getTextureID(){return texture.getTextureID();}
	public void setBoundingBoxes(Map<Integer, BoundingBox> boxes){this.boundingBoxes = new HashMap<>(boxes);}
	public BoundingBox getBoundingBox(int tileNumber){
		if(boundingBoxes.containsKey(tileNumber)) {
			//Log.i("physic","    found box " + tileNumber);
			return boundingBoxes.get(tileNumber);
		} else {
			//Log.i("physic","    box " + tileNumber + " not found");
			return null;
		}
	}
}
