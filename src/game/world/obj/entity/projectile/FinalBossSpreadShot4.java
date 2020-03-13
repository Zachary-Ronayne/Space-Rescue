package game.world.obj.entity.projectile;

import java.awt.image.BufferedImage;

import game.libs.Images;
import game.world.levels.Level;
import game.world.obj.entity.Player;

public class FinalBossSpreadShot4 extends FinalBossNormalShot{

	public static double SIZE = 16;
	
	public FinalBossSpreadShot4(double x, double y, double angle, Level containerLevel, Player player){
		super(x, y, angle, containerLevel, player);
		setWidth(SIZE);
		setHeight(SIZE);
		path.setAmount(3);
	}
	
	@Override
	protected void endPath(){
		super.endPath();
		for(int i = 0; i < 4; i++){
			FinalBossNormalShot f = new FinalBossNormalShot(
					getCenterX() - FinalBossNormalShot.SIZE / 2,
					getCenterY() - FinalBossNormalShot.SIZE / 2,
					i * 90 + 45, containerLevel, player);
			
			f.path.setAmount(1.5);
			containerLevel.addObject(f);
		}
	}
	
	protected BufferedImage getImage(){	
		return Images.finalBossSpreadShot4;
	}
	
}
