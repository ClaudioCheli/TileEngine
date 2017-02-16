package renderEngine;

import org.lwjgl.util.vector.Matrix4f;

public interface Renderable {
	
	public void render();
	
	public void bindProjectionMatrix(Matrix4f projectionMatrix);

}
