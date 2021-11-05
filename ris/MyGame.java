package ris;

import java.io.IOException;
//import java.awt.*;
//import java.awt.geom.AffineTransform;
//import java.io.*;
//import java.net.InetAddress;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//import java.rmi.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Random;

//import net.java.games.input.Controller;
//import net.java.games.input.Event;

import tage.*;
import tage.shapes.*;

//import tage.rml.*;
//import tage.audio.AudioManagerFactory;
//import tage.audio.AudioResource;
//import tage.audio.AudioResourceType;
//import tage.audio.IAudioManager;
//import tage.audio.Sound;
//import tage.audio.SoundType;

//import tage.input.GenericInputManager;
//import tage.input.InputManager;
//import tage.input.action.AbstractInputAction;

//import tage.asset.material.Material;
//import tage.rage.asset.texture.Texture;
//import tage.asset.texture.TextureManager;
//import tage.game.*;
//import tage.rendersystem.*;
//import tage.rendersystem.Renderable.*;
//import tage.scene.*;
//import tage.scene.Camera.Frustum.*;
//import tage.scene.controllers.RotationController;
//import tage.util.BufferUtil;
//import tage.util.Configuration;
//import tage.rml.*;
//import tage.rendersystem.gl4.GL4RenderSystem;
//import tage.rendersystem.shader.GpuShaderProgram;
//import tage.rendersystem.states.FrontFaceState;
//import tage.rendersystem.states.RenderState;
//import tage.rendersystem.states.TextureState;
//import tage.networking.IGameConnection.ProtocolType;
//import tage.physics.PhysicsEngine;
//import tage.physics.PhysicsEngineFactory;
//import tage.physics.PhysicsObject;
import tage.input.*;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;


//import java.util.UUID;
//import java.util.Vector;

/*import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;*/

//import static ray.rage.scene.SkeletalEntity.EndType.*;
//import static tage.scene.SkeletalEntity.EndType.*;

//import ray.audio.*;
//import tage.audio.*;
//import com.jogamp.openal.ALFactory;

import org.joml.*;



public class MyGame extends VariableFrameRateGame {

	/*
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isConnected;
	private Vector<UUID> gameObjectsToRemove;
	
	public static boolean isTerrain;
	
	public IAudioManager audioMgr;
	Sound backgroundMusic, stationSound, NPCSound;
	static Sound laserFireSound;
	Sound shipNoiseSound;
	

	
	//Declaration area
	Random random = new Random();
	
	SceneManager sm;
	static Engine eng;
	EntityMaker eMaker;
	NodeMaker nm;
	
	private PhysicsEngine physicsEng;

	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	String elapsTimeStr, counterStr, dispStr;

	int elapsTimeSec, counter = 0;

	//private CameraController cameraController;
	private Camera camera;
	//private SceneNode dolphinN, stationN;
	private SceneNode shipN, shipNBlue, stationN, terrainContN, enemyCraftN, dropShipN, rightHandN, 
	flagPlatformdN, laserBoltN, SecondShipN, Object4N, Object3N, Object2N, Object1N, stationBlueN, Object2bN, BlueCockpitN,
	asteroidM1, asteroidM2, asteroidM3, asteroidM4, asteroidM5, asteroidM6, asteroidM7, asteroidM8;
	
	private PhysicsObject shipPhysObj;
	
	private PatrolEnemy npc1;
	private SceneNode patrolNPC;
	
	private SceneNode[] earthPlanets = new SceneNode[13];
	
	throttleUp controlTest;
	throttleDown controlTest2;
	throttleLeft controlTest3;
	throttleRight controlTest4;
	destroyTerrain controlTest8;
	
	ToggleLights toggleLights;
	
	

	private InputManager im;
	private TextureManager tm;
	private ThrottleController tc;
	
	
	private float planetHeight = 1f;
	private float speedScale = 4;
	private float yawDegrees = 80;
	private float pitchDegrees = 80;
	
	private int shipLives = 3;
	
	private PatrolEnemy[] npcs;
	*/
	private FlightController playerController;

	private PhysicsEngine physicsEngine;
	private static Engine engine;
	private InputManager im;

	private GameObject cockpitO, shipObj, satellite, blueShipO;

	private PhysicsObject shipPhysObj;

	private ObjShape asteroidM, cockpitGreyS, cockpitBlueS, blueShipS, laserS, throttleS, scoreS, npcS;
	private TextureImage stuff, things, cockpitBlueT, blueShipT, laserT, throttleT, scoreT, npcT;
	private Light ambientLight, light1, light2;
	private NodeController rc, sc;
	private int spaceBox; // skybox
	private SceneGraph sceneGraph;

	private float vals[] = new float[16];

	private float deltaTime, previousTime;

	public MyGame(){ super(); }

	public static void main(String[] args){
		MyGame game = new MyGame();
		System.out.println("main");

		engine = new Engine(game,1400,1000);
		game.initializeSystem();
		game.game_loop();
		/*System.out.println("args: " + args[0] + " " + args[1]);
		Game game = new MyGame(args[0], Integer.parseInt(args[1]));
		//Game game = new MyGame("yes", 5);
		
		FSEM();
		chooseTeam();
		
		try {
			game.startup();
			game.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			game.shutdown();
			game.exit();
		}*/
	}

	//faster than typing System.out.println();
	private void print(String s) {
		System.out.println(s);
	}
	
	public void loadShapes(){
		blueShipS = new ImportedModel("blueGhost.obj");
		cockpitBlueS = new ImportedModel("blueCockpit.obj");
		laserS = new ImportedModel("otherLaser.obj");
		throttleS = new ImportedModel("throttleIndicator.obj");
		scoreS = new ImportedModel("scoreIndicator.obj");
		npcS = new ImportedModel("DropShipVer4.obj");

	}

	public void loadTextures(){
		blueShipT = new TextureImage("blueGhost.png");
		cockpitBlueT = new TextureImage("blueCockpit.png");
		laserT = new TextureImage("laserBolt.png");
		throttleT = new TextureImage("throttleIndicator.png");
		scoreT = new TextureImage("scoreIndicator.png");
		npcT = new TextureImage("DropShipVer4.png");

	}

	public void buildObjects(){
		Matrix4f initialTranslation, initialRotation, initialScale;

		//shipObj = new GameObject(GameObject.root(), cockpitBlueS, cockpitBlueT);
		shipObj = new GameObject(GameObject.root(), cockpitBlueS);

		initialTranslation = (new Matrix4f()).translation(0,0,0);
		initialScale = (new Matrix4f()).scaling(1f);
		//blueShipO.setLocalTranslation(initialTranslation);
		shipObj.setLocalTranslation(initialTranslation);

		//blueShipO.setLocalScale(initialScale);
		shipObj.setLocalScale(initialScale);
	}

