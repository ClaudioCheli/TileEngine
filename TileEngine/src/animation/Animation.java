package animation;

import animation.playerAnimation.IdleDownAnimation;
import animation.playerAnimation.IdleLeftAnimation;
import animation.playerAnimation.IdleRightAnimation;
import animation.playerAnimation.IdleUpAnimation;
import animation.playerAnimation.RunningDownAnimation;
import animation.playerAnimation.RunningLeftAnimation;
import animation.playerAnimation.RunningRightAnimation;
import animation.playerAnimation.RunningUpAnimation;

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
	
	public Animation(int type, String animationName, int animationLength, int frames[]){
		this.type   = type;
        this.name   = animationName;
        this.length = animationLength;
        this.ids    = frames;
	}
	
	public static Animation makeAnimation(String type, int animationLength, int[] frames) {
		switch (type) {
		case "idle_left":
			return new IdleLeftAnimation(Animation.ANIMATION_IDLE_LEFT, type, animationLength, frames);
		case "idle_right":
			return new IdleRightAnimation(Animation.ANIMATION_IDLE_RIGHT, type, animationLength, frames);
		case "idle_down":
			return new IdleDownAnimation(Animation.ANIMATION_IDLE_DOWN, type, animationLength, frames);
		case "idle_up":
			return new IdleUpAnimation(Animation.ANIMATION_IDLE_UP, type, animationLength, frames);
		case "walk_left":
			return new RunningLeftAnimation(Animation.ANIMATION_RUN_LEFT, type, animationLength, frames);
		case "walk_right":
			return new RunningRightAnimation(Animation.ANIMATION_RUN_RIGHT, type, animationLength, frames);
		case "walk_down":
			return new RunningDownAnimation(Animation.ANIMATION_RUN_DOWN, type, animationLength, frames);
		case "walk_up":
			return new RunningUpAnimation(Animation.ANIMATION_RUN_UP, type, animationLength, frames);
		}
		return null;
	}
	
	public abstract void update(long time);
	public abstract void start(long time);
	public abstract void stop();

	public int getType(){return type;}
	
	public abstract int getCurrentID();
	protected abstract void setCurrentID(int id);
	
}
