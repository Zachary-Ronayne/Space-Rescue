package game.world.obj;

import java.awt.Graphics2D;

import game.libs.Images;
import game.libs.Sounds;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class PlayerHealthPack extends GameObject{
	
	private Player player;
	private Level containerLevel;
	
	private int heal;
	
	private boolean up;
	private double baseLoc;
	private int loc;
	
	public PlayerHealthPack(double x, double y, int heal, Player player, Level containerLevel){
		super(x, y, 12, 12, (int)(MAX_RENDER_PRIORITY * .3), false);
		this.player = player;
		this.containerLevel = containerLevel;
		
		this.heal = heal;
		
		up = true;
		baseLoc = getY();
		loc = 0;
	}
	
	@Override
	protected void tickOverride(){
		if(up){
			loc++;
			if(loc > 40) up = false;
		}
		else{
			loc--;
			if(loc < -40) up = true;
		}
		
		setY(baseLoc + loc / 8);
		
		if(!player.isFullHealth() && getBounds().intersects(player.getBounds())){
			player.heal(heal);
			containerLevel.getEffectsPlayer().playSound(Sounds.HEALTH_PACK);
			containerLevel.removeObject(this);
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(heal > 0 && heal < 5) g.drawImage(Images.healthPacks[heal - 1], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
		else if(heal >= 5) g.drawImage(Images.healthPacks[3], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
	}
	
}
