package ris;

/*import ray.physics.PhysicsObject;
import ray.rage.scene.SceneNode;
import ray.rml.Vector3;
*/
import tage.*;
import tage.physics.*;

import org.joml.*;

import java.util.Arrays;

//import a3.myGameEngine.VectorMath;

public class PatrolChaseStrategy implements PatrolStrategy {

	private PatrolEnemy patrolEnemy;
	//private SceneNode npc;
	private GameObject npc;
	//private SceneNode target;
	private GameObject target;
	private PhysicsObject npcPhys;
	private float power = 10f;
	
	//SceneNode[] lasers;
	GameObject[] lasers;
	
	private float chaseSpeed = 0.05f;
	float hitRange = 3f;
	
	//public PatrolChaseStrategy(PatrolEnemy pe, SceneNode n, SceneNode t, SceneNode[] ls) {
	public PatrolChaseStrategy(PatrolEnemy pe, GameObject n, GameObject t, GameObject[] ls) {
		patrolEnemy = pe;
		npc = n;
		target = t;
		npcPhys = npc.getPhysicsObject();
		lasers = ls;
	}
	
	@Override
	public void move(float deltaTime) {
		System.out.println("CHASE MOVING");
		
		//Vector3 start = npc.getWorldPosition();
		Vector3f start = npc.getWorldLocation();
		//Vector3 end = target.getWorldPosition();
		Vector3f end = target.getWorldLocation();
		
		//Vector3 direction = end.sub(start);
		Vector3f direction = end.sub(start);
		
		direction = direction.mul(chaseSpeed);
		
		npcPhys.setLinearVelocity(toFloatArray(direction));
		
		shooting(deltaTime);
		laserCheck();
	}
	
	private int shootCycle = 0;
	float timeSinceLastShot = 0;
	float fireRate = 1.0f;
	
	private void shooting(float deltaTime) {
		timeSinceLastShot += deltaTime;
		if(timeSinceLastShot > 1/fireRate) {
			shoot();
		}
	}
	
	private float laserSpeed = 300;
	
	private void shoot() {
	
		timeSinceLastShot = 0;
		
		PhysicsObject p1 = lasers[shootCycle].getPhysicsObject();
		
		//Vector3 to = target.getWorldPosition();
		Vector3f to = target.getWorldLocation();
		//Vector3 from = npc.getWorldPosition();
		Vector3f from = npc.getWorldLocation();
		//Vector3 start = to.sub(from).normalize();
		Vector3f start = to.sub(from).normalize();
		//Vector3 forward = start;
		Vector3f forward = start;
		
		double[] d1 = p1.getTransform();
		
		d1[12] = start.x() + from.x();
		d1[13] = start.y() + from.y();
		d1[14] = start.z() + from.z();
		
		p1.setTransform(d1);
		
		//p1.setLinearVelocity(forward.mul(laserSpeed).toFloatArray());
		p1.setLinearVelocity(toFloatArray(forward.mul(laserSpeed)));
		
		shootCycle += 1;
		shootCycle%=(lasers.length);
	}
	
	//checks for collision between npc laser and player ship
	private void laserCheck() {
		//for(SceneNode l : lasers) {
		for(GameObject l : lasers) {
			if(VectorMath.distance(l.getWorldLocation(), target.getWorldLocation()) < hitRange) {
				patrolEnemy.npcHit();
				break;
			}
		}
	}

	private float[] toFloatArray(Vector3f v){
		float[] array = {v.x(), v.y(), v.z()};
		return array;
	}
}
