package finiteStateMachines.playerStates;

import org.lwjgl.util.vector.Vector2f;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class RunningDownState extends PlayerState {

	@Override
	public void enter(Player player) {
		runningDownAnimation.start(Timer.getTime());
	}

	@Override
	public PlayerState handleInput(Player player) {
		//checkKeyEvents(player);
		direction = player.getDirection();
		if(direction.y == 0){
			if(direction.x == -1)
				return runningLeftState;
			else if(direction.x == 1)
				return runningRightState;
			else 
				return idleDownState;
		} else if(direction.y == -1){
			return runningUpState;
		} else {
			return null;
		}
	}

	@Override
	public void update(Player player) {
		float displacement = Player.SPEED * Timer.getFrameTime();
		player.setPosition(new Vector2f(direction.x*displacement, direction.y*displacement));
		runningDownAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		runningDownAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return runningDownAnimation.getCurrentID();
	}

}
