package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Shader's base class
 * @author Cheli Claudio
 *
 */
public abstract class Shader {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Load shader's source from vertexShaderPath and fragmentShaderPath
	 * Create the program
	 * Bind the shader's attributes 
	 * Get all the uniform's location
	 * @param vertexShaderPath The vertex shader path
	 * @param fragmentShaderPath The fragment shader path
	 */
	public Shader(String vertexShaderPath, String fragmentShaderPath){
		vertexShaderID   = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
		programID		 = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocation();
	}
	
	/**
	 * Use this program
	 */
	public void start(){
		GL20.glUseProgram(programID);
	}
	
	/**
	 * call glUseProgram(0)
	 */
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	protected abstract void getAllUniformLocation();
	
	/**
	 * Get the uniformName location in the Shader.programID program
	 * @param uniformName The name of the uniform int the Shader.programID program
	 * @return The uniform location
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected abstract void bindAttributes();
	
	/**
	 * Bind the attribute attrib in program Shadre.programID whit the name variableName
	 * @param attrib The attribute in program Shader.programID
	 * @param variableName The name of the attribute
	 */
	protected void bindAttribute(int attrib, String variableName){
		GL20.glBindAttribLocation(programID, attrib, variableName);
	}
	
	/**
	 * Load an int uniform
	 * @param location The location of the uniform, in ints
	 * @param value The value of the uniform, in ints
	 */
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Load an float uniform
	 * @param location The location of the uniform, in ints
	 * @param value The value of the uniform, in floats
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Load a vec3 uniform
	 * @param location The location of the uniform, in ints
	 * @param vector The value of the uniform, in org.lwjgl.util.vector.Vector3f
	 */
	protected void loadVector3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Load a vec2 uniform
	 * @param location The location of the uniform, in ints
	 * @param vector The value of the uniform, in org.lwjgl.util.vector.Vector2f
	 */
	protected void loadVector2f(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	/**
	 * Load a boolean uniform, the value is converted in float
	 * @param location The location of the uniform, in ints
	 * @param value The value of the uniform, in bools
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value)
			toLoad = 1;
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * Load a mat4 uniform
	 * @param location The location of the uniform, in ints
	 * @param matrix the value of the uniform, in org.lwjgl.util.vector.Matrix4f
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
	 * Delete programs and shaders
	 */
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	public int getProgramID(){return programID;}
	
	/**
	 * Load and compile the shader located at shaderPath of the type type
	 * @param shaderPath The path of the shader
	 * @param type The type of the shader, GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
	 * @return The shaderID, in ints
	 */
	private static int loadShader(String shaderPath, int type){
		StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(shaderPath));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
	}

}
