package builder;

import renderEngine.Entity;

public abstract class EntityBuilder {

	protected Entity entity;
	
	public abstract void createEntity() throws Exception;
	public abstract void createTile() throws Exception;
	public abstract void createTileset();
	public abstract void createAnimation();
	public abstract void createShader();
	public abstract void bindBuffers();
	
	
	public Entity getEntity(){
		return entity;
	}
	
}
