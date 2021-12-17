package ris;

import tage.*;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

import tage.shapes.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.*;

import org.joml.*;

public class ShipController {
	
	MyGame game;
	SceneGraph sg;
	ScriptEngine jsEngine;
	File paramFile;
	long fileLastModifiedTime;
	
	private Engine eng;
	private PhysicsEngine physics;
	private FlightController FC;
	private GameObject ship;
	private PhysicsObject shipPhys;
	
	private float pitchRate = 150f;
	private float pitchAccel = 5f;
	private float rollRate = 150f;
	private float rollAccel = 5f;
	private float yawRate = 15f;
	private float yawAccel = 5f;
	private float shipSpeed = 10f;
	private float shipAccel = 5f;
	private float throttleAccel = 1f;
	private float throttleRate;
	
	//these are the physics object transform indexes. in the physicsObject.getTransform();
	//it returns a double[] with 16 elements
	//12 = x
	//13 = y
	//14 = z
	
	//private float vectorUpdateAccel = 1f;
	//private float dampenRatio = 0.9f;
	//private float moveX, moveY, moveZ;
	//private Vector3 moveVector = Vector3f.createZeroVector();
	//private Vector3 thrustVector;
	//private float currentSpeed;
	
	boolean firing = false;
	
	GameObject[] lasers;
	GameObject[] throttleIndicator;
	
	//private float pitch, leftVertical, pitchForward, pitchBackward;
	private float pitch, pitchForward, pitchBackward, controllerPitch;
	
	private float roll, rollLeft, rollRight, controllerRoll;
	
	private float yaw, yawLeft, yawRight, controllerYaw;
	
	private float throttle, throttleUp, throttleDown, controllerThrottle;
	
	NodeMaker nm;
	
	public ShipController(MyGame g, FlightController f) throws IOException {
		System.out.println("Ship Controller");
		setupJavascript();
		
		game = g;
		sg = game.getSceneGraph();
		eng = game.getEngine();
		physics = game.getPhysicsEngine();
		FC = f;
		ship = game.getPlayerShip();
		
		nm = game.getNodeMaker();
		
		lasers = nm.makeLasers();		
		
		shipPhys = ship.getPhysicsObject();
		
		setupThrottleIndicator();
		setupParams();
	}
	
	private GameObject throttleBase;
	private Vector3f[] throttlePositions;
	private void setupThrottleIndicator() throws IOException {

		throttleBase = new GameObject(GameObject.root());
		throttleBase.applyParentRotationToPosition(true);
		
		throttleIndicator = nm.makeThrottleIndicators();
		
		throttlePositions = new Vector3f[throttleIndicator.length];
		
		throttleBase.setParent(ship);
		
		throttleBase.setLocalLocation(throttleIndicator[4].getWorldLocation());
		
		throttleBase.roll(90);
		throttleBase.yaw(-55);
		throttleBase.roll(180);
		
		Vector3f position = new Vector3f(0.4f,-0.7f,0.5f);
		
		throttleBase.setLocalLocation(position);
		
		for(int i=0; i<throttleIndicator.length;i++)
		{
			throttlePositions[i] = throttleIndicator[i].getLocalLocation();
			throttleIndicator[i].setParent(throttleBase);
		}
	}
	
	private void setupParams() {
		pitchRate = (float)(double)jsEngine.get("pitchRate");
		pitchRate = (float)(double)jsEngine.get("pitchRate");
		pitchAccel = (float)(double)jsEngine.get("pitchAccel");
		rollRate = (float)(double)jsEngine.get("rollRate");
		rollAccel = (float)(double)jsEngine.get("rollAccel");
		yawRate = (float)(double)jsEngine.get("yawRate");
		shipSpeed = (float)(double)jsEngine.get("shipSpeed");
	}
	
	private void setupJavascript() {
		ScriptEngineManager factory = new ScriptEngineManager();
		
		//get the JavaScript engine
		jsEngine = factory.getEngineByName("js");
		
		//setuoDefaultParams script
		//executeScript("scripts/defaultParams.js");
		paramFile = new File("scripts/defaultParams.js");
		//System.out.println("getName: " + paramFile.getName());
		executeScript("scripts/" + paramFile.getName());
	}
	
	private void executeScript(String scriptFileName) {
		executeScript(jsEngine, scriptFileName);
	}
	
