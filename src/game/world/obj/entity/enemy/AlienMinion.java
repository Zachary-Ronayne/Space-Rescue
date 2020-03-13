package game.world.obj.entity.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.libs.Sounds;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class AlienMinion extends MeleeAlien{
	
	public static final double SIZE = 8;
	
	public AlienMinion(double x, double y, Level containerLevel, Player track, boolean defaultAIOn){
		super(x, y, containerLevel, track, defaultAIOn);
		setWidth(SIZE);
		setHeight(SIZE);
		setHealth(1);
		
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		BufferedImage img = Images.minionAlienFrames[getAnimationFrameIndex()];
		if(faceLeft) g.drawImage(img, (int)Math.round(getScreenX(x) - 1 + img.getWidth()), (int)Math.round(getScreenY(y) - 1), -img.getWidth(), img.getHeight(), null);
		else g.drawImage(img, (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y) - 1), null);
	}
	
	@Override
	protected double getRange(){
		return 1;
	}
	
	@Override
	protected double getSpeed(){
		return 1.5;
	}
	
	@Override
	protected double getJump(){
		return 2.5;
	}
	
	protected void playDamageNoise(){
		containerLevel.getEffectsPlayer().playSound(Sounds.ALIEN_DAMAGE_3);
	}
	
}
