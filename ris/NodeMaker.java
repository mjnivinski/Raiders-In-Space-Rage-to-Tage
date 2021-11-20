package ris;

import java.io.IOException;

import tage.*;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;

import tage.shapes.*;
import tage.nodeControllers.*;

import java.lang.Math;
import java.awt.*;

import java.awt.event.*;

import java.io.*;
import javax.swing.*;
import org.joml.*;

public class NodeMaker {

	private MyGame game;
	private SceneGraph sg;
	private Engine eng;
	private PhysicsEngine physics;
	private GameObject ship;

	private ObjShape laserS, throttleS, scoreS, npcS, asteroid1S, asteroid2S;
	private TextureImage laserT, throttleT, scoreT, npcT, asteroid1T, asteroid2T;

	private float vals[] = new float[16];
	
	public NodeMaker(MyGame g) {
		game = g;
		sg = g.getSceneGraph();
		eng = g.getEngine();
		physics = g.getPhysicsEngine();
		ship = g.getPlayerShip();
		loadShapesAndTextures();
	}

	private void loadShapesAndTextures(){
		System.out.println("Load Shapes and Textures for Nodemaker");
		
		//I had game create all of these getters because I thought that you couldn't load assets outside of a specific method
		//but now that I have written the getters, I wonder if this methodology is better to track all of the assets in the game
		//should all of the asset loads be in one spot,
		//should all of the asset loads only exist in their relative positions.
		//project is too small to worry, something larger might have a necessity on either strategy.

		//laserS = new ImportedModel("otherLaser.obj");
		laserS = game.getLaserShape();
		//laserT = new TextureImage("laserBolt.png");
		laserT = game.getLaserTexture();
		
		//throttleS = new ImportedModel("throttleIndicator.obj");
		throttleS = game.getThrottleShape();
		//throttleT = new TextureImage("throttleIndicator.png");
		throttleT = game.getThrottleTexture();
		
		//scoreS = new ImportedModel("scoreIndicator.obj");
		scoreS = game.getScoreShape();
		//scoreT = new TextureImage("scoreIndicator.png");
		scoreT = game.getScoreTexture();
		
		//npcS = new ImportedModel("DropShipVer4.obj");
		npcS = game.getNPCShape();
		//npcT = new TextureImage("DropShipVer4.png");
		npcT = game.getNPCTexture();

		asteroid1S = game.getAsteroid1Shape();
		asteroid1T = game.getAsteroid1Texture();

		asteroid2S = game.getAsteroid2Shape();
		asteroid2T = game.getAsteroid2Texture();
	}
	
	public GameObject[] makeLasers() throws IOException {
		int laserCount = 16;
		GameObject[] list = new GameObject[laserCount];
		
		for(int i=0; i<list.length; i++) {
			//String s = "playerLasers" + Integer.toString(i);
			list[i] = makeLaser();
		}
		
		return list;
	}
	
	public GameObject[] makeGhostLasers() throws IOException {
		
		int laserCount = 16;
		GameObject[] list = new GameObject[laserCount];
		
		for(int i=0; i<list.length; i++) {
			//String s = "ghostLasers" + Integer.toString(i);
			list[i] = makeLaser();
		}
		
		return list;
	}
	
	public GameObject[] makeNPCLasers() throws IOException {
		int laserCount = 4;
		GameObject[] list = new GameObject[laserCount];
		
		for(int i=0; i<list.length; i++) {
			list[i] = makeLaser();
		}
		
		return list;
	}
	
	public GameObject makeLaser() throws IOException {
		GameObject laser = new GameObject(GameObject.root(), laserS, laserT);
		
		float farAway = 10000f;
		float scale = 0.5f;
		laser.setLocalTranslation((new Matrix4f()).translation(farAway,farAway,farAway));
		laser.setLocalScale((new Matrix4f()).scaling(scale));
		
		float mass = 1.0f;
		double[] tempTransform;
		
		Matrix4f translation  = new Matrix4f(laser.getLocalTranslation());
		tempTransform = MyGame.toDoubleArray(translation.get(vals));
		PhysicsObject laserPhysicsObject = physics.addSphereObject(physics.nextUID(), mass, tempTransform, 1.0f);
		laserPhysicsObject.setDamping(0, 0);
		laser.setPhysicsObject(laserPhysicsObject);
		
		laser.lookAt(ship);
		
		return laser;
	}
	
	private Vector3f throttleOffset;
	private float throttleGap = 0.07f;
	public GameObject[] makeThrottleIndicators() throws IOException {
		GameObject[] theHud = new GameObject[10];
		
		for(int i=0; i<10; i++) {
			theHud[i] = makeThrottleIndicator("throttleIndicator" + i);
		}
		
		for(int i=0;i<10;i++) {
			theHud[i].setLocalLocation(new Vector3f(-1 * i*throttleGap + (throttleGap*10/2),0,0));
		}
		return theHud;
	}
	
