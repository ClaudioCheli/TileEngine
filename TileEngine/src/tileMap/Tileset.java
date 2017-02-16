package tileMap;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import texture.TilesetTexture;

public class Tileset {
	
	private int textureWidth, textureHeight, tilesNumber, tileWidth, tileHeight, firstTileID, lastTileID, columns, rows;
	private String tilesetName;
	
	private TilesetTexture texture;
	
	public Tileset(String definitionPath, String imagePath){
		try {
			readFile(definitionPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		texture = new TilesetTexture(imagePath, new Vector2f(textureWidth, textureHeight));
	}
	
	private void readFile(String filePath) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(filePath));
		
		Node tileset = document.getFirstChild();
		if(tileset.hasAttributes()){
			for(int i=0; i<tileset.getAttributes().getLength(); i++){
				Node attribute = tileset.getAttributes().item(i);
				switch (attribute.getNodeName()) {
				case "name":
					tilesetName = attribute.getTextContent();
					break;
				case "width":
					textureWidth = Integer.parseInt(attribute.getTextContent());
					break;
				case "height":
					textureHeight = Integer.parseInt(attribute.getTextContent());
					break;
				case "tilesNumber":
					tilesNumber = Integer.parseInt(attribute.getTextContent());
					break;
				case "tileWidth":
					tileWidth = Integer.parseInt(attribute.getTextContent());
					break;
				case "tileHeight":
					tileHeight = Integer.parseInt(attribute.getTextContent());
					break;
				}
			}
			columns = textureWidth/tileWidth;
			rows = textureHeight/tileHeight;
		}
		
		System.out.println("Tileset name: " + tilesetName);
		System.out.println("Player texture width: " + textureWidth);
		System.out.println("Player texture height: " + textureHeight);
		System.out.println("Player texture tilesNumber: " + tilesNumber);
		System.out.println("Player texture tilesWidth: " + tileWidth);
		System.out.println("Player texture tilesHeight: " + tileHeight);
		System.out.println("Player texture columns: " + columns);
		System.out.println("Player texture rows: " + rows);
	}
	
	public int getNumberOfRows(){return rows;}
	
	public int getNumberOfColumns(){return columns;}
	
	public Vector2f getTileDimension(){return new Vector2f(tileWidth, tileHeight);}
	
	public String getName(){return tilesetName;}
	
	public int getTextureID(){return texture.getTextureID();}
	
	public TilesetTexture getTexture(){return texture;}

	public int getLastTileID() {return lastTileID;}

	public void setLastTileID(int lastTileID) {this.lastTileID = lastTileID;}

	public int getFirstTileID() {return firstTileID;}

	public void setFirstTileID(int firstTileID) {this.firstTileID = firstTileID;}

}
