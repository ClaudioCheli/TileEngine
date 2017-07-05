package toolBox;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Buffer {
	
	/**
	 * return a <code>FloatBuffer</code> containig <code>data's</code> data
	 * @param data The data to be loaded into the buffer, in float[]
	 * @return FloatBuffer
	 */
	public static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer storeDataInFloatBuffer(Float[] data){
		float[] dataFl = new float[data.length];
		for(int i=0; i<data.length; i++)
			dataFl[i] = data[i].floatValue();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(dataFl);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * return a <code>IntBuffer</code> containig <code>data's</code> data
	 * @param data The data to be loaded into the buffer, in int[]
	 * @return IntBuffer
	 */
	public static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(Integer[] data){
		int[] dataInt = new int[data.length];
		for(int i=0; i<data.length; i++)
			dataInt[i] = data[i].intValue();
		IntBuffer buffer = BufferUtils.createIntBuffer(dataInt.length);
		buffer.put(dataInt);
		buffer.flip();
		return buffer;
	}

}
