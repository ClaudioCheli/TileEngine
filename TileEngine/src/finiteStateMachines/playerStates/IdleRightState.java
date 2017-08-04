package finiteStateMachines.playerStates;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class IdleRightState extends PlayerState {

	@Override
	public void enter(Player player) {
		idleRightAnimation.start(Timer.getTime());
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
				return null;
		} else if(direction.x == -1){
			return runningLeftState;
		} else {
			return runningRightState;
		}
	}

	@Override
	public void update(Player player) {
		idleRightAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		idleRightAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return idleRightAnimation.getCurrentID();
	}

}
