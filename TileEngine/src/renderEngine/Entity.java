package renderEngine;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import animation.Animation;
import shaders.Shader;
import tileMap.Tile;
import tileMap.Tileset;

public abstract class Entity implements Renderable{
	
	public abstract void setTile(Tile tile);
	public abstract void setTileset(List<Tileset> tileset);
	public abstract void setAnimation(List<Animation> animations);
	public abstract void setShader(Shader shader);
	public abstract void bindBuffers();
	
	public abstract void setType(int type);
	
	public abstract void updatePosition(Vector2f position);
	public abstract Vector2f getPosition();
	public abstract void setPosition(Vector2f position);
	
}
