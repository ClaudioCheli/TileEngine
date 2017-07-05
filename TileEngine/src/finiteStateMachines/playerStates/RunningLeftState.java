package finiteStateMachines.playerStates;

import org.lwjgl.util.vector.Vector2f;

import entities.Player;
import finiteStateMachines.PlayerState;
import input.Input;
import toolBox.Timer;

public class RunningLeftState extends PlayerState {

	@Override
	public void enter(Player player) {
		runningLeftAnimation.start(Timer.getTime());
	}

	@Override
	public PlayerState handleInput(Player player) {
		//checkKeyEvents(player);
		direction = player.getDirection();
		if(direction.x == 0){
			if(direction.y == -1)
				return runningUpState;
			else if(direction.y == 1)
				return runningDownState;
			else 
				return idleLeftState;
		} else if(direction.x == 1){
			return runningRightState;
		} else {
			return null;
		}
	}

	@Override
	public void update(Player player) {
		float displacement = Player.SPEED * Timer.getFrameTime();
		player.setPosition(new Vector2f(direction.x*displacement, direction.y*displacement));
		runningLeftAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		runningLeftAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return runningLeftAnimation.getCurrentID();
	}

}
