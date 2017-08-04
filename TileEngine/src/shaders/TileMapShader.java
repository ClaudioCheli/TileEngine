package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class TileMapShader extends Shader{

	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_tilesetNumberOfRows;
	private int location_tilesetNumberOfColumns;
	private int location_samplerArray;

	/**
	 * Call the Shader constructor whit parameters 
	 * VERTEX_SHADER_PATH and FRAGMENT_SHADER_PATH
	 */
	public TileMapShader(String vertexShaderFile, String fragmentShaderFile) {
		super(vertexShaderFile, fragmentShaderFile);
	}

	/**
	 * Get all the uniform locations.
	 * For every location attributes call Shader.getUniformLocation(String uniformName)
	 */
	@Override
	protected void getAllUniformLocation() {
		location_viewMatrix 			= super.getUniformLocation("view");
		location_projectionMatrix 		= super.getUniformLocation("projection");
		location_tilesetNumberOfRows	= super.getUniformLocation("tilesetNumberOfRows");
		location_tilesetNumberOfColumns = super.getUniformLocation("tilesetNumberOfColumns");
		location_samplerArray			= super.getUniformLocation("myTextures");
	}

	/**
	 * Bind the shader's attribute whith a locations
	 * For every shader's attribute call Shader.bindAttribute(int attrib, String variableName)
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "tilePosition");
	}

	/**
	 * Load in the program Shader.ProgramID the number of tileset's rows
	 * @param tilesetNumberOfRows The number of tileset's row, in ints
	 */
	public void loadTilesetNumberOfRows(int tilesetNumberOfRows){
		super.loadInt(location_tilesetNumberOfRows, tilesetNumberOfRows);
	}
	
	/**
	 * Load in the program Shader.ProgramID the number of tileset's columns
	 * @param tilesetNumberOfColumns The number of tileset's columns, in ints
	 */
	public void loadTilesetNumberOfColumns(int tilesetNumberOfColumns){
		super.loadInt(location_tilesetNumberOfColumns, tilesetNumberOfColumns);
	}
	
	/**
	 * Load in the program Shader.ProgramID the projection matrix
	 * @param matrix The projection Matrix, in org.lwjgl.util.vector.Matrix4f
	 */
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	/**
	 * Load in the program Shader.ProgramID the view matrix
	 * @param matrix The view Matrix, in org.lwjgl.util.vector.Matrix4f
	 */
	public void loadViewMatrix(Matrix4f matrix){
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void connectTextureUnits(int[] units){
		super.loadIntArray(location_samplerArray, units);
	}
	
}
