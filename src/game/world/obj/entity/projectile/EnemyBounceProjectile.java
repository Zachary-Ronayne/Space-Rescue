package game.world.obj.entity.projectile;

import java.awt.Graphics2D;

import game.libs.Images;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.entity.Player;

public class EnemyBounceProjectile extends Projectile{

	public static final int MAX_TIME = 120;
	public static final double SIZE = 6;
	
	private Player player;
	
	private int timer;
	
	public EnemyBounceProjectile(double x, double y, double angle, Level containerLevel, Player player){
		super(x, y, SIZE, SIZE, (int)(MAX_RENDER_PRIORITY * .8), new Vector2D(3.5, angle), containerLevel);
		this.player = player;
		timer = MAX_TIME;
	}

	@Override
	protected void tickOverride(){
		super.tickOverride();
		timer--;
		
		if(getBounds().intersects(player.getHitBox())){
			player.dealDamage(1);
			endPath();
		}
		
		GameObject in = getInSolidObject(containerLevel);
		if(in != null){
			collideWithObjects(containerLevel);
			collideWithTiles(containerLevel);
			
			double a = getAngleSlope(in);
			double m = getTotalVector().getDegrees();
			path.setAngleDegrees(-Math.abs(a - m));
			path.setAmount(path.getAmount() * .9);
		}
		
		if(path.getAmount() < .3) endPath();
	}
	
	@Override
	public void hitFloor(){
		super.hitFloor();
		resetGravity();
	}
	
	@Override
	protected boolean testEndPath(){
		return timer <= 0;
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		Graphics2D gCopy = (Graphics2D)g.create();
		gCopy.rotate(getTotalVector().getRadians(), getScreenCenterX(x), getScreenCenterY(y));
		gCopy.drawImage(Images.bounceAttack, (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y) - 1), null);
	}
	
}
