package game.world.obj.entity;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import game.input.Controlable;
import game.libs.Images;
import game.libs.Sounds;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.entity.projectile.PlayerAttack;

public class Player extends GameEntity implements Controlable{
	
	public static final double BASE_MOVE_SPEED = 2;
	public static final double BASE_JUMP_SPEED = 2.8;
	
	public static final int MAX_HELATH = 5;
	
	public static final int ATTACK_TIME = 20;
	
	public static final double NORMAL_HEIGHT = 22;
	public static final double DUCKING_HEIGHT = 14;
	
	/**
	 * The level this player is in
	 */
	private Level containerLevel;
	
	private boolean controlsEnabled;
	
	private Vector2D jump;
	private int canJumpTime;
	private Vector2D move;
	private boolean jumpDown;
	private int jumping;
	
	private boolean leftDown;
	private boolean rightDown;
	private boolean faceRight;
	
	private boolean duckDown;
	private boolean ducking;
	
	private int onWall;
	
	private boolean useDown;
	private boolean attackDown;
	private int attackTimer;
	
	private int health;
	private int invulnTime;
	
	private int resetGravity;
	
	//animation variables
	private static final int MAX_ANIMAITON_TIME = 19;
	private int animationTime;
	private double lastX;
	
	//sound variables
	private int footStepTimer;
	
	public Player(double x, double y, Level containerLevel){
		super(x, y, 10, NORMAL_HEIGHT, (int)(MAX_RENDER_PRIORITY * .9), true);
		
		controlsEnabled = true;
		
		jump = new Vector2D(BASE_JUMP_SPEED, 270);
		canJumpTime = 0;
		move = new Vector2D(0, 0);
		jumpDown = false;
		jumping = 0;
		
		duckDown = false;
		ducking = false;
		
		addVector(move);
		
		leftDown = false;
		rightDown = false;
		faceRight = true;
		onWall = 0;
		
		useDown = false;
		attackDown = false;
		attackTimer = ATTACK_TIME;
		
		this.containerLevel = containerLevel;
		
		health = MAX_HELATH;
		invulnTime = 0;
		
		resetGravity = 0;
		
		animationTime = 0;
		lastX = getX();
		
		footStepTimer = 0;
	}

	@Override
	protected void tickOverride(){
		
		//animation code
		if(move.getAmount() != 0){
			if(animationTime > 0) animationTime--;
			else animationTime = MAX_ANIMAITON_TIME;
		}
		else animationTime = 0;
		boolean onFloor = getHitFloorTime() != 0;
		if(lastX == getX() && onFloor || !onFloor && getTotalVector().getY() >= 0) animationTime = 2;
		
		if(getHitFloorTime() != 0 && move.getAmount() != 0 && lastX != getX()){
			if(footStepTimer == 0){
				if(ducking) containerLevel.getEffectsPlayer().playSound(Sounds.FOOT_STEP_2);
				else containerLevel.getEffectsPlayer().playSound(Sounds.FOOT_STEP_1);
			}
		}
		else footStepTimer = 1;
		
		footStepTimer--;
		if(footStepTimer < 0) footStepTimer = 19;
		
		lastX = getX();
		
		super.tickOverride();
		
		if(resetGravity > 0) resetGravity--;
		
		if(invulnTime > 0) invulnTime--;
		
		if(jumping > 0 && getTotalVector().getY() >= 0) jumpDown = false;
		
		if((!jumpDown || jumping <= 0) && hasVector(jump)) {
			removeVector(jump);
			resetGravity();
		}
		if(canJumpTime > 0) canJumpTime--;
		if(jumping > 0 && !jumpDown) jumping--;
		
		if(onWall > 0) onWall--;
		else if(!hasVector(move)) addVector(move);
		
		collideWithTiles(containerLevel);
		collideWithObjects(containerLevel);
		
		if(attackTimer <= 0 && attackDown){
			double addAngle = 0;
			double addY = 0;
			if(duckDown){
				addAngle = 30;
				addY = 2;
			}
			
			if(faceRight) containerLevel.addObject(new PlayerAttack(getX() + getWidth() - 4, getCenterY() - PlayerAttack.SIZE / 2 + addY, addAngle, containerLevel));
			else containerLevel.addObject(new PlayerAttack(getX(), getCenterY() - PlayerAttack.SIZE / 2 + addY, 180 - addAngle, containerLevel));
			attackTimer = ATTACK_TIME;
			containerLevel.getEffectsPlayer().playSound(Sounds.PLAYER_GUN_SHOT);
		}
		if(attackTimer > 0) attackTimer--;
		
		if(getY() > containerLevel.getLevelHeight() + getHeight() * 2) dealDamage(MAX_HELATH + 100000);
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(!(invulnTime > 30 && (invulnTime / 8) % 2 == 1)){
			BufferedImage img;
			int duck = 0;
			if(ducking) duck = 1;
			
			boolean down = getTotalVector().getY() >= 0;
			boolean onGround = getHitFloorTime() != 0;
			if(down && onGround || onGround && !down) img = Images.playerFrames[animationTime / ((MAX_ANIMAITON_TIME + 1) / 4)][duck];
			else img = Images.playerFrames[animationTime / ((MAX_ANIMAITON_TIME + 1) / 2) + duck * 2][2];
			
			double diff = (img.getWidth() - getWidth()) / 2;
			
			if(faceRight) g.drawImage(img, (int)Math.round(getScreenX(x) - diff), (int)Math.round(getScreenY(y) - 1), 16, 24, null);
			else g.drawImage(img, (int)Math.round(getScreenX(x) + 16 - diff), (int)Math.round(getScreenY(y) - 1), -16, 24, null);
		}
	}
	
	public int getHealth(){
		return health;
	}
	
