package game.world.obj.entity;

import java.util.ArrayList;

import game.util.calc.Vector2D;
import game.world.obj.GameObject;

public abstract class GameEntity extends GameObject{
	
	public static final double GRAVITY_CONSTANT = 0.06;
	
	protected Vector2D gravity;
	protected ArrayList<Vector2D> vectors;
	
	public GameEntity(double x, double y, double width, double height, int renderPriority, boolean collide){
		super(x, y, width, height, renderPriority, collide);
		
		gravity = new Vector2D(0, 90);
		
		vectors = new ArrayList<Vector2D>();
		vectors.add(gravity);
		
		this.collide = collide;
	}
	
	@Override
	protected void tickOverride(){
		move();
		
		addGravity();
	}
	
	protected void move(){
		Vector2D add = getTotalVector();
		
		addX(add.getX());
		addY(add.getY());
	}
	
	public Vector2D getTotalVector(){
		Vector2D add = new Vector2D(0, 0);
		for(int i = 0; i < vectors.size(); i++){
			add = Vector2D.addVectors(add, vectors.get(i));
		}
		return add;
	}
	
	public ArrayList<Vector2D> getVectors(){
		return vectors;
	}
	
	public void addVector(Vector2D v){
		vectors.add(v);
	}
	
	public void removeVector(Vector2D v){
		removeVector(v.getUUID());
	}
	
	public void removeVector(int UUID){
		for(int i = 0; i < vectors.size(); i++){
			if(vectors.get(i).getUUID() == UUID){
				vectors.remove(i);
				return;
			}
		}
	}
	
	public boolean hasVector(Vector2D v){
		return hasVector(v.getUUID());
	}
	
	public boolean hasVector(int UUID){
		for(int i = 0; i < vectors.size(); i++) if(vectors.get(i).getUUID() == UUID) return true;
		return false;
	}
	
	public void addGravity(){
		gravity.addAmount(GRAVITY_CONSTANT);
	}
	
	public double getGravityAmount(){
		return gravity.getAmount();
	}
	
	public void resetGravity(){
		gravity.setAmount(0);
		gravity.setAngleDegrees(90);
	}
	
	@Override
	public void hitFloor(){
		super.hitFloor();
		resetGravity();
	}
}
