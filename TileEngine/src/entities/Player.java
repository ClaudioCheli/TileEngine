package entities;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import animation.Animation;
import finiteStateMachines.PlayerState;
import input.Input;
import renderEngine.Entity;
import shaders.PlayerShader;
import shaders.Shader;
import test.Error;
import tileMap.Tile;
import tileMap.Tileset;

public class Player extends Entity implements Observer{
	
	private int VAO, VBO, EBO;
	private PlayerShader shader;
	private Tile tile;
	private List<Tileset> tilesets;
	private PlayerState state;
	private Vector2f direction = new Vector2f();

	public static final float SPEED = 0.2f;
	
	private boolean keyState[] = new boolean[Input.MAX_KEYS];

	//private Input input;

	
	public Player(){}
	
	/**
	 * Render the player
	 */
	@Override
	public void render() {
		shader.start();
		
		bindAttribute();
		bindTexture();
		bindUniform();
		GL11.glDrawElements(GL11.GL_TRIANGLES, tile.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		Error.exitOnGLError("Error: draw");
		unbindAttribute();
		
		shader.stop();
	}
	
    @Override
    public void handleInput(){
        PlayerState newState = state.handleInput(this);
        if(newState != null){
            state = newState;
            state.enter(this);
        }
    }

    @Override
    public void update() {
        state.update(this);
    }

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
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		
		GL30.glBindVertexArray(VAO);
		
		//Bind vertexBuffer		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		FloatBuffer vertexBuffer = tile.getVertexBuffer();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*Float.BYTES,0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//Bind indexBuffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		IntBuffer indexBuffer = tile.getIndexBuffer();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		Error.exitOnGLError("Error: bindBuffer");
	}

    @Override
    public void setAnimation(List<Animation> animations) {
        PlayerState.setAnimations(animations);
        state = PlayerState.idleRightState;
        state.enter(this);
    }

	@Override
    public void setTile(Tile tile) { this.tile = tile; }
    @Override
    public void setTileset(List<Tileset> tilesets) { this.tilesets = tilesets; }
    @Override
    public void setType(int type){}
    @Override
    public void setShader(Shader shader) { this.shader = (PlayerShader) shader; }
    public PlayerShader getShader(){return shader;}
    public int getVAO(){return VAO;}
    public void updatePosition(Vector2f position){ increasePosition(new Vector3f(position.x,position.y,0.0f)); }
    public Vector2f getPosition(){ return new Vector2f(tile.getPosition().x, tile.getPosition().y); }
    public void setPosition(Vector2f position){ tile.setPosition(new Vector3f(position.x, position.y, 0.0f)); }
    public void increasePosition(Vector3f delta){ tile.increasePosition(delta); }
    
    public boolean[] getKeyStates(){return keyState;}
    public void setKeyState(boolean[] keyState){this.keyState = keyState;}
    
    public synchronized void setDirection(Vector2f direction){this.direction = direction;}
    public synchronized Vector2f getDirection(){return direction;}
    
	/**
	 * Load player's uniforms
	 */
	private void bindUniform(){
		shader.loadModelMatrix(tile.getModelMatrix());
		shader.loadTilesetNumberOfColumns(tilesets.get(0).getNumberOfColumns());
		shader.loadTilesetNumberOfRows(tilesets.get(0).getNumberOfRows());
		shader.loadTextureIndex(state.getAnimationID());
		Error.exitOnGLError("Error: bindUniform");
	}
	

	/**
	 * Bind the player's texture
	 */
	private void bindTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tilesets.get(0).getTextureID());
		
		Error.exitOnGLError("Error: bindTexture");
	}
	
	/**
	 * Bind VAO and index buffer  
	 */
	private void bindAttribute(){
		GL30.glBindVertexArray(VAO);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		
		Error.exitOnGLError("Error: bindAttribute");
	}
	
	/**
	 * Unbind VAO and index buffer
	 */
	private void unbindAttribute(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void update(Observable inputObs, Object arg1) {
		if(inputObs.getClass() == Input.class ){
			keyState = ((Input) inputObs).getKeyStates();
			
			Vector2f direction = new Vector2f();
			int w = keyState[Input.W] ? 1 : 0;
			int s = keyState[Input.S] ? 1 : 0;
			direction.y = -w + s;
			int a = keyState[Input.A] ? 1 : 0;
			int d = keyState[Input.D] ? 1 : 0;
			direction.x = -a + d;
			setDirection(direction);

		}
	}
	
}
