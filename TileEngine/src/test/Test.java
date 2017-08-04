package test;

import static org.junit.Assert.*;

import org.lwjgl.util.vector.Vector2f;

import toolBox.SquareBoundingBox;

public class Test {

	@org.junit.Test
	public void testSetPosition() {
		SquareBoundingBox box = new SquareBoundingBox(new Vector2f(),
				new Vector2f());
		Vector2f position = new Vector2f(10, 20);
		box.setPosition(position);
		assertEquals(10, box.getPosition().x, 0.001);
		assertEquals(20, box.getPosition().y, 0.001);

	}


}
