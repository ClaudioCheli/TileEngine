package finiteStateMachines.playerStates;

import org.lwjgl.util.vector.Vector2f;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class RunningRightState extends PlayerState {

	@Override
	public void enter(Player player) {
		runningRightAnimation.start(Timer.getTime());
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
				return idleRightState;
		} else if(direction.x == -1){
			return runningLeftState;
		} else {
			return null;
		}
	}

	@Override
	public void update(Player player) {
		float displacement = Player.SPEED * Timer.getFrameTime();
		player.updatePosition(new Vector2f(direction.x*displacement, direction.y*displacement));
		runningRightAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		runningRightAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return runningRightAnimation.getCurrentID();
	}

}
