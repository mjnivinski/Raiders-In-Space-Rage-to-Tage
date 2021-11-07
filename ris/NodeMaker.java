package ris;

import java.io.IOException;

import tage.*;

//import tage.rml.Vector3;
//import tage.rml.Degreef;
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

	private ObjShape laserS, throttleS, scoreS, npcS;
	private TextureImage laserT, throttleT, scoreT, npcT;

	private float vals[] = new float[16];
	
	public NodeMaker(MyGame g) {
		game = g;
		sg = g.getSceneGraph();
		eng = g.getEngine();
		this.physics = g.getPhysicsEngine();
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
			//String s = name + "npcLasers" + Integer.toString(i);
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
			//TODO the localLocation might be wrong.
			//theHud[i].setLocalPosition(-1 * i*throttleGap + (throttleGap*10/2),0,0);
			//new Vector3f(-1 * i*throttleGap + (throttleGap*10/2),0,0);
			//theHud[i].setLocalLocation(-1 * i*throttleGap + (throttleGap*10/2),0,0);
			theHud[i].setLocalLocation(new Vector3f(-1 * i*throttleGap + (throttleGap*10/2),0,0));
		}
		return theHud;
	}
	
	private GameObject makeThrottleIndicator(String name) throws IOException {
		
		GameObject throttle = new GameObject(GameObject.root(), throttleS, throttleT);
		
		float scale = 0.08f;
		throttle.setLocalScale((new Matrix4f()).scaling(scale));
		throttle.setParent(ship);
		
		//TODO pitch the throttle
		//throttle.pitch(Degreef.createFrom(270));
		
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
		//score.setParent(ship);

		//TODO figure out the rotation for the throttle indicators, or any thing for that matter.
		//ti.pitch(Degreef.createFrom(270));
		
		return score;
	}
	
	public GameObject makeNPC(String name, Vector3f position) throws IOException {

		GameObject npc = new GameObject(GameObject.root(), npcS, npcT);

		//GameObject npc = sm.getRootSceneNode().createChildSceneNode(name);
		
		npc.setLocalLocation(new Vector3f(position.x(), position.y(), position.z()));

		//npc.setLocalPosition
		
		//float mass = 1.0f;
		//float up[] = {0,1,0};
		//double[] temptf;

		float mass = 1.0f;
		double[] tempTransform;
		
		Matrix4f translation  = new Matrix4f(npc.getLocalTranslation());
		tempTransform = MyGame.toDoubleArray(translation.get(vals));
		PhysicsObject npcPhysicsObject = physics.addSphereObject(physics.nextUID(), mass, tempTransform, 1.0f);
		npcPhysicsObject.setDamping(0, 0);
		npc.setPhysicsObject(npcPhysicsObject);
		
		return npc;
	}
}
