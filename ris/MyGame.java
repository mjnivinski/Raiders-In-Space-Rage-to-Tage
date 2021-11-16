package ris;

import java.io.IOException;
import java.util.ArrayList;
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
import tage.nodeControllers.*;
import tage.Light;

import tage.input.*;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

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

	private ThrottleController throttleController;

	private NodeMaker nm;

	private GameObject cockpitO, shipObj, satellite, blueShipO;

	private GameObject tempShip;
	private GameObject terrain;

	private GameObject[] asteroids;

	private PhysicsObject shipPhysObj;

	private ObjShape asteroidM, cockpitGreyS, cockpitBlueS, blueShipS, laserS, throttleS, scoreS, npcS,
					 asteroid1S, asteroid2S;
	
	private TextureImage stuff, things, cockpitBlueT, blueShipT, laserT, throttleT, scoreT, npcT,
					 asteroid1T, asteroid2T;
	private ObjShape terrainS;
	private TextureImage terrainT, terrainHM;

	private GameObject hand;
	private AnimatedShape handS;
	private TextureImage handT;


	private Light ambientLight, light1, light2;
	private NodeController rc, sc;
	private int spaceBox; // skybox
	private SceneGraph sceneGraph;

	private float vals[] = new float[16];

	private float deltaTime, previousTime;
	private int shipLives = 3;

	private DestroyTerrain DT;
	private ToggleLights TL;

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
		System.out.println("loadShapes");
		blueShipS = new ImportedModel("blueGhost.obj");
		cockpitBlueS = new ImportedModel("blueCockpit.obj");
		laserS = new ImportedModel("otherLaser.obj");
		throttleS = new ImportedModel("throttleIndicator.obj");
		scoreS = new ImportedModel("scoreIndicator.obj");
		npcS = new ImportedModel("DropShipVer4.obj");
		asteroid1S = new ImportedModel("Asteroid1.obj");
		asteroid2S = new ImportedModel("Asteroid2.obj");
		terrainS = new TerrainPlane();
		handS = new AnimatedShape("MyFettHandVer5.rkm", "MyFettHandVer5.rks");
	}

	public void loadTextures(){
		blueShipT = new TextureImage("blueGhost.png");
		cockpitBlueT = new TextureImage("blueCockpit.png");
		laserT = new TextureImage("laserBolt.png");
		throttleT = new TextureImage("throttleIndicator.png");
		scoreT = new TextureImage("scoreIndicator.png");
		npcT = new TextureImage("DropShipVer4.png");
		asteroid1T = new TextureImage("Object3.png");
		asteroid2T = new TextureImage("Object4.png");
		terrainT = new TextureImage("carpet.png");
		terrainHM = new TextureImage("scribble.jpg");
		handT = new TextureImage("FettArmVer5.png");
	}

	public void buildObjects(){
		Matrix4f initialTranslation, initialRotation, initialScale;

		shipObj = new GameObject(GameObject.root(), cockpitBlueS, cockpitBlueT);
		//shipObj = new GameObject(GameObject.root(), cockpitBlueS);
		//shipObj = new GameObject(GameObject.root());
		//shipObj = new GameObject(GameObject.root(), null, cockpitBlueT);

		tempShip = new GameObject(GameObject.root(), blueShipS, blueShipT);
		tempShip.setLocalLocation(tempShip.getLocalLocation().add(new Vector3f(10,0,0)));

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
			nm = new NodeMaker(this);
			setupInputs();
			setupSceneObjects();
		} catch (Exception e) {
			print("Unable to setup game");
			System.exit(0);
		}
		print("done initializingGame");
	}
	
	public void loadSkyBoxes(){
		System.out.println("loadSkyBoxes");
		spaceBox = (engine.getSceneGraph()).loadCubeMap("spaceBox");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(spaceBox);
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}
	
	protected void setupCamera() {
		getCamera().setLocation(new Vector3f(0f,0f,25f));
	}

	private void setupPhysics() {
		print("setupPhysics()");
		String engine = "tage.physics.JBullet.JBulletPhysicsEngine";
		print("done initializing engine");
		float[] gravity = {0f,1f,0f};

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

	//TODO inputs and controls ,still need to destroy terrain and toggle lights
	//seperate methods for keyboards/gamepads
	protected void setupInputs() throws IOException {
		im = new InputManager();
		playerController = new FlightController(this);
		
		setupAdditionalControls();
	}

	private void setupSceneObjects() throws IOException{
		asteroids = nm.makeAsteroids();
		setupScoreIndicator();
		setupLights();
		setupTerrain();
		setupAnimations();
		throttleController = new ThrottleController(this);
	}

	public void update() {
		livesUpdate();
		updateDeltaTime();
		im.update(deltaTime);
		physicsUpdate();
		playerController.update();
		//updateLights();
		animationUpdate();
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
	
	static int chooseTeam;
	private static void chooseTeam() {
		chooseTeam = JOptionPane.showConfirmDialog(null,  "Join Grey Team? (No Joins Blue Team)", "Blue", JOptionPane.YES_NO_OPTION);
		//0 is yes
		//1 is no
		System.out.println("chooseTeam: " + chooseTeam);
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
			
		createAnimations(sm);
			    	
		setupNetworking();
		
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
 
		//Right Hand
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
		
		
		
		lightHolder.attachChild(headlightNode1);
		
		lightHolder.attachChild(headlightNode2);
		
		lightHolder.attachChild(headlightNode3);
		
		lightHolder.pitch(Degreef.createFrom(-90));
		
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

		//TODO Setup start locations
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
	*/
	
	//TODO Lives Hud still needs to be confirmed by losing a life/all lives
	//private SceneNode[] lives;
	private GameObject[] lives;

	//private Vector3 scorePosition = Vector3f.createFrom(0.8f,-0.3f,-0.8f);
	private Vector3f scorePosition = new Vector3f(0.8f,-0.3f,-0.8f);
	
	//private SceneNode scoreHolder;
	private GameObject scoreHolder;
	//private Vector3[] livesPositions;
	private Vector3f[] livesPositions;
	private void setupScoreIndicator() throws IOException {
		lives = nm.makeScoreIndicators();
		livesPositions = new Vector3f[lives.length];
		
		//scoreHolder = sm.getRootSceneNode().createChildSceneNode("scoreHolder");
		scoreHolder = new GameObject(shipObj);
		scoreHolder.applyParentRotationToPosition(true);
		
		for(int i=0; i<lives.length;i++) {
			livesPositions[i] = lives[i].getLocalLocation();
			//scoreHolder.attachChild(lives[i]);
			lives[i].setParent(scoreHolder);
		}
		
		//shipN.attachChild(scoreHolder);
		//scoreHolder.setParent(shipObj);
		
		//scoreHolder.setLocalPosition(scorePosition);
		scoreHolder.setLocalLocation(scorePosition);
		
		//scoreHolder.roll(Degreef.createFrom(90));
		
		
		//scoreHolder.roll(Degreef.createFrom(90));
		scoreHolder.roll(90);
		//scoreHolder.pitch(Degreef.createFrom(40));
		scoreHolder.pitch(40);
	}
	
	private void livesUpdate() {
		//for(SceneNode n : lives) {
		for(GameObject n : lives) {
			n.setLocalLocation(new Vector3f(10000,10000,10000));
		}
		for(int i=0; i<shipLives;i++) {
			lives[i].setLocalLocation(livesPositions[i]);
		}
		if(shipLives == 0) {
			System.exit(0);
		}
	}

	Light headlight1;
	Light headlight2;
	Light headlight3;

	private GameObject lightHolder;
	private void setupLights(){
		//lightHolder = sm.getRootSceneNode().createChildSceneNode("lightHolder");
		lightHolder = new GameObject(GameObject.root());
		
		//Light headlight1 = sm.createLight("headlight1", Light.Type.SPOT);
		headlight1 = new Light();
		
		//Light headlight1 = Light();
		headlight1.setType(Light.LightType.SPOTLIGHT);

		
		//headlight1.setConeCutoffAngle(Degreef.createFrom(10));
		headlight1.setCutoffAngle(10);
		headlight1.setSpecular(255,255,255);
		headlight1.setRange(10f);

		//SceneNode headlightNode1 = sm.getRootSceneNode().createChildSceneNode("headlightNode1");
		//headlightNode1.attachObject(headlight1);
		
		//Light headlight2 = sm.createLight("headlight2", Light.Type.SPOT);
		headlight2 = new Light();
		headlight2.setType(Light.LightType.SPOTLIGHT);
		//headlight2.setConeCutoffAngle(Degreef.createFrom(10));
		headlight2.setCutoffAngle(10);
		headlight2.setSpecular(255,255,255);
		headlight2.setRange(10f);
		
		
		//SceneNode headlightNode2 = sm.getRootSceneNode().createChildSceneNode("headlightNode2");
		//headlightNode2.attachObject(headlight2);
		
		//Light headlight3 = sm.createLight("headlight3", Light.Type.SPOT);
		headlight3 = new Light();
		headlight3.setType(Light.LightType.SPOTLIGHT);
		//headlight3.setConeCutoffAngle(Degreef.createFrom(10));
		headlight3.setCutoffAngle(10);
		headlight3.setSpecular(255,255,255);
		headlight3.setRange(10f);

		//SceneNode headlightNode3 = sm.getRootSceneNode().createChildSceneNode("headlightNode3");
		//headlightNode3.attachObject(headlight3);
		
		//headlightNode1.setLocalPosition(-0.3f,0,0);
		//headlightNode3.setLocalPosition(0.3f,0,0);
		
		
		
		//lightHolder.attachChild(headlightNode1);
		
		//lightHolder.attachChild(headlightNode2);
		
		//lightHolder.attachChild(headlightNode3);
		
		//lightHolder.pitch(Degreef.createFrom(-90));
		lightHolder.pitch(-90);
		
		//shipN.attachChild(lightHolder);
		lightHolder.setParent(getPlayerShip());
		lightHolder.lookAt(new Vector3f(0,1,0));
		lightHolder.applyParentRotationToPosition(true);
	}

	private void updateLights(){
		Vector3f location = lightHolder.getWorldLocation();
		Vector3f up = shipObj.getLocalUpVector();
		//System.out.println(location + " " + shipObj.getLocalUpVector());
		//print("" + shipObj.getWorldLocation().sub(lightHolder.getWorldLocation()));
		headlight1.setLocation(location);
		//headlight2.setLocation(location);
		//headlight3.setLocation(location);
		headlight1.setDirection(up);
		headlight2.setDirection(up);
		headlight3.setDirection(up);

		print(shipObj.getWorldUpVector() + " " + headlight1.getDirection()[0] + "," + headlight1.getDirection()[1] + ","
		 + headlight1.getDirection()[2]);
	}

	private void setupTerrain(){
		terrain = new GameObject(GameObject.root(), terrainS, terrainT);
		terrain.setHeightMap(terrainHM);
		terrain.up(-100);
		Matrix4f scale = (new Matrix4f()).scaling(1000,50,1000);
		terrain.setLocalScale(scale);
	}

	private void setupAnimations(){
		handS.loadAnimation("throttleUpAndPause", "ThrustUpAndPause.rka");
		handS.loadAnimation("throttleDownAndPause", "ThrustDownAndPause.rka");
		handS.loadAnimation("throttleBackFromUp", "FromUp_GoDown_andPause.rka");
		handS.loadAnimation("throttleBackFromDown", "FromDown_GoUp_andPause.rka");
		hand = new GameObject(shipObj, handS, handT);
		hand.up(-0.5f);
		hand.left(-0.7f);
		hand.applyParentRotationToPosition(true);
	}

	private void animationUpdate(){
		throttleController.update();
		handS.updateAnimation();
	}

	/*
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
		
		lightHolder.attachChild(headlightNode1);
		
		lightHolder.attachChild(headlightNode2);
		
		lightHolder.attachChild(headlightNode3);
		
		lightHolder.pitch(Degreef.createFrom(-90));
		
		shipN.attachChild(lightHolder);
	}*/
	

	/*
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
	*/

	private void setupAdditionalControls() {
		ArrayList<Controller> controllers = im.getControllers();
		ArrayList<String> keyboards = new ArrayList<String>();
		
		for (int i = 0; i < controllers.size(); i++) {
			if (controllers.get(i).getType() == Controller.Type.KEYBOARD)
				keyboards.add(controllers.get(i).getName());
		}
		
		DT = new DestroyTerrain();
		TL = new ToggleLights();
		
		for (int i = 0; i < keyboards.size(); i++) {
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.L, TL,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			im.associateAction(keyboards.get(i), net.java.games.input.Component.Identifier.Key.U, DT,
					InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		}
	}

	/*
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
	//TODO update
	public void update() {
	//protected void update(Engine engine) {

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
	*/
	
	private float animationSpeed = 4f;
	public void throttleUpAndPauseAnimation()
	{ 
		handS.playAnimation("throttleUpAndPause", animationSpeed, AnimatedShape.EndType.PAUSE, 0);
	}
	
	public void throttleDownAndPauseAnimation()
	{ 
		handS.playAnimation("throttleDownAndPause", animationSpeed, AnimatedShape.EndType.PAUSE, 0);
	}
	
	public void throttleBackFromUpAnimation()
	{
		handS.playAnimation("throttleBackFromUp", animationSpeed, AnimatedShape.EndType.PAUSE, 0);
	}
	
	public void throttleBackFromDownAnimation()
	{
		handS.playAnimation("throttleBackFromDown", animationSpeed, AnimatedShape.EndType.PAUSE, 0);
	}

	private class DestroyTerrain extends AbstractInputAction {

		@Override
		public void performAction(float arg0, Event e) {
			terrain.up(-10000);
		}
	}

	//TODO implement this hotkey
	private class ToggleLights extends AbstractInputAction {
		
		private boolean on = true;
		@Override
		public void performAction(float arg0, Event e) {
			print("Toggle Lights");
			/*if(on) {
				lightHolder.setLocalPosition(10000,10000,10000);
				on=false;
			}
			else {
				lightHolder.setLocalPosition(0,0,0);
				on=true;
			}
			*/
		}
	}

	/*
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

	*/
	
	/*
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

	*/

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
	}

	long elapsedTime;
	long prevTime;
	long diffTime;
	private void updateDeltaTime(){
		diffTime = System.currentTimeMillis() - prevTime;
		prevTime = System.currentTimeMillis();
		deltaTime = (float)diffTime/1000;
	}

	private void physicsUpdate(){
		Matrix4f mat = new Matrix4f();
		Matrix4f mat2 = new Matrix4f().identity();
		//TODO check for collision
		//checkForCollisions();
		physicsEngine.update((float)diffTime);
		for (GameObject go:engine.getSceneGraph().getGameObjects())
		{
			if (go.getPhysicsObject() != null)
			{
				mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
				mat2.set(3,0,mat.m30()); mat2.set(3,1,mat.m31()); mat2.set(3,2,mat.m32());
				go.setLocalTranslation(mat2);
			}
		}
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
	public NodeMaker getNodeMaker() { return nm; }
	public GameObject getPlayerShip() { return shipObj; }
	public ObjShape getLaserShape() { return laserS; }
	public ObjShape getThrottleShape() { return throttleS; }
	public ObjShape getScoreShape() { return scoreS; }
	public ObjShape getNPCShape() { return laserS; }
	public ObjShape getAsteroid1Shape() { return asteroid1S; }
	public ObjShape getAsteroid2Shape() { return asteroid2S; }
	public TextureImage getLaserTexture() { return laserT; }
	public TextureImage getThrottleTexture() { return throttleT; }
	public TextureImage getScoreTexture() { return scoreT; }
	public TextureImage getNPCTexture() { return laserT; }
	public TextureImage getAsteroid1Texture() { return asteroid1T; }
	public TextureImage getAsteroid2Texture() { return asteroid2T; }
}