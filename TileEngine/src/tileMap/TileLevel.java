package tileMap;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class TileLevel {
	
	private String name;
	private List<Tileset> tilesets;
	private String activeTileset;
	private List<Integer> tilesID;
	private Vector2f levelDimension;
	private Vector2f tileDimension;
	private float tilesPositions[];

	public TileLevel(String name, List<Tileset> tilesets, String activeTileset, List<Integer> tilesID,
			Vector2f levelDimension, Vector2f tileDimension){
		this.name = name;
		this.tilesets = new ArrayList<>(tilesets);
		this.activeTileset = activeTileset;
		this.tilesID = new ArrayList<>(tilesID);
		this.levelDimension = new Vector2f(levelDimension.x, levelDimension.y);
		this.tileDimension = new Vector2f(tileDimension.x, tileDimension.y);
		calculateTilePositions();
		//printInfo();
	}
	
	public Vector2f getDimension(){return new Vector2f(levelDimension.x, levelDimension.y);}
	
	public float[] getTilePositions(){return tilesPositions;}
	
	public String getName(){return name;}
	
	public Tileset getActiveTileset(){return findTileset(activeTileset);}
	
	public int[] getTilesID(){
		int data[] = new int[tilesID.size()];
		for(int i=0; i<tilesID.size(); i++)
			data[i] = tilesID.get(i);
		return data;
	}
	
	private void calculateTilePositions(){
		tilesPositions = new float[(int) (levelDimension.x*levelDimension.y)*3];
		int k=0;
		for(int i=0; i<levelDimension.x; i++){
			for(int j=0; j<levelDimension.y; j++){
				tilesPositions[k++] = j*tileDimension.x;
				tilesPositions[k++] = i*tileDimension.y;
				tilesPositions[k++] = 50;
			}
		}
	}
	
	private Tileset findTileset(String name){
		int i = 0;
		while(i<tilesets.size()){
			if(tilesets.get(i).getName().matches(name))
				return tilesets.get(i);
			i++;
		}
		return null;
	}
	
	private void printInfo(){
		System.out.println("TileLevel: " + name);
		System.out.println("Active tileset: " + activeTileset);
		System.out.println("tileLevel dimension: " + levelDimension.x + ", " + levelDimension.y );
		for(int i=0; i<levelDimension.x; i++){
			for(int j=0; j<levelDimension.y; j++){
				System.out.print(tilesID.get((int)(j+(levelDimension.x*i))) + ", ");
			}
			System.out.println("");
		}
		int k=0;
		for(int i=0; i<levelDimension.x; i++){
			for(int j=0; j<levelDimension.y; j++){
				System.out.print(tilesPositions[k++] + ", " + tilesPositions[k++] + ", " + tilesPositions[k++] + " | ");
			}
			System.out.println("");
		}
	}

}