	private void executeScript(ScriptEngine engine, String scriptFileName) {
		try {
			FileReader fileReader = new FileReader(scriptFileName);
			engine.eval(fileReader); //execute the script statements in the file
			fileReader.close();
		}
		catch(FileNotFoundException e1) {
			System.out.println(scriptFileName + " not found " + e1);
		}
		catch(IOException e2) {
			System.out.println("IO problem with " + scriptFileName + e2);
		}
		catch(ScriptException e3) {
			System.out.println("ScriptException in " + scriptFileName + e3);
		}
		catch(NullPointerException e4) {
			System.out.println("Null ptr exception in " + scriptFileName + e4);
		}
	}
	
	float deltaTime;
	public void update() {
		deltaTime = game.getDeltaTime();
		
		long modTime = paramFile.lastModified();
		if (modTime > fileLastModifiedTime)
		{
			long fileLastModifiedTime = modTime;
			this.executeScript("scripts/" + paramFile.getName());
			setupParams();
		}
		
		throttleUpdate();
		
		pitch();
		roll();
		yaw();
		throttle();
		
		updatePosition();
		
		shooting();
	}
	
	//updates the throttle hud
	//private Vector3 farAway = Vector3f.createFrom(10000,10000,1000);
	private Vector3f farAway = new Vector3f(10000f,10000f,10000f);

	float throttleGuage;
	private void throttleUpdate() {
		throttleGuage = throttle*throttleIndicator.length;
		
		for(int i=0;i<throttleIndicator.length;i++) {
			throttleIndicator[i].setLocalLocation(farAway);
		}
		
		if(throttleGuage < 0.1) return;
		
		for(int i=0;i<throttleGuage;i++) {
			throttleIndicator[i].setLocalLocation(throttlePositions[i]);
		}
		
		if(throttleGuage < throttleIndicator.length) {
			throttleIndicator[throttleIndicator.length-1].setLocalLocation(farAway);
		}
	}
	
	private void updatePosition() {
		//TODO updatePosition
		//System.out.println("updatePosition()");
		//System.out.println(ship.getWorldLocation());
		//Vector3 direction = ship.getWorldForwardAxis();
		Vector3f direction = ship.getWorldForwardVector();

		
		
		//System.out.println(direction + " throttle " + throttle);
		//System.out.println("shipSpeed: " + shipSpeed + " throttle: " + throttle);
		direction = direction.mul(shipSpeed * throttle);

		//System.out.println(direction);
		
		float[] velocities = new float[] {direction.x(),direction.y(),direction.z()};
		shipPhys.setLinearVelocity(velocities);
		//float[] velocity = shipPhys.getLinearVelocity();
		//System.out.println(velocity[0] + " " + velocity[1] + " " + velocity[2]);
		
		updateVerticalPosition();
	}
	
	float terrainHeight;
	float shipHeight;
	//float heightDifference;
	public void updateVerticalPosition()
	{
		Vector3f position = ship.getWorldLocation();
		terrainHeight = game.getTerrain().getHeight(position.x(), position.z()) + game.getTerrain().getWorldLocation().y() + 15;

		shipHeight = position.y();

		if(terrainHeight > shipHeight) {
			double[] transform = shipPhys.getTransform();
			transform[13] = (double)terrainHeight;
			shipPhys.setTransform(transform);
		}
	}
	
	private void pitch() {
		float value = keyVsGamepad(pitchForward, pitchBackward, controllerPitch);
		value = SimpleMath.parabolicSmooth(value);
		pitch = SimpleMath.lerp(pitch, value, pitchAccel * deltaTime);
		ship.pitch(pitchRate * pitch * deltaTime);
	}
	
	private void roll() {
		float value = keyVsGamepad(rollLeft, rollRight, controllerRoll);
		value = SimpleMath.parabolicSmooth(value);
		roll = SimpleMath.lerp(roll, value, rollAccel * deltaTime);
		ship.roll(rollRate * roll * deltaTime);
	}
	
	private void yaw() {
		float value = keyVsGamepad(yawLeft, yawRight, controllerYaw);
		value = SimpleMath.parabolicSmooth(value);
		yaw = SimpleMath.lerp(yaw, value, yawAccel * deltaTime);
		ship.yaw(yawRate * yaw * deltaTime);
	}
	
