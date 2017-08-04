package camera;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position	= new Vector3f();
	private float pitch, yaw, roll;
	
	private Matrix4f view = new Matrix4f();
	
	public Camera(){
		pitch	= 0;
		yaw		= 0;
		roll	= 0;
	}
	
	public void move(Vector2f position){
		Vector3f delta = new Vector3f(this.position.x - position.x, this.position.y - position.y, 0);
		view.translate(delta);
		this.position.x = position.x;
		this.position.y = position.y;
	}

	public void createViewMatrix(){
		view.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1,0,0), view,
				view);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0,1,0), view,
				view);
		Vector3f negativeCameraPos = new Vector3f(-position.x,-position.y,-position.z);
		Matrix4f.translate(negativeCameraPos, view, view);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	public Matrix4f getViewMatrix(){return view;}
	
	/*
	private Vector3f position	= new Vector3f();
	private Vector3f rotation	= new Vector3f();
	private Vector3f look		= new Vector3f();
	private Vector3f up			= new Vector3f();
	
	private Matrix4f view = new Matrix4f();
	
	public Camera(Vector3f position, Vector3f rotation){
		this.position = position;
		this.rotation = rotation;	
		createMatrix();
	}
	
	public void createMatrix(){
		view.setIdentity();
		view.rotate(0, rotation);
		view.translate(position);
	}
	
	public Camera(Vector3f position, Vector3f look, Vector3f up){
		this.position.x	= position.x;
		this.position.y	= position.y;
		this.position.y	= position.y;
		this.look.x		= look.x;
		this.look.y		= look.y;
		this.look.z		= look.z;
		this.up.x		= up.x;
		this.up.y		= up.y;
		this.up.z		= up.z;
		
		view.setIdentity();
		//lookAt(position, look, up);
	}
	
	/*public void lookAt(Vector3f position, Vector3f look, Vector3f up){
		Vector3f forward = new Vector3f();
		Vector3f.sub(position, look, forward);
		forward.normalise();
		Vector3f upV = new Vector3f(up);
		upV.normalise();
		Vector3f side = new Vector3f();
		Vector3f.cross(forward, upV, side);
		Vector3f.cross(side, forward, upV);
		view.m00 = side.x;
		view.m10 = side.y;
		view.m20 = side.z;
		view.m01 = upV.x;
		view.m11 = upV.y;
		view.m21 = upV.z;
		view.m02 = -forward.x;
		view.m12 = -forward.y;
		view.m22 = -forward.z;
		
		Matrix4f.translate(position, view, view);
	}
	


	public void move(Vector3f position){
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
		this.look.x		= position.x;
		this.look.y		= position.y;
		createMatrix();
		//lookAt(position, look, up);
	}
	
	public Matrix4f getViewMatrix(){ return view;}
*/
}
