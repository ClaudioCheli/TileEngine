package tileMap;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class TileLevel {

	private String name;
	private Vector2f tileDimension = new Vector2f();
	private Vector2f levelDimension = new Vector2f();
	private List<Integer> levelData = new ArrayList<>();
	private float[] positions;
	private Tileset tileset;

	public TileLevel() {
	}

	public void calculatePositions() {
		positions = new float[(int) (levelDimension.x * levelDimension.y * 3)];
		int k = 0;
		for (int i = 0; i < levelDimension.y; i++) {
			for (int j = 0; j < levelDimension.x; j++) {
				positions[k] = j * tileDimension.x;
				k++;
				positions[k] = i * tileDimension.y;
				k++;
				positions[k] = -10;
				k++;
			}
			
		}
	}

	public void setData(String data) {
		data = data.replaceAll("\\s+", "");
		String[] tilesBuffer = data.split(",");
		for (String str : tilesBuffer) {
			levelData.add(Integer.parseInt(str));
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLevelDimension(Vector2f dimension) {
		this.levelDimension = dimension;
	}

	public void setTileDimension(Vector2f dimension) {
		this.tileDimension = dimension;
	}

	public int getTilesNumber() {
		return positions.length / 3;
	}

	public int getTileId(int tile) {
		try {
			return levelData.get(tile).intValue();
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
	}

	public Vector2f getDimension() {
		return new Vector2f(levelDimension);
	}

	public Vector2f getTilePositions(int tile) {
		return new Vector2f(positions[tile * 3], positions[tile * 3 + 1]);
	}

	public float[] getPositions() {
		return positions;
	}

	public List<Integer> getTextureIndex() {
		return levelData;
	}

	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}

	public Tileset getTileset() {
		return tileset;
	}

}
