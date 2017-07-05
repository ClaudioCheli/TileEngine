package toolBox;

import org.lwjgl.util.vector.Vector2f;

public class SquareBoundingBox implements BoundingBox {

	private Vector2f position;
	private Vector2f dimension;
	
	public SquareBoundingBox(Vector2f position, Vector2f dimension) {
		this.position   = new Vector2f(position.x, position.y);
        this.dimension  = new Vector2f(dimension.x, dimension.y);
	}

	@Override
	public void setPosition(Vector2f pos) {
		position.x = pos.x;
        position.y = pos.y;
	}

	@Override
	public Vector2f getPosition() {return new Vector2f(position);}
	
	public Vector2f getDimension() {return new Vector2f(dimension);}
 
}
