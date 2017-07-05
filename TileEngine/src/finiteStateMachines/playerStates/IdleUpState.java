package finiteStateMachines.playerStates;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class IdleUpState extends PlayerState {

	@Override
	public void enter(Player player) {
		idleUpAnimation.start(Timer.getTime());
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
				return null;
		} else if(direction.y == 1){
			return runningDownState;
		} else {
			return runningUpState;
		}
	}

	@Override
	public void update(Player player) {
		idleUpAnimation.update(Timer.getTime());
	}

	@Override
	public void exit() {
		idleUpAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return idleUpAnimation.getCurrentID();
	}

}
