package game.world.obj.entity.projectile;

import java.util.ArrayList;

import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.entity.GameEntity;
import game.world.obj.entity.enemy.Enemy;

public abstract class Projectile extends GameEntity{
	
	/**
	 * The direction and speed of this projectile
	 */
	protected Vector2D path;
	/**
	 * The level this projectile is in
	 */
	protected Level containerLevel;
	
	public Projectile(double x, double y, double width, double height, int renderPriority, Vector2D path, Level containerLevel){
		super(x, y, width, height, renderPriority, false);
		this.path = path;
		this.containerLevel = containerLevel;
		
		addVector(path);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		
		if(testEndPath()) endPath();
	}
	
	/**
	 * @return true if this object should stop its path and do its effect, false if it should continue to move
	 */
	protected abstract boolean testEndPath();
	
	/**
	 * This method should perform the action this projectile should do when it ends its path. Call super to remove the object from the level
	 */
	protected void endPath(){
		containerLevel.removeObject(this);
	}
	
	/**
	 * @param l
	 * @return The nearest enemy (based on the centers of this projective and the enemy) that intersects this projecitle. 
	 * If no enemy intersects this projective then null is returned.
	 */
	public Enemy getContainingEnemy(Level l){
		Enemy e = null;
		int near = -1;
		ArrayList<GameObject> objs = l.getIntersectingObjects(this);
		for(int i = 0; i < objs.size(); i++){
			if(objs.get(i) instanceof Enemy && (near == -1 || getCenter().distance(objs.get(i).getCenter()) < getCenter().distance(objs.get(near).getCenter()))) near = i;
		}
		if(near != -1) e = (Enemy)(objs.get(near));
		return e;
	}
	
}
