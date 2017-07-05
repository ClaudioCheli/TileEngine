package animation;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Animation{

	public static final int ANIMATION_IDLE_LEFT = 1;
	public static final int ANIMATION_IDLE_RIGHT = 2;
	public static final int ANIMATION_IDLE_DOWN = 3;
	public static final int ANIMATION_IDLE_UP = 4;
	public static final int ANIMATION_RUN_LEFT = 5;
	public static final int ANIMATION_RUN_RIGHT = 6;
	public static final int ANIMATION_RUN_DOWN = 7;
	public static final int ANIMATION_RUN_UP = 8;
	
	protected int type;
	
	protected int ids[];
	protected int index;
	protected int currentID;
	protected int length;
	protected long startTime;
	protected int frameTime = 200;
	protected String name; 
	
	/*public Animation(String name, int length, NodeList frames){
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
	}*/
	
	public Animation(int type, String animationName, int animationLength, int frames[]){
		this.type   = type;
        this.name   = animationName;
        this.length = animationLength;
        this.ids    = frames;
	}
	
	public abstract void update(long time);
	public abstract void start(long time);
	public abstract void stop();

	public int getType(){return type;}
	
	public abstract int getCurrentID();
	protected abstract void setCurrentID(int id);
	
	/*public void start(long time){
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
*/
	
}
