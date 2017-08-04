package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import camera.Camera;
import display.DisplayManager;

public class Renderer {
	
	private static final float NEAR_PLANE = 0;
	private static final float FAR_PLANE  = 100;
	private static final float RIGHT 	  = DisplayManager.WIDTH;
	private static final float LEFT 	  = 0;
	private static final float TOP 		  = 0;
	private static final float BOTTOM	  = DisplayManager.HEIGHT;
	
	public Matrix4f projectionMatrix;
	
	public Renderer(){
		createOrtographicProjectionMatrix();
	}
	
	/**
	 * Render a list of <code>Renderable</code> object
	 * @param renderables The list of object to be rendered, in <code>List&#60Renderable&#62</code>
	 */
	public void render(List<Entity> renderables){
		for (Entity renderable : renderables) {
			renderable.update();
			renderable.bindProjectionMatrix(projectionMatrix);
			renderable.render();
		}
	}
	
	public void render(List<Renderable> renderables, Camera camera){
		DisplayManager.clear();
		for(Renderable renderable : renderables){
			renderable.update();
			renderable.bindProjectionMatrix(projectionMatrix);
			renderable.bindViewMatrix(camera.getViewMatrix());
			renderable.render();
		}
	}
	
	public void clear(){
		GL11.glClearColor(0.2f, 0.3f, 0.5f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Create projection matrix whith ortographic projection
	 */
	private void createOrtographicProjectionMatrix(){
		float FMinN = FAR_PLANE-NEAR_PLANE;
		float FPlusN = FAR_PLANE+NEAR_PLANE;
		projectionMatrix = new Matrix4f();
		projectionMatrix.setZero();
		projectionMatrix.m00 = 2/(RIGHT-LEFT);
		projectionMatrix.m11 = 2/(TOP-BOTTOM);
		projectionMatrix.m22 = -2/FMinN;
		projectionMatrix.m30 = -(RIGHT+LEFT)/(RIGHT-LEFT);
		projectionMatrix.m31 = -(TOP+BOTTOM)/(TOP-BOTTOM);
		projectionMatrix.m32 = -(FPlusN)/(FMinN);
		projectionMatrix.m33 = 1;
	}
	
	


}
