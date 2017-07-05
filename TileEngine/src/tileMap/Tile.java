package tileMap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import toolBox.Buffer;

public class Tile {
	
	private Vector2f dimension 	  = new Vector2f(1, 1);
	private Vector3f position     = new Vector3f(0, 0, 0);
	private float rotationAngle   = 0;
	private Vector3f rotationAxis = new Vector3f(0, 0, 1);
	private Vector3f scale 		  = new Vector3f(1, 1, 1);
	private Matrix4f modelMatrix  = new Matrix4f();
	
	private float vertex[] = {
			1.0f, 0.0f, 0.0f, // Top Right
			1.0f, 1.0f, 0.0f, // Bottom Right
			0.0f, 1.0f, 0.0f, // Bottom Left
			0.0f, 0.0f, 0.0f 
	};
	
	private float texture[] = {
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f,
		0.0f, 0.0f
	};
	
	private int index[] = {
			0, 1, 3,
			1, 2, 3
	};
	
	public Tile(Vector2f dim){
		dimension.x = dim.x;
		dimension.y = dim.y;
		modelMatrix.setIdentity();
		for(int i=0; i<vertex.length; i+=3){
			vertex[i]		*= dim.x;
			vertex[i + 1]	*= dim.y;
		}
	}

	private float[] createVertexArray(){
		float[] vertexArray = new float[vertex.length + texture.length];
        int ver = 0;
        int tex = 0;
        int count = 0;
        for(int i=0; i<vertex.length/3; i++){
            vertexArray[count] = vertex[ver];  ver++; count++;
            vertexArray[count] = vertex[ver];  ver++; count++;
            vertexArray[count] = vertex[ver];  ver++; count++;
            vertexArray[count] = texture[tex]; tex++; count++;
            vertexArray[count] = texture[tex]; tex++; count++;
        }

        return vertexArray;
	}
	
	public FloatBuffer getVertexBuffer(){
		return Buffer.storeDataInFloatBuffer(createVertexArray());
	}
	
	public IntBuffer getIndexBuffer(){
		return Buffer.storeDataInIntBuffer(index);
	}
	
	public int getIndexCount() {return index.length;}
	public Vector2f getDimension() {return dimension;}
	public Vector3f getPosition() {return position;}
	public Matrix4f getModelMatrix() {return modelMatrix;}	
	
	public void setPosition(Vector3f position) {
		this.position.x = position.x; this.position.y = position.y; this.position.z = position.z;
		modelMatrix.translate(position);
	}
	
	public void increasePosition(Vector3f delta){
		Vector3f.add(position, delta, position);
		modelMatrix.translate(position);
	}

	public void setRotation(Vector3f rotationAxis, float rotationAngle){
		this.rotationAxis.x = rotationAxis.x;
		this.rotationAxis.y = rotationAxis.y;
		this.rotationAxis.z = rotationAxis.z;
		this.rotationAngle	= rotationAngle;
		modelMatrix.rotate(rotationAngle, rotationAxis);
	}
	
	public void setScale(Vector3f scale){
		this.scale.x = scale.x;
        this.scale.y = scale.y;
        this.scale.z = scale.z;
        modelMatrix.scale(scale);
	}

	
}
