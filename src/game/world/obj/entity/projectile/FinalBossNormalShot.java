package game.world.obj.entity.projectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class FinalBossNormalShot extends EnemyNormalShot{

	public static final double SIZE = 10;
	
	private double angle;
	
	public FinalBossNormalShot(double x, double y, double angle, Level containerLevel, Player player){
		super(x, y, angle, containerLevel, player);
		
		path.setAmount(2.8);
		
		setWidth(SIZE);
		setHeight(SIZE);
		
		angle = Math.random() * 360;
		
		addVector(gravity);
	}
	
	@Override
	public void addGravity(){
		gravity.addAmount(GRAVITY_CONSTANT * .1);
	}
	
	
	@Override
	protected boolean testEndPath(){
		return inSolidObject(containerLevel);
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		angle += 10;
		if(angle >= 360) angle = 0;
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		Graphics2D g2 = (Graphics2D)g.create();
		g2.rotate(Math.toRadians(angle), getScreenCenterX(x), getScreenCenterY(y));
		
		BufferedImage img = getImage();
		double diff = (img.getWidth() - getWidth()) / 2;
		
		g2.drawImage(img, (int)Math.round(getScreenX(x) - diff), (int)Math.round(getScreenY(y) - diff), null);
	}
	
	protected BufferedImage getImage(){
		return Images.finalBossNormalShot;
	}
	
}
