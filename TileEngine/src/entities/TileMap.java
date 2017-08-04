package entities;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL43;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.Renderable;
import shaders.TileMapShader;
import test.Error;
import texture.TilesetTexture;
import tileMap.TileLevel;
import toolBox.Buffer;

public class TileMap implements Renderable{
	
    private int VAO, VBO, EBO, positionVBO, textureIndexVBO, ssbo;
    
    private int texturesNumber;

    //private List<Tileset> tilesets = new ArrayList<>();
    private List<TilesetTexture> textures = new ArrayList<>();
    private List<TileLevel> tileLevels = new ArrayList<>();
    private int tileCount = 0;
    
    // Equals in every tilesets
    private int tilesetsRow;
    private int tilesetsColumn;

    public static Vector2f dimension;

    private TileMapShader shader;

    private float[] vertexArray = {
            // Positions
            64.0f,  0.0f, 0.0f, 1.0f, 0.0f, // Top Right
            64.0f, 64.0f, 0.0f, 1.0f, 1.0f, // Bottom Right
            0.0f, 64.0f, 0.0f, 0.0f, 1.0f, // Bottom Left
            0.0f,  0.0f, 0.0f, 0.0f, 0.0f  // Top Left
    };

    private int[] indexArray = {
            0, 1, 3, // First Triangle
            1, 2, 3  // Second Triangle
    };

	
    public TileMap(){

    }

	
	/**
	 * Render the tile map
	 */
    @Override
    public void render() {
        shader.start();
        bindAttribute();
        bindTexture();
        bindUniform();
        bindSSBO();
        GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, indexArray.length, GL11.GL_UNSIGNED_INT, 0, tileCount);
        unbindAttribute();

        shader.stop();
    }
    
    @Override
    public void handleInput() {

    }

    private void bindUniform() {
        shader.loadTilesetNumberOfRows(tilesetsRow);
        shader.loadTilesetNumberOfColumns(tilesetsColumn);
    }

	/**
	 * Bind the texture for the level <code>level</code>
	 * @param level The level's id, in ints
	 */
	public void bindTexture() {
		for(int i=0; i<texturesNumber; i++){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(i).getTextureID());
			//System.out.println("texture " + textures.get(i).getTextureID() + " connected to unit " + (GL13.GL_TEXTURE0 + i));
			Error.exitOnGLError("Error: tileMap glTexSubImage3D " + i);
		}
	}
	
	private void bindSSBO(){
		 GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 3, ssbo);
	}
	
	/**
	 * Bind VAO and index buffer  
	 */
	private void bindAttribute(){
		GL30.glBindVertexArray(VAO);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
	}

	/**
	 * Unbind VAO and index buffer
	 */
	private void unbindAttribute(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}
	
    @Override
    public void update() {

    }
	
	/**
	 * Load projection matrix in <code>shader</code>
	 * @param projectionMatrix The projection matrix's value, in org.lwjgl.util.vector.Matrix4f
	 */
	@Override
	public void bindProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	@Override
	public void bindViewMatrix(Matrix4f viewMatrix){
		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.stop();
	}
	
	/**
	 * Create VAO and VBOs 
	 */
	public void bindBuffers(){
		VAO 			= GL30.glGenVertexArrays();
		VBO 			= GL15.glGenBuffers();
		EBO 			= GL15.glGenBuffers();
		positionVBO 	= GL15.glGenBuffers();
		textureIndexVBO	= GL15.glGenBuffers();
		ssbo			= GL15.glGenBuffers();
		
		GL30.glBindVertexArray(VAO);
		
		// Bind vertex and texture buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		FloatBuffer vertexBuffer = Buffer.storeDataInFloatBuffer(vertexArray);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*Float.BYTES,0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//Bind positionBuffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionVBO);
		List<Float> positionsList = new ArrayList<>();
		for(TileLevel level : tileLevels){
			float[] levelPos = level.getPositions();
			for(int i=0; i<levelPos.length; i++){
				positionsList.add(levelPos[i]);
			}
		}
		
		FloatBuffer positionBuffer = Buffer.storeDataInFloatBuffer(positionsList.toArray(new Float[positionsList.size()]));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 3*Float.BYTES, 0);
		GL33.glVertexAttribDivisor(2, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Bind textureIndex buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureIndexVBO);
		List<Integer> textureIndexList = new ArrayList<>();
		for(TileLevel level : tileLevels){
			textureIndexList.addAll(level.getTextureIndex());
		}
		int indexAr[] = new int[textureIndexList.size()];
		for(int i=0; i<textureIndexList.size(); i++){
			indexAr[i] = textureIndexList.get(i).intValue();
		}
		IntBuffer textureIndexBuffer = Buffer.storeDataInIntBuffer(indexAr);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureIndexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(3, 1, GL11.GL_UNSIGNED_INT, false, 1*4, 0);
		GL33.glVertexAttribDivisor(3, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// Bind ssbo
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo);
		GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, textureIndexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
		
		//Bind indexBuffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		IntBuffer indexBuffer = Buffer.storeDataInIntBuffer(indexArray);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
	}
	
	//public void setTileset(List<Tileset> tilesets){this.tilesets = tilesets;}
	public void setTileLevels(List<TileLevel> tileLevels){
		tilesetsRow	 	= tileLevels.get(0).getTileset().getNumberOfRows();
		tilesetsColumn 	= tileLevels.get(0).getTileset().getNumberOfColumns();
		
		List<String> tilesetNames = new ArrayList<>();
		for(TileLevel level : tileLevels){
			if(!isInList(tilesetNames, level.getTileset().getName())){
				tilesetNames.add(level.getTileset().getName());
				textures.add(level.getTileset().getTexture());
			}
		}
		texturesNumber = tilesetNames.size();
		
		for(TileLevel level : tileLevels)
			tileCount += level.getTilesNumber();
		this.tileLevels = tileLevels;
	}
	
	private boolean isInList(List<String> tilesetNames, String name){
		for(String str : tilesetNames){
			if(str.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
    public TileLevel getTileLevel(int level){return tileLevels.get(level);}
    //public Tileset getTileset(int set){return tilesets.get(set);}
	public void setShader(TileMapShader shader){
		this.shader = shader;
		int[] units = {GL13.GL_TEXTURE0,GL13.GL_TEXTURE0+1};
		shader.connectTextureUnits(units);
    	//for(int i=0; i<32; i++)
    	//	shader.loadTexture(i);
	}

}
