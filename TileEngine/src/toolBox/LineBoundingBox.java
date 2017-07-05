package toolBox;

import org.lwjgl.util.vector.Vector2f;

public class LineBoundingBox implements BoundingBox {
	
	 private Vector2f position;
	    private Vector2f endpoints[] = new Vector2f[2];
	    private Vector2f relativeEndpoints[] = new Vector2f[2];

	public LineBoundingBox(Vector2f position, Vector2f endpoints[]) {
		this.position   = new Vector2f(position.x, position.y);
        this.endpoints[0]  = new Vector2f(endpoints[0].x, endpoints[0].y);
        this.endpoints[1]  = new Vector2f(endpoints[1].x, endpoints[1].y);
        this.relativeEndpoints[0]  = new Vector2f(endpoints[0].x, endpoints[0].y);
        this.relativeEndpoints[1]  = new Vector2f(endpoints[1].x, endpoints[1].y);
    }

	@Override
	public void setPosition(Vector2f pos) {
        position.x = pos.x;
        position.y = pos.y;
        endpoints[0].x = relativeEndpoints[0].x + pos.x;
        endpoints[0].y = relativeEndpoints[0].y + pos.y;
        endpoints[1].x = relativeEndpoints[1].x + pos.x;
        endpoints[1].y = relativeEndpoints[1].y + pos.y;
	}

    @Override
    public Vector2f getPosition(){return position;}
    public Vector2f[] getEndpoints(){return endpoints;}

}
