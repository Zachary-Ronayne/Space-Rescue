package game.world.obj.hazzard;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import game.libs.Images;
import game.libs.Sounds;
import game.util.calc.Vector2D;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.entity.Player;

public class AcidPool extends GameObject{
	
	private Player player;
	
	private static final Vector2D MOVE = new Vector2D(4, 270);
	
	public static final int ANIMATE_TIME = 32;
	private int animationTimer;
	
	private Level containerLevel;
	
	public AcidPool(double x, double y, double width, double height, Player player, Level containerLevel){
		super(x, y, width, height, (int)(MAX_RENDER_PRIORITY * .7), false);
		this.player = player;
		
		animationTimer = ((int)(Math.random() * 4) + 1) * (ANIMATE_TIME / 4) - 1;
		
		this.containerLevel = containerLevel;
	}
	
	@Override
	protected void tickOverride(){
		if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())){
			double rand = Math.random();
			if(rand > 0.75) containerLevel.getEffectsPlayer().playSound(Sounds.ACID_1);
			else if(rand > 0.5) containerLevel.getEffectsPlayer().playSound(Sounds.ACID_2);
			else if(rand > 0.25) containerLevel.getEffectsPlayer().playSound(Sounds.ACID_3);
			else containerLevel.getEffectsPlayer().playSound(Sounds.ACID_4);
		}
		
		animationTimer--;
		if(animationTimer < 0) animationTimer = ANIMATE_TIME - 1;
		
		if(player.hasVector(MOVE) && (player.getHitFloorTime() != 0 || player.getHitCeilingTime() != 0)){
			player.removeVector(MOVE);
			player.resetGravity();
		}
		
		if(new Rectangle2D.Double(getX(), getY() + 2, getWidth(), getHeight() - 2).intersects(player.getBounds())){
			if(player.canTakeDamage()) player.dealDamage(1);
			if(!player.hasVector(MOVE)) player.addVector(MOVE);
			player.resetGravity();
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		int w = (int)(getWidth() / TILE_SIZE);
		int h = (int)(getHeight() / TILE_SIZE);
		
		int a = animationTimer / (ANIMATE_TIME / 4);
		
		for(int i = 0; i < w; i++) g.drawImage(Images.acidPool[3 - a], (int)Math.round(getScreenX(x) + i * TILE_SIZE), (int)Math.round(getScreenY(y)), null);
		for(int i = 0; i < w; i++){
			for(int j = 1; j < h; j++){
				g.drawImage(Images.acidPool[7 - a], (int)Math.round(getScreenX(x) + i * TILE_SIZE), (int)Math.round(getScreenY(y) + j * TILE_SIZE), null);
			}
		}
	}

	
}
