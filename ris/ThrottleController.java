package ris;

import java.io.IOException;
import tage.*;

public class ThrottleController {
	
	private MyGame game;

	public ThrottleController(MyGame g) throws IOException {
		game = g;
	}
	
	private int throttleSign = 0;
	private int lastThrottleSign;
	public void update() {
		lastThrottleSign = throttleSign;
		throttleSign = game.getThrottleSign();
		
		switch(throttleSign) {
			case -1:
				if(lastThrottleSign == 0 || lastThrottleSign == 1) {
					game.throttleDownAndPauseAnimation();
				}
				break;
			case 0:
				if(lastThrottleSign == -1) {
					game.throttleBackFromDownAnimation();
				}
				if(lastThrottleSign == 1) {
					game.throttleBackFromUpAnimation();
				}
				break;
			case 1:
				if(lastThrottleSign == 0 || lastThrottleSign == -1) {
					game.throttleUpAndPauseAnimation();
				}
				break;
		}
	}
}