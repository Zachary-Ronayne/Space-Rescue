package game.world.obj.entity.enemy;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.libs.Sounds;
import game.world.levels.Level;
import game.world.obj.entity.Player;
import game.world.obj.entity.projectile.EnemyBounceProjectile;

public class BounceRangedAlien extends MeleeAlien{
	
	public static final int ATTACK_TIME = 60;
	
	protected int attackTimer;
	
	public BounceRangedAlien(double x, double y, Level containerLevel, Player track, boolean defaultAIOn){
		super(x, y, containerLevel, track, defaultAIOn);
		setWidth(10);
		setHeight(30);
		setHealth(4);
		
		attackTimer = 0;
	}
	
	public BounceRangedAlien(double x, double y, Level containerLevel, Player track){
		this(x, y, containerLevel, track, true);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		if(attackTimer > 0) attackTimer--;
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		BufferedImage img = Images.bounceAlienFrames[getAnimationFrameIndex()];
		
		double diff = (img.getWidth() - getWidth()) / 2;
		
		if(faceLeft) g.drawImage(img, (int)Math.round(getScreenX(x) - 1 - diff), (int)Math.round(getScreenY(y) - 1), null);
		else g.drawImage(img, (int)Math.round(getScreenX(x) + 15 - diff), (int)Math.round(getScreenY(y) - 1), -16, 32, null);
	}
	
	@Override
	protected double getRange(){
		return TILE_SIZE * 5;
	}
	
	@Override
	protected double getJump(){
		return 1.8;
	}
	
	@Override
	protected void attack(){
		if(attackTimer > 0) return;
		
		attackTimer = ATTACK_TIME;
		double a = Math.toDegrees(getAngleTo(player));
		double xx;
		if(a > 90 && a < 270) xx = getX();
		else xx = getX() + getWidth() - EnemyBounceProjectile.SIZE;
		
		containerLevel.addObject(
				new EnemyBounceProjectile(xx,
				getCenterY() - 6,
				a, containerLevel, player));
		if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_SHOOT_1);
	}
	
	@Override
	public Double getHitBox(){
		return new Rectangle2D.Double(getX() - 2, getY() - 1, getWidth() + 4, getHeight() + 1);
	}

	@Override
	protected void playDamageNoise(){
		containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_DAMAGE_2);
	}
}
