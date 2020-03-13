package game.world.obj;

import java.awt.Graphics2D;

import game.libs.Images;
import game.libs.Sounds;
import game.world.levels.Level;
import game.world.obj.entity.GameEntity;
import game.world.obj.entity.enemy.Enemy;
import game.world.obj.entity.enemy.FinalBoss;

public class FinalBossShield extends Enemy{

	public static final double EXTEND_HEIGHT = 4;
	
	private boolean enabled;
	private GameEntity link;
	
	private int animationTimer;
	
	private boolean dontRender;
	
	/**
	 * Creates a shield that absorbs all play attacks
	 * @param x
	 * @param y
	 * @param containerLevel
	 */
	public FinalBossShield(double x, double y, Level containerLevel){
		super(x, y, 0, 0, (int)(MAX_RENDER_PRIORITY * .4), 1, false, containerLevel);
		
		enabled = false;
		removeVector(gravity);
		
		link = null;
		
		animationTimer = 0;
		
		dontRender = false;
	}
	
	/**
	 * Causes this shield to inherent all movement from a given entity
	 * @param e
	 */
	public void linkToEntity(GameEntity e){
		if(e == null) return;
		vectors = e.getVectors();
		link = e;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void dontRender(boolean dont){
		dontRender = dont;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
		if(this.enabled){
			setWidth(4);
			setHeight(FinalBoss.HEIGHT + EXTEND_HEIGHT * 2);
		}
		else{
			setWidth(0);
			setHeight(0);
		}
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		animationTimer--;
		if(animationTimer < 0) animationTimer = 15;
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(dontRender) return;
		if(enabled){
			if(link != null && isLeft(link.getBounds())) g.drawImage(Images.finalBossShield[animationTimer / 4], (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y) - 2), null);
			else g.drawImage(Images.finalBossShield[animationTimer / 4], (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) - 2), -6, 68, null);
		}
	}
	
	@Override
	protected void collide(){}
	
	@Override
	public void takeDamage(int damage){
		containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_SHIELD_HIT);
	}
	
	@Override
	public void kill(Level l){}
	
}
