package gameObjectBuilder;

import renderEngine.Entity;

public class EntityCreationDirector {
	
	private EntityBuilder builder;

	public void createEntity() throws Exception{
		builder.createEntity();
		builder.createTile();
		builder.createTileset();
		builder.createAnimation();
		builder.createShader();
		builder.bindBuffers();
	}
	
	public void setEntityBuilder(EntityBuilder builder){
		this.builder = builder;
	}
	
	public Entity getEntity(){
		return builder.getEntity();
	}

}