	public void initializeGame() {
		print("initializeGame");
		Light.setGlobalAmbient(0.5f,0.5f,0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(0,10f,0));
		(engine.getSceneGraph()).addLight(light1);

		Vector3f position = new Vector3f(0,0,0);
		try {
			setupCamera();
			setupPhysics();
			setupInputs();
		} catch (Exception e) {
			print("Unable to setup game");
			System.exit(0);
		}
		print("done initializingGame");
	}
	
	//TODO confirm skybox loaded properly (look around)
	public void loadSkyBoxes(){
		System.out.println("loadSkyBoxes");
		spaceBox = (engine.getSceneGraph()).loadCubeMap("spaceBox");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(spaceBox);
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}
	
	//TODO setup cameras
	protected void setupCamera() {
		print("setupCamera");

		//little less work than before
		//the camera is already loaded in.
		getCamera().setLocation(new Vector3f(0f,0f,25f));
	}

	//TODO setup physics
	private void setupPhysics() {
		print("setupPhysics()");
		String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
		print("done initializing engine");
		float[] gravity = {0f,0f,0f};

		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		physicsEngine.setGravity(gravity);

		float mass = 1.0f;
		float up[] = {0,1,0};
		double[] tempTransform;

		Matrix4f translation  = new Matrix4f(shipObj.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		shipPhysObj = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass, tempTransform, 1.0f);
		shipObj.setPhysicsObject(shipPhysObj);
	}

	//TODO inputs and controls
	//seperate methods for keyboards/gamepads
	protected void setupInputs() throws IOException {
		//im = new GenericInputManager();
		im = new InputManager();
		playerController = new FlightController(this);
		
		//setupAdditionalTestControls(im);
		
		//animationThrottleUp()
	}

	public void update() {
		updateDeltaTime();
		playerController.update();
		im.update(deltaTime);

		
		//Matrix4f rotate;
		//rotate = shipObj.getLocalRotation();
		//rotate.rotateY(0.05f);
		//shipObj.setLocalRotation(rotate);
	}



