package game.world.obj.hazzard;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import game.libs.Images;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.entity.Player;

public class Lazer extends GameObject{

	public static final int SPEED = 3;
	public static final double MIN_SIZE = 4;
	
	private Player player;
	
	private int timer;
	private int maxTime;
	private boolean growing;
	private double base;
	private double baseSize;
	
	private boolean neg;
	private boolean vert;
	
	private static final Vector2D MOVE = new Vector2D(SPEED, 0);
	
	private int animationTimer;
	public static final int MAX_ANIMATION_TIME = 7;
	
	protected Level containerLevel;
	
	/**
	 * @param x
	 * @param y
	 * @param width the maximum width of this lazer
	 * @param height the maximum height of this lazer
	 * @param timer the current time this object has until it toggles of or off again
	 * @param maxTime the amount of time the lazer is off, then the amount of time it is on
	 * @param neg true if this lazer shoots in the negative direction, false otherwise
	 * @param vert true if this lazer shoots vertically, false otherwise
	 * @param player
	 */
	public Lazer(int x, int y, int length, int timer, int maxTime, boolean neg, boolean vert, Player player, Level containerLevel){
		super(x, y, 0, 0, (int)(MAX_RENDER_PRIORITY * 0.9), false);
		
		this.containerLevel = containerLevel;
		
		if(vert){
			setWidth(TILE_SIZE * 2);
			setHeight(length);
		}
		else{
			setWidth(length);
			setHeight(TILE_SIZE * 2);
		}
		
		this.player = player;
		
		this.timer = timer;
		this.maxTime = maxTime;
		if(this.timer > this.maxTime) this.timer = this.maxTime;
		growing = false;
		
		if(vert){
			baseSize = getHeight();
			base = getY() + getHeight();
			
			if(getHeight() <= MIN_SIZE) throw new IllegalArgumentException("height must be > " + MIN_SIZE);
		}
		else{
			baseSize = getWidth();
			base = getX() + getWidth();
			
			if(getWidth() <= MIN_SIZE) throw new IllegalArgumentException("width must be > " + MIN_SIZE);
		}
		
		this.neg = neg;
		this.vert = vert;
		
		animationTimer = (int)(Math.random() * MAX_ANIMATION_TIME);
	}

