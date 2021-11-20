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
import tage.audio.*;

import tage.networking.*;

import tage.input.*;

import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.PhysicsEngineFactory;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

import net.java.games.input.*;
import tage.input.action.AbstractInputAction;

import java.io.*;
import java.util.*;
import java.util.UUID;
import java.net.InetAddress;

import java.net.UnknownHostException;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import tage.networking.IGameConnection.ProtocolType;

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
	private boolean isOnline;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isConnected;
	private Vector<UUID> gameObjectsToRemove;

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
					 asteroid1S, asteroid2S, earthS;
	
	private TextureImage stuff, things, cockpitBlueT, blueShipT, laserT, throttleT, scoreT, npcT,
					 asteroid1T, asteroid2T, earthT;
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

	private IAudioManager audioMgr;
	private Sound backgroundMusic, stationSound, NPCSound, laserFireSound, shipNoiseSound;

	public MyGame(){
		super();
		isOnline = false;
	}

	public MyGame(String serverAddr, int sPort) {
		super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.UDP;
		isOnline = true;
	}

	public static void main(String[] args){
		MyGame game;
		if(args.length > 0){
			game = new MyGame(args[0], Integer.parseInt(args[1]));
		}
		else{
			game = new MyGame();
		}
		System.out.println("main");

		engine = new Engine(game,1400,1000);
		game.initializeSystem();
		game.game_loop();
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
		handS = new AnimatedShape("MyFettHandVer5.rkm", "MyFettHandVer5.rks");
		earthS = new Sphere();
		terrainS = new TerrainPlane();
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
		handT = new TextureImage("FettArmVer5.png");
		terrainT = new TextureImage("carpet.png");
		terrainHM = new TextureImage("scribble.jpg");
		earthT = new TextureImage("earth.jpg");
	}

	public void buildObjects(){

		GameObject earth = new GameObject(GameObject.root(), earthS, earthT);
		//0earth.setHeightMap(terrainHM);

		Matrix4f initialTranslation, initialRotation, initialScale;

		shipObj = new GameObject(GameObject.root(), cockpitBlueS, cockpitBlueT);
		//shipObj = new GameObject(GameObject.root(), cockpitBlueS);
		//shipObj = new GameObject(GameObject.root());
		//shipObj = new GameObject(GameObject.root(), null, cockpitBlueT);

		//tempShip = new GameObject(GameObject.root(), blueShipS, blueShipT);
		//tempShip.setLocalLocation(tempShip.getLocalLocation().add(new Vector3f(10,0,0)));

		initialTranslation = (new Matrix4f()).translation(0,0,0);
		initialScale = (new Matrix4f()).scaling(1f);
		//blueShipO.setLocalTranslation(initialTranslation);
		shipObj.setLocalTranslation(initialTranslation);

		//blueShipO.setLocalScale(initialScale);
		shipObj.setLocalScale(initialScale);
	}
	
	public void initializeGame() {
		sceneGraph = getSceneGraph();
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

	protected void setupInputs() throws IOException {
		im = new InputManager();
		playerController = new FlightController(this);
		
		setupAdditionalControls();
	}

	private void setupSceneObjects() throws IOException{
		print("setupSceneObjects");
		asteroids = nm.makeAsteroids();
		setupPatrolNPC();
		setupScoreIndicator();
		setupLights();
		setupTerrain();
		setupAnimations();
		throttleController = new ThrottleController(this);
		//setupAudio();
		setupNetworking();
	}

	public void update() {
		livesUpdate();
		updateDeltaTime();
		im.update(deltaTime);
		physicsUpdate();
		playerController.update();
		updateLights();
		animationUpdate();
		//audioUpdate();
		//npcUpdates();
		processNetworking();
	}
	
	private void setupNetworking() {
		if(!isOnline) return;
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
	
	float networkTimer = 0;
	float tickRate = 100;
	private void processNetworking() {
		if(!isOnline) return;

		networkTimer += deltaTime;
		
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
	

	PatrolEnemy[] patrolEnemies;
	private void setupPatrolNPC() throws IOException{
		patrolEnemies = new PatrolEnemy[10];
		
		Vector3f position1 = new Vector3f(20,35,145);
		patrolEnemies[0] = npcFactory(position1);
		
		Vector3f position2 = new Vector3f(0,27,145);
		//patrolEnemies[1] = npcFactory(position2);
		
		Vector3f position3 = new Vector3f(-20,27,145);
		//patrolEnemies[2] = npcFactory(position3);
	}
	
	private PatrolEnemy npcFactory(Vector3f position) throws IOException {
		GameObject npc = nm.makeNPC(position);
		PatrolEnemy pe = new PatrolEnemy(npc,this,position);
		GameObject[] targets = new GameObject[] {shipObj};
		pe.setTargets(targets);
		return pe;
	}
	
	private void npcUpdates() {
		for(PatrolEnemy p : patrolEnemies) {
			if(p!=null) {
				p.update(deltaTime);
			}
		}
	}
	
	private GameObject[] lives;

	private Vector3f scorePosition = new Vector3f(0.8f,-0.3f,-0.8f);
	
	private GameObject scoreHolder;
	private Vector3f[] livesPositions;
	private void setupScoreIndicator() throws IOException {
		lives = nm.makeScoreIndicators();
		livesPositions = new Vector3f[lives.length];
		
		scoreHolder = new GameObject(shipObj);
		scoreHolder.applyParentRotationToPosition(true);
		
		for(int i=0; i<lives.length;i++) {
			livesPositions[i] = lives[i].getLocalLocation();
			lives[i].setParent(scoreHolder);
		}
		
		scoreHolder.setLocalLocation(scorePosition);
		scoreHolder.roll(90);
		scoreHolder.pitch(40);
		scoreHolder.pitch(-90);
	}
	
	private void livesUpdate() {
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
		lightHolder = new GameObject(GameObject.root());
		
		headlight1 = new Light();
		
		headlight1.setType(Light.LightType.SPOTLIGHT);

		headlight1.setCutoffAngle(10);
		headlight1.setSpecular(100,100,100);
		headlight1.setRange(10f);

		headlight2 = new Light();
		headlight2.setType(Light.LightType.SPOTLIGHT);
		headlight2.setCutoffAngle(10);
		headlight2.setSpecular(255,255,255);
		headlight2.setRange(10f);
		
		headlight3 = new Light();
		headlight3.setType(Light.LightType.SPOTLIGHT);
		headlight3.setCutoffAngle(10);
		headlight3.setSpecular(255,255,255);
		headlight3.setRange(10f);

		lightHolder.pitch(-90);
		
		lightHolder.setParent(getPlayerShip());
		lightHolder.lookAt(new Vector3f(0,1,0));
		lightHolder.applyParentRotationToPosition(true);

		sceneGraph.addLight(headlight1);
		sceneGraph.addLight(headlight2);
		sceneGraph.addLight(headlight3);
	}

	Vector3f lightsOffset = new Vector3f(0,0,0);
	private void updateLights(){
		headlight1.setLocation(shipObj.getWorldLocation());
		headlight1.setDirection(shipObj.getWorldForwardVector().add(lightsOffset));
		/*
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
		*/
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
		Matrix4f scale  = (new Matrix4f()).scaling(0.5f,0.5f,0.5f);
		hand.setLocalScale(scale);
		hand.up(-1);
		hand.left(-1f);
		hand.applyParentRotationToPosition(true);
	}

	private void animationUpdate(){
		throttleController.update();
		handS.updateAnimation();
	}

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

	//TODO ghost avatar
	public void setupGhostAvatar(GhostAvatar ghost, int team) throws IOException {
		GameObject ghostN;
		ghostN = new GameObject(GameObject.root(), blueShipS, blueShipT);
		
		ghostN.yaw(180);
		
		float mass = 1.0f;
		float up[] = {0,1,0};
		double[] temptf;
		double[] tempTransform;

		Matrix4f translation = new Matrix4f(ghostN.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		PhysicsObject ghostPhysObj = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass, tempTransform, 1.0f);
		ghostN.setPhysicsObject(ghostPhysObj);
		
		ghost.setNode(ghostN);
	}
	
	//private SceneNode makeGreyGhost(SceneNode ghostN, GhostAvatar ghost) throws IOException {
	private GameObject makeGreyGhost(GameObject ghostN, GhostAvatar ghost) throws IOException {
		//Entity shipE = sm.createEntity("ghostShip" + ghost.getID() , "GhostShips-f.obj");
		//shipE.setPrimitive(Primitive.TRIANGLES);

		//SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
		//ghostN = sm.getRootSceneNode().createChildSceneNode(shipE.getName() + "Node");
		//ghostN.attachObject(shipE);
		//ghostN = new GameObject(GameObject.root(), npcS, npcT);
		return ghostN;
	}
	/*
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
		npcUpdates();
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
	*/
	
	Vector3f startPositionZero = new Vector3f(0,35,-90);
	Vector3f startPositionOne = new Vector3f(0,35,380);
	public void hit() {
		//Return player to starting position based on team
		
		shipLives--;
		
		/*
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
		*/
	}
	
	public void shootNetworking() {
		if(protClient!=null) {
			protClient.sendShootMessage();
		}
	}

	public void setIsConnected(boolean b) { isConnected = b; }
	
	public Vector3f getPlayerPosition() {
		return getPlayerShip().getWorldLocation();
	}
	
	public Vector3f getPlayerDirection() {
		return new Vector3f(shipObj.getPhysicsObject().getLinearVelocity());
	}
	
	public int getPlayerTeam() {
		return 1;
	}
	
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

	private class ToggleLights extends AbstractInputAction {
		
		private boolean on = true;
		@Override
		public void performAction(float arg0, Event e) {
			if(on) {
				lightsOffset = new Vector3f(10000,10000,10000);
				on=false;
			}
			else {
				lightsOffset = new Vector3f(0,0,0);
				on=true;
			}
			
		}
	}

	private void setupAudio()
	{ 
		print("initAudio setup");
		AudioResource theMusic, theStation, npcBeeps, theShipNoise, theFrickinLasers;
		audioMgr = AudioManagerFactory.createAudioManager("tage.audio.joal.JOALAudioManager");
		
		print("audioMgr: " + audioMgr);
		if (!audioMgr.initialize()) {
			System.out.println("The Audio Manager failed to initialize!");
			return;
		}
		
		theMusic = audioMgr.createAudioResource("assets/sounds/bensound-epic.wav", AudioResourceType.AUDIO_SAMPLE);
		theStation = audioMgr.createAudioResource("assets/sounds/Cartoon-warp-02.wav", AudioResourceType.AUDIO_SAMPLE);
		npcBeeps = audioMgr.createAudioResource("assets/sounds/Robot_blip-Marianne_Gagnon-120342607.wav", AudioResourceType.AUDIO_SAMPLE);
		theShipNoise = audioMgr.createAudioResource("assets/sounds/RocketThrusters-SoundBible.com-1432176431.wav", AudioResourceType.AUDIO_SAMPLE);
		theFrickinLasers = audioMgr.createAudioResource("assets/sounds/LaserBlasts-SoundBible.com-108608437.wav", AudioResourceType.AUDIO_SAMPLE);
		
		backgroundMusic = new Sound(theMusic, SoundType.SOUND_MUSIC, 1, true);
		stationSound = new Sound(theStation, SoundType.SOUND_EFFECT, 400, true);
		NPCSound = new Sound(npcBeeps, SoundType.SOUND_EFFECT, 20, true);
		laserFireSound = new Sound(theFrickinLasers, SoundType.SOUND_EFFECT, 2, false);
		shipNoiseSound = new Sound(theShipNoise, SoundType.SOUND_EFFECT, 10, true);
		
		backgroundMusic.initialize(audioMgr);
		backgroundMusic.setMaxDistance(10.0f);
		backgroundMusic.setMinDistance(0.5f);
		backgroundMusic.setRollOff(5.0f);

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
		
		setEarParameters();
		
		shipNoiseSound.play();
		//backgroundMusic.play();
		//stationSound.play();
	}

	private void audioUpdate(){
		laserFireSound.setLocation(shipObj.getWorldLocation());
		setEarParameters();
	}

	private void setEarParameters()
	{
		audioMgr.getEar().setLocation(shipObj.getWorldLocation());
		audioMgr.getEar().setOrientation(getCamera().getN(), new Vector3f(0,1,0));
	}
	
	public void playFireSound()
	{
		//TODO turn this back on to hear laser sounds
		//laserFireSound.play();
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
	public GameObject getTerrain() { return terrain; }
	public ObjShape getLaserShape() { return laserS; }
	public ObjShape getThrottleShape() { return throttleS; }
	public ObjShape getScoreShape() { return scoreS; }
	public ObjShape getNPCShape() { return npcS; }
	public ObjShape getAsteroid1Shape() { return asteroid1S; }
	public ObjShape getAsteroid2Shape() { return asteroid2S; }
	public TextureImage getLaserTexture() { return laserT; }
	public TextureImage getThrottleTexture() { return throttleT; }
	public TextureImage getScoreTexture() { return scoreT; }
	public TextureImage getNPCTexture() { return npcT; }
	public TextureImage getAsteroid1Texture() { return asteroid1T; }
	public TextureImage getAsteroid2Texture() { return asteroid2T; }
}