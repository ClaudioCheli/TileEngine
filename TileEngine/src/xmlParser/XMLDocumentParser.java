package xmlParser;

import java.io.FileNotFoundException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDocumentParser {

	private Document document;
	
	public XMLDocumentParser(Document document) {
		this.document = document;
	}
	
	public NodeList getElementsByTagName(String name){
		return document.getElementsByTagName(name);
	}
	
	/**
	 * return the node's child named <code>name</code>
	 * @param name The child's name
	 * @param node The parent node
	 * @return The node if found or null if not
	 */
	public Node getNamedChild(String name, Node node){
		NodeList childs = node.getChildNodes();
		int i = 0;
		while (i<childs.getLength()) {
			if(childs.item(i).getNodeName().equalsIgnoreCase(name))
				return childs.item(i);
			i++;
		}
		return null;
	}
	
	public NodeList getNamedChilds(String name, Node node){
		XMLNodeList tmp = new XMLNodeList();
		NodeList childs = node.getChildNodes();
		for(int i=0; i<childs.getLength(); i++){
			if(childs.item(i).getNodeName().equalsIgnoreCase(name))
				tmp.add(childs.item(i));
			i++;
		}
		return tmp;
	}

}


