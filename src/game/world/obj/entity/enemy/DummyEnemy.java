package game.world.obj.entity.enemy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.world.levels.Level;
import game.world.obj.entity.Player;

public class DummyEnemy extends Enemy{
	
	public static final int MAX_TIME = 60;
	
	private int timer;
	private Player player;
	
	public DummyEnemy(double x, double y, Level containerLevel, Player player){
		super(x, y, TILE_SIZE, TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .7), 5, true, containerLevel);
		timer = MAX_TIME;
		this.player = player;
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		
		if(timer > 0) timer--;
		else{
			if(player.getNormalCenter().distance(getCenter()) < (player.getWidth() + getWidth()) * 1.5) player.dealDamage(1);
			timer = MAX_TIME;
		}
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		g.setColor(new Color(255, 0, 0, 128 - (int)(128 * (timer / (double)MAX_TIME))));
		g.fillOval((int)Math.round(getScreenX(x) - timer / 2 + 1), (int)Math.round(getScreenY(y) - timer / 2 + 1), (int)getWidth() + timer + 2, (int)getHeight() + timer + 2);
		
		g.setColor(Color.RED);
		g.fillRect((int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight());
		g.setColor(Color.BLACK);
		g.setFont(new Font("Impact", Font.PLAIN, 15));
		g.drawString("" + getHealth(), (int)Math.round(getScreenX(x) + 3), (int)Math.round(getScreenY(y) + 17));
	}
	
}
