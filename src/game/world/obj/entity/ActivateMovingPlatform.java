package game.world.obj.entity;

import java.awt.geom.Point2D;

import game.world.obj.GameObject;

public class ActivateMovingPlatform extends MovingPlatform{

	private GameObject object;
	
	protected boolean started;
	
	/**
	 * Creates a moving platform that will not start moving until the given object collides with this object
	 * @param width
	 * @param height
	 * @param speed
	 * @param start
	 * @param end
	 * @param object
	 */
	public ActivateMovingPlatform(int width, int height, double speed, Point2D.Double start, Point2D.Double end, GameObject object){
		super(width, height, speed, start, end);
		this.object = object;
		
		started = false;
	}
	
	@Override
	protected void tickOverride(){
		if(started) super.tickOverride();
	}
	
	@Override
	public void sideHit(GameObject obj){
		super.sideHit(obj);
		if(obj != null && object != null && !started && obj.getUUID() == object.getUUID()) start();
	}
	
	@Override
	public void botHit(GameObject obj){
		super.botHit(obj);
		if(!started && obj.getUUID() == object.getUUID()) start();
	}
	
	@Override
	public void topHit(GameObject obj){
		super.topHit(obj);
		if(!started && object != null && obj != null && obj.getUUID() == object.getUUID()) start();
	}
	
	public void start(){
		started = true;
	}
}
