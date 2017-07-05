package builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import animation.Animation;
import animation.playerAnimation.IdleDownAnimation;
import animation.playerAnimation.IdleLeftAnimation;
import animation.playerAnimation.IdleRightAnimation;
import animation.playerAnimation.IdleUpAnimation;
import animation.playerAnimation.RunningDownAnimation;
import animation.playerAnimation.RunningLeftAnimation;
import animation.playerAnimation.RunningRightAnimation;
import animation.playerAnimation.RunningUpAnimation;
import entities.Player;
import shaders.PlayerShader;
import texture.TilesetTexture;
import tileMap.Tile;
import tileMap.Tileset;

public class PlayerBuilder extends EntityBuilder{
	
	private static final String PLAYER_FILE = "res/entities/knight.xml";
	private static final String PLAYER_DEF	= "res/tilesets/knight_def.xml";

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document playerDocument;
	private Document playerDefDocument;
	
	@Override
	public void createEntity() throws Exception {
		entity = new Player();
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		playerDocument		= builder.parse(new File(PLAYER_FILE));
		playerDefDocument	= builder.parse(new File(PLAYER_DEF));
	}

	@Override
	public void createTile() throws Exception{
		int tileWidth	= 0;
		int tileHeight	= 0;
		NodeList tilesetElements = playerDefDocument.getElementsByTagName("tileset");
		for(int i=0; i<tilesetElements.getLength(); i++){
			for(int j=0; j<tilesetElements.item(i).getAttributes().getLength(); j++){
				Node attribute = tilesetElements.item(i).getAttributes().item(j);
				switch (attribute.getNodeName()) {
				case "tileWidth":
					tileWidth = Integer.parseInt(attribute.getTextContent());
					break;
				case "tileHeight":
					tileHeight = Integer.parseInt(attribute.getTextContent());
					break;
				}
			}
		}
		
		Tile tile = new Tile(new Vector2f(tileWidth, tileHeight));
		tile.setPosition(new Vector3f(0, 0, 0));
		tile.setRotation(new Vector3f(0, 0, 1), 0);
		tile.setScale(new Vector3f(1, 1, 1));
		entity.setTile(tile);
	}

	@Override
	public void createTileset() {
		int textureWidth=0, textureHeight=0, tilesNumber=0, tileWidth=0, tileHeight=0;
		String name = "", imagePath = "";
		NodeList tilesetElements = playerDefDocument.getElementsByTagName("tileset");
		for(int i=0; i<tilesetElements.getLength(); i++){
			for(int j=0; j<tilesetElements.item(i).getAttributes().getLength(); j++){
				Node attribute = tilesetElements.item(i).getAttributes().item(j);
				switch (attribute.getNodeName()) {
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
				case "name":
					name = attribute.getTextContent();
					break;
				case "src":
					imagePath = attribute.getTextContent();
				}
			}
		}
		
		Tileset tileset = new Tileset();
		tileset.setName(name);
		tileset.setNumberOfRows(textureHeight/tileHeight);
		tileset.setNumberOfColumns(textureWidth/tileWidth);
		tileset.setTexture(new TilesetTexture(imagePath, new Vector2f(textureWidth, textureHeight)));
		List<Tileset> tilesets = new ArrayList<>();
		tilesets.add(tileset);
		entity.setTileset(tilesets);
	}

	@Override
	public void createAnimation() {
		NodeList animationNode = playerDocument.getElementsByTagName("animation");
		int animationLength=0, frameID=0;
		int frames[] = new int[1];
		List<Animation> animations = new ArrayList<>();
		Animation idleRight	= null;
		Animation idleLeft	= null;
		Animation idleDown	= null;
		Animation idleUp	= null;
		Animation runRight	= null;
		Animation runLeft	= null;
		Animation runDown	= null;
		Animation runUp		= null;
		String animationName = "";
		for(int i=0; i<animationNode.getLength(); i++){
			for(int attrNum=0; attrNum<animationNode.item(i).getAttributes().getLength(); attrNum++){
				Node attribute = animationNode.item(i).getAttributes().item(attrNum);
				switch(attribute.getNodeName()){
				case "name":
					animationName = attribute.getTextContent();
					break;
				case "length":
					animationLength = Integer.parseInt(attribute.getTextContent());
					frames = new int[animationLength];
				}
			}
			NodeList animationFrames = animationNode.item(i).getChildNodes();
			for(int frame=0; frame<animationFrames.getLength(); frame++){
				if(animationFrames.item(frame).getNodeName().equalsIgnoreCase("frame")){
					for(int attrNum=0; attrNum<animationFrames.item(frame).getAttributes().getLength(); attrNum++){
						Node attribute = animationFrames.item(frame).getAttributes().item(attrNum);
						frameID = Integer.parseInt(animationFrames.item(frame).getAttributes().getNamedItem("id").getTextContent());
						frames[frameID] = Integer.parseInt(animationFrames.item(frame).getAttributes().getNamedItem("subTextureId").getTextContent());
						/*switch (attribute.getNodeName()) {
						case "id":
							frameID = Integer.parseInt(attribute.getTextContent());
							break;
						case "subTextureId":
							frames[frameID] = Integer.parseInt(attribute.getTextContent());
							break;
						}*/
					}
				}
			}
			switch (animationName) {
			case "idle_left":
				idleLeft = new IdleLeftAnimation(Animation.ANIMATION_IDLE_LEFT, animationName, animationLength, frames);
				animations.add(idleLeft);
				break;
			case "idle_right":
				idleRight = new IdleRightAnimation(Animation.ANIMATION_IDLE_RIGHT, animationName, animationLength, frames);
				animations.add(idleRight);
				break;
			case "idle_down":
				idleDown = new IdleDownAnimation(Animation.ANIMATION_IDLE_DOWN, animationName, animationLength, frames);
				animations.add(idleDown);
				break;
			case "idle_up":
				idleUp = new IdleUpAnimation(Animation.ANIMATION_IDLE_UP, animationName, animationLength, frames);
				animations.add(idleUp);
				break;
			case "walk_left":
				runLeft = new RunningLeftAnimation(Animation.ANIMATION_RUN_LEFT, animationName, animationLength, frames);
				animations.add(runLeft);
				break;
			case "walk_right":
				runRight = new RunningRightAnimation(Animation.ANIMATION_RUN_RIGHT, animationName, animationLength, frames);
				animations.add(runRight);
				break;
			case "walk_down":
				runDown = new RunningDownAnimation(Animation.ANIMATION_RUN_DOWN, animationName, animationLength, frames);
				animations.add(runDown);
				break;
			case "walk_up":
				runUp = new RunningUpAnimation(Animation.ANIMATION_RUN_UP, animationName, animationLength, frames);
				animations.add(runUp);
				break;
			}
		}
		entity.setAnimation(animations);
	}

	@Override
	public void createShader() {
		PlayerShader shader = new PlayerShader();
		entity.setShader(shader);
	}

	@Override
	public void bindBuffers() {
		entity.bindBuffers();
	}

}
