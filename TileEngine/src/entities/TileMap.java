package entities;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import renderEngine.Renderable;
import shaders.TileMapShader;
import test.Error;
import tileMap.TileLevel;
import tileMap.Tileset;
import toolBox.Buffer;

public class TileMap implements Renderable{
	
	private static final float tileVertex[] = {
			// Positions		// Texture
			64.0f,  0.0f, 0.0f, 1.0f, 0.0f, // Top Right
			64.0f, 64.0f, 0.0f, 1.0f, 1.0f, // Bottom Right
			 0.0f, 64.0f, 0.0f, 0.0f, 1.0f, // Bottom Left
			 0.0f,  0.0f, 0.0f, 0.0f, 0.0f  // Top Left
	};
	
	private static final int tileIndex[] = {
			0, 1, 3,	// First Triangle
			1, 2, 3		// Second Triangle
	};
	
	private int VAO, VBO, EBO, positionVBO;
	private int ssbo[];
	private TileMapShader shader;
	
	private List<Tileset> tilesets = new ArrayList<Tileset>();
	private List<TileLevel> levels = new ArrayList<TileLevel>();
	
	/**
	 * Create a tile map whit the info contained in the file located at <code>tileMapDefinitionPath</code> path
	 * @param tileMapDefinitionPath The path of definition file
	 * @param shader The tile map shader
	 */
	public TileMap(String tileMapDefinitionPath, TileMapShader shader){
		this.shader = shader;
		try {
			findResource(tileMapDefinitionPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bindBuffer();
	}
	
	/**
	 * Render the tile map
	 */
	@Override
	public void render() {
		shader.start();
		
		bindAttribute();
		
		//for(int level=0; level<levels.size(); level++){
			bindTexture(0);
			bindUniform(0);
			bindLevelSSBO(0);
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, tileIndex.length, GL11.GL_UNSIGNED_INT, 0, (int) (levels.get(0).getDimension().x*levels.get(0).getDimension().y)*4);
		//}
		unbindAttribute();
		
		shader.stop();
	}

	/**
	 * Bind the texture for the level <code>level</code>
	 * @param level The level's id, in ints
	 */
	public void bindTexture(int level) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, levels.get(level).getActiveTileset().getTextureID());
		
		Error.exitOnGLError("Error: tileMap bindTexture");
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
	
	/**
	 * Load level's uniforms in <code>shader</code>
	 * @param level The level's id, in ints
	 */
	private void bindUniform(int level){
		//System.out.println("columns: " + levels.get(level).getActiveTileset().getNumberOfColumns());
		//System.out.println("rows: " + levels.get(level).getActiveTileset().getNumberOfRows());
		shader.loadTilesetNumberOfColumns(17);
		shader.loadTilesetNumberOfRows(17);
	}
	
	/**
	 * Bind VAO and index buffer  
	 */
	private void bindAttribute(){
		GL30.glBindVertexArray(VAO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		
		Error.exitOnGLError("Error: tilaMap bindAttribute");
	}
	
	/**
	 * Load textureID's ssbo of current <code>level</code> in <code>shader</code>
	 * @param level The level's id, in ints
	 */
	private void bindLevelSSBO(int level){
		GL30.glBindVertexArray(VAO);
		GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 3, ssbo[level]);
	}
	
	/**
	 * Create VAO and VBOs 
	 */
	private void bindBuffer(){
		VAO 		= GL30.glGenVertexArrays();
		VBO 		= GL15.glGenBuffers();
		EBO 		= GL15.glGenBuffers();
		positionVBO = GL15.glGenBuffers();
		ssbo = new int[levels.size()];
		for(int i=0; i<levels.size(); i++)
			ssbo[i] = GL15.glGenBuffers();
		
		
		GL30.glBindVertexArray(VAO);
		
		//Bind vertexBuffer		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		FloatBuffer fBuffer = Buffer.storeDataInFloatBuffer(tileVertex);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fBuffer, GL15.GL_STATIC_DRAW);
		
		//vertexPosition
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5*Float.BYTES,0);
		GL20.glEnableVertexAttribArray(0);
		
