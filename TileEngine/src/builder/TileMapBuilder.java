package builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import entities.TileMap;
import renderEngine.Renderable;
import shaders.TileMapShader;
import texture.TilesetTexture;
import tileMap.TileLevel;
import tileMap.Tileset;
import toolBox.BoundingBox;
import toolBox.LineBoundingBox;
import toolBox.SquareBoundingBox;
import xmlParser.XMLDocumentParser;

public class TileMapBuilder{
	
	private TileMap tileMap;

	private static final String TILEMAP_FILE = "res/entities/level1_test.xml";
	private static final String TILEMAP_DEF	= "res/tilesets/terrain_tileset_test.xml";
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	private XMLDocumentParser tileMapParser;
	private XMLDocumentParser tileMapDefParser;
	
	public void createEntity() throws Exception{
		tileMap = new TileMap();
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		tileMapParser 		= new XMLDocumentParser(builder.parse(new File(TILEMAP_FILE)));
		tileMapDefParser	= new XMLDocumentParser(builder.parse(new File(TILEMAP_DEF)));
	}

	public void createTileset() {
		
		NodeList tilesetsNode = tileMapDefParser.getElementsByTagName("tileset");
		Tileset tileset = null;
		List<Tileset> tilesets = new ArrayList<>();
		Map<Integer, BoundingBox> boundingBoxes = new HashMap<>();
		int textureWidth=1, textureHeight=1, tileWidth=1, tileHeight=1, tileID=0;
		String boxType = "", texturePath="";
		Vector2f boundingBoxPosition	= new Vector2f();
		Vector2f boundingBoxDimension	= new Vector2f();
		Vector2f boundingBoxEndpoints[]	= new Vector2f[2];
		
		for(int i=0; i<tilesetsNode.getLength(); i++){
			tileset = new Tileset();
			
			NamedNodeMap attributeMap = tilesetsNode.item(i).getAttributes();
			textureWidth	= Integer.parseInt(attributeMap.getNamedItem("width").getTextContent());
			textureHeight	= Integer.parseInt(attributeMap.getNamedItem("height").getTextContent());
			tileWidth		= Integer.parseInt(attributeMap.getNamedItem("tilewidth").getTextContent());
			tileHeight		= Integer.parseInt(attributeMap.getNamedItem("tileheight").getTextContent());
			tileset.setName(attributeMap.getNamedItem("name").getTextContent());
			tileset.setNumberOfRows(textureHeight/tileHeight);
			tileset.setNumberOfColumns(textureWidth/tileWidth);
			tilesets.add(tileset);
			
			Node image 	= tileMapDefParser.getNamedChild("image", tilesetsNode.item(i));
			texturePath = image.getAttributes().getNamedItem("source").getTextContent();
			tileset.setTexture(new TilesetTexture(texturePath, new Vector2f(textureWidth, textureHeight)));

			NodeList tiles = tileMapDefParser.getNamedChilds("tile", tilesetsNode.item(i));
			for(int j=0; j<tiles.getLength(); j++){
				tileID = Integer.parseInt(tiles.item(j).getAttributes().getNamedItem("id").getTextContent());
				Node object = tileMapDefParser.getNamedChild("object", tileMapDefParser.getNamedChild("objectgroup", tiles.item(j)));
				NamedNodeMap objectAttributes = object.getAttributes();
				boxType = objectAttributes.getNamedItem("type").getTextContent();
				boundingBoxPosition.x 	= Integer.parseInt(objectAttributes.getNamedItem("x").getTextContent());
				boundingBoxPosition.y 	= Integer.parseInt(objectAttributes.getNamedItem("y").getTextContent());
				boundingBoxDimension.x 	= Integer.parseInt(objectAttributes.getNamedItem("width").getTextContent());
				boundingBoxDimension.y 	= Integer.parseInt(objectAttributes.getNamedItem("height").getTextContent());
				if(boxType.equalsIgnoreCase("square")){
					boundingBoxes.put(tileID, new SquareBoundingBox(new Vector2f(boundingBoxPosition), new Vector2f(boundingBoxDimension)) );
				} else if(boxType.equalsIgnoreCase("line")) {
					Node polygon = tileMapDefParser.getNamedChild("polygon", object);
					String allPoints = polygon.getAttributes().getNamedItem("points").getTextContent();
					String point[] = allPoints.split(" ");
					String coordinate[];
					coordinate = point[0].split(",");
					boundingBoxEndpoints[0]		= new Vector2f();
					boundingBoxEndpoints[0].x	= Integer.parseInt(coordinate[0]);
					boundingBoxEndpoints[0].y	= Integer.parseInt(coordinate[1]);
					coordinate = point[1].split(",");
					boundingBoxEndpoints[1]		= new Vector2f();
					boundingBoxEndpoints[1].x	= Integer.parseInt(coordinate[0]);
					boundingBoxEndpoints[1].y	= Integer.parseInt(coordinate[1]);
					
					boundingBoxes.put(tileID, new LineBoundingBox(new Vector2f(boundingBoxPosition), boundingBoxEndpoints));
				}
			}
			tileset.setBoundingBoxes(boundingBoxes);
		}
		tileMap.setTileset(tilesets);
	}

	public void createTileLevels() {
		TileLevel level = null;
		List<TileLevel> tileLevels = new ArrayList<>();
		String data = "";
		int tileWidth=0, tileHeight=0;
		Node map = tileMapParser.getElementsByTagName("map").item(0);
		tileWidth	= Integer.parseInt(map.getAttributes().getNamedItem("tilewidth").getTextContent());
		tileHeight	= Integer.parseInt(map.getAttributes().getNamedItem("tileheight").getTextContent());
		NodeList layers = tileMapParser.getElementsByTagName("layer");
		for(int lv=0; lv<layers.getLength(); lv++){
			level = new TileLevel();
			level.setTileDimension(new Vector2f(tileWidth, tileHeight));
			level.setName(layers.item(lv).getAttributes().getNamedItem("name").getTextContent());
			level.setLevelDimension(new Vector2f(
					Integer.parseInt(layers.item(lv).getAttributes().getNamedItem("width").getTextContent()),
					Integer.parseInt(layers.item(lv).getAttributes().getNamedItem("height").getTextContent())));
			
			Node dataNode = tileMapParser.getNamedChild("data", layers.item(lv));
			data = dataNode.getTextContent();
			level.setData(data);
			level.calculatePositions();
			tileLevels.add(level);
		}
		tileMap.setTileLevels(tileLevels);
	}

	public void createShader() {
		TileMapShader shader = new TileMapShader();
		tileMap.setShader(shader);
	}

	public void bindBuffers() {
		tileMap.bindBuffers();
	}
	
	public Renderable getEntity(){return tileMap;}

}
