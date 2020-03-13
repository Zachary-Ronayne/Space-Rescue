package game.world.obj.entity.projectile;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.libs.Images;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.Tile;
import game.world.obj.entity.Player;
import game.world.obj.entity.enemy.Enemy;

public class PlayerAttack extends Projectile{
	
	public static final double SPEED = 2.5;
	
	public static final double SIZE = 4;
	
	private static final int DESPAWN_TIME = 60;
	
	private int despawnTimer;
	
	public PlayerAttack(double x, double y, double angle, Level containerLevel){
		super(x, y, SIZE, SIZE, (int)(MAX_RENDER_PRIORITY * .8), new Vector2D(SPEED, angle), containerLevel);
		despawnTimer = DESPAWN_TIME;
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		gravity.addAmount(-GRAVITY_CONSTANT * .85);
		
		Enemy e = getContainingEnemy(containerLevel);
		if(e != null && e.getHitBox().intersects(getBounds())){
			e.takeDamage(1);
			endPath();
		}
		
		despawnTimer--;
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		Graphics2D gCopy = (Graphics2D)g.create();
		gCopy.rotate(getTotalVector().getRadians(), getScreenCenterX(x), getScreenCenterY(y));
		gCopy.drawImage(Images.playerAttack, (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
	}
	
	@Override
	protected boolean testEndPath(){
		return despawnTimer <= 0 || inSolidObject(containerLevel);
	}
	
	
	@Override
	public boolean inSolidObject(Level l){
		ArrayList<Tile> ts = l.getCollideTiles(getBounds());
		for(int j = 0; j < ts.size(); j++){
			if(ts.get(j).getUUID() != getUUID() && ts.get(j).getTile().getCollide() && ts.get(j).getBounds().intersects(getBounds())) return true; 
		}
		
		ArrayList<GameObject> objs = l.getObjects();
		for(int j = 0; j < objs.size(); j++){
			if(!(objs.get(j) instanceof Enemy) &&objs.get(j).getUUID() != getUUID() && !(objs.get(j) instanceof Player) && objs.get(j).getCollide() && objs.get(j).getBounds().intersects(getBounds())) return true; 
		}
		
		return false;
	}
	
}
