package renderEngine;

import org.lwjgl.util.vector.Matrix4f;

public interface Renderable {
	
	void render();
	void handleInput();
	void update();
	void bindProjectionMatrix(Matrix4f projectionMatrix);
	void bindViewMatrix(Matrix4f viewMatrix);

}
