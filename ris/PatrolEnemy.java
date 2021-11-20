package ris;

import java.io.IOException;

//import ris.MyGame;
//import a3.SceneCreation.NodeMaker;
//import a3.myGameEngine.VectorMath;
/*
import ray.ai.behaviortrees.BTAction;
import ray.ai.behaviortrees.BTCompositeType;
import ray.ai.behaviortrees.BTSelector;
import ray.ai.behaviortrees.BTSequence;
import ray.ai.behaviortrees.BTStatus;
import ray.ai.behaviortrees.BTCondition;
import ray.ai.behaviortrees.BehaviorTree;
import ray.physics.PhysicsEngine;
import ray.rage.Engine;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
*/

import tage.*;
import tage.ai.behaviortrees.*;

import org.joml.*;

//controls a Node that orbits a position and then chases enemies until too far away from defense position or enemy too far away.

public class PatrolEnemy {
	
	MyGame game;
	NodeMaker nm;
	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
		
	float thinkStartTime;
	float tickStartTime;
	float lastThinkUpdateTime;
	float lastTickUpdateTime;
		
	float radius = 5;
	float defenseTether = 150;
	float enemyTether = 150;
	
	float chaseSpeed;
	float returnSpeed;
	
	float hitRange = 1.5f;
		
	boolean returning = false;
	boolean chasing = false;
	
	//SceneNode[] possibleTargets;
	GameObject[] possibleTargets;
	//SceneNode[] lasers;
	GameObject[] lasers;
		
	//SceneNode npc;
	GameObject npc;
	//SceneNode target; //might replace with a position to guard
	GameObject target; //might replace with a position to guard
	
	PatrolStrategyContext context;
	
	//public PatrolEnemy(SceneNode n, MyGame g, Vector3 p) throws IOException {
	public PatrolEnemy(GameObject n, MyGame g, Vector3f p) throws IOException {
		game = g;
		npc = n;
		//nm = new NodeMaker(g.getEngine(),g.getSceneManager(),g.getPhysicsEngine());
		nm = game.getNodeMaker();
		setupPE();
		context = new PatrolStrategyContext(this,n,p,lasers);
		
	}
	
	private void setupPE() throws IOException {
		getLasers();
		setupBehaviorTree();
	}
	
	
	public void getLasers() throws IOException {
		//lasers = nm.makeNPCLasers(npc.getName());
		lasers = nm.makeNPCLasers();
	}
	
	public void update(float time) {
		float deltaTime = time/1000;
		//context.execute(deltaTime);
		context.execute(time);
		bt.update(time);
		//context.execute(deltaTime);
		
	}
		
	private void setupBehaviorTree() {
		bt.insertAtRoot(new BTSelector(10));
		
		bt.insert(10, new BTSequence(11));
		bt.insert(11, new ReturnCheck(false));//condition
		bt.insert(11, new ReturnAction());   //action
		
		bt.insert(10, new BTSequence(12));
		bt.insert(12, new ChaseCheck(false));//condition
		bt.insert(12,  new ChaseAction());  //action
		
		bt.insert(10, new BTSequence(13));
		bt.insert(13, new PatrolCheck(false));//condition
		bt.insert(13, new PatrolAction());   //action
	}
	
	//public void setTargets(SceneNode[] targets) {
	public void setTargets(GameObject[] targets) {
		possibleTargets = targets;
	}
		
	private class ReturnCheck extends BTCondition{

		public ReturnCheck(boolean toNegate) {
			super(toNegate);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean check() {
			
			//print("return Check");
			
			//print("return check: " + returning);
			//if the patroller is currently returning return true
			if(returning) return true;
			
			return false;
		}
	}
		
	private class ReturnAction extends BTAction{

		@Override
		protected BTStatus update(float arg0) {
			// TODO Auto-generated method stub
			//System.out.println("return Action");
			//check and see if we should change to PatrolPatrolStrategy
			//print("return action");
			if(!context.stillReturning()) {
				//print("returning complete");
				returning = false;
				context.patrolHome();
			}
			return BTStatus.BH_SUCCESS;
		}
	}
	
	private class ChaseCheck extends BTCondition{

		public ChaseCheck(boolean toNegate) {
			super(toNegate);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean check() {
			if(chasing) return true;

			return false;
		}
	}
	
	private class ChaseAction extends BTAction{

		@Override
		protected BTStatus update(float arg0) {
			print("chase Action");
			//Check and see if we should change to PatrolReturnStrategy
			
			if(!context.stillChasing()) {
				chasing = false;
				returning = true;
				context.returnHome();
			}
			
			return BTStatus.BH_SUCCESS;
		}
	}
	
	public void npcHit() {
		chasing = false;
		returning = true;
		context.returnHome();
		game.hit();
	}
	
	private class PatrolCheck extends BTCondition{

		public PatrolCheck(boolean toNegate) {
			super(toNegate);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean check() {
			//print("patrol Check");
			// TODO Auto-generated method stub
			
			if(!chasing && !returning) return true;
			
			return false;
		}
	}
	
	private class PatrolAction extends BTAction{

		@Override
		protected BTStatus update(float arg0) {
			// TODO Auto-generated method stub
			//check if an enemy is within range to begin chasing
			//System.out.println("patrol Action");
			
			//for(SceneNode target:possibleTargets) {
			for(GameObject target:possibleTargets) {
				//if(VectorMath.distance(target.getWorldPosition(), npc.getWorldPosition()) < enemyTether) {
				if(VectorMath.distance(target.getWorldLocation(), npc.getWorldLocation()) < enemyTether) {
					context.chaseEnemy(target);
					print("CHASING");
					chasing = true;
					return BTStatus.BH_SUCCESS;
				}
			}
			
			return BTStatus.BH_SUCCESS;
		}
	}
	
	private void print(String s) {
		System.out.println(s);
	}
}

