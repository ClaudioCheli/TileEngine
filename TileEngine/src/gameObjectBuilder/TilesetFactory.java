package gameObjectBuilder;

import java.util.ArrayList;
import java.util.List;

import tileMap.Tileset;
import xmlParser.XMLDocumentParser;

public class TilesetFactory {
	
	private static TilesetFactory factory = null;
	
	private List<Tileset> tilesets = new ArrayList<>();
	
	private TilesetFactory(){}
	
	public static TilesetFactory getInstance(){
		if(factory == null)
			factory = new TilesetFactory();
		return factory;
	}
	
	public Tileset makeTileset(String tilesetName, XMLDocumentParser tilesetsParser){
		Tileset tileset = null;
		if((tileset = isInTilesets(tilesetName)) == null){
			tileset = TilesetBuilder.createTileset(tilesetName, tilesetsParser);
			tilesets.add(tileset);
		}
		return tileset;
	}
	
	private Tileset isInTilesets(String name){
		Tileset tileset = null;
		int i = 0;
		while (tileset == null && i<tilesets.size()) {
			if(tilesets.get(i).getName().equalsIgnoreCase(name)){
				tileset = tilesets.get(i);
			}
			i++;
		}
		return tileset;
	}

}
