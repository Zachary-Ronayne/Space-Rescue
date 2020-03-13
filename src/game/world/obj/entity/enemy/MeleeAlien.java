package game.world.obj.entity.enemy;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.libs.Sounds;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class MeleeAlien extends Enemy{
	
	protected Vector2D move;
	protected Vector2D jump;
	protected Player player;
	
	private int onGround;
	private boolean canTurn;
	protected boolean faceLeft;
	
	private boolean aiOn;
	
	private boolean defaultAIOn;
	
	private int animationTimer;
	protected double lastX;
	
	public static final int MAX_ANIMATION_TIME = 19;
	
	private static final Vector2D MOVE_PLAYER = new Vector2D(0, 0);
	private static int HIT_PLAYER_UUID = -1;
	
	public MeleeAlien(double x, double y, Level containerLevel, Player track, boolean defaultAIOn){
		super(x, y, 12, 24, (int)(MAX_RENDER_PRIORITY * .8), 5, true, containerLevel);
		move = new Vector2D(0, 0);
		if(defaultAIOn) move.setAmount(getSpeed());
		addVector(move);
		
		jump = new Vector2D(getJump(), 270);
		
		this.player = track;
		
		onGround = 0;
		canTurn = true;
		faceLeft = false;
		
		aiOn = false;
		
		this.defaultAIOn = defaultAIOn;
		
		lastX = getX();
		
		animationTimer = (int)(Math.random() * 4 + 1) * ((MAX_ANIMATION_TIME + 1) / 4) - 1;
	}
	
	public MeleeAlien(double x, double y, Level containerLevel, Player track){
		this(x, y, containerLevel, track, true);
	}
	
	protected int getAnimationFrameIndex(){
		return animationTimer / ((MAX_ANIMATION_TIME + 1) / 4);
	}
	
	@Override
	protected void tickOverride(){
		playFootStepSound();
		
		if(lastX != getX() && !hasVector(jump)){
			if(animationTimer > 0) animationTimer--;
			else animationTimer = MAX_ANIMATION_TIME;
		}
		else animationTimer = 0;
		lastX = getX();
		
		
		if(hasVector(jump) && (getHitFloorTime() != 0 || getHitCeilingTime() != 0)){
			removeVector(jump);
			resetGravity();
		}
		
		super.tickOverride();
		
		if(getUUID() == HIT_PLAYER_UUID && player.hasVector(MOVE_PLAYER)){
			if(player.getHitWallTime() != 0 || MOVE_PLAYER.getAmount() < .1) player.removeVector(MOVE_PLAYER);
			MOVE_PLAYER.setAmount(MOVE_PLAYER.getAmount() * .9);
		}
		
		if(aiOn){
			if(canTurn){
				if(player.getNormalCenter().distance(getCenter()) > player.getWidth() * 2 || !isAbove(player.getBounds())){
					double a = 0;
					if(player.getCenterX() - getCenterX() < -getRange()){
						a = 180;
						move.setAmount(getSpeed());
					}
					else if(Math.abs(player.getCenterX() - getCenterX()) > getRange()){
						a = 0;
						move.setAmount(getSpeed());
					}
					else move.setAmount(0);
					move.setAngleDegrees(a);
					
					if(onGround == 1) jump();
				}
				else move.setAmount(getSpeed());
			}
			
			if(onGround > 0) onGround--;
			else canTurn = false;
			
			if(player.getNormalCenter().distance(getCenter()) < getRange() * 1.3){
				attack();
			}
			
			if(getHitBox().intersects(player.getBounds())){
				if(player.canTakeDamage()){
					player.resetGravity();
					MOVE_PLAYER.setAngleRadians(getAngleTo(player));
					MOVE_PLAYER.setAmount(3);
					HIT_PLAYER_UUID = getUUID();
					if(!player.hasVector(MOVE_PLAYER)) player.addVector(MOVE_PLAYER);
				}
				player.dealDamage(1);
			}
			
			faceLeft = player.isLeft(getBounds());
		}
		else if(player.getCenter().distance(getCenter()) < TILE_SIZE * 8){
			aiOn = true;
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		BufferedImage img = Images.meleeAlienFrames[getAnimationFrameIndex()];
		
		double diff = (img.getWidth() - getWidth()) / 2;
		
		if(faceLeft) g.drawImage(img, (int)Math.round(getScreenX(x) - diff), (int)Math.round(getScreenY(y) - 2), null);
		else g.drawImage(img, (int)Math.round(getScreenX(x) + 18 - diff), (int)Math.round(getScreenY(y) - 2), -18, 28, null);
	}
	
	@Override
	public void kill(Level l){
		super.kill(l);
		if(getUUID() == HIT_PLAYER_UUID){
			HIT_PLAYER_UUID = -1;
			player.removeVector(MOVE_PLAYER);
		}
	}
	
	protected void playFootStepSound(){
		if(super.shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY()) && 
				animationTimer == 0 && getHitFloorTime() != 0 && lastX != getX()) containerLevel.getEffectsPlayer().playSound(Sounds.FOOT_STEP_3);
	}
	
	protected double getJump(){
		return 2.2;
	}
	
	protected double getRange(){
		return 0;
	}
	
	protected double getSpeed(){
		return 1;
	}
	
	protected void attack(){
	}
	
	@Override
	public void takeDamage(int damage){
		if(aiOn){
			super.takeDamage(damage);
			playDamageNoise();
		}
	}
	
	protected void playDamageNoise(){
		containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_DAMAGE_1);
	}
	
	public void turnOn(){
		aiOn = true;
	}
	
	@Override
	public void hitWall(){
		super.hitWall();
		canTurn = true;
		if(aiOn){
			if(onGround >= 1 && getCenter().distance(player.getCenter()) > TILE_SIZE * 1.5) jump();
			if(player.getNormalCenter().distance(getCenter()) > getWidth() * 2 || !isAbove(player.getBounds())){
				move.addAngleDegrees(180);
				faceLeft = !faceLeft;
			}
		}
		else if(defaultAIOn){
			move.addAngleDegrees(180);
			faceLeft = !faceLeft;
		}
	}
	
	@Override
	public void hitFloor(){
		onGround = 3;
		canTurn = true;
		super.hitFloor();
	}
	
	private void jump(){
		if(player.getNormalCenter().distance(getCenter()) > getWidth() * 2 && !hasVector(jump)) addVector(jump);
	}
	
	@Override
	public Double getHitBox(){
		return new Rectangle2D.Double(getX() - 3, getY() - 1, getWidth() + 6, getHeight() + 2);
	}
	
}
