package game.world.obj.entity.projectile;

import java.awt.Graphics2D;

import game.libs.Images;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class EnemyNormalShot extends Projectile{
	
	public static final double WIDTH = 6;
	public static final int MAX_TIME = 200;
	
	protected Player player;
	
	protected int timer;
	
	public EnemyNormalShot(double x, double y, double angle, Level containerLevel, Player player){
		super(x, y, WIDTH, WIDTH, (int)(MAX_RENDER_PRIORITY * .8), new Vector2D(2, angle), containerLevel);
		
		this.player = player;
		
		removeVector(gravity);
		
		timer = MAX_TIME;
	}
	
	@Override
	protected boolean testEndPath(){
		return timer <= 0 || inSolidObject(containerLevel);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		timer--;
		
		if(getBounds().intersects(player.getHitBox())){
			player.dealDamage(1);
			endPath();
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		Graphics2D gCopy = (Graphics2D)g.create();
		gCopy.rotate(getTotalVector().getRadians(), getScreenCenterX(x), getScreenCenterY(y));
		gCopy.drawImage(Images.normalAttack, (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y) - 1), null);
	}
	
}