	/*

	public MyGame(String serverAddr, int sPort) {
		super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.UDP;
	}

	//TODO Main, setup networking, windows
	public static void main(String[] args) {
		System.out.println("args: " + args[0] + " " + args[1]);
		Game game = new MyGame(args[0], Integer.parseInt(args[1]));
		//Game game = new MyGame("yes", 5);
		
		FSEM();
		chooseTeam();
		
		try {
			game.startup();
			game.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			game.shutdown();
			game.exit();
		}
	}
	
	private void setupNetworking() {
		print("setupNetworking");
		gameObjectsToRemove = new Vector<UUID>();
		
		isConnected = false;
		try {
			protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		}
		catch(UnknownHostException e) {e.printStackTrace();}
		catch(IOException e) {e.printStackTrace(); }
		
		
		if(protClient == null) {
			print("missing protocol host");
		}
		else {
			//ask client protocol to send initial join message
			//to server, with a unique modifier for this client
			
			print("sendJoinMessage()");
			
			protClient.sendJoinMessage();
		}
		
	}

	//TODO maybe delete
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		print("setupWindow");
		
		if(fullScreen == 0) {
			rs.createRenderWindow(true);
		}
		else if(fullScreen == 1) {
			rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
		}
		
		
	}
	
	//TODO might be unnecessary
	static int fullScreen = 1;
	private static void FSEM() {
		
		fullScreen = JOptionPane.showConfirmDialog(null,  "Full Screen?", "choose one", JOptionPane.YES_NO_OPTION);
	    //0 is yes
		//1 is no
		System.out.println("fullScreen: " + fullScreen);
	}
	
	static int chooseTeam;
	private static void chooseTeam() {
		chooseTeam = JOptionPane.showConfirmDialog(null,  "Join Grey Team? (No Joins Blue Team)", "Blue", JOptionPane.YES_NO_OPTION);
		//0 is yes
		//1 is no
		System.out.println("chooseTeam: " + chooseTeam);
	}
	
	//TODO setup cameras
	@Override
	protected void setupCameras(SceneManager sm, RenderWindow rw) {
		
		print("setupCameras");
		SceneNode rootNode = sm.getRootSceneNode();
		Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
		rw.getViewport(0).setCamera(camera);

		camera.setRt((Vector3f) Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f) Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f) Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		camera.setPo((Vector3f) Vector3f.createFrom(0.0f, 0.0f, 0.0f));
		SceneNode cameraNode = rootNode.createChildSceneNode(camera.getName() + "Node");
		cameraNode.attachObject(camera);
		camera.setPo((Vector3f) Vector3f.createFrom(0, 0, 1f));
		this.camera = camera;
		camera.setMode('r');
	}
	
	//TODO Ship selection
	private void selectShip() throws IOException {
		if(chooseTeam == 0) {
			makePlayerGrey();
		}
		else {
			makePlayerBlue();
		}
	}


	//TODO various scene setup
	@Override
	protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		this.sm = sm;
		this.eng = eng;
		eMaker = new EntityMaker(eng,sm);
		
		
		print("Setup Scene");
		setupShip();
		
		
		
		
		
		
	   	if (tm == null)
				tm = eng.getTextureManager();
	    	
	    	SkyBox sky = sm.createSkyBox("thesky");
	    	
	    	Texture skyFront = tm.getAssetByPath("2front.png");
	    	Texture skyBack = tm.getAssetByPath("2back.png");
	    	Texture skyBottom = tm.getAssetByPath("2top.png");
	    	Texture skyTop = tm.getAssetByPath("2bottom.png");
	    	Texture skyLeft = tm.getAssetByPath("2left.png");
	    	Texture skyRight = tm.getAssetByPath("2right.png");
	    	
	    	
	    	AffineTransform skyTransform = new AffineTransform();
	    	skyTransform.translate(0.0, skyFront.getImage().getHeight());
	    	skyTransform.scale(1.0, 1.0);
	    	skyFront.transform(skyTransform);
	    	skyBack.transform(skyTransform);
	    	skyLeft.transform(skyTransform);
	    	skyRight.transform(skyTransform);
			skyTop.transform(skyTransform);
			skyBottom.transform(skyTransform);
	    	
	    	sky.setTexture(skyFront, SkyBox.Face.FRONT);
	    	sky.setTexture(skyBack, SkyBox.Face.BACK);
	    	sky.setTexture(skyTop, SkyBox.Face.TOP);
	    	sky.setTexture(skyBottom, SkyBox.Face.BOTTOM);
	    	sky.setTexture(skyLeft, SkyBox.Face.LEFT);
	    	sky.setTexture(skyRight, SkyBox.Face.RIGHT);
	    	
	    	sm.setActiveSkyBox(sky);
	    	
			    	createAllNodes(sm);
			    	
			    	
			    	
		
		createAnimations(sm);
			    	
		setupNetworking();
		
		print("setup audio");
	
		print("setup physics");
		setupPhysics();
		nm = new NodeMaker(eng, sm, physicsEng);
		setupPatrolNPC(eng,sm);
		setupInputs(sm);
		sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
		initAudio(sm);
		
		tc = new ThrottleController(sm,eng,this,shipN);
		setupLights();
		setupScoreIndicator();
		
		print("setup done");
	}
	
	//TODO animation setup
	private void createAnimations(SceneManager sm) throws IOException {
 
		//Right Handl
    	SkeletalEntity rightHand = sm.createSkeletalEntity("rightHandAv", "MyFettHandVer5.rkm", "MyFettHandVer5.rks");
    	
    	Texture tex6 = sm.getTextureManager().getAssetByPath("FettArmVer5.png");
    	TextureState tstate6 = (TextureState) sm.getRenderSystem()
    	.createRenderState(RenderState.Type.TEXTURE);
    	tstate6.setTexture(tex6);
    	rightHand.setRenderState(tstate6);
   	
    	SceneNode rightHandN = sm.getRootSceneNode().createChildSceneNode("rightHandNode");
    	rightHandN.attachObject(rightHand);
    	float scale = 0.5f;
    	rightHandN.scale(scale,scale,scale);//
    	rightHandN.translate(-0.7f, -0.5f, 0);
    		
    		
    			
    	rightHand.loadAnimation("throttleUpAndBackAnimation", "ThrustUpAndBack.rka");
    	rightHand.loadAnimation("throttleDownAndBackAnimation", "ThrustDownAndBack2.rka");
    	rightHand.loadAnimation("throttleLeftAndBackAnimation", "ThrustLeftandBack.rka");
    	rightHand.loadAnimation("throttleRightAndBackAnimation", "ThrustRightandBack.rka");
    			
    			
    	rightHand.loadAnimation("throttleUpAndPause", "ThrustUpAndPause.rka");
    	rightHand.loadAnimation("throttleDownAndPause", "ThrustDownAndPause.rka");
    	rightHand.loadAnimation("throttleBackFromUp", "FromUp_GoDown_andPause.rka");
    	rightHand.loadAnimation("throttleBackFromDown", "FromDown_GoUp_andPause.rka");
    			
    		
    	shipN.attachChild(rightHandN);
    	rightHandN.moveDown(0.5f);
    			
    			//AnimationStar
    			
    			SkeletalEntity movingStar = 
    					sm.createSkeletalEntity("movingStar", "Object6.rkm", "Object6.rks");
    			
    	    	Texture tex8 = sm.getTextureManager().getAssetByPath("Object6.png");
    	    	TextureState tstate8 = (TextureState) sm.getRenderSystem()
    	    	.createRenderState(RenderState.Type.TEXTURE);
    	    	tstate8.setTexture(tex8);
    	   	movingStar.setRenderState(tstate8);
    	   	
        	SceneNode movingStarN =
        			sm.getRootSceneNode().createChildSceneNode("movingStarNode");
        	movingStarN.attachObject(movingStar);
        	//movingStarN.scale(0.1f, 0.1f, 0.1f);//
        	movingStarN.setLocalPosition(0.0f, 55f, 0.0f);
        	
        	movingStar.loadAnimation("Object6", "Object6.rka");
        	
        	movingStar.playAnimation("Object6", 0.5f, LOOP, 0);
		
	}

	//TODO create all nodes
	private void createAllNodes(SceneManager sm) throws IOException {
	  	Tessellation tessE = sm.createTessellation("tessE", 7);
    	tessE.setSubdivisions(8f);
    	SceneNode tessN =
    	sm.getRootSceneNode().
	    	createChildSceneNode("TessN");
    	tessN.attachObject(tessE);
    	tessN.translate(Vector3f.createFrom(-6.2f, -2.2f, 3.2f));
    	// tessN.yaw(Degreef.createFrom(37.2f));
    	tessN.scale(2000, 956, 2000);
    	tessE.setHeightMap(this.getEngine(), "scribble.jpg");
    	tessE.setTexture(this.getEngine(), "carpet.png");
    	tessE.setTextureTiling(55, 155);
    	tessE.setMultiplier(5);
    	
    	isTerrain = true;
    	
    	tessN.setLocalPosition(-200.0f, -50.0f, -45.0f);

    	
    	Entity stationE = sm.createEntity("station", "SpaceStationAlpha-b.obj");
    	stationE.setPrimitive(Primitive.TRIANGLES);
		stationN = sm.getRootSceneNode().createChildSceneNode("stationNode");
		stationN.moveBackward(60.0f);
		stationN.moveUp(25f);
		stationN.moveLeft(4f);
		stationN.attachObject(stationE);
		stationN.moveUp(10);
		
		print("stationN: " + stationN.getWorldPosition());
		
		RotationController rc2 =
		    	new RotationController(Vector3f.createUnitVectorY(), .03f);
		    	rc2.addNode(stationN);
		    	sm.addController(rc2);
		    	
		    	Entity Object2bE = sm.createEntity("object2b", "Object2.obj");
		    	Object2bE.setPrimitive(Primitive.TRIANGLES);
		    	Object2bN = sm.getRootSceneNode().createChildSceneNode(Object2bE.getName() + "Node");
		    	Object2bN.moveBackward(80.0f);
		    	Object2bN.moveUp(25f);
		    	Object2bN.moveLeft(4f);
		    	Object2bN.attachObject(Object2bE);
		    	
		    	//
		    	Entity stationBlueE = sm.createEntity("stationBlue", "SpaceStationAlpha-b.obj");
		    	stationBlueE.setPrimitive(Primitive.TRIANGLES);
		    	stationBlueN = sm.getRootSceneNode().createChildSceneNode(stationBlueE.getName() + "Node");
		    	stationBlueN.moveForward(350.0f);
		    	stationBlueN.moveUp(25f);
		    	stationBlueN.moveLeft(4f);
		    	stationBlueN.attachObject(stationBlueE);
		    	
		    	print("stationBlueN: " + stationBlueN.getWorldPosition());//fd
				
				
				RotationController rc4 =
				    	new RotationController(Vector3f.createUnitVectorY(), .03f);
				    	rc4.addNode(stationBlueN);
				    	sm.addController(rc4);
				    	
				    	Entity Object2E = sm.createEntity("object2", "Object2.obj");
				    	Object2E.setPrimitive(Primitive.TRIANGLES);
				    	Object2N = sm.getRootSceneNode().createChildSceneNode(Object2E.getName() + "Node");
				    	Object2N.moveForward(370.0f);
				    	Object2N.moveUp(25f);
				    	Object2N.moveRight(4f);
				    	Object2N.attachObject(Object2E);
				    	
				    	
					      TextureManager tm = eng.getTextureManager();
					        Texture blueTexture = tm.getAssetByPath("stationBlue.png");
					        RenderSystem rs = sm.getRenderSystem();
					        TextureState state = (TextureState)rs.createRenderState(RenderState.Type.TEXTURE);
					        state.setTexture(blueTexture);
					        stationBlueE.setRenderState(state);
		    	
		    	
					        //here though
		    	Entity Object3E = sm.createEntity("object3", "Object3.obj");
		    	Object3E.setPrimitive(Primitive.TRIANGLES);
		    	Object3N = sm.getRootSceneNode().createChildSceneNode(Object3E.getName() + "Node");
		    	Object3N.moveForward(100.0f);
		    	Object3N.moveUp(72f);
		    	Object3N.moveRight(300f);
		    	Object3N.setLocalScale(20, 20, 20);
		    	Object3N.attachObject(Object3E);
				
				
				RotationController rc3 =
				    	new RotationController(Vector3f.createUnitVectorY(), .01f);
				    	rc3.addNode(Object3N);
				    	sm.addController(rc3);
				    	
				    	
				    	Entity Object4E = sm.createEntity("object4", "Object4.obj");
				    	Object4E.setPrimitive(Primitive.TRIANGLES);
				    	Object4N = sm.getRootSceneNode().createChildSceneNode(Object4E.getName() + "Node");
				    	Object4N.moveBackward(100.0f);
				    	Object4N.moveUp(150f);
				    	Object4N.moveLeft(300f);
				    	Object4N.setLocalScale(64, 64, 64);
				    	Object4N.attachObject(Object4E);
						
						
						RotationController rc5 =
						    	new RotationController(Vector3f.createUnitVectorZ(), -.06f);
						    	rc5.addNode(Object4N);
						    	sm.addController(rc5);
				    	
				    	
				    	Entity Object1E = sm.createEntity("object1", "Object1Ver2.obj");
				    	Object1E.setPrimitive(Primitive.TRIANGLES);
				    	Object1N = sm.getRootSceneNode().createChildSceneNode(Object1E.getName() + "Node");
				    	Object1N.moveBackward(400.0f);
				    	Object1N.moveUp(88f);
				    	Object1N.moveRight(100f);
				    //	Object1N.setLocalScale(20, 20, 20);
				    	Object1N.attachObject(Object1E);
				    	
				    
		
		    	Entity dropShipE = sm.createEntity("dropShip", "DropShipVer4.obj");
		    	dropShipE.setPrimitive(Primitive.TRIANGLES);
		    	dropShipN = sm.getRootSceneNode().createChildSceneNode(dropShipE.getName() + "Node");
		    	dropShipN.moveBackward(30.0f);
		    	dropShipN.moveUp(55f);
		    	dropShipN.moveRight(4f);
		    	dropShipN.attachObject(dropShipE);
		    	
		    	
		    	
		    	
		    	
		
		    	
				camera.getParentNode().moveUp(2);
				
				shipN.attachChild(camera.getParentNode());
				camera.getParentNode().setLocalPosition(0,0,0);
				print("ship position: " + shipN.getWorldPosition());
				

				sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));

				Light plight = sm.createLight("testLamp0", Light.Type.POINT);
				plight.setAmbient(new Color(.3f, .3f, .3f));
				plight.setDiffuse(new Color(.7f, .7f, .7f));
				plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
				plight.setRange(5f);

				SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
				plightNode.attachObject(plight);

				//headlight goes in ship
				Light headlight = sm.createLight("headlight", Light.Type.SPOT);
				headlight.setConeCutoffAngle(Degreef.createFrom(10));
				headlight.setSpecular(Color.white);

				SceneNode headlightNode = sm.getRootSceneNode().createChildSceneNode("headlightNode");
				headlightNode.attachObject(headlight);

				//this.getEngine().getSceneManager().getSceneNode("myShipNode").attachChild(headlightNode);
				shipN.attachChild(headlightNode);
				
				setupAsteroidField(sm);
		
	}
	
	//TODO asteroid setups
	private void setupAsteroidField(SceneManager sm) throws IOException {
		Entity asteroidM1E = sm.createEntity("asteroidM1", "Object3.obj");
		asteroidM1E.setPrimitive(Primitive.TRIANGLES);
    	asteroidM1 = sm.getRootSceneNode().createChildSceneNode(asteroidM1E.getName() + "Node");
    	asteroidM1.moveForward(100.0f);
    	asteroidM1.moveUp(100f);
    	asteroidM1.moveLeft(612f);
    	asteroidM1.setLocalScale(20, 20, 20);
    	asteroidM1.attachObject(asteroidM1E);
		
		    	
		    	Entity asteroidM2E = sm.createEntity("asteroidM2", "Object4.obj");
		    	asteroidM2E.setPrimitive(Primitive.TRIANGLES);
		    	asteroidM2 = sm.getRootSceneNode().createChildSceneNode(asteroidM2E.getName() + "Node");
		    	asteroidM2.moveForward(100.0f);
		    	asteroidM2.moveUp(150f);
		    	asteroidM2.moveLeft(600f);
		    	asteroidM2.setLocalScale(64, 64, 64);
		    	asteroidM2.attachObject(asteroidM2E);
				
		    	Entity asteroidM3E = sm.createEntity("asteroidM3", "Object4.obj");
		    	asteroidM3E.setPrimitive(Primitive.TRIANGLES);
		    	asteroidM3 = sm.getRootSceneNode().createChildSceneNode(asteroidM3E.getName() + "Node");
		    	asteroidM3.moveForward(106.0f);
		    	asteroidM3.moveUp(112f);
		    	asteroidM3.moveLeft(577f);
		    	asteroidM3.setLocalScale(30, 30, 30);
		    	asteroidM3.attachObject(asteroidM3E);
				
						Entity asteroidM4E = sm.createEntity("asteroidM4", "Object3.obj");
						asteroidM4E.setPrimitive(Primitive.TRIANGLES);
						asteroidM4 = sm.getRootSceneNode().createChildSceneNode(asteroidM4E.getName() + "Node");
						asteroidM4.moveForward(115.0f);
						asteroidM4.moveUp(145f);
						asteroidM4.moveRight(560f);
						asteroidM4.setLocalScale(12, 12, 12);
						asteroidM4.attachObject(asteroidM4E);
						
						Entity asteroidM5E = sm.createEntity("asteroidM5", "Object3.obj");
						asteroidM5E.setPrimitive(Primitive.TRIANGLES);
						asteroidM5 = sm.getRootSceneNode().createChildSceneNode(asteroidM5E.getName() + "Node");
						asteroidM5.moveForward(100.0f);
						asteroidM5.moveUp(135f);
						asteroidM5.moveLeft(637f);
						asteroidM5.setLocalScale(35, 20, 35);
						asteroidM5.attachObject(asteroidM5E);

						Entity Maker.Asteroid("asteroidM1", "Object3.obj", sm, 100.0f, 135f, 637f, new Vector3f(35,20,35));
						Entity Maker.Asteroid("asteroidM2", "Object3.obj", sm, 100.0f, 135f, 637f, new Vector3f(35,20,35));
						Entity Maker.Asteroid("asteroidM3", "Object3.obj", sm, 100.0f, 135f, 637f, new Vector3f(35,20,35));
						Entity Maker.Asteroid("asteroidM4", "Object3.obj", sm, 100.0f, 135f, 637f, new Vector3f(35,20,35));
						Entity Maker.Asteroid("asteroidM5", "Object3.obj", sm, 100.0f, 135f, 637f, new Vector3f(35,20,35));
								
								
								RotationController rc13 =
								    	new RotationController(Vector3f.createUnitVectorY(), .07f);
										rc13.addNode(asteroidM5);
								    	sm.addController(rc13);
								    	
								    	
								    	Entity asteroidM6E = sm.createEntity("asteroidM6", "Object4.obj");
								    	asteroidM6E.setPrimitive(Primitive.TRIANGLES);
								    	asteroidM6 = sm.getRootSceneNode().createChildSceneNode(asteroidM6E.getName() + "Node");
								    	asteroidM6.moveForward(115.0f);
								    	asteroidM6.moveUp(200f);
								    	asteroidM6.moveLeft(612f);
								    	asteroidM6.setLocalScale(90, 90, 90);
								    	asteroidM6.attachObject(asteroidM6E);
										
								    	
								    	Entity asteroidM7E = sm.createEntity("asteroidM7", "Object4.obj");
								    	asteroidM7E.setPrimitive(Primitive.TRIANGLES);
								    	asteroidM7 = sm.getRootSceneNode().createChildSceneNode(asteroidM7E.getName() + "Node");
								    	asteroidM7.moveForward(130.0f);
								    	asteroidM7.moveUp(127f);
								    	asteroidM7.moveLeft(577f);
								    	asteroidM7.setLocalScale(5, 10, 15);
								    	asteroidM7.attachObject(asteroidM7E);
										
										
										RotationController rc15 =
										    	new RotationController(Vector3f.createUnitVectorZ(), .4f);
										    	rc15.addNode(asteroidM3);
										    	sm.addController(rc15);
										    	
												Entity asteroidM8E = sm.createEntity("asteroidM8", "Object3.obj");
												asteroidM8E.setPrimitive(Primitive.TRIANGLES);
												asteroidM8 = sm.getRootSceneNode().createChildSceneNode(asteroidM8E.getName() + "Node");
												asteroidM8.moveForward(155.0f);
												asteroidM8.moveUp(100f);
												asteroidM8.moveRight(400f);
												asteroidM8.setLocalScale(88, 100, 88);
												asteroidM8.attachObject(asteroidM8E);
												
												
		    	
	}
	
	//TODO lights
	private SceneNode lightHolder;
	private void setupLights() {
		
		lightHolder = sm.getRootSceneNode().createChildSceneNode("lightHolder");
		
		
		
		Light headlight1 = sm.createLight("headlight1", Light.Type.SPOT);
		headlight1.setConeCutoffAngle(Degreef.createFrom(10));
		headlight1.setSpecular(Color.white);
		headlight1.setRange(10f);

		SceneNode headlightNode1 = sm.getRootSceneNode().createChildSceneNode("headlightNode1");
		headlightNode1.attachObject(headlight1);
		
		Light headlight2 = sm.createLight("headlight2", Light.Type.SPOT);
		headlight2.setConeCutoffAngle(Degreef.createFrom(10));
		headlight2.setSpecular(Color.white);
		headlight2.setRange(10f);

		SceneNode headlightNode2 = sm.getRootSceneNode().createChildSceneNode("headlightNode2");
		headlightNode2.attachObject(headlight2);
		
		Light headlight3 = sm.createLight("headlight3", Light.Type.SPOT);
		headlight3.setConeCutoffAngle(Degreef.createFrom(10));
		headlight3.setSpecular(Color.white);
		headlight3.setRange(10f);

		SceneNode headlightNode3 = sm.getRootSceneNode().createChildSceneNode("headlightNode3");
		headlightNode3.attachObject(headlight3);
		
		headlightNode1.setLocalPosition(-0.3f,0,0);
		headlightNode3.setLocalPosition(0.3f,0,0);
		
		
		//lightHolder.attachObject(headlightNode1);
		lightHolder.attachChild(headlightNode1);
		//lightHolder.attachObject(headlightNode2);
		lightHolder.attachChild(headlightNode2);
		//lightHolder.attachObject(headlightNode3);
		lightHolder.attachChild(headlightNode3);
		
		lightHolder.pitch(Degreef.createFrom(-90));
		
		//lightHolder.moveBackward(2);

		//this.getEngine().getSceneManager().getSceneNode("myShipNode").attachChild(headlightNode);
		shipN.attachChild(lightHolder);
	}

	//TODO ship setup with color and team
	//ship is setup with code provided
	private void setupShip() throws IOException {
		print("setupShip");
		
		if(chooseTeam == 0) {
			makePlayerGrey();
		}
		else makePlayerBlue();
	}
	
	private void makePlayerGrey() throws IOException {
		print("makePlayerGrey");
		Entity shipE = sm.createEntity("ship", "cockpitMk3j.obj");
		shipE.setPrimitive(Primitive.TRIANGLES);

		//SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		shipN = sm.getRootSceneNode().createChildSceneNode(shipE.getName() + "Node");

		//TODO
		//set start location for this team
		shipN.setLocalPosition(startPositionZero);
		print("shipLocation: " + shipN.getLocalPosition());
		shipN.attachObject(shipE);
		shipN.yaw(Degreef.createFrom(180));

		sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
	}
	
	private void makePlayerBlue() throws IOException {
		print("makePlayerBlue");
		Entity shipE = sm.createEntity("ship", "blueCockpit.obj");
		shipE.setPrimitive(Primitive.TRIANGLES);
		
		//SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		shipN = sm.getRootSceneNode().createChildSceneNode(shipE.getName() + "Node");
		
		//set start location for this team
		shipN.setLocalPosition(startPositionOne);
		shipN.attachObject(shipE);
		shipN.yaw(Degreef.createFrom(180));

		Entity BlueCockpitE = sm.createEntity("BlueCockpit", "cockpitMk3j.obj");
        BlueCockpitE.setPrimitive(Primitive.TRIANGLES);
        //BlueCockpitN = sm.getRootSceneNode().createChildSceneNode(BlueCockpitE.getName() + "Node");
        //BlueCockpitN.moveForward(0.0f);
        //BlueCockpitN.moveUp(25f);
        //BlueCockpitN.moveRight(0f);
        //BlueCockpitN.attachObject(BlueCockpitE);

        TextureManager tm1 = eng.getTextureManager();
        Texture blueTexture2 = tm1.getAssetByPath("cockpitMk3jB-Blue.png");
        RenderSystem rs2 = sm.getRenderSystem();
        TextureState state1 = (TextureState)rs2.createRenderState(RenderState.Type.TEXTURE);
        state1.setTexture(blueTexture2);
        BlueCockpitE.setRenderState(state1);
	}
	
	//TODO lives hud stuff
	private SceneNode[] lives;
	private Vector3 scorePosition = Vector3f.createFrom(0.8f,-0.3f,-0.8f);
	//private Vector3 scorePosition = Vector3f.createFrom(0,0,0);
	private SceneNode scoreHolder;
	private Vector3[] livesPositions;
	private void setupScoreIndicator() throws IOException {
		lives = nm.makeScoreIndicators();
		livesPositions = new Vector3[lives.length];
		
		scoreHolder = sm.getRootSceneNode().createChildSceneNode("scoreHolder");
		
		for(int i=0; i<lives.length;i++) {
			livesPositions[i] = lives[i].getLocalPosition();
			scoreHolder.attachChild(lives[i]);
		}
		
		shipN.attachChild(scoreHolder);
		
		scoreHolder.setLocalPosition(scorePosition);
		
		//scoreHolder.roll(Degreef.createFrom(90));
		
		
		scoreHolder.roll(Degreef.createFrom(90));
		scoreHolder.pitch(Degreef.createFrom(40));
		
		scoreHolder.moveBackward(1);
		scoreHolder.moveLeft(0.1f);
	}
	
	private void livesUpdate() {
		for(SceneNode n : lives) {
			n.setLocalPosition(10000,10000,10000);
		}
		for(int i=0; i<shipLives;i++) {
			lives[i].setLocalPosition(livesPositions[i]);
		}
		if(shipLives == 0) {
			System.exit(0);
		}
	}
	

	//TODO NPC stuff
	private void setupPatrolNPC(Engine eng, SceneManager sm) throws IOException{
		
		//SceneNode npc1 = nm.makeNPC("npc1");
		
		npcs = new PatrolEnemy[10];
		
		Vector3 position1 = Vector3f.createFrom(20,35,145);
		npcs[0] = npcFactory("npcOne", position1);
		
		Vector3 position2 = Vector3f.createFrom(0,27,145);
		npcs[1] = npcFactory("npcTwo", position1);
		
		Vector3 position3 = Vector3f.createFrom(-20,27,145);
		npcs[2] = npcFactory("npcThree", position1);
		
		
	}
	
	private PatrolEnemy npcFactory(String name, Vector3 position) throws IOException {
		SceneNode node = nm.makeNPC(name, position);
		PatrolEnemy pe = new PatrolEnemy(node,this,position);
		SceneNode[] targets = new SceneNode[] {shipN};
		pe.setTargets(targets);
		return pe;
	}
	
	private void npcUpdates() {
		for(PatrolEnemy p : npcs) {
			if(p!=null) {
				p.update(eng.getElapsedTimeMillis());
			}
		}
	}
	
	//TODO setup physics
	private void setupPhysics() {
		System.out.println("setupPhysics");
		String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
		
		physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEng.initSystem();
		
		float mass = 1.0f;
		float up[] = {0,1,0};
		double[] temptf;
		
		temptf = toDoubleArray(shipN.getLocalTransform().toFloatArray());
		PhysicsObject shipPhysicsObject = physicsEng.addSphereObject(physicsEng.nextUID(), mass, temptf, 1.0f);
		shipN.setPhysicsObject(shipPhysicsObject);
	}

	//TODO inputs and controls
	//seperate methods for keyboards/gamepads
	protected void setupInputs(SceneManager sm) throws IOException {
		im = new GenericInputManager();
		playerController = new FlightController(this, camera, camera.getParentSceneNode(), shipN, im, sm, physicsEng);
		
		setupAdditionalTestControls(im);
		
		//animationThrottleUp()
	}
	
	private void setupAdditionalTestControls(InputManager im) {
		ArrayList<Controller> controllers = im.getControllers();
		ArrayList<String> keyboards = new ArrayList<String>();
		
		for (int i = 0; i < controllers.size(); i++) {
			if (controllers.get(i).getType() == Controller.Type.KEYBOARD)
				keyboards.add(controllers.get(i).getName());
		}
		
		controlTest = new throttleUp();
		controlTest2 = new throttleDown();
		controlTest3 = new throttleLeft();
		controlTest4 = new throttleRight();
		controlTest8 = new destroyTerrain();
		
		toggleLights = new ToggleLights();
		
		for (int i = 0; i < keyboards.size(); i++) {
			
			//im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.O, controlTest,
			//		InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			//im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.P, controlTest2,
			//		InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			//im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.K, controlTest3,
			//		InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.L, toggleLights,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.U, controlTest8,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
			
		}
	}
	

	
	//TODO ghost avatar
	public void setupGhostAvatar(GhostAvatar ghost, int team) throws IOException {
		
		System.out.println("setting up ghost " + ghost.getID().toString());
		SceneNode ghostN = sm.getRootSceneNode().createChildSceneNode(ghost.getID().toString());
		
		if(team==0) {
			ghostN = makeGreyGhost(ghostN,ghost);
		}else {
			ghostN = makeBlueGhost(ghostN,ghost);
		}

		ghostN.setLocalPosition((Vector3f) Vector3f.createFrom(-2, 0, 0));
		//ghostN.attachObject(shipE);
		ghostN.yaw(Degreef.createFrom(180));
		
		float mass = 1.0f;
		double[] temptf;
		temptf = MyGame.toDoubleArray(ghostN.getLocalTransform().toFloatArray());
		PhysicsObject shipPhysicsObject = physicsEng.addSphereObject(physicsEng.nextUID(), mass, temptf, 1.0f);
		ghostN.setPhysicsObject(shipPhysicsObject);
		
		ghost.setNode(ghostN);
	}
	
	private SceneNode makeGreyGhost(SceneNode ghostN, GhostAvatar ghost) throws IOException {
		Entity shipE = sm.createEntity("ghostShip" + ghost.getID() , "GhostShips-f.obj");
		shipE.setPrimitive(Primitive.TRIANGLES);

		//SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		ghostN = sm.getRootSceneNode().createChildSceneNode(shipE.getName() + "Node");
		ghostN.attachObject(shipE);
		return ghostN;
	}
	
	private SceneNode makeBlueGhost(SceneNode ghostN, GhostAvatar ghost) throws IOException {
		//print("makeBlueGhost()");
		Entity shipE = sm.createEntity("ghostShip" + ghost.getID() , "blueGhost.obj");
		shipE.setPrimitive(Primitive.TRIANGLES);

		//SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		ghostN = sm.getRootSceneNode().createChildSceneNode(shipE.getName() + "Node");
		ghostN.attachObject(shipE);
		return ghostN;
	}

	*/
	
