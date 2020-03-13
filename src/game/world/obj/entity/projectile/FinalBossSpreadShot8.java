package game.world.obj.entity.projectile;

import java.awt.image.BufferedImage;

import game.libs.Images;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class FinalBossSpreadShot8 extends FinalBossSpreadShot4{
	
	public FinalBossSpreadShot8(double x, double y, double angle, Level containerLevel, Player player){
		super(x, y, angle, containerLevel, player);
	}
	
	@Override
	protected void endPath(){
		super.endPath();
		for(int i = 0; i < 8; i++){
			double a = i * 45;
			if(a == 0) a = 350;
			else if(a == 180) a = 190;
			FinalBossNormalShot f = new FinalBossNormalShot(
					getCenterX() - FinalBossNormalShot.SIZE / 2,
					getCenterY() - FinalBossNormalShot.SIZE / 2,
					a, containerLevel, player);
			
			f.path.setAmount(1.5);
			containerLevel.addObject(f);
		}
	}
	
	protected BufferedImage getImage(){
		return Images.finalBossSpreadShot8;
	}
	
}
