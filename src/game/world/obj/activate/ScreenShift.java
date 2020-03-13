package game.world.obj.activate;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;

public class ScreenShift extends GameObject implements Activatable{
	
	private Level containerLevel;
	private GameObject previousFocus;
	
	private Point2D.Double endLook;
	
	private Vector2D move;
	
	private int started;
	private int waitTime;
	
	public ScreenShift(Point2D.Double startLook, Point2D.Double endLook, double speed, int waitTime, Level containerLevel, GameObject previousFocus){
		super(startLook.x, startLook.y, 0, 0, MIN_RENDER_PRIORITY, false);
		
		this.containerLevel = containerLevel;
		this.previousFocus = previousFocus;
		
		this.endLook = endLook;
		move = new Vector2D(speed, Math.toDegrees(Math.PI + Math.atan2(startLook.y - endLook.y, startLook.x - endLook.x)));
		
		started = -99;
		this.waitTime = waitTime;
	}
	
	public boolean isStarted(){
		return started != -99;
	}
	
	@Override
	public void activate(){
		started = waitTime;
		containerLevel.setCameraObject(this);
	}
	
	public void end(){
		containerLevel.setCameraObject(previousFocus);
		containerLevel.removeObject(this);
	}
	
	@Override
	protected void tickOverride(){
		if(started > 0){
			if(getLoc().distance(endLook) > move.getAmount()){
				addX(move.getX());
				addY(move.getY());
			}
			else{
				setX(endLook.x);
				setY(endLook.y);
				started--;
				if(started <= 0) end();
			}
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){}
	
}
