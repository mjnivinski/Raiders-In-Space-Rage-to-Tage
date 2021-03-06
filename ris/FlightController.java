package ris;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

import tage.*;
import tage.input.*;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import org.joml.*;

//Controller
public class FlightController {
	
	private MyGame game;
	private Engine eng;
	private PhysicsEngine physics;
	private InputManager im;
	private Camera camera;
	
	private GameObject ship;
	private GameObject shipSeat;
	
	ShipController shipController;
	
	//Gamepad Abstract Action Classes
	
	ControllerThrottle CT;
	ControllerRoll CR;
	ControllerYaw CY;
	ControllerPitch CP;
	
	FireWeapon FW;
	
	RollLeft rollLeft;
	RollRight rollRight;
	PitchForward pitchForward;
	PitchBackward pitchBackward;
	YawLeft yawLeft;
	YawRight yawRight;
	ThrottleUp throttleUp;
	ThrottleDown throttleDown;
	
	KeyboardFireWeapon KFW;
	
	Vector3f cameraOffset = new Vector3f(0f,0f,-1.7f);
	
	float deltaTime;
	
	Vector3f basePosition;
	
	//public FlightController(MyGame g, Camera c, SceneNode cN, SceneNode t, InputManager im, SceneManager sm, PhysicsEngine physics) throws IOException {
	public FlightController(MyGame g) throws IOException {
		System.out.println("Flight Controller");
		game = g;
		camera = game.getCamera();
		eng = game.getEngine();
		im = g.getInputManager();
		ship = g.getPlayerShip();
		physics = g.getPhysicsEngine();
		
		setupInput();
		
		shipController = new ShipController(game, this);
		
		shipSeat = new GameObject(ship);
		shipSeat.setLocalLocation(cameraOffset);
		shipSeat.applyParentRotationToPosition(true);
		
		basePosition = shipSeat.getLocalLocation();
	}
	
	private void setupInput() {
		System.out.println("setupInput Flightcontroller");
		String gamepadName = im.getFirstGamepadName();
		
		ArrayList<Controller> controllers = im.getControllers();
		ArrayList<String> keyboards = new ArrayList<String>();
		
		for (int i = 0; i < controllers.size(); i++) {
			if (controllers.get(i).getType() == Controller.Type.KEYBOARD)
				keyboards.add(controllers.get(i).getName());
		}
		
		if(gamepadName != null) setupGamepad(im, gamepadName);
		setupKeyboard(im);
	}
	
	private void setupGamepad(InputManager im, String controllerName) {
		CT  = new ControllerThrottle();
		CR = new ControllerRoll();
		CY = new ControllerYaw();
		CP = new ControllerPitch();
		FW = new FireWeapon();

		im.associateAction(controllerName, net.java.games.input.Component.Identifier.Axis.X, CR,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction(controllerName, net.java.games.input.Component.Identifier.Axis.Y, CT,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction(controllerName, net.java.games.input.Component.Identifier.Axis.RX, CY,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction(controllerName, net.java.games.input.Component.Identifier.Axis.RY, CP,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction(controllerName, net.java.games.input.Component.Identifier.Axis.Z, FW,
				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	
	private void setupKeyboard(InputManager im) {
		ArrayList<Controller> controllers = im.getControllers();
		ArrayList<String> keyboards = new ArrayList<String>();
		
		for (int i = 0; i < controllers.size(); i++) {
			if (controllers.get(i).getType() == Controller.Type.KEYBOARD)
				keyboards.add(controllers.get(i).getName());
		}
		
		rollLeft = new RollLeft();
		rollRight = new RollRight();
		pitchForward = new PitchForward();
		pitchBackward = new PitchBackward();
		throttleUp = new ThrottleUp();
		throttleDown = new ThrottleDown();
		yawLeft = new YawLeft();
		yawRight = new YawRight();
		KFW = new KeyboardFireWeapon();
		
		for (int i = 0; i < keyboards.size(); i++) {
			
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.Q, rollLeft,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.E, rollRight,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.S, pitchForward,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.W, pitchBackward,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.LSHIFT, throttleUp,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.LCONTROL, throttleDown,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.A, yawLeft,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.D, yawRight,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.SPACE, KFW,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		}
	}
	
	//private void cameraLook() {
	//	print("\nfd: " + cameraN.getWorldPosition());
	//}
	
	public void update() {
		deltaTime = game.getDeltaTime();
		shipController.update();
		updateCamera();
	}
	
	/*****************************************************************
	 * GAMEPAD SECTION												 *
	 *****************************************************************
	 */
	
	public float parabolicSmooth(float v) {
		if(v < 0) {
			v*=v;
			v*=-1;
		}
		else v*=v;
		
		return v;
	}
	
	private class ControllerThrottle extends AbstractInputAction {
		float value;
		
		@Override
		public void performAction(float arg0, Event e) {
			
			if(Math.abs(e.getValue()) < 0.2) value = 0;
			else value = -1 * e.getValue();
			
			value = parabolicSmooth(value);
			
			shipController.setControllerThrottle(value);
		}
	}
	
	
	private class ControllerRoll extends AbstractInputAction {
		float value;
		
		@Override
		public void performAction(float arg0, Event e) {
			if(Math.abs(e.getValue()) > 0.25f) value = e.getValue();
			else value = 0;
			
			shipController.setControllerRoll(value);
		}
	}
	
	private class ControllerYaw extends AbstractInputAction {
		
		float value;
		
		@Override
		public void performAction(float arg0, Event e) {
			if(Math.abs(e.getValue()) > 0.2f) value = -1 * e.getValue();
			else value = 0;
			
			shipController.setControllerYaw(value);
		}
	}
	
	private class ControllerPitch extends AbstractInputAction {
		//float pValue;
		//float value;
		float eValue;
		//float smoothSpeed = 15f;
		//float offset = 2;
				
		@Override
		public void performAction(float arg0, Event e) {
			if(Math.abs(e.getValue()) > 0.25f) eValue = -1 * e.getValue();
			else eValue = 0;
			
			shipController.setControllerPitch(eValue);
		}
	}
	
	/*****************************************************************
	 * KEYBOARD SECTION												 *
	 *****************************************************************
	 */
	
	private class RollLeft extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setRollLeft(-1 * e.getValue());
		}
	}
	
	private class RollRight extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setRollRight(-1 * e.getValue());
		}
	}
	
	private class PitchForward extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setPitchFoward(e.getValue());
		}
	}
	