	/*
	//TODO update marker
	public void update() {
	//protected void update(Engine engine) {

		
		livesUpdate();
		
		//System.out.println("update");//
		
		updateDefaults(engine);
		
		processNetworking(engine.getElapsedTimeMillis());
		
		SceneManager sm = engine.getSceneManager();
		SceneNode stationN = sm.getSceneNode("stationNode");
		SceneNode Object1N = sm.getSceneNode("object1Node");
		SceneNode movingStarN = sm.getSceneNode("movingStarNode");
		playerController.update();
		
		
		//scoreHolder.roll(Degreef.createFrom(10 * engine.getElapsedTimeMillis()/1000));

		npcUpdates();
		
		
		//System.out.println("x:" + shipN.getLocalPosition().x());
		//System.out.println("y:" + shipN.getLocalPosition().y());
		//System.out.println("z:" + shipN.getLocalPosition().z());

		Object1N.moveLeft(.3f);
		Object3N.moveRight(.1f);
		Object4N.moveBackward(.1f);
		dropShipN.moveForward(.08f);
		
		movingStarN.moveForward(.2f);
		movingStarN.moveLeft(.2f);
		//dropShipN.roll(Degreef.createFrom(1));

		float deltaTime = engine.getElapsedTimeMillis()/1000;
		
		tc.update(deltaTime);
		
		
	
		SkeletalEntity rightHand = (SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
		
		rightHand.update();
		
					SkeletalEntity movingStar =
		(SkeletalEntity) eng.getSceneManager().getEntity("movingStar");
		movingStar.update();
		
		
		//System.out.println("update");
		
		//System.out.println("station world position is " + stationN.getWorldPosition());
		
		//print("" + stationN.getWorldPosition());
		//print("" + stationSound);
		stationSound.setLocation(stationN.getWorldPosition());
		laserFireSound.setLocation(shipN.getWorldPosition());
		shipNoiseSound.setLocation(Object1N.getWorldPosition());
		setEarParameters(sm);
		
		Matrix4 mat;
		physicsEng.update(eng.getElapsedTimeMillis());
		for (SceneNode s : engine.getSceneManager().getSceneNodes())
		{ 
			if (s.getPhysicsObject() != null){
				mat = Matrix4f.createFrom(toFloatArray(s.getPhysicsObject().getTransform()));
				s.setLocalPosition(mat.value(0,3),mat.value(1,3),
				mat.value(2,3));
			}
		}
	}
	*/

