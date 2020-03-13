package game.world.obj.entity.enemy;

import java.awt.geom.Rectangle2D;

import game.world.levels.Level;
import game.world.obj.entity.GameEntity;

public abstract class Enemy extends GameEntity{
	
	private int health;
	
	protected Level containerLevel;
	
	public Enemy(double x, double y, double width, double height, int renderPriority, int health, boolean collide, Level containerLevel){
		super(x, y, width, height, renderPriority, collide);
		
		this.health = health;
		
		this.containerLevel = containerLevel;
	}
	
	protected void collide(){
		collideWithTiles(containerLevel);
		collideWithObjects(containerLevel);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		testDead();
		
		collide();
	}
	
	/**
	 * @return The hit box of this enemy, used for determining if projectiles hit them
	 */
	public Rectangle2D.Double getHitBox(){
		return getBounds();
	}
	
	/**
	 * Tests to see if this enemy is dead, if it is dead it is killed
	 * @return 
	 */
	public boolean testDead(){
		if(health <= 0){
			kill(containerLevel);
			return true;
		}
		return false;
	}
	
	public void takeDamage(int damage){
		health -= damage;
		testDead();
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void kill(Level l){
		l.removeObject(this);
	}
	
}
