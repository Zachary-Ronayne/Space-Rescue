package game.world.obj.entity.enemy;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.libs.Images;
import game.libs.Sounds;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.FinalBossShield;
import game.world.obj.GameObject;
import game.world.obj.activate.ScreenShift;
import game.world.obj.entity.Player;
import game.world.obj.entity.projectile.FinalBossNormalShot;
import game.world.obj.entity.projectile.FinalBossSpreadShot4;
import game.world.obj.entity.projectile.FinalBossSpreadShot8;

public class FinalBoss extends Enemy{
	
	public static final int MAX_HEALTH = 40;
	public static final double WIDTH = 36;
	public static final double HEIGHT = 52;
	
	public static final double CENTER_X = TILE_SIZE * 18.75;
	public static final double CENTER_Y = TILE_SIZE * 9;
	
	public static final double MOVE_CENTER_SPEED = 4;
	
	public static final double PUSH_PLAYER_SPEED = 4;
	
	public static final int DASH_ATTACK_CD = 300;
	public static final int DASH_ATTACK_DELAY = 45;
	public static final double DASH_ATTACK_SPEED = 4;
	
	public static final int BASIC_ATTACK_CD = 95;
	public static final int BASIC_ATTACK_DELAY = 24;
	public static final int SPREAD_ATTACK_CNT_MAX = 3;
	
	public static final int MINION_SPAWN_CD = 240;
	public static final int MINION_SPAWN_DELAY = 45;
	
	public static final int SHIELD_SWAP_CD = 240;
	
	public static final int MAX_WIN_ANIMATION_DELAY = 240;
	public static final int MAX_STAGE_CNT = 120;
	
	private Player player;
	
	private ScreenShift zoomAtBoss;
	private Vector2D bossMove;
	private boolean fightStarted;
	
	private boolean dead;
	private int winAnimationDelay;
	private double deathAngle;
	
	private Vector2D recenter;
	
	private Vector2D pushPlayer;
	
	private Vector2D dashAtPlayer;
	private int dashAttackTimer;
	private double dashAngle;
	private boolean dashReset;
	
	private int basicAttackTimer;
	private int spreadAttackCnt;
	
	private int minionSummonTimer;
	
	private FinalBossShield shield;
	private int shieldTimer;
	private boolean shieldOnLeft;
	private boolean shieldMoving;
	
	private int fightStage;
	private int nextStageCnt;
	private boolean stageReset;
	
	private int animationTimer;
	private static final int MAX_ANIMATION_TIME = 39;
	
	private int floatTime;
	private boolean floatUp;
	private static final int MAX_FLOAT_TIME = 59;
	
	public FinalBoss(Level containerLevel, Player player){
		super(TILE_SIZE * 17.25, -HEIGHT, WIDTH, HEIGHT, MAX_RENDER_PRIORITY, MAX_HEALTH, false, containerLevel);
		this.player = player;
		
		player.setControlsEnabled(false);
		
		bossMove = new Vector2D(1, 90);
		zoomAtBoss = new ScreenShift(
				new Point2D.Double(TILE_SIZE * 9, TILE_SIZE * 12),
				new Point2D.Double(TILE_SIZE * 18.5, TILE_SIZE * 5),
				1, 30, containerLevel, this){
			@Override
			public void end(){
				super.end();
				addVector(bossMove);
			}
		};
		containerLevel.addObject(zoomAtBoss);
		fightStarted = false;
		
		dead = false;
		winAnimationDelay = -1;
		deathAngle = 0;
		
		recenter = new Vector2D(0, 0);
		addVector(recenter);
		
		pushPlayer = new Vector2D(0, 0);
		
		dashAtPlayer = new Vector2D(0, 0);
		addVector(dashAtPlayer);
		dashAttackTimer = DASH_ATTACK_CD + 60;
		dashAngle = getAngleTo(player);
		dashReset = true;
		
		removeVector(gravity);
		
		basicAttackTimer = BASIC_ATTACK_CD;
		spreadAttackCnt = SPREAD_ATTACK_CNT_MAX;
		
		minionSummonTimer = MINION_SPAWN_CD;
		
		shield = new FinalBossShield(-1000, -1000, containerLevel);
		containerLevel.addObject(shield);
		shield.linkToEntity(this);
		shieldTimer = SHIELD_SWAP_CD;
		shieldOnLeft = true;
		shieldMoving = false;
		
		fightStage = 0;
		nextStageCnt = 0;
		stageReset = true;
		
		animationTimer = MAX_ANIMATION_TIME;
		
		floatTime = 0;
		floatUp = false;
	}
	
