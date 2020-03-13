package game.world.obj.activate;

import java.awt.geom.Point2D.Double;

import game.world.obj.entity.ActivateMovingPlatform;

public class OneUseMovePlatform extends ActivateMovingPlatform implements Activatable{

	public OneUseMovePlatform(int width, int height, double speed, Double start, Double end){
		super(width, height, speed, start, end, null);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		if(getLoc().distance(end) <= move.getAmount()){
			setX(end.x);
			setY(end.y);
			move.setAmount(0);
			removeVector(move);
		}
	}
	
	@Override
	public void start(){}

	@Override
	public void activate(){
		started = true;
		start = end;
	}
	
}