	/*
	
	
	float networkTimer = 0;
	float tickRate = 100;
	protected void processNetworking(float deltaTime) {
		
		networkTimer += deltaTime/1000;
		
		if(protClient.hitUpdate()) {
			hit();
			shipLives--;
		}
		
		if(networkTimer > 1/tickRate) {
			networkTimer = 0;
			protClient.sendMoveMessage(getPlayerPosition(), getPlayerDirection());
		}
		
		if(protClient!=null) {
			protClient.processPackets();
		}
	}
	
	Vector3 startPositionZero = Vector3f.createFrom(0,35,-90);
	Vector3 startPositionOne = Vector3f.createFrom(0,35,380);
	public void hit() {
		//Return player to starting position based on team
		
		shipLives--;
		
		if(getPlayerTeam() == 0) {
			double[] transform = shipN.getPhysicsObject().getTransform();
			
			transform[12] = startPositionZero.x();
			transform[13] = startPositionZero.y();
			transform[14] = startPositionZero.z();
			
			shipN.getPhysicsObject().setTransform(transform);
		}
		else {
			double[] transform = shipN.getPhysicsObject().getTransform();
			
			transform[12] = startPositionOne.x();
			transform[13] = startPositionOne.y();
			transform[14] = startPositionOne.z();
			
			shipN.getPhysicsObject().setTransform(transform);
		}
	}
	
	public void shootNetworking() {
		if(protClient!=null) {
			protClient.sendShootMessage();
		}
	}

	float testLerp = 0f;
	float elapsedTestTime = 10f;
	
	// method holds all actions that are used in every game, such as rendering and
	// calculating elapsed time.
	private void updateDefaults(Engine engine) {
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime += engine.getElapsedTimeMillis();
		im.update(elapsTime);
	}

	public float getSpeedScale() {
		return speedScale;
	}

	public float getYawDegrees() {
		return yawDegrees;
	}

	public float getPitchDegrees() {
		return pitchDegrees;
	}
	
	public void setIsConnected(boolean b) { isConnected = b; }
	
	public Vector3 getPlayerPosition() {
		return shipN.getWorldPosition();
	}
	
	public Vector3 getPlayerDirection() {
		return Vector3f.createFrom(shipN.getPhysicsObject().getLinearVelocity());
	}
	
	public int getPlayerTeam() {
		return chooseTeam;
	}
	
	
	public static void throttleUpAndBackAnimation()
	{ 
		SkeletalEntity rightHand = (SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
		rightHand.playAnimation("throttleUpAndBackAnimation", 0.5f, NONE, 0);

	}
	
	public static void throttleDownAndBackAnimation()
	{ 
		SkeletalEntity rightHand = (SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
		rightHand.playAnimation("throttleDownAndBackAnimation", 0.5f, NONE, 0);
	}
	
	public static void throttleLeftAndBackAnimation()
	{ 

		SkeletalEntity rightHand =
	(SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
	rightHand.playAnimation("throttleLeftAndBackAnimation", 0.5f, NONE, 0);

	}
	
	public static void throttleRightAndBackAnimation()
	{ 

		SkeletalEntity rightHand = (SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
		rightHand.playAnimation("throttleRightAndBackAnimation", 0.5f, NONE, 0);
	}
	
	
	
	private float animationSpeed = 4f;
	//public static void throttleUpAndPauseAnimation()
	public void throttleUpAndPauseAnimation()
	{ 

		SkeletalEntity rightHand =
	(SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
	rightHand.playAnimation("throttleUpAndPause", animationSpeed, SkeletalEntity.EndType.PAUSE, 0);

	}
	
	//public static void throttleDownAndPauseAnimation()
	public void throttleDownAndPauseAnimation()
	{ 
		System.out.println("throttleDownAndBackAnimation");
		SkeletalEntity rightHand =
				(SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
		rightHand.playAnimation("throttleDownAndPause", animationSpeed, SkeletalEntity.EndType.PAUSE, 0);

	}
	
	//public static void throttleBackFromUpAnimation()
	public void throttleBackFromUpAnimation()
	{ 

		SkeletalEntity rightHand =
	(SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
	rightHand.playAnimation("throttleBackFromUp", animationSpeed, SkeletalEntity.EndType.PAUSE, 0);

	}
	
	//public static void throttleBackFromDownAnimation()
	public void throttleBackFromDownAnimation()
	{ 

		SkeletalEntity rightHand =
	(SkeletalEntity) eng.getSceneManager().getEntity("rightHandAv");
	rightHand.playAnimation("throttleBackFromDown", animationSpeed, SkeletalEntity.EndType.PAUSE, 0);

	}
	
	
	private class destroyTerrain extends AbstractInputAction {

		@Override
		public void performAction(float arg0, Event e) {
			
			SceneNode tessN = eng.getSceneManager().
			getSceneNode("TessN");
			
			//tessN.setLocalPosition(8000.0f, 8000.0f, 8000.0f);
			tessN.moveDown(8000);
		}
	}

	
	private class throttleUp extends AbstractInputAction {
				
		@Override
		public void performAction(float arg0, Event e) {
			throttleUpAndBackAnimation();
		}
	}
	
	
	private class throttleDown extends AbstractInputAction {
		
		@Override
		public void performAction(float arg0, Event e) {
			throttleDownAndPauseAnimation();
		}
	}
	
	private class throttleLeft extends AbstractInputAction {
		
		@Override
		public void performAction(float arg0, Event e) {
			throttleLeftAndBackAnimation();
		}
	}
	
	
	private class throttleRight extends AbstractInputAction {
		
		@Override
		public void performAction(float arg0, Event e) {
			throttleRightAndBackAnimation();
		}
	}
	
	private class ToggleLights extends AbstractInputAction {
		
		private boolean on = true;
		@Override
		public void performAction(float arg0, Event e) {
			if(on) {
				lightHolder.setLocalPosition(10000,10000,10000);
				on=false;
			}
			else {
				lightHolder.setLocalPosition(0,0,0);
				on=true;
			}
		}
	}
	

	
	
	public void setEarParameters(SceneManager sm)
	{ 
		
		SceneNode shipN = eng.getSceneManager().getSceneNode("shipNode");
		Vector3 avDir = shipN.getWorldForwardAxis();
		// note - should get the camera's forward direction
		// - avatar direction plus azimuth
		//audioMgr.getEar().setLocation(stationN.getWorldPosition());
		//audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));
		audioMgr.getEar().setLocation(shipN.getWorldPosition());
		audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));

	}
	
	public void initAudio(SceneManager sm)
	{ 
		print("initAudio setup");
		Configuration configuration = sm.getConfiguration();
		String sfxPath = configuration.valueOf("assets.sounds.path");
		String musicPath = configuration.valueOf("assets.music.path");
		AudioResource theMusic, theStation, npcBeeps, theShipNoise, theFrickinLasers;
		audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
		
		print("audioMgr: " + audioMgr);
		if (!audioMgr.initialize()) {
			System.out.println("The Audio Manager failed to initialize :(");
			return;
		}
		
		theMusic = audioMgr.createAudioResource(musicPath + "bensound-epic.wav", AudioResourceType.AUDIO_STREAM);
		theStation = audioMgr.createAudioResource(sfxPath + "Cartoon-warp-02.wav", AudioResourceType.AUDIO_SAMPLE);
		npcBeeps = audioMgr.createAudioResource(sfxPath + "Robot_blip-Marianne_Gagnon-120342607.wav", AudioResourceType.AUDIO_SAMPLE);
		theShipNoise = audioMgr.createAudioResource(sfxPath + "RocketThrusters-SoundBible.com-1432176431.wav", AudioResourceType.AUDIO_SAMPLE);
		theFrickinLasers = audioMgr.createAudioResource(sfxPath + "LaserBlasts-SoundBible.com-108608437.wav", AudioResourceType.AUDIO_SAMPLE);
		

		//  NPCSound, laserFireSound, shipNoiseSound;
		

		
		backgroundMusic = new Sound(theMusic, SoundType.SOUND_MUSIC, 1, true);
		stationSound = new Sound(theStation, SoundType.SOUND_EFFECT, 400, true);
		NPCSound = new Sound(npcBeeps, SoundType.SOUND_EFFECT, 20, true);
		laserFireSound = new Sound(theFrickinLasers, SoundType.SOUND_EFFECT, 2, false);
		shipNoiseSound = new Sound(theShipNoise, SoundType.SOUND_EFFECT, 10, true);
		
	
			backgroundMusic.initialize(audioMgr);
			backgroundMusic.play();
			

			stationSound.initialize(audioMgr);
			stationSound.setMaxDistance(10.0f);
			stationSound.setMinDistance(0.5f);
			stationSound.setRollOff(5.0f);
			
			shipNoiseSound.initialize(audioMgr);
			shipNoiseSound.setMaxDistance(10.0f);
			shipNoiseSound.setMinDistance(0.5f);
			shipNoiseSound.setRollOff(5.0f);
			
			NPCSound.initialize(audioMgr);
			NPCSound.setMaxDistance(10.0f);
			NPCSound.setMinDistance(0.5f);
			NPCSound.setRollOff(5.0f);
			
			laserFireSound.initialize(audioMgr);
			laserFireSound.setMaxDistance(5.0f);
			laserFireSound.setMinDistance(0.5f);
			laserFireSound.setRollOff(2.5f);
			
			
			SceneNode stationN = sm.getSceneNode("stationNode");
		
			
			SceneNode shipN = sm.getSceneNode("shipNode");
			
			SceneNode Object1N = sm.getSceneNode("object1Node");
			
			shipNoiseSound.setLocation(Object1N.getWorldPosition());
			
			setEarParameters(sm);
			
			shipNoiseSound.play();
			stationSound.play();
		//	NPCSound.play();
	}
	
	public static void playFireSound()
	{
		laserFireSound.play();
	}
	

	public int getThrottleSign() {
		return playerController.getThrottleSign();
	}
	
	private int getPitchSign() {
		return playerController.getPitchSign();
	}

	private int getRollSign() {
		return playerController.getRollSign();
	}

	private int getYawSign() {
		return playerController.getYawSign();

	public SceneManager getSceneManager() { return sm; }
	public Engine getEngine() { return eng; }
	public PhysicsEngine getPhysicsEngine() { return physicsEng; }
	public NodeMaker getNodeMaker() { return nm; }
	public SceneNode getShip() { return shipN; }
	*/

