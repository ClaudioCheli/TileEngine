package gameObjectBuilder;

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
import toolBox.Util;
import xmlParser.XMLDocumentParser;

public class TileMapBuilder{
	
	private TileMap tileMap;
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	private XMLDocumentParser tileMapParser;
	private XMLDocumentParser tilesetsParser;
	
	private String vertexShaderFile;
	private String fragmentShaderFile;
	
	private List<Tileset> tilesets;
	
	int tileWidth 	= 0;
	int tileHeight 	= 0;
	
	public TileMapBuilder(String mapFile, String mapTilesetDefinition,
			String vertexShaderFile, String fragmentShaderFile) throws Exception{
		this.vertexShaderFile 	= vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		tileMapParser 		= new XMLDocumentParser(builder.parse(new File(Util.ENTITIES_PATH + mapFile)));
		tilesetsParser	= new XMLDocumentParser(builder.parse(new File(Util.TILESETS_PATH + mapTilesetDefinition)));
	}
	
	public void createEntity(){
		tileMap = new TileMap();
	}

	public void createTileset() {
		/*NodeList tilesetsNode = tilesetsParser.getElementsByTagName("tileset");
		Tileset tileset = null;
		tilesets = new ArrayList<>();
		Map<Integer, BoundingBox> boundingBoxes = new HashMap<>();
		int textureWidth=1, textureHeight=1, tileID=0;
		String boxType = "", textureName="";
		Vector2f boundingBoxPosition	= new Vector2f();
		Vector2f boundingBoxDimension	= new Vector2f();
		Vector2f boundingBoxEndpoints[]	= new Vector2f[2];
		
		for(int i=0; i<tilesetsNode.getLength(); i++){
			tileset = new Tileset();
			
			NamedNodeMap tilesetAttributes = tilesetsNode.item(i).getAttributes();
			textureWidth	= Integer.parseInt(tilesetAttributes.getNamedItem("width").getTextContent());
			textureHeight	= Integer.parseInt(tilesetAttributes.getNamedItem("height").getTextContent());
			tileWidth		= Integer.parseInt(tilesetAttributes.getNamedItem("tilewidth").getTextContent());
			tileHeight		= Integer.parseInt(tilesetAttributes.getNamedItem("tileheight").getTextContent());
			textureName 	= tilesetAttributes.getNamedItem("src").getTextContent();
			tileset.setName(tilesetAttributes.getNamedItem("name").getTextContent());
			tileset.setNumberOfRows(textureHeight/tileHeight);
			tileset.setNumberOfColumns(textureWidth/tileWidth);
			tileset.setTexture(new TilesetTexture(Util.TILESETS_PATH + textureName, new Vector2f(textureWidth, textureHeight)));
			tilesets.add(tileset);
			
			NodeList tiles = tilesetsParser.getNamedChilds("tile", tilesetsNode.item(i));
			for(int j=0; j<tiles.getLength(); j++){
				tileID = Integer.parseInt(tiles.item(j).getAttributes().getNamedItem("id").getTextContent());
				Node object = tilesetsParser.getNamedChild("object", tilesetsParser.getNamedChild("objectgroup", tiles.item(j)));
				NamedNodeMap objectAttributes = object.getAttributes();
				boxType = objectAttributes.getNamedItem("type").getTextContent();
				boundingBoxPosition.x 	= Integer.parseInt(objectAttributes.getNamedItem("x").getTextContent());
				boundingBoxPosition.y 	= Integer.parseInt(objectAttributes.getNamedItem("y").getTextContent());
				boundingBoxDimension.x 	= Integer.parseInt(objectAttributes.getNamedItem("width").getTextContent());
				boundingBoxDimension.y 	= Integer.parseInt(objectAttributes.getNamedItem("height").getTextContent());
				if(boxType.equalsIgnoreCase("square")){
					boundingBoxes.put(tileID, new SquareBoundingBox(new Vector2f(boundingBoxPosition), new Vector2f(boundingBoxDimension)) );
				} else if(boxType.equalsIgnoreCase("line")) {
					Node polygon = tilesetsParser.getNamedChild("polygon", object);
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
		tileMap.setTileset(tilesets);*/
	}

	public void createTileLevels() {
		TilesetFactory tilesetFactory = TilesetFactory.getInstance();
		TileLevel level = null;
		List<TileLevel> tileLevels = new ArrayList<>();
		String data = "", tilesetName = "";
		NodeList layers = tileMapParser.getElementsByTagName("layer");
		for(int lv=0; lv<layers.getLength(); lv++){
			level = new TileLevel();
			level.setName(layers.item(lv).getAttributes().getNamedItem("name").getTextContent());
			level.setLevelDimension(new Vector2f(
					Integer.parseInt(layers.item(lv).getAttributes().getNamedItem("width").getTextContent()),
					Integer.parseInt(layers.item(lv).getAttributes().getNamedItem("height").getTextContent())));
			
			//-----------------------------------------------------------------------------
			
			Node property = tileMapParser.getNamedChild("property", 
					tileMapParser.getNamedChild("properties", layers.item(lv)));
			
			tilesetName = property.getAttributes().getNamedItem("value").getTextContent();
			level.setTileset(tilesetFactory.makeTileset(tilesetName, tilesetsParser));
			
			//-----------------------------------------------------------------------------
			
			level.setTileDimension(new Vector2f(level.getTileset().getTileDimension().x,
					level.getTileset().getTileDimension().y));
			Node dataNode = tileMapParser.getNamedChild("data", layers.item(lv));
			data = dataNode.getTextContent();
			level.setData(data);
			level.calculatePositions();
			tileLevels.add(level);
		}
		tileMap.setTileLevels(tileLevels);
	}

	public void createShader() {
		TileMapShader shader = new TileMapShader(vertexShaderFile, fragmentShaderFile);
		tileMap.setShader(shader);
	}

	public void bindBuffers() {
		tileMap.bindBuffers();
	}
	
	public Renderable getEntity(){return tileMap;}

}
