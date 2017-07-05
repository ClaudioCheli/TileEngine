package finiteStateMachines;

import animation.Animation;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import animation.playerAnimation.IdleDownAnimation;
import animation.playerAnimation.IdleLeftAnimation;
import animation.playerAnimation.IdleRightAnimation;
import animation.playerAnimation.IdleUpAnimation;
import animation.playerAnimation.RunningDownAnimation;
import animation.playerAnimation.RunningLeftAnimation;
import animation.playerAnimation.RunningRightAnimation;
import animation.playerAnimation.RunningUpAnimation;
import entities.Player;
import finiteStateMachines.playerStates.IdleDownState;
import finiteStateMachines.playerStates.IdleLeftState;
import finiteStateMachines.playerStates.IdleRightState;
import finiteStateMachines.playerStates.IdleUpState;
import finiteStateMachines.playerStates.RunningDownState;
import finiteStateMachines.playerStates.RunningLeftState;
import finiteStateMachines.playerStates.RunningRightState;
import finiteStateMachines.playerStates.RunningUpState;

public abstract class PlayerState {
	
	public static IdleLeftState		idleLeftState		= new IdleLeftState();
	public static IdleRightState	idleRightState		= new IdleRightState();
	public static IdleDownState		idleDownState		= new IdleDownState();
	public static IdleUpState		idleUpState			= new IdleUpState();
	public static RunningLeftState	runningLeftState	= new RunningLeftState();
	public static RunningRightState	runningRightState	= new RunningRightState();
	public static RunningDownState	runningDownState	= new RunningDownState();
	public static RunningUpState	runningUpState		= new RunningUpState();
	
	protected static IdleRightAnimation		idleRightAnimation;
	protected static IdleLeftAnimation		idleLeftAnimation;
	protected static IdleDownAnimation		idleDownAnimation;
	protected static IdleUpAnimation		idleUpAnimation;
	protected static RunningLeftAnimation	runningLeftAnimation;
	protected static RunningRightAnimation	runningRightAnimation;
	protected static RunningDownAnimation	runningDownAnimation;
	protected static RunningUpAnimation		runningUpAnimation;
	
	protected Vector2f direction = new Vector2f();
	
	public abstract void enter(Player player);
	public abstract PlayerState handleInput(Player player);
	public abstract void update(Player player);
	public abstract void exit();
	public abstract int getAnimationID();
	
	/*protected void checkKeyEvents(Player player){
		boolean keyState[] = player.getKeyStates();
		List<Integer> keyEvents = player.getKeyEvents();
		boolean bW = false;
		boolean bA = false;
		boolean bS = false;
		boolean bD = false;
		for(int i=0; i<keyEvents.size(); i++){
			System.out.println(keyEvents.get(i));
			switch (keyEvents.get(i)) {
			case Input.W:
				bW = true;
				break;
			case Input.A:
				bA = true;
				break;
			case Input.S:
				bS = true;
				break;
			case Input.D:
				bD = true;
				break;
			}
		}
		
		if(bW || bS){
			int w = keyState[Input.W] ? 1 : 0;
			int s = keyState[Input.S] ? 1 : 0;
			direction.y = -w + s;
		}
		if(bA || bD){
			int a = keyState[Input.A] ? 1 : 0;
			int d = keyState[Input.D] ? 1 : 0;
			direction.x = -a + d;
		}
		if(bW)
			System.out.print("W ");
		if(bA)
			System.out.print("A ");
		if(bD)
			System.out.print("D ");
		if(bS)
			System.out.print("S ");
		//System.out.println("direction: " + direction.x + ", " + direction.y);
	}*/
	
	public static void setAnimations(List<Animation> animations){
		for(Animation animation : animations){
			switch (animation.getType()) {
			case Animation.ANIMATION_IDLE_LEFT:
				idleLeftAnimation = (IdleLeftAnimation) animation;
				break;
			case Animation.ANIMATION_IDLE_RIGHT:
				idleRightAnimation = (IdleRightAnimation) animation;
				break;
			case Animation.ANIMATION_IDLE_DOWN:
				idleDownAnimation = (IdleDownAnimation) animation;
				break;
			case Animation.ANIMATION_IDLE_UP:
				idleUpAnimation = (IdleUpAnimation) animation;
				break;
			case Animation.ANIMATION_RUN_LEFT:
				runningLeftAnimation = (RunningLeftAnimation) animation;
				break;
			case Animation.ANIMATION_RUN_RIGHT:
				runningRightAnimation = (RunningRightAnimation) animation;
				break;
			case Animation.ANIMATION_RUN_DOWN:
				runningDownAnimation = (RunningDownAnimation) animation;
				break;
			case Animation.ANIMATION_RUN_UP:
				runningUpAnimation = (RunningUpAnimation) animation;
				break;
			}
		}
	}

}