	@Override
	protected void tickOverride(){
		
		Rectangle2D.Double bounds;
		if(vert) bounds = new Rectangle2D.Double(getX() + 2, getY(), getWidth() - 4, getHeight());
		else bounds = new Rectangle2D.Double(getX(), getY() + 2, getWidth(), getHeight() - 4);
		boolean intersects = player.getBounds().intersects(bounds);
		if(intersects){
			if(vert){
				if(player.isRight(getBounds())) MOVE.setAngleDegrees(0);
				else MOVE.setAngleDegrees(180);
			}
			else{
				if(player.isAbove(getBounds())) MOVE.setAngleDegrees(270);
				else MOVE.setAngleDegrees(90);
			}
			MOVE.setAmount(SPEED);
			if(!player.hasVector(MOVE)) player.addVector(MOVE);
			if(player.canTakeDamage()) player.dealDamage(1);
			player.resetGravity();
		}
		
		int a = 5;
		if(vert){
			if(player.hasVector(MOVE) && (player.getHitWallTime() != 0 || MOVE.getAmount() <= SPEED * 0.01)){
				player.removeVector(MOVE);
				if(!intersects) player.resetGravity();
				MOVE.setAmount(SPEED);
			}
			else MOVE.addAmount(-SPEED * 0.005);
			
			if(neg){
				if(growing && getHeight() < baseSize) addHeight(a);
				else if(!growing && getHeight() > 0) addHeight(-a);
				
				if(getHeight() <= MIN_SIZE) setHeight(MIN_SIZE);
				else if(getHeight() >= baseSize) setHeight(baseSize);
				
				setY(base - getHeight());
			}
			else{
				if(growing && getHeight() < baseSize) addHeight(a);
				else if(!growing && getHeight() > MIN_SIZE) addHeight(-a);
				
				if(getHeight() <= MIN_SIZE) setHeight(MIN_SIZE);
				else if(getHeight() >= baseSize) setHeight(baseSize);
			}
		}
		else{
			if(player.hasVector(MOVE) && (player.getHitFloorTime() != 0 || player.getHitCeilingTime() != 0)){
				player.removeVector(MOVE);
				player.resetGravity();
			}
			
			if(neg){
				if(growing && getWidth() < baseSize) addWidth(a);
				else if(!growing && getWidth() > 0) addWidth(-a);
				if(getWidth() <= MIN_SIZE) setWidth(MIN_SIZE);
				else if(getWidth() >= baseSize) setWidth(baseSize);
			}
			else{
				if(growing && getWidth() < baseSize) addWidth(a);
				else if(!growing && getWidth() > 0) addWidth(-a);
				
				if(getWidth() <= MIN_SIZE) setWidth(MIN_SIZE);
				else if(getWidth() >= baseSize) setWidth(baseSize);
				
				setX(base - getWidth());
			}
		}
		
		if(timer > 0) timer--;
		else{
			timer = maxTime;
			growing = !growing;
		}
		
		animationTimer--;
		if(animationTimer <= 0) animationTimer = MAX_ANIMATION_TIME;
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		int frame = animationTimer / ((MAX_ANIMATION_TIME + 1) / 8);
		
		if(vert){
			if(neg){
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 4; j++){
						g.drawImage(Images.lazerVertical[i][11 - j + 4 * (frame % 8)], (int)Math.round(getScreenX(x) + i * 14 + 2), (int)Math.round(getScreenY(y) + j - 4), null);
					}
					for(int j = 0; j < getHeight(); j++){
						g.drawImage(Images.lazerVertical[i][(j + frame) % 8], (int)Math.round(getScreenX(x) + i * 14 + 2), (int)Math.round((getScreenY(y) + getHeight()) - j - 1), null);
					}
				}
			}
			else{
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 4; j++){
						g.drawImage(Images.lazerVertical[i][11 - j + 4 * (frame % 8)], (int)Math.round(getScreenX(x) + i * 14 + 2), (int)Math.round((getScreenY(y) + getHeight()) - j + 3), null);
					}
					for(int j = 0; j < getHeight(); j++){
						g.drawImage(Images.lazerVertical[i][(j + frame) % 8], (int)Math.round(getScreenX(x) + i * 14 + 2), (int)Math.round(getScreenY(y) + j), null);
					}
				}
			}
		}
		else{
			if(neg){
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 4; j++){
						g.drawImage(Images.lazerHorizontal[11 - j + 4 * (frame % 8)][i], (int)Math.round((getScreenX(x) + getWidth()) - j + 3), (int)Math.round(getScreenY(y) + i * 14 + 2), null);
					}
					for(int j = 0; j < getWidth(); j++){
						g.drawImage(Images.lazerHorizontal[(j + frame) % 8][i], (int)Math.round(getScreenX(x) + j), (int)Math.round(getScreenY(y) + i * 14 + 2), null);
					}
				}
			}
			else{
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 4; j++){
						g.drawImage(Images.lazerHorizontal[11 - j + 4 * (frame % 8)][i], (int)Math.round(getScreenX(x) + j - 4), (int)Math.round(getScreenY(y) + i * 14 + 2), null);
					}
					for(int j = 0; j < getWidth(); j++){
						g.drawImage(Images.lazerHorizontal[(j + frame) % 8][i], (int)Math.round((getScreenX(x) + getWidth()) - j - 1), (int)Math.round(getScreenY(y) + i * 14 + 2), null);
					}
				}
			}
		}
	}
	
	@Override
	public Rectangle2D.Double getScreenBounds(double x, double y){
		if(vert) return new Rectangle2D.Double(getScreenX(x), getScreenY(y) - 4, getWidth(), getHeight() + 8);
		else return new Rectangle2D.Double(getScreenX(x) - 4, getScreenY(y), getWidth() + 8, getHeight());
	}
}
