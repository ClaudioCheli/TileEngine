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

}