	@Override
	protected void tickOverride(){
		animationTimer--;
		if(animationTimer <= 0) animationTimer = MAX_ANIMATION_TIME;
		if(super.shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY()) &&
				dashAttackTimer > DASH_ATTACK_DELAY && minionSummonTimer > MINION_SPAWN_DELAY && basicAttackTimer > BASIC_ATTACK_DELAY &&
				animationTimer == (int)(MAX_ANIMATION_TIME * .75)) containerLevel.getEffectsPlayer().playSound(Sounds.FLAP_WINGS_1);
		
		super.tickOverride();
		
		if(winAnimationDelay > 0){
			winAnimationDelay--;
			deathAngle += 7;
			setWidth(WIDTH * (double)winAnimationDelay / MAX_WIN_ANIMATION_DELAY);
			setHeight(HEIGHT * (double)winAnimationDelay / MAX_WIN_ANIMATION_DELAY);
			if(winAnimationDelay <= 0) containerLevel.winLevel();
		}
		
		if(dashAttackTimer > DASH_ATTACK_DELAY && basicAttackTimer > BASIC_ATTACK_DELAY && minionSummonTimer > MINION_SPAWN_DELAY){
			if(floatUp) floatTime--;
			else floatTime++;
			if(Math.abs(floatTime) > MAX_FLOAT_TIME) floatUp = !floatUp;
		}
		
