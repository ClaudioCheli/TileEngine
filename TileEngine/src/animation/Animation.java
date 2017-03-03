package animation;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Animation{

	private int ids[];
	private int index;
	private int currentID;
	private int length;
	private long startingTime;
	private int frameTime = 200;
	private String name; 
	
	public Animation(String name, int length, NodeList frames){
		this.name = name;
		this.length = length;
		ids = new int[length];
		for(int frame=0; frame<frames.getLength(); frame++){
			if(frames.item(frame).getNodeType() == Node.ELEMENT_NODE){
				NamedNodeMap frameAttributes = frames.item(frame).getAttributes();
				int id=0;
				int subTextureId =0;

				for(int frameAttribute=0; frameAttribute<frameAttributes.getLength(); frameAttribute++){
					Node attribute = frameAttributes.item(frameAttribute);
					switch (attribute.getNodeName()) {
					case "id":
						id = Integer.parseInt(attribute.getTextContent());
						break;

					case "subTextureId":
						subTextureId = Integer.parseInt(attribute.getTextContent());
						break;
					}
				}
				ids[id] = subTextureId;
			}
		}
	}
	
	public void start(long time){
		startingTime = time;
		currentID = ids[0];
		index = 0;
	}
	
	public void update(long time){
		if(time - startingTime > frameTime){
			index = (index+1)%length;
			currentID = ids[index];
		    startingTime += frameTime;
		}
	}
	
	public void stop(){
		startingTime = 0;
		currentID = ids[0];
		index = 0;
	}
	
	public int getCurrentID(){return ids[index];}
	
	public String getName(){return name;}

	
}
