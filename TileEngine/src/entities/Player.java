package entities;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import animation.Animation;
import input.Input;
import renderEngine.Renderable;
import shaders.PlayerShader;
import test.Error;
import tileMap.Tileset;
import toolBox.Buffer;
import toolBox.Timer;

public class Player implements Renderable, Observer{
	
	private static final float playerVertex[] = {
			// Positions		 // Texture
			64.0f,   0.0f, 0.0f, 1.0f, 0.0f, // Top Right
			64.0f, 64.0f, 0.0f, 1.0f, 1.0f, // Bottom Right
			 0.0f, 64.0f, 0.0f, 0.0f, 1.0f, // Bottom Left
			 0.0f,   0.0f, 0.0f, 0.0f, 0.0f  // Top Left
	};

	private static final int playerIndex[] = {
			0, 1, 3,	// First Triangle
			1, 2, 3		// Second Triangle
	};

	private int VAO, VBO, EBO;
	private Matrix4f modelMatrix = new Matrix4f();
	private Vector3f position;
	private float rotationAngle = 0;
	private Vector3f rotationAxis = new Vector3f(0, 0, 1);
	private Vector3f scale = new Vector3f(1f, 1f, 1);

	private long lastFrame = 0;
	private boolean moving = false;
	private float velocity = 100; //in pixel per second
	private Vector3f direction = new Vector3f();
	
	private Animation walkUp;
	private Animation walkDown;
	private Animation walkLeft;
	private Animation walkRight;
	private Animation idleDown;
	private Animation currentAnimation;
	private List<Tileset> tilesets = new ArrayList<Tileset>();
	
	private Input input;
	
	private PlayerShader shader;
	
	/**
	 * Create a player whit the info contained in the file located at <code>playerDefinitionPath</code> path
	 * @param playerDefinitionPath The path of definition file
	 * @param shader The player shader
	 */
	public Player(String playerDefinitionPath, PlayerShader shader, Input input) {
		this.shader = shader;
		this.input = input;
		position = new Vector3f(0, 0, 0);
		try {
			findResource(playerDefinitionPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		createModelMatrix();
		bindBuffer();
	}
	
	/**
	 * Update player's properties like position, etc...
	 * @param deltaT the time elapsed since last frame
	 */
	public void updatePosition(long deltaT, long time){
		float deltaS = (float) deltaT/1000;
		float displacement = deltaS*velocity;
		//System.out.println("deltaS: " + deltaS + ", DeltaT: " + deltaT + ", Displacement: " + displacement);
		
		Vector3f delta = new Vector3f(direction.x*displacement, direction.y*displacement, direction.z*displacement);
		increasePosition(delta);	
		currentAnimation.update(Timer.getTime());
	}
	
	/**
	 * Render the player
	 */
	@Override
	public void render() {
		shader.start();
		
		bindAttribute();
		bindTexture();
		bindUniform();
		GL11.glDrawElements(GL11.GL_TRIANGLES, playerIndex.length, GL11.GL_UNSIGNED_INT, 0);
		Error.exitOnGLError("Error: draw");
		unbindAttribute();
		
		shader.stop();
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
	 * Load player's uniforms
	 */
	private void bindUniform(){
		shader.loadModelMatrix(modelMatrix);
		shader.loadTilesetNumberOfColumns(tilesets.get(0).getNumberOfColumns());
		shader.loadTilesetNumberOfRows(tilesets.get(0).getNumberOfRows());
		shader.loadTextureIndex(currentAnimation.getCurrentID());
		
		Error.exitOnGLError("Error: bindUniform");
	}
	
	/**
	 * Create VAO and VBOs 
	 */
	private void bindBuffer(){
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();
		EBO = GL15.glGenBuffers();
		
		GL30.glBindVertexArray(VAO);
		
		//Bind vertexBuffer		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		FloatBuffer fBuffer = Buffer.storeDataInFloatBuffer(playerVertex);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		//vertexPosition
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*Float.BYTES,0);
		
		//texture
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);
		GL30.glBindVertexArray(0);
		
		//Bind indexBuffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		IntBuffer iBuffer = Buffer.storeDataInIntBuffer(playerIndex);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		Error.exitOnGLError("Error: bindBuffer");
	}
	
	/**
	 * Unbind VAO and index buffer
	 */
	private void unbindAttribute(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
	}
	
	/**
	 * Create tilesets
	 * @param playerDefinitionPath The player definition's path
	 * @throws Exception
	 */
	private void findResource(String playerDefinitionPath) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(playerDefinitionPath));
		
		loadTileset(document.getElementsByTagName("tileset"));
		