	private class PitchBackward extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setPitchBackward(e.getValue());
		}
	}
	
	private class YawRight extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setYawRight(e.getValue());
		}
	}
	
	private class YawLeft extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setYawLeft(e.getValue());
		}
	}
	
	private class ThrottleUp extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setThrottleUp(e.getValue());
		}
	}
	
	private class ThrottleDown extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			shipController.setThrottleDown(e.getValue());
		}
	}
	
	private class FireWeapon extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			if( e.getValue() < -0.1f) shipController.setFiring(true);
			else shipController.setFiring(false);
		}
	}
	
	private class KeyboardFireWeapon extends AbstractInputAction {
		@Override
		public void performAction(float arg0, Event e) {
			if(e.getValue() == 1) shipController.setFiring(true);
			else shipController.setFiring(false);
		}
	}
	
	
	float timer = 0;
	
	public void updateCamera() {
		
		//setN forward
		//setV up
		//setU right
		camera.setN((Vector3f) shipSeat.getWorldForwardVector().normalize());
		camera.setV((Vector3f) shipSeat.getWorldUpVector().normalize());
		camera.setU((Vector3f) shipSeat.getWorldRightVector().normalize());
		
		//Vector3f rV = (Vector3f) ship.getWorldRightVector();
		//rV = (Vector3f) Vector3f.createFrom(-1 * rV.x(), -1 * rV.y(), -1 * rV.z());
		//rV = new Vector3f(-1 * rV.x(), -1 * rV.y(), -1 * rV.z());
		//camera.setU((Vector3f) rV.normalize());
		
		shipSeat.setLocalLocation(shipSeat.getLocalLocation());

		//camera.setLocation(shipSeat.getWorldLocation().add(cameraOffset));
		//camera.setLocation(shipSeat.getWorldLocation());
		camera.setLocation(shipSeat.getWorldLocation());
		//camera.setPo((Vector3f) cameraN.getWorldPosition());
		
		//do not uncomment camera.setPo((Vector3f) target.getWorldPosition());
		
		//TODO these methods shift the camera to the side or backward with the throttle
		//honestly not that much of a priority
		//cameraThrottleShift();
		//cameraTurnShift();
	}
	
	
	private void cameraThrottleShift() {
		
		//TODO camera focuses forward and backward based on the amount of throttle

		float throttleDampen = 0.025f;
		
		//Get forward Vector
		Vector3f forward = camera.getN();
		
		//forward = forward.mult(1 + (shipController.getThrottle() * throttleDampen));
		forward = forward.mul(1 + (shipController.getThrottle() * throttleDampen));
		
		camera.setN((Vector3f) forward);
	}
	
	private void cameraTurnShift() {

		//TODO move camera around in cockpit, probably do it later
		/*
		float rollRatio = 0.03f;
		float pitchRatio = 0.04f;
		float yawRatio = 0.025f;
		
		//the ship rolls the camera shifts slightly in the opposite direction
		//negative left. positive right
		Vector3 rollVector = cameraN.getLocalRightAxis().mult(shipController.getRoll()).mult(rollRatio);
		
		//when the ship rolls, the perspective leans into that direction and slightly downward.
		// -(x^2)/20
		float parabolaY = shipController.getRoll();
		parabolaY *= -1 * parabolaY/20;
		rollVector = rollVector.add(0,parabolaY,0);
		
		//the ship pitches the camera shifts slightly in the opposite direction
		//pitch up negative. pitch down positive
		Vector3 pitchVector = cameraN.getLocalUpAxis().mult(shipController.getPitch()).mult(pitchRatio);
		
		//the ship yaws the camera shifts slightly in the opposite direction
		//positive left, negative right
		Vector3 yawVector = cameraN.getLocalRightAxis().mult(shipController.getYaw()).mult(yawRatio);
		
		//Add all the vectors together
		cameraN.setLocalPosition(basePosition.add(rollVector).add(pitchVector).add(yawVector));
		*/
	}
	
	public int getThrottleSign() {
		return shipController.getThrottleSign();
	}

	public int getPitchSign() {
		float pitch = shipController.getThrottle();
		int sign = 0;
		if(pitch > 0) sign = 1;
		else if(pitch < 0) sign = -1;
		
		return sign;
	}
	public int getRollSign() {
		float roll = shipController.getThrottle();
		int sign = 0;
		if(roll > 0) sign = 1;
		else if(roll < 0) sign = -1;
		
		return sign;
	}
	public int getYawSign() {
		float yaw = shipController.getThrottle();
		int sign = 0;
		if(yaw > 0) sign = 1;
		else if(yaw < 0) sign = -1;
		
		return sign;
	}
	
	private void print(String s) {
		System.out.println(s);
	}
}

















