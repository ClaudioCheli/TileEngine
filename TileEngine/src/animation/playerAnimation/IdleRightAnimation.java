package animation.playerAnimation;

import animation.Animation;

public class IdleRightAnimation extends Animation {

	public IdleRightAnimation(int type, String animationName, int animationLength, int[] frames) {
		super(type, animationName, animationLength, frames);
		this.frameTime = 200;
	}

	@Override
	public void update(long time) {
		if(time - startTime > frameTime){
			index = (index+1)%length;
			currentID = ids[index];
			startTime += frameTime;
		}
	}

	@Override
	public void start(long time) {
		startTime = time;
		currentID = ids[0];
		index = 0;
	}

	@Override
	public void stop() {
		startTime = 0;
		currentID = ids[0];
		index = 0;
	}

	@Override
	public int getCurrentID() {
		return currentID;
	}

	@Override
	protected void setCurrentID(int id) {

	}

}