		loadAnimation(document.getElementsByTagName("animation"));
	}
	
	private void loadTileset(NodeList tilesetElements){
		String tilesetDefinitionPath = null;
		String tilesetImagePath = null;
		
		for(int i=0; i<tilesetElements.getLength(); i++){
			for(int j=0; j<tilesetElements.item(i).getAttributes().getLength(); j++){
				Node attribute = tilesetElements.item(i).getAttributes().item(j);
				switch (attribute.getNodeName()) {
				case "definitionPath":
					tilesetDefinitionPath = attribute.getTextContent();
					break;
				case "imagePath":
					tilesetImagePath = attribute.getTextContent();
					break;
				}
			}
			if(tilesetDefinitionPath != null && tilesetImagePath != null)
				tilesets.add(new Tileset(tilesetDefinitionPath, tilesetImagePath));
		}
	}
	
	private void loadAnimation(NodeList animationElements){
		String name = "";
		int length = 0;
		for(int i=0; i<animationElements.getLength(); i++){
			NamedNodeMap animationAttributes = animationElements.item(i).getAttributes();
			for(int j=0; j<animationAttributes.getLength(); j++){
				Node attribute = animationAttributes.item(j);
				switch (attribute.getNodeName()) {
				case "name":
					name = attribute.getTextContent();
					break;
				case "length":
					length = Integer.parseInt(attribute.getTextContent());
					break;
				}
			}
			switch (name) {
			case "walk_up":
				walkUp = new Animation(name, length, animationElements.item(i).getChildNodes());
				break;
			case "walk_down":
				walkDown = new Animation(name, length, animationElements.item(i).getChildNodes());
				break;
			case "walk_left":
				walkLeft = new Animation(name, length, animationElements.item(i).getChildNodes());
				break;
			case "walk_right":
				walkRight = new Animation(name, length, animationElements.item(i).getChildNodes());
				break;
			case "idle_down":
				idleDown = new Animation(name, length, animationElements.item(i).getChildNodes());
			}
			
		}
		currentAnimation = idleDown;
	}
	
	private void createModelMatrix(){
		modelMatrix = new Matrix4f();
		modelMatrix.translate(position);
		modelMatrix.rotate(rotationAngle, rotationAxis);
		modelMatrix.scale(scale);
	}
	
	public void setPosition(Vector3f position){
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	/**
	 * Increase the player's position of delta
	 * @param delta the amount of movement
	 */
	public void increasePosition(Vector3f delta){
		position.translate(delta.x, delta.y, delta.z);
		modelMatrix.translate(delta);
		//System.out.println("Player position: " + position.x + ", " + position.y + ", " + position.z);
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
	public void update(Observable inputObs, Object arg1) {
		if(inputObs == input){
			currentAnimation.stop();		
			List<Integer> keyEvents = (List<Integer>) arg1;
			boolean bW = false;
			boolean bA = false;
			boolean bS = false;
			boolean bD = false;
			for(int i=0; i<keyEvents.size(); i++){
				switch (keyEvents.get(i)) {
				case Input.W:
					bW = true;
					break;
				case Input.A:
					bA = true;
					break;
				case Input.S:
					bS = true;
					break;
				case Input.D:
					bD = true;
					break;
				}
			}
			
			if(bW || bS){
				int w = input.getKeyState(Input.W) ? 1 : 0;
				int s = input.getKeyState(Input.S) ? 1 : 0;
				direction.y = -w + s;
				if(direction.y == 1)
					currentAnimation = walkDown;
				if(direction.y == -1)
					currentAnimation = walkUp;
				if(direction.y == 0 && direction.x == 1)
					currentAnimation = walkRight;
				if(direction.y == 0 && direction.x == -1)
					currentAnimation = walkLeft;
			}
			
			if(bA || bD){
				int a = input.getKeyState(Input.A) ? 1 : 0;
				int d = input.getKeyState(Input.D) ? 1 : 0;
				direction.x = -a + d;
				if(direction.x == 1)
					currentAnimation = walkRight;
				if(direction.x == -1)
					currentAnimation = walkLeft;
				if(direction.x == 0 && direction.y == 1)
					currentAnimation = walkDown;
				if(direction.x == 0 && direction.y == -1)
					currentAnimation = walkUp;
			}

			if(!input.getKeyState(Input.W) 
					&& !input.getKeyState(Input.A)
					&& !input.getKeyState(Input.S)
					&& !input.getKeyState(Input.D)){
				currentAnimation = idleDown;
			}
			
			currentAnimation.start(Timer.getTime());
		}
	}

}
