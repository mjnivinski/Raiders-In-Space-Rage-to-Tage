package ris;

import java.io.IOException;

import tage.*;

/*
import static ray.rage.scene.SkeletalEntity.EndType.NONE;
import ris.MyGame;
import ray.rage.Engine;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
*/

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



















