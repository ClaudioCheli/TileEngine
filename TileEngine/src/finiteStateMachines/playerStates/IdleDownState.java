package finiteStateMachines.playerStates;

import entities.Player;
import finiteStateMachines.PlayerState;
import toolBox.Timer;

public class IdleDownState extends PlayerState {

	@Override
	public void enter(Player player) {
		idleDownAnimation.start(Timer.getTime());
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
		idleDownAnimation.update(Timer.getTime());
		
	}

	@Override
	public void exit() {
		idleDownAnimation.stop();
	}

	@Override
	public int getAnimationID() {
		return idleDownAnimation.getCurrentID();
	}

}
