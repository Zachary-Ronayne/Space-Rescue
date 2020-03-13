package game.world.obj.entity.enemy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import game.libs.Images;
import game.libs.Sounds;
import game.world.levels.Level;

public class TrainingDummy extends Enemy{
	
	private Color fill;
	
	public TrainingDummy(double x, double y, Level containerLevel){
		super(x, y, 14, 32, (int)(MAX_RENDER_PRIORITY * .4), 10, true, containerLevel);
		fill = new Color(50, 50, 127);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		keepInLevelBounds(containerLevel);
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		g.drawImage(Images.trainingDummmy, (int)Math.round(getScreenX(x) - 1), (int)Math.round(getScreenY(y)), null);
		g.setColor(fill);
		g.setFont(new Font("Impact", Font.PLAIN, 10));
		g.drawString("" + (getHealth() - 1), (int)Math.round(getScreenX(x) + 6), (int)Math.round(getScreenY(y) + 24));
	}
	
	@Override
	public void takeDamage(int damage){
		super.takeDamage(damage);
		fill = new Color(50, 50, (int)(Math.random() * 87) + 100);
		containerLevel.getEffectsPlayer().playSound(Sounds.BOSS_SHIELD_HIT);
	}
	
	@Override
	public void kill(Level l){
		setHealth(10);
	}
	
	@Override
	public Double getHitBox(){
		return new Rectangle2D.Double(getX() - 1, getY(), getWidth() + 2, getHeight());
	}
	
}
