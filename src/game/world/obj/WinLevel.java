package game.world.obj;

import java.awt.Graphics2D;

import game.world.levels.Level;
import game.world.obj.entity.Player;

public class WinLevel extends GameObject{
	
	private Level containerLevel;
	private Player player;
	
	public WinLevel(double x, double y, Level containterLevel, Player player){
		super(x, y, 0, 0, MIN_RENDER_PRIORITY, false);
		this.containerLevel = containterLevel;
		this.player = player;
	}

	@Override
	protected void tickOverride(){
		if(getCenter().distance(player.getNormalCenter()) < 16) containerLevel.winLevel();
	}
	
	@Override
	public boolean shouldRender(double x, double y){
		return false;
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){}
	
}
