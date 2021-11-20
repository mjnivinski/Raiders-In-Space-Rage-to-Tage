package ris;

import tage.*;
import tage.physics.PhysicsObject;
import org.joml.*;

public class PatrolPatrolStrategy implements PatrolStrategy {

	//SceneNode npc;
	GameObject npc;
	//SceneNode target; //might replace target with position
	GameObject target; //might replace target with position
	PhysicsObject phys;
	Vector3f position;
	float radius;
	//public PatrolPatrolStrategy(SceneNode n, SceneNode t, float r) {
	public PatrolPatrolStrategy(GameObject n, Vector3f p, float r) {
		npc = n;
		//target = t;
		position = p;
		radius = r;
	}
	
	//public PatrolPatrolStrategy(SceneNode n, Vector3 p) {
	public PatrolPatrolStrategy(GameObject n, Vector3f p) {
		npc = n;
		position = p;
		radius = 5f;
		phys = npc.getPhysicsObject();
	}
	
	@Override
	public void move(float deltaTime) {
		System.out.println("move in patrolpatrolStrategy");
		Vector3f towardPosition = new Vector3f(0,0,0);
		System.out.println(npc + " " + position + " " + phys);

		position.sub(npc.getWorldLocation(),towardPosition);
		phys.setLinearVelocity(toFloatArray(towardPosition));
		//move npc around target at Z = 0
	}

	private float[] toFloatArray(Vector3f v){
		float[] array = {v.x(), v.y(), v.z()};
		return array;
	}
}