		if(fightStarted){
			if(nextStageCnt > 0){
				dashAttackTimer = DASH_ATTACK_CD;
				basicAttackTimer = BASIC_ATTACK_CD;
				minionSummonTimer = MINION_SPAWN_CD;
				setHealth(MAX_HEALTH);
				dashReset = true;
				dashAtPlayer.setAmount(0);
				player.heal(Player.MAX_HELATH);
				if(fightStage != 2){
					pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					if(player.hasVector(pushPlayer)) pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					else player.addVector(pushPlayer);
				}
				
				nextStageCnt--;
				if(nextStageCnt <= 0){
					fightStage++;
					player.setControlsEnabled(true);
					if(fightStage == 3){
						dead = true;
					}
				}
			}
			
			if(!dead){
				//push player
				boolean hasPush = player.hasVector(pushPlayer);
				
				if(hasPush){
					if(pushPlayer.getAmount() < 0.05) player.removeVector(pushPlayer);
					pushPlayer.setAmount(pushPlayer.getAmount() * .97);
				}
				if(hasPush && player.getHitWallTime() != 0) player.removeVector(pushPlayer);
				
				if(player.getBounds().intersects(getBounds())){
					player.dealDamage(1);
					pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					if(player.isLeft(getBounds())) pushPlayer.setAngleDegrees(180);
					else pushPlayer.setAngleDegrees(0);
					if(hasPush) pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					else player.addVector(pushPlayer);
				}
				
				//dash at player
				if(dashAttackTimer > 0){
					if(dashAttackTimer == DASH_ATTACK_DELAY){
						containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_DASH);
						double a = Math.toDegrees(Math.atan2(getCenterY() - (player.getY()), getCenterX() - player.getCenterX()) + Math.PI);
						if(a < 5) a = 5;
						else if(a > 175){
							if(a < 270) a = 175;
							else a = 5;
						}
						dashAngle = a;
					}
					if(minionSummonTimer > MINION_SPAWN_DELAY) dashAttackTimer--;
					if(dashAttackTimer <= DASH_ATTACK_DELAY){
						double rad = Math.toRadians(dashAngle);
						addX(-Math.cos(rad) * 1.3);
						addY(-Math.sin(rad) * 1.3);
					}
					if(dashAttackTimer <= 0){
						dashAtPlayer.setAngleDegrees(dashAngle);
						dashAtPlayer.setAmount(DASH_ATTACK_SPEED);
					}
				}
				if(dashAtPlayer.getAmount() != 0 && inSolidObject(containerLevel)) resetDashAtackVector();
				
				//go back to center
				boolean inCenter = (Math.abs(getX() - CENTER_X) < TILE_SIZE * 10 && Math.abs(getY() - CENTER_Y) < TILE_SIZE);
				
				if(!dashReset || !stageReset){
					recenter.setAmount(MOVE_CENTER_SPEED);
					recenter.setAngleRadians(Math.atan2(getCenterY() - CENTER_Y, getCenterX() - CENTER_X) + Math.PI);
				}
				else recenter.setAmount(0);
				
				if(getCenter().distance(CENTER_X, CENTER_Y) < TILE_SIZE && !stageReset) stageReset = true;
				if(inCenter && !dashReset) dashReset = true;
				if(nextStageCnt > 0 || winAnimationDelay > 0) return;
				
				//use basic attack
				if(inCenter){
					if(basicAttackTimer > 0 && dashAttackTimer > DASH_ATTACK_DELAY && minionSummonTimer > MINION_SPAWN_DELAY) basicAttackTimer--;
					
					if(basicAttackTimer == 4){
						if(spreadAttackCnt != 1 || fightStage <= 0) containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_SHOOT_1);
						else containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_SHOOT_2);
					}
					if(basicAttackTimer <= 0){
						basicAttackTimer = BASIC_ATTACK_CD;
						if(spreadAttackCnt <= 0){
							if(fightStage > 1) spreadAttackCnt = SPREAD_ATTACK_CNT_MAX - 1;
							else spreadAttackCnt = SPREAD_ATTACK_CNT_MAX;
						}
						spreadAttackCnt--;
						//use normal attack
						double xAdd = 0;
						double yAdd = -8;
						if(player.isLeft(getBounds())) xAdd = -10;
						else xAdd = 10;
						
						if(spreadAttackCnt > 0 || fightStage <= 0){
							containerLevel.addObject(new FinalBossNormalShot(
									getCenterX() - FinalBossNormalShot.SIZE / 2 + xAdd,
									getCenterY() - FinalBossNormalShot.SIZE / 2 + yAdd,
									Math.toDegrees(getAngleTo(player)), containerLevel, player));
						}
						//use spread attack
						else{
							if(fightStage == 1){
								containerLevel.addObject(new FinalBossSpreadShot4(
										getCenterX() - FinalBossSpreadShot4.SIZE / 2 + xAdd,
										getCenterY() - FinalBossSpreadShot4.SIZE / 2 + yAdd,
										Math.toDegrees(getAngleTo(player)), containerLevel, player));
							}
							else if(fightStage > 1){
								containerLevel.addObject(new FinalBossSpreadShot8(
										getCenterX() - FinalBossSpreadShot8.SIZE / 2 + xAdd,
										getCenterY() - FinalBossSpreadShot8.SIZE / 2 + yAdd,
										Math.toDegrees(getAngleTo(player)), containerLevel, player));
								if(spreadAttackCnt == SPREAD_ATTACK_CNT_MAX) spreadAttackCnt--;
							}
						}
					}
				}
				
				//if past the first fight stage, do the second fight stage stuff
				if(fightStage > 0){
					if(inCenter){
						int cnt = 0;
						ArrayList<GameObject> objs = containerLevel.getObjects();
						for(GameObject o : objs) if(o instanceof AlienMinion) cnt++;
						
						if(cnt < fightStage && basicAttackTimer > BASIC_ATTACK_DELAY && dashAttackTimer > DASH_ATTACK_DELAY && minionSummonTimer > 0) minionSummonTimer--;
						
						if(minionSummonTimer == MINION_SPAWN_DELAY) containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_SUMMON);
						if(minionSummonTimer <= 0){
							if(cnt < fightStage){
								minionSummonTimer = MINION_SPAWN_CD;
								AlienMinion m = new AlienMinion(
										getCenterX() - AlienMinion.SIZE / 2,
										getCenterY() - AlienMinion.SIZE / 2,
										containerLevel, player, true);
								m.turnOn();
								containerLevel.addObject(m);
							}
							else minionSummonTimer = MINION_SPAWN_CD;
						}
					}
					
					//if past the second fight stage, do the third and final stage stuff
					if(fightStage > 1){
						double l = getX() - shield.getWidth() - 2;
						double r = getX() + getWidth() + 4;
						
						if(winAnimationDelay <= 0){
							if(!shield.isEnabled()){
								shield.setEnabled(true);
								if(shieldOnLeft) shield.setX(l);
								else shield.setX(r);
								shield.setY(getY() - FinalBossShield.EXTEND_HEIGHT);
							}
							
							if(shieldTimer > 0){
								shieldTimer--;
								if(shieldTimer <= 0){
									shieldOnLeft = !shieldOnLeft;
									shieldMoving = true;
								}
							}
							else{
								if(shieldMoving){
									if(shieldOnLeft && shield.getX() > l) shield.addX(-3);
									else if(shield.getX() < r && !shieldOnLeft) shield.addX(3);
									
									if(shieldOnLeft && shield.getX() <= l || shield.getX() >= r && !shieldOnLeft){
										shieldTimer = SHIELD_SWAP_CD;
										shieldMoving = false;
									}
								}
							}
							if(!shieldMoving){
								if(shieldOnLeft) shield.setX(l);
								else shield.setX(r);
								shield.setY(getY() - FinalBossShield.EXTEND_HEIGHT);
							}
						}
					}
				}
			}
		}
		else{
			if(!zoomAtBoss.isStarted()){
				zoomAtBoss.activate();
			}
			else if(getY() >= CENTER_Y){
				if(hasVector(bossMove)){
					setY(CENTER_Y);
					removeVector(bossMove);
					zoomAtBoss = new ScreenShift(
							new Point2D.Double(TILE_SIZE * 18.5, TILE_SIZE * 10.75),
							new Point2D.Double(TILE_SIZE * 9, TILE_SIZE * 12),
							1, 60, containerLevel, player){
						
						@Override
						public void end(){
							super.end();
							fightStarted = true;
							player.setControlsEnabled(true);
						}
					};
					containerLevel.addObject(zoomAtBoss);
				}
			}
		}
		
		keepInLevelBounds(containerLevel);
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		int w;
		int mx;
		BufferedImage img;
		
		if(dashAttackTimer <= DASH_ATTACK_DELAY){
			img = Images.finalBossFrames[1][1];
			
			double deg = dashAtPlayer.getDegrees();
			
			if(dashAttackTimer > 0){
				if(dashAngle > 90 && dashAngle < 270){
					w = 40;
					mx = 0;
				}
				else{
					w = -40;
					mx = 40;
				}
			}
			else {
				if(deg > 90 && deg < 270){
					w = 40;
					mx = 0;
				}
				else{
					w = -40;
					mx = 40;
				}
			}
		}
		else{
			if(basicAttackTimer <= BASIC_ATTACK_DELAY) img = Images.finalBossFrames[0][1];
			else if(minionSummonTimer <= MINION_SPAWN_DELAY) img = Images.finalBossFrames[2 + (animationTimer / 4) % 2][1];
			else img = Images.finalBossFrames[animationTimer / ((MAX_ANIMATION_TIME + 1) / 4)][0];
			if(player.isLeft(getBounds())){
				w = 40;
				mx = 0;
			}
			else{
				w = -40;
				mx = 40;
			}
		}
		if(winAnimationDelay >= 0){
			Graphics2D g2 = (Graphics2D)g.create();
			g2.rotate(Math.toRadians(deathAngle), getScreenCenterX(x), getScreenCenterY(y));
			g2.drawImage(img, (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight(), null);
		}
		else{
			if(nextStageCnt > 0){
				Graphics2D g2 = (Graphics2D)g.create();
				g2.rotate(Math.toRadians(nextStageCnt * 7), getScreenCenterX(x), getScreenCenterY(y));
				BufferedImage img2 = Images.finalBossExplode;
				int size = (int)(1300.0 * (MAX_STAGE_CNT - nextStageCnt) / MAX_STAGE_CNT);
				g2.drawImage(img2, (int)Math.round(getScreenCenterX(x) - size * .5), (int)Math.round(getScreenCenterY(y) - size * .5), size, size, null);
			}
			g.drawImage(img, (int)Math.round(getScreenX(x) + mx), (int)Math.round(getScreenY(y)), w, 56, null);
		}
	}
	
	@Override
	public boolean shouldRender(double x, double y){
		return true;
	}
	
	@Override
	public void kill(Level l){
		if(!dead){
			if(nextStageCnt <= 0){
				for(GameObject obj : containerLevel.getObjects()){
					if(obj instanceof AlienMinion) ((AlienMinion)obj).kill(containerLevel);
				}
				
				setHealth(MAX_HEALTH);
				
				player.heal(Player.MAX_HELATH);
				
				stageReset = false;
				resetDashAtackVector();
				dashReset = true;
				
				if(fightStage == 2){
					containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_DEATH);
					shield.linkToEntity(null);
					shield.setEnabled(false);
					shield.dontRender(true);
					winAnimationDelay = MAX_WIN_ANIMATION_DELAY;
				}
				else{
					containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_NEXT_STAGE);
					player.setControlsEnabled(false);
					
					pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					if(player.isLeft(getBounds())) pushPlayer.setAngleDegrees(180);
					else pushPlayer.setAngleDegrees(0);
					if(player.hasVector(pushPlayer)) pushPlayer.setAmount(PUSH_PLAYER_SPEED);
					else player.addVector(pushPlayer);
					nextStageCnt = MAX_STAGE_CNT;
				}
			}
		}
	}
	
	@Override
	public void takeDamage(int damage){
		if(winAnimationDelay != -1 || !fightStarted || nextStageCnt > 0) return;
		containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_TAKE_DAMAGE);
		super.takeDamage(damage);
	}
	
	@Override
	public void addGravity(){
		gravity.addAmount(GRAVITY_CONSTANT * .2);
	}
	
	@Override
	public double getY(){
		return super.getY() + floatTime / 4.0;
	}
	
	@Override
	protected void collide(){}
	
	private void resetDashAtackVector(){
		dashAtPlayer.setAmount(0);
		dashAttackTimer = DASH_ATTACK_CD;
		dashReset = false;
	}

	@Override
	public Double getHitBox(){
		return new Rectangle2D.Double(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);
	}
	
	@Override
	public Double getBounds(){
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void keepInLevelBounds(Level l){
		if(fightStarted) super.keepInLevelBounds(l);
	}
	
}
