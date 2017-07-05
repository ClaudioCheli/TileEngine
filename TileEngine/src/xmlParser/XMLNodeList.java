package xmlParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLNodeList implements NodeList{

	private List<Node> nodes = new ArrayList<>();

	@Override
	public int getLength() {
		return nodes.size();
	}

	@Override
	public Node item(int index) {
		return nodes.get(index);
	}
	
	public void add(Node node){
		nodes.add(node);
	}

}