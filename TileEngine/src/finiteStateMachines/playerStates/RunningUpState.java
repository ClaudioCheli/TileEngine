package finiteStateMachines.playerStates;

import org.lwjgl.util.vector.Vector2f;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class RunningUpState extends PlayerState {

	@Override
	public void enter(Player player) {
		runningUpAnimation.start(Timer.getTime());
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
				return idleUpState;
		} else if(direction.y == 1){
			return runningDownState;
		} else {
			return null;
		}
	}

	@Override
	public void update(Player player) {
		float displacement = Player.SPEED * Timer.getFrameTime();
		player.setPosition(new Vector2f(direction.x*displacement, direction.y*displacement));
		runningUpAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		runningUpAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return runningUpAnimation.getCurrentID();
	}

}