	public boolean isFullHealth(){
		return health == MAX_HELATH;
	}
	
	public boolean canTakeDamage(){
		return invulnTime <= 0;
	}
	
	public void heal(int amount){
		health += amount;
		if(health > MAX_HELATH) health = MAX_HELATH;
	}
	
	public void dealDamage(int damage){
		if(damage <= 0 || !canTakeDamage()) return;
		containerLevel.getEffectsPlayer().playSound(Sounds.PLAYER_DAMAGE);
		invulnTime = 90;
		health -= damage;
		if(health < 0) health = 0;
		if(health > MAX_HELATH) health = MAX_HELATH;
	}
	
	private void setSpeed(double speed){
		if(ducking) speed *= 0.5;
		move.setAmount(speed);
	}
	
	private void setDucking(boolean duck){
		if(ducking == duck) return;
		
		if(duck){
			setY(getY() + getHeight() - DUCKING_HEIGHT);
			setHeight(DUCKING_HEIGHT);
		}
		else{
			setY(getY() + getHeight() - NORMAL_HEIGHT);
			setHeight(NORMAL_HEIGHT);
		}
		
		ducking = duck;
	}
	
	public void setControlsEnabled(boolean controlsEnabled){
		this.controlsEnabled = controlsEnabled;
		if(!this.controlsEnabled){
			move.setAmount(0);
			setDucking(false);
			leftDown = false;
			rightDown = false;
			jumpDown = false;
			duckDown = false;
			useDown = false;
			attackDown = false;
		}
	}
	
	/**
	 * @return The bounds of the player as if they were not crouching if they were crouching
	 */
	@Override
	public Point2D.Double getNormalCenter(){
		return new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight() - NORMAL_HEIGHT / 2);
	}
	
	/**
	 * @return The hit box of the player, used for determining if projectiles hit them
	 */
	public Rectangle2D.Double getHitBox(){
		return new Rectangle2D.Double(getX() - 1, getY(), 12, getHeight());
	}
	
	/**
	 * @return An amount of ticks representative of when gravity was last reset
	 */
	public int getResetGravity(){
		return resetGravity;
	}
	
	@Override
	public void resetGravity(){
		super.resetGravity();
		resetGravity = 2;
	}
	
	@Override
	public void hitFloor(){
		updateHitFloor();
		
		if(getTotalVector().getY() > 0){
			super.hitFloor();
			removeVector(jump);
			canJumpTime = 15;
		}
	}
	
	@Override
	public void hitCeiling(){
		super.hitCeiling();
		removeVector(jump);
		resetGravity();
	}
	
	@Override
	public void hitWall(){
		super.hitWall();
		onWall = 8;
		removeVector(move);
	}
	
	@Override
	public void pause(){
		super.pause();
		leftDown = false;
		rightDown = false;
		jumpDown = false;
		duckDown = false;
		useDown = false;
		attackDown = false;
		move.setAmount(0);
		setDucking(false);
	}
	
	@Override
	public void pressLeft(){
		if(!controlsEnabled) return;
		if(!leftDown){
			leftDown = true;
			setSpeed(BASE_MOVE_SPEED);
			move.setAngleDegrees(180);
			faceRight = false;
		}
	}
	@Override
	public void pressRight(){
		if(!controlsEnabled) return;
		if(!rightDown){
			rightDown = true;
			setSpeed(BASE_MOVE_SPEED);
			move.setAngleDegrees(0);
			faceRight = true;
		}
	}
	@Override
	public void pressJump(){
		if(!controlsEnabled) return;
		if(!jumpDown && canJumpTime > 0 && !hasVector(jump)){
			jumpDown = true;
			jumping = 10;
			addVector(jump);
			canJumpTime = 0;
			resetGravity();
		}
	}
	@Override
	public void pressDuck(){
		if(!controlsEnabled) return;
		if(!duckDown){
			duckDown = true;
			setDucking(true);
			if(move.getAmount() > 0) setSpeed(BASE_MOVE_SPEED);
		}
	}
	@Override
	public void pressAttack(){
		if(!controlsEnabled) return;
		if(!attackDown) attackDown = true;
	}
	@Override
	public void pressUse(){
		if(!controlsEnabled) return;
		if(!useDown){
			useDown = true;
			containerLevel.useUsables(this, getWidth() * 1.5);
		}
	}
	@Override
	public void pressPause(){
	}
	
	@Override
	public void releaseLeft(){
		if(!controlsEnabled) return;
		if(leftDown){
			leftDown = false;
			if(rightDown){
				setSpeed(BASE_MOVE_SPEED);
				move.setAngleDegrees(0);
				faceRight = true;
			}
			else setSpeed(0);
		}
	}
	@Override
	public void releaseRight(){
		if(!controlsEnabled) return;
		if(rightDown){
			rightDown = false;
			if(leftDown){
				setSpeed(BASE_MOVE_SPEED);
				move.setAngleDegrees(180);
				faceRight = false;
			}
			else setSpeed(0);
		}
	}
	@Override
	public void releaseJump(){
		if(!controlsEnabled) return;
		if(jumpDown){
			jumpDown = false;
			jumping = 0;
		}
	}
	@Override
	public void releaseDuck(){
		if(!controlsEnabled) return;
		if(duckDown){
			duckDown = false;
			setDucking(false);
			if(move.getAmount() > 0) setSpeed(BASE_MOVE_SPEED);
		}
	}
	@Override
	public void releaseAttack(){
		if(!controlsEnabled) return;
		if(attackDown) attackDown = false;
	}
	@Override
	public void releaseUse(){
		if(!controlsEnabled) return;
		if(useDown) useDown = false;
	}
	@Override
	public void releasePause(){
	}
}
