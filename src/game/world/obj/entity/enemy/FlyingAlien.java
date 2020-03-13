package game.world.obj.entity.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.libs.Sounds;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.entity.Player;
import game.world.obj.entity.projectile.EnemyNormalShot;

public class FlyingAlien extends Enemy{
	
	public static final double MAX_SPEED = 0.8;
	public static final double BACK_SPEED = 0.7 * MAX_SPEED;
	
	public static final int ATTACK_TIME = 70;
	
	private Player player;
	
	private Vector2D mainMove;
	private Vector2D reverseMove;
	private int reverseTime;
	
	private int attackTimer;
	
	private boolean aiOn;
	
	private boolean faceRight;
	
	private int animationTimer;
	private boolean animationUp;
	
	public static final int MAX_ANIMATION_TIME = 19;
	
	public FlyingAlien(double x, double y, Level containerLevel, Player player){
		super(x, y, TILE_SIZE, TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .8), 3, false, containerLevel);
		this.player = player;
		mainMove = new Vector2D(0, 0);
		reverseMove = new Vector2D(0, 0);
		
		addVector(mainMove);
		addVector(reverseMove);
		
		reverseTime = 0;
		attackTimer = 1;
		
		aiOn = false;
		
		faceRight = true;
		
		animationTimer = (int)(Math.random() * 4 + 1) * ((MAX_ANIMATION_TIME + 1) / 4) - 1;
		animationUp = true;
	}
	
	@Override
	public void addGravity(){
		gravity.addAmount(GRAVITY_CONSTANT * .25);
	}
	
	@Override
	protected void collide(){}
	
	@Override
	
	protected void tickOverride(){
		if(animationTimer <= 0 || animationTimer >= MAX_ANIMATION_TIME) animationUp = !animationUp;
		if(animationUp) animationTimer++;
		else animationTimer--;
		
		if(super.shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY()) && 
				animationTimer == 0) containerLevel.getEffectsPlayer().playSound(Sounds.FLAP_WINGS_2);
		
		faceRight = player.getCenterX() > getCenterX();
		
		if(!aiOn && player.getCenter().distance(getCenter()) < TILE_SIZE * 8){
			aiOn = true;
			mainMove.setAmount(MAX_SPEED);
		}
		else if(aiOn){
			mainMove.setAngleRadians(getAngleTo(player));
			if(player.getCenter().distance(getCenter()) < TILE_SIZE * 4){
				removeVector(mainMove);
				removeVector(reverseMove);
			}
			else{
				if(!hasVector(mainMove)) addVector(mainMove);
				if(!hasVector(reverseMove)) addVector(reverseMove);
			}
			
			GameObject obj = getInSolidObject(containerLevel);
			if(obj != null){
				reverseTime = 60;
				mainMove.setAmount(MAX_SPEED - BACK_SPEED);
				reverseMove.setAmount(BACK_SPEED);
				reverseMove.setAngleRadians(getAngleTo(obj) + Math.PI);
			}
		}
		
		if(gravity.getAmount() > 0.8) reverseGravity();
		
		super.tickOverride();
		
		if(aiOn){
			reverseTime--;
			if(reverseTime <= 0){
				mainMove.setAmount(MAX_SPEED);
				reverseMove.setAmount(0);
			}
			if(attackTimer > 0){
				attackTimer--;
				if(attackTimer <= 0){
					attackTimer = ATTACK_TIME;
					double addX;
					if(faceRight) addX = 4;
					else addX = -4;
					containerLevel.addObject(new EnemyNormalShot(
							getCenterX() - EnemyNormalShot.WIDTH * .5 + addX,
							getCenterY() - EnemyNormalShot.WIDTH * .5 + 2,
							Math.toDegrees(getAngleTo(player)), containerLevel, player));
					if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_SHOOT_2);
				}
			}
			
			if(getHitBox().intersects(player.getBounds())){
				player.dealDamage(1);
			}
		}
		
		collideWithTiles(containerLevel);
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		BufferedImage img = Images.flyingAlienFrames[animationTimer / ((MAX_ANIMATION_TIME + 1) / 4)];
		if(faceRight) g.drawImage(img, (int)Math.round(getScreenX(x) - 1 + img.getWidth()), (int)Math.round(getScreenY(y) - 3), -img.getWidth(), img.getHeight(), null);
		else g.drawImage(img, (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y) - 3), null);
	}
	
	@Override
	public void hitCeiling(){
		super.hitCeiling();
		reverseGravity();
	}
	
	@Override
	public void hitFloor(){
		super.hitFloor();
		reverseGravity();
	}
	
	private void reverseGravity(){
		gravity.addAngleDegrees(180);
		gravity.setAmount(0);
	}
	
	@Override
	public void takeDamage(int damage){
		super.takeDamage(damage);
		containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_DAMAGE_4);
	}
	
}