	private void throttle() {
		//TODO throttle()
		//System.out.println("throttle before: " + throttle);

		float value = keyVsGamepad(throttleUp, throttleDown, controllerThrottle);
		
		//System.out.println("throttle: " + throttle + " throttleAccel: " + throttleAccel + " value: " + value + " deltaTime: " + deltaTime);
		throttle += throttleAccel * value * deltaTime;
		
		if(throttle > 1) throttle = 1;
		if(throttle < 0f) throttle = 0f;
		//if(throttle < 0.01f) throttle = 0.001f;
		//TODO completely unecessary thing to investigate, why does the ship move if it gets set to 0?
		//It's because we use the current speed to calculate the next speed (only with multiplication)
		//So it needs to be able to accelerate from 0

		//System.out.println("throttle after: " + throttle);
	}
	
	private float keyVsGamepad(float keyPos, float keyNeg, float controllerValue) {
		float value;
		
		float keyValue = keyPos - keyNeg;
		
		if(keyValue > 0) {
			if(controllerValue > 0) value = keyValue;
			else value = keyValue + controllerValue;
		}
		else if(keyValue < 0) {
			if(controllerValue < 0) value = keyValue;
			else value = keyValue + controllerValue;
		}
		else value = controllerValue;
		
		return value;
	}
	
	private int shootCycle = 0;
	float timeSinceLastShot = 0;
	float fireRate = 4.0f;
	private void shooting() {
		timeSinceLastShot += deltaTime;
		if(firing) {
			if(timeSinceLastShot > 1/fireRate) shoot();
		}
	}
	
	private float laserSpeed = 400;
	private void shoot() {
		timeSinceLastShot = 0;
		
		PhysicsObject p1 = lasers[shootCycle].getPhysicsObject();
		PhysicsObject p2 = lasers[shootCycle+1].getPhysicsObject();
		
		Vector3f up = ship.getWorldUpVector();
		Vector3f right = ship.getWorldRightVector().mul(2);
		Vector3f forward = ship.getWorldForwardVector();
		Vector3f position = ship.getWorldLocation().add(forward.mul(10)).sub(up.mul(2));
		
		double[] d1 = p1.getTransform();
		double[] d2 = p2.getTransform();
		
		d1[12] = position.x() + right.x();
		d1[13] = position.y() + right.y();
		d1[14] = position.z() + right.z();
		
		d2[12] = position.x() - right.x();
		d2[13] = position.y() - right.y();
		d2[14] = position.z() - right.z();
		
		p1.setTransform(d1);
		p2.setTransform(d2);
		
		forward.mul(laserSpeed);
		p1.setLinearVelocity(toFloatArray(forward));
		p2.setLinearVelocity(toFloatArray(forward));
		
		shootCycle += 2;
		shootCycle%=(lasers.length);
		
		game.playFireSound();

		game.shootNetworking();
	}
	
	public int getThrottleSign() {
		float value = keyVsGamepad(throttleUp, throttleDown, controllerThrottle);
		/*
		throttle+= throttleAccel * value * deltaTime;
		
		if(throttle > 1) throttle = 1;
		//if(throttle < 0) throttle = 0;
		if(throttle < 0.01f) throttle = 0.01f;*/
		
		int valueInt;
		if(value > 0) valueInt = 1;
		else if (value < 0) valueInt = -1;
		else valueInt = 0;
		
		return valueInt;
	}
	
	public void setControllerThrottle(float v) { controllerThrottle = v;}
	public void setControllerRoll(float v) { controllerRoll = v;}
	public void setControllerPitch(float v) { controllerPitch = v;}
	public void setControllerYaw(float v) { controllerYaw = v;}
	
	public void setRollLeft(float v) { rollLeft = v;}
	public void setRollRight(float v) { rollRight = v;}
	public void setPitchFoward(float v) { pitchForward = v;}
	public void setPitchBackward(float v) { pitchBackward = v;}
	public void setYawLeft(float v) { yawLeft = v; }
	public void setYawRight(float v) { yawRight = v; }
	public void setThrottleUp(float v) { throttleUp = v; }
	public void setThrottleDown(float v) { throttleDown = v; }
	
	public float getThrottle() { return throttle; }
	public float getRoll() { return roll; }	
	public float getPitch() { return pitch; }	
	public float getYaw() { return yaw; }
	
	public void setFiring(boolean f) { firing = f; }
	
	private float[] toFloatArray(Vector3f v){
		float[] array = {v.x(), v.y(), v.z()};
		return array;
	}
	
	private void print(String s) {
		System.out.println(s);
	}
}
