package game.world.obj.hazzard;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import game.libs.Images;
import game.util.calc.Vector2D;
import game.world.obj.GameObject;
import game.world.obj.entity.Player;

public class Spike extends GameObject{
	
	private Player player;
	
	private static final Vector2D MOVE = new Vector2D(2.1, 270);
	
	/**
	 * @param x
	 * @param y
	 * @param numSpikes the amount of tiles this spike takes up, must be an integer
	 * @param player
	 */
	public Spike(double x, double y, int numSpikes, Player player){
		super(x, y, TILE_SIZE * numSpikes, TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .2), false);
		this.player = player;
	}
	
	@Override
	protected void tickOverride(){
		if(player.hasVector(MOVE) && (player.getHitFloorTime() != 0 || player.getHitCeilingTime() != 0)){
			player.removeVector(MOVE);
			player.resetGravity();
		}
		
		if(new Rectangle2D.Double(getX(), getY() + 3, getWidth(), getHeight() - 3).intersects(player.getBounds())){
			if(player.canTakeDamage()) player.dealDamage(1);
			if(!player.hasVector(MOVE)) player.addVector(MOVE);
			player.resetGravity();
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		int num = (int)(getWidth() / TILE_SIZE);
		
		if(num == 1) g.drawImage(Images.spikes[3], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
		else{
			g.drawImage(Images.spikes[0], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
			for(int i = 1; i < num - 1; i++) g.drawImage(Images.spikes[1], (int)Math.round(getScreenX(x) + i * TILE_SIZE), (int)Math.round(getScreenY(y)), null);
			g.drawImage(Images.spikes[2], (int)Math.round(getScreenX(x) + (num - 1) * TILE_SIZE), (int)Math.round(getScreenY(y)), null);
		}
	}
	
}
