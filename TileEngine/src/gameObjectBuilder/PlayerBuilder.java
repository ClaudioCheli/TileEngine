package gameObjectBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import animation.Animation;
import entities.Player;
import shaders.PlayerShader;
import texture.TilesetTexture;
import tileMap.Tile;
import tileMap.Tileset;
import toolBox.Util;
import xmlParser.XMLDocumentParser;

public class PlayerBuilder extends EntityBuilder{

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	private XMLDocumentParser playerParser;
	private XMLDocumentParser playerTilesetParser;
	
	private String vertexShaderFile;
	private String fragmentShaderFile;
	
	private int tileWidth = 0;
	private int tileHeight = 0;
	
	public PlayerBuilder(String playerFile, String playerTilesetDefinition,
			String vertexShaderFile, String fragmentShaderFile) throws Exception {
		this.vertexShaderFile 	= vertexShaderFile;
		this.fragmentShaderFile = fragmentShaderFile;
		factory 				= DocumentBuilderFactory.newInstance();
		builder 				= factory.newDocumentBuilder();
		playerParser		= new XMLDocumentParser(builder.parse(new File(Util.ENTITIES_PATH + playerFile)));
		playerTilesetParser	= new XMLDocumentParser(builder.parse(new File(Util.TILESETS_PATH + playerTilesetDefinition)));
	}
	
	@Override
	public void createEntity(){
		entity = new Player();
	}

	@Override
	public void createTile() throws Exception{
		NodeList tilesetElements = playerTilesetParser.getElementsByTagName("tileset");
		if(tilesetElements != null){
			NamedNodeMap tilesetAttributes = tilesetElements.item(0).getAttributes();
			tileWidth 	= Integer.parseInt(tilesetAttributes.getNamedItem("tileWidth").getTextContent());
			tileHeight 	= Integer.parseInt(tilesetAttributes.getNamedItem("tileHeight").getTextContent()); 
		}
		
		Tile tile = new Tile(new Vector2f(tileWidth, tileHeight));
		tile.setPosition(new Vector3f(0, 0, 0));
		tile.setRotation(new Vector3f(0, 0, 1), 0);
		tile.setScale(new Vector3f(1, 1, 1));
		entity.setTile(tile);
	}

	@Override
	public void createTileset() {
		int textureWidth=0, textureHeight=0;
		String tilesetName = "", textureName = "";
		List<Tileset> tilesets = new ArrayList<>();
		NodeList tilesetElements = playerTilesetParser.getElementsByTagName("tileset");
		for(int i=0; i<tilesetElements.getLength(); i++){
			Tileset tileset = new Tileset();
			NamedNodeMap tilesetAttribute = tilesetElements.item(i).getAttributes();
			textureWidth	= Integer.parseInt(tilesetAttribute.getNamedItem("width").getTextContent());
			textureHeight	= Integer.parseInt(tilesetAttribute.getNamedItem("height").getTextContent());
			tilesetName		= tilesetAttribute.getNamedItem("name").getTextContent();
			textureName		= tilesetAttribute.getNamedItem("src").getTextContent(); 
			tileset.setName(tilesetName);
			tileset.setNumberOfRows(textureHeight/tileHeight);
			tileset.setNumberOfColumns(textureWidth/tileWidth);
			tileset.setTexture(new TilesetTexture(Util.TILESETS_PATH + textureName, new Vector2f(textureWidth, textureHeight)));
			tilesets.add(tileset);
		}
		
		entity.setTileset(tilesets);
	}

	@Override
	public void createAnimation() {
		NodeList animationNodes = playerParser.getElementsByTagName("animation");
		int animationLength=0, frameID=0;
		int frames[] = new int[1];
		List<Animation> animations = new ArrayList<>();
		String animationName = "";
		for(int i=0; i<animationNodes.getLength(); i++){
			NamedNodeMap animationAttributes = animationNodes.item(i).getAttributes();
			animationName 	= animationAttributes.getNamedItem("name").getTextContent();
			animationLength = Integer.parseInt(animationAttributes.getNamedItem("length").getTextContent()); 
			frames = new int[animationLength];

			NodeList animationFrames = playerParser.getNamedChilds("frame", animationNodes.item(i));
			for(int frame=0; frame<animationFrames.getLength(); frame++){
				NamedNodeMap frameAttributes = animationFrames.item(frame).getAttributes();
				frameID 		= Integer.parseInt(frameAttributes.getNamedItem("id").getTextContent());
				frames[frameID]	= Integer.parseInt(frameAttributes.getNamedItem("subTextureId").getTextContent());
			}
			animations.add(Animation.makeAnimation(animationName, animationLength, frames));
		}
		entity.setAnimation(animations);
	}

	@Override
	public void createShader() {
		PlayerShader shader = new PlayerShader(vertexShaderFile, fragmentShaderFile);
		entity.setShader(shader);
	}

	@Override
	public void bindBuffers() {
		entity.bindBuffers();
	}

}
