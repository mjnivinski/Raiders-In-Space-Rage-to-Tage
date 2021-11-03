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
import tage.rml.Degreef;
import tage.rml.Vector3;

import java.io.*;
import javax.swing.*;
import org.joml.*;


public class NodeMaker {

	private SceneGraph sg;
	private Engine eng;
	private PhysicsEngine physics;
	private GameObject ship;

	private ObjShape laserS, throttleS, scoreS;
	private TextureImage laserT, throttleT, scoreT;

	private float vals[] = new float[16];
	
	public NodeMaker(MyGame g) {
		sg = g.getSceneGraph();
		eng = g.getEngine();
		this.physics = g.getPhysicsEngine();
		ship = g.getPlayerShip();
		loadShapesAndTextures();
	}

	private void loadShapesAndTextures(){
		System.out.println("loadShapesAndTextures");
		laserS = new ImportedModel("otherLaser.obj");
		laserT = new TextureImage("laserBolt.png");

		throttleS = new ImportedModel("throttleIndicator.obj");
		throttleT = new TextureImage("throttleIndicator.png");

		scoreS = new ImportedModel("scoreIndicator.obj");
		scoreT = new TextureImage("scoreIndicator.png");
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
		//float up[] = {0,1,0};
		double[] tempTransform;
		
		Matrix4f translation  = new Matrix4f(laser.getLocalTranslation());
		tempTransform = MyGame.toDoubleArray(translation.get(vals));
		PhysicsObject laserPhysicsObject = physics.addSphereObject(physics.nextUID(), mass, tempTransform, 1.0f);
		laserPhysicsObject.setDamping(0, 0);
		laser.setPhysicsObject(laserPhysicsObject);
		
		laser.lookAt(ship);
		
		return laser;
	}
	
	private Vector3 throttleOffset;
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

		//GameObject laser = new GameObject(GameObject.root(), laserS, laserT);
		
		//Entity tie = sm.createEntity(name, "throttleIndicator.obj");
		//tie.setPrimitive(Primitive.TRIANGLES);
		
		//ti.attachObject(tie);
		
		float scale = 0.08f;

		throttle.setLocalScale((new Matrix4f()).scaling(scale));
		
		//throttleIndicator.setLocalScale(Vector3f.createFrom(scale,scale,scale));
		
		//ship.attachChild(throttleIndicator);
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
		
		GameObject ti = sm.getRootSceneNode().createChildSceneNode(name);
		
		Entity tie = sm.createEntity(name, "scoreIndicator.obj");
		tie.setPrimitive(Primitive.TRIANGLES);
		
		ti.attachObject(tie);
		
		float scale = 0.06f;
		
		ti.setLocalScale(Vector3f.createFrom(scale,scale,scale));
		
		ship.attachChild(ti);
		
		//TODO figure out the rotation for the throttle indicators, or any thing for that matter.
		//ti.pitch(Degreef.createFrom(270));
		
		return ti;
	}
	
	public GameObject makeNPC(String name, Vector3 position) throws IOException {
		GameObject npc = sm.getRootSceneNode().createChildSceneNode(name);
		
		Entity e = sm.createEntity(name + "i", "DropShipVer4.obj");
		e.setPrimitive(Primitive.TRIANGLES);
		
		npc.attachObject(e);
		
		npc.setLocalPosition(position);
		
		float mass = 1.0f;
		//float up[] = {0,1,0};
		double[] temptf;
		
		temptf = MyGame.toDoubleArray(npc.getLocalTransform().toFloatArray());
		PhysicsObject shipPhysicsObject = physics.addSphereObject(physics.nextUID(), mass, temptf, 1.0f);
		shipPhysicsObject.setDamping(0, 0);
		npc.setPhysicsObject(shipPhysicsObject);
		
		return npc;
	}
}