		//texture
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);		
		GL20.glEnableVertexAttribArray(1);
		
		//Bind positionBuffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionVBO);
		FloatBuffer positionBuffer = Buffer.storeDataInFloatBuffer(levels.get(0).getTilePositions());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 3*Float.BYTES, 0);
		GL33.glVertexAttribDivisor(2, 1);
		GL20.glEnableVertexAttribArray(2);
		
		//bind ssbo
		for(int i=0; i<ssbo.length; i++){
			GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, ssbo[i]);
			GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, Buffer.storeDataInIntBuffer(levels.get(i).getTilesID()), GL15.GL_STATIC_DRAW);
		}
		GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		
		//Bind indexBuffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
		IntBuffer iBuffer = Buffer.storeDataInIntBuffer(tileIndex);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}

	/**
	 * Unbind VAO and index buffer
	 */
	private void unbindAttribute(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Create tileLevels and tilesets
	 * @param tileMapDefinitionPath The tile map definition's path
	 * @throws Exception
	 */
	private void findResource(String tileMapDefinitionPath) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(tileMapDefinitionPath));
		
		//Crea i tilesets
		loadTileset(document.getElementsByTagName("tileset"));

		//Crea i tileLevels
		loadTileLevels(document.getElementsByTagName("layer"));
		
		
	}

	private void loadTileset(NodeList nodes){
		String tilesetDefinitionPath = null;
		String tilesetImagePath = null;

		//NodeList nodeList = document.getElementsByTagName("tileset");
		for (int i=0; i<nodes.getLength(); i++) {
			for(int j=0; j<nodes.item(i).getAttributes().getLength(); j++){
				Node attribute = nodes.item(i).getAttributes().item(j);
				switch (attribute.getNodeName()) {
				case "definitionPath":
					tilesetDefinitionPath = attribute.getTextContent();
					break;
				case "imagePath":
					tilesetImagePath = attribute.getTextContent();
					break;
				}
			}
			//System.out.println("definitionPath: " + tilesetDefinitionPath);
			//System.out.println("imagePath: " + tilesetImagePath);
			if(tilesetDefinitionPath != null && tilesetImagePath != null)
				tilesets.add(new Tileset(tilesetDefinitionPath, tilesetImagePath));
		}
	}

	private void loadTileLevels(NodeList layers){
		String tileLevelName = ""; 
		String activeTileset = "";
		List<Integer> data = new ArrayList<>();
		Vector2f tileLevelDimension = new Vector2f();

		for(int layerNumber=0; layerNumber<layers.getLength(); layerNumber++){
			Node layer = layers.item(layerNumber);
			if(layer.hasAttributes()){
				NamedNodeMap layerAttributes = layer.getAttributes();
				tileLevelName = layerAttributes.getNamedItem("name").getTextContent();
				tileLevelDimension.x = Integer.parseInt(layerAttributes.getNamedItem("width").getTextContent());
				tileLevelDimension.y = Integer.parseInt(layerAttributes.getNamedItem("height").getTextContent());
				System.out.println("TileLevel name: " + tileLevelName);
				System.out.println("TileLevel dimension: " + tileLevelDimension.x + " " + tileLevelDimension.y);
			}

			NodeList layerChild = layer.getChildNodes();
			for(int layerChildNumber=0; layerChildNumber<layerChild.getLength(); layerChildNumber++){
				switch (layerChild.item(layerChildNumber).getNodeName()) {
				case "properties":
					NodeList properties = layerChild.item(layerChildNumber).getChildNodes();
					for(int propertiesNumber=0; propertiesNumber<properties.getLength(); propertiesNumber++){
						if(properties.item(propertiesNumber).hasAttributes()){
							NamedNodeMap propertyAttributes = properties.item(propertiesNumber).getAttributes();
							if(propertyAttributes.getNamedItem("name").getTextContent().equalsIgnoreCase("tileset"))
								activeTileset = propertyAttributes.getNamedItem("value").getTextContent();
							System.out.println("active tileSet: " + activeTileset);
						}
					}	
					break;
				case "data":
					String sData = layerChild.item(layerChildNumber).getTextContent();
					sData = sData.replaceAll("\\s+", "");
					String[] tilesBuffer = sData.split(",");
					data = new ArrayList<>();
					for(int i=0; i< tilesBuffer.length; i++)
						data.add(Integer.parseInt(tilesBuffer[i]));
					//for(int i=0; i<data.size(); i++)
					//	System.out.print(data.get(i));
					//System.out.println("");
					break;
				}
			}

		levels.add(new TileLevel(tileLevelName, tilesets, activeTileset, data, tileLevelDimension, new Vector2f(64, 64)));
	}
}


}