	long elapsedTime;
	long prevTime;
	long diffTime;
	private void updateDeltaTime(){
		diffTime = System.currentTimeMillis() - prevTime;
		prevTime = System.currentTimeMillis();
		deltaTime = (float)diffTime/1000;
	}

	private float[] toFloatArray(double[] arr)
	{ 
		if (arr == null) return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++)
		{
			ret[i] = (float)arr[i];
		}
	return ret;
	}

	public static double[] toDoubleArray(float[] arr) 
	{
		if (arr == null) return null;
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++)
		{ 
			ret[i] = (double)arr[i];
		}
		return ret;
	}

	public float getDeltaTime() { return deltaTime; }
	public Camera getCamera(){ return (engine.getRenderSystem().getViewport("MAIN").getCamera()); }
	public Engine getEngine() { return engine; }
	public PhysicsEngine getPhysicsEngine() { return physicsEngine; }
	public InputManager getInputManager() { return im; }
	public SceneGraph getSceneGraph() { return engine.getSceneGraph(); }
	public GameObject getPlayerShip() { return shipObj; }
	public ObjShape getLaserShape() { return laserS; }
	public ObjShape getThrottleShape() { return laserS; }
	public ObjShape getScoreShape() { return laserS; }
	public ObjShape getNPCShape() { return laserS; }
	public TextureImage getLaserTexture() { return laserT; }
	public TextureImage getThrottleTexture() { return laserT; }
	public TextureImage getScoreTexture() { return laserT; }
	public TextureImage getNPCTexture() { return laserT; }
}