	private GameObject makeThrottleIndicator(String name) throws IOException {
		GameObject throttle = new GameObject(GameObject.root(), throttleS, throttleT);
		float scale = 0.08f;
		throttle.setLocalScale((new Matrix4f()).scaling(scale));
		throttle.setParent(ship);
		throttle.pitch(270);
		throttle.applyParentRotationToPosition(true);
		
		return throttle;
	}
	
	float scoreGap = 0.15f;
	public GameObject[] makeScoreIndicators() throws IOException {
		GameObject[] theHud = new GameObject[4];
		
		for(int i=0; i<theHud.length; i++) {
			theHud[i] = makeScoreIndicator("scoreIndicator" + i);
		}
		
		for(int i=0;i<theHud.length;i++) {
			//theHud[i].setLocalPosition(-1 * i*scoreGap + (scoreGap*theHud.length/2),0,0);
			//theHud[i].setLocalPosition(-1 * i*scoreGap + (scoreGap*theHud.length/2),0,0);
			theHud[i].setLocalLocation(new Vector3f(-1 * i*scoreGap + (scoreGap*theHud.length/2),0,0));
		}
		return theHud;
	}
	
	private GameObject makeScoreIndicator(String name) throws IOException {
		GameObject score = new GameObject(ship, scoreS, scoreT);
		float scale = 0.06f;
		score.setLocalScale((new Matrix4f()).scaling(scale));
		score.applyParentRotationToPosition(true);
		return score;
	}
	
	public GameObject makeNPC(Vector3f position) throws IOException {

		GameObject npc = new GameObject(GameObject.root(), npcS, npcT);
		
		npc.setLocalLocation(new Vector3f(position.x(), position.y(), position.z()));

		float mass = 1.0f;
		double[] tempTransform;
		
		Matrix4f translation  = new Matrix4f(npc.getLocalTranslation());
		tempTransform = MyGame.toDoubleArray(translation.get(vals));
		PhysicsObject npcPhysicsObject = physics.addSphereObject(physics.nextUID(), mass, tempTransform, 1.0f);
		npcPhysicsObject.setDamping(0, 0);
		npc.setPhysicsObject(npcPhysicsObject);
		
		return npc;
	}

	public GameObject[] makeAsteroids(){
		GameObject[] asteroids = new GameObject[8];

		asteroids[0] = new GameObject(GameObject.root(), asteroid1S, asteroid1T);
		asteroids[0].setLocalLocation(new Vector3f(612,100,100));
		asteroids[0].setLocalScale((new Matrix4f()).scaling(2));
		
		asteroids[1] = new GameObject(GameObject.root(), asteroid2S, asteroid1T);
		asteroids[1].setLocalLocation(new Vector3f(600,150,100));
		asteroids[1].setLocalScale((new Matrix4f()).scaling(6.4f));
		
		asteroids[2] = new GameObject(GameObject.root(), asteroid2S, asteroid2T);
		asteroids[2].setLocalLocation(new Vector3f(577,112,106));
		asteroids[2].setLocalScale((new Matrix4f()).scaling(3));
		
		asteroids[3] = new GameObject(GameObject.root(), asteroid1S, asteroid2T);
		asteroids[3].setLocalLocation(new Vector3f(-560,145,115));
		asteroids[3].setLocalScale((new Matrix4f()).scaling(1.2f));
		
		asteroids[4] = new GameObject(GameObject.root(), asteroid1S, asteroid1T);
		asteroids[4].setLocalLocation(new Vector3f(637,135,100));
		asteroids[4].setLocalScale((new Matrix4f()).scaling(3.5f,2.0f,3.5f));

		asteroids[5] = new GameObject(GameObject.root(), asteroid2S, asteroid2T);
		asteroids[5].setLocalLocation(new Vector3f(615,200,115));
		asteroids[5].setLocalScale((new Matrix4f()).scaling(9));
		
		asteroids[6] = new GameObject(GameObject.root(), asteroid2S, asteroid2T);
		asteroids[6].setLocalLocation(new Vector3f(577,127,130));
		asteroids[6].setLocalScale((new Matrix4f()).scaling(.5f,1.0f,1.5f));
		
		asteroids[7] = new GameObject(GameObject.root(), asteroid1S, asteroid1T);
		asteroids[7].setLocalLocation(new Vector3f(-400,100,135));
		asteroids[7].setLocalScale((new Matrix4f()).scaling(8.8f,10.0f,8.8f));
		
		RotationController rc = new RotationController(game.getEngine(), new Vector3f(0,1,0), 1f);
		rc.setSpeed(0.0007f);
		rc.addTarget(asteroids[4]);
		(eng.getSceneGraph()).addNodeController(rc);
		rc.toggle();

		RotationController rc2 = new RotationController();
		rc2.setSpeed(0.004f);
		rc2.addTarget(asteroids[2]);

		return asteroids;
	}
}
