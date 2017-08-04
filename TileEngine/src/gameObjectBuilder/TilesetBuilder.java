package gameObjectBuilder;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import texture.TilesetTexture;
import tileMap.Tileset;
import toolBox.BoundingBox;
import toolBox.LineBoundingBox;
import toolBox.SquareBoundingBox;
import toolBox.Util;
import xmlParser.XMLDocumentParser;

public class TilesetBuilder {

	public static Tileset createTileset(String tilesetName, XMLDocumentParser tilesetsParser) {
		Tileset tileset = new Tileset();
		setAttributes(tileset, tilesetName, tilesetsParser);
		createBoundingBoxes(tileset, tilesetName, tilesetsParser);
		return tileset;
	}

	private static void setAttributes(Tileset tileset, String tilesetName, XMLDocumentParser tilesetsParser) {
		NodeList tilesets = tilesetsParser.getElementsByTagName("tilesets");

		for (int i = 0; i < tilesets.getLength(); i++) {
			NodeList tilesetNodes = tilesets.item(i).getChildNodes();
			int textureWidth = 1, textureHeight = 1, tileWidth = 0, tileHeight = 0;
			String textureName = "";

			Node tilesetNode = null;
			int index = 0;
			while (tilesetNode == null && index < tilesetNodes.getLength()) {
				NamedNodeMap tilesetAttributes = tilesetNodes.item(index).getAttributes();
				if (tilesetAttributes != null) {
					Node nameNode = tilesetAttributes.getNamedItem("name");
					String name = nameNode.getTextContent();
					if (name.equalsIgnoreCase(tilesetName))
						tilesetNode = tilesetNodes.item(index);
				}
				index++;
			}
			NamedNodeMap tilesetAttributes = tilesetNode.getAttributes();
			textureWidth = Integer.parseInt(tilesetAttributes.getNamedItem("width").getTextContent());
			textureHeight = Integer.parseInt(tilesetAttributes.getNamedItem("height").getTextContent());
			tileWidth = Integer.parseInt(tilesetAttributes.getNamedItem("tilewidth").getTextContent());
			tileHeight = Integer.parseInt(tilesetAttributes.getNamedItem("tileheight").getTextContent());
			tileset.setTileDimension(new Vector2f(tileWidth, tileHeight));
			textureName = tilesetAttributes.getNamedItem("src").getTextContent();
			tileset.setName(tilesetAttributes.getNamedItem("name").getTextContent());
			tileset.setNumberOfRows(textureHeight / tileHeight);
			tileset.setNumberOfColumns(textureWidth / tileWidth);
			tileset.setTexture(
					new TilesetTexture(Util.TILESETS_PATH + textureName, new Vector2f(textureWidth, textureHeight)));
		}
	}

	private static void createBoundingBoxes(Tileset tileset, String tilesetName, XMLDocumentParser tilesetsParser) {
		NodeList tilesetsNodes = tilesetsParser.getElementsByTagName("tileset");
		Node tilesetNode = null;
		Map<Integer, BoundingBox> boundingBoxes = new HashMap<>();
		int tileID = 0, index = 0;
		String boxType = "";
		Vector2f boundingBoxPosition = new Vector2f();
		Vector2f boundingBoxDimension = new Vector2f();
		Vector2f boundingBoxEndpoints[] = new Vector2f[2];
		while (tilesetNode == null && index < tilesetsNodes.getLength()) {
			if (tilesetsNodes.item(index).getAttributes().getNamedItem("name").getTextContent()
					.equalsIgnoreCase(tilesetName))
				tilesetNode = tilesetsNodes.item(index);
			index++;
		}
		NodeList tiles = tilesetsParser.getNamedChilds("tile", tilesetNode);
		for (int j = 0; j < tiles.getLength(); j++) {
			tileID = Integer.parseInt(tiles.item(j).getAttributes().getNamedItem("id").getTextContent());
			Node object = tilesetsParser.getNamedChild("object",
					tilesetsParser.getNamedChild("objectgroup", tiles.item(j)));
			NamedNodeMap objectAttributes = object.getAttributes();
			boxType = objectAttributes.getNamedItem("type").getTextContent();
			boundingBoxPosition.x = Integer.parseInt(objectAttributes.getNamedItem("x").getTextContent());
			boundingBoxPosition.y = Integer.parseInt(objectAttributes.getNamedItem("y").getTextContent());
			boundingBoxDimension.x = Integer.parseInt(objectAttributes.getNamedItem("width").getTextContent());
			boundingBoxDimension.y = Integer.parseInt(objectAttributes.getNamedItem("height").getTextContent());
			if (boxType.equalsIgnoreCase("square")) {
				boundingBoxes.put(tileID,
						new SquareBoundingBox(new Vector2f(boundingBoxPosition), new Vector2f(boundingBoxDimension)));
			} else if (boxType.equalsIgnoreCase("line")) {
				Node polygon = tilesetsParser.getNamedChild("polygon", object);
				String allPoints = polygon.getAttributes().getNamedItem("points").getTextContent();
				String point[] = allPoints.split(" ");
				String coordinate[];
				coordinate = point[0].split(",");
				boundingBoxEndpoints[0] = new Vector2f();
				boundingBoxEndpoints[0].x = Integer.parseInt(coordinate[0]);
				boundingBoxEndpoints[0].y = Integer.parseInt(coordinate[1]);
				coordinate = point[1].split(",");
				boundingBoxEndpoints[1] = new Vector2f();
				boundingBoxEndpoints[1].x = Integer.parseInt(coordinate[0]);
				boundingBoxEndpoints[1].y = Integer.parseInt(coordinate[1]);

				boundingBoxes.put(tileID, new LineBoundingBox(new Vector2f(boundingBoxPosition), boundingBoxEndpoints));
			}
		}
		tileset.setBoundingBoxes(boundingBoxes);
	}

}
