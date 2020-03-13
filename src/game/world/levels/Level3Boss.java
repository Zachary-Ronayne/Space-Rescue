package game.world.levels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.libs.Files;
import game.libs.Images;
import game.world.obj.Decoration;
import game.world.obj.GameObject;
import game.world.obj.Tile;
import game.world.obj.entity.Player;
import game.world.obj.entity.enemy.FinalBoss;

public final class Level3Boss{
	
	/**
	 * Sets the given level to the boss level
	 * @param l
	 */
	public static void setLevel(Level l){
		l.clear();
		
		//tiles
		try{
			BufferedImage grid = ImageIO.read(new File(Files.NEW_LEVELS + "/3Boss.png"));
			l.setTiles(Level.loadTileFromImage(grid));
		}catch(IOException e){
			System.out.println("Failed to create: level 3 boss");
			e.printStackTrace();
		}
		
		//player
		Player p = new Player(t(1.6), t(14), l);
		l.addObject(p);
		
		l.setCameraObject(p);

		//decorations
		final int R1 = GameObject.MIN_RENDER_PRIORITY + 1;
		final int R2 = (int)(GameObject.MAX_RENDER_PRIORITY * 0.8);
		l.addObject(new Decoration(t(1) + 5, t(13) + 8, R1 + 2, Images.alienDoor));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(5), t(6 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(8), t(6 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(11), t(3 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(15), t(3 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(20), t(3 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(24), t(3 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(27), t(6 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(30), t(6 + i * 3), R1, Images.alienWindow));
		double[] posC = new double[]{
			12,1, 16,2, 20.5,2, 22,1, 25,2, 28,3, 29,6, 26,4, 23,6, 22,4, 19,3, 17,5, 14,4, 12,5, 10,4, 7,3.5, 6,4, 4,6, 7,8, 13,9, 16,8, 18,9, 19,9, 23,8, 26,9, 30,8, 33,9, 34,12, 30,11, 29,13, 25,11,
			20,11, 19,13, 16,11, 14,12, 11,11, 10,13, 7,11, 4,12, 2,13, 4,15, 1,15, 11,15, 13,14, 17,15, 21,15, 24,14, 27,14, 29,15, 32,14, 34,15
		};
		double[] posB = new double[]{
			1,1, 4,2, 1,4, 0,5.5, 1,8, 1,11, 0,14, 2,16, 3,16, 7,16, 9,16, 13,16, 18,16, 21,16, 24,16, 28,16, 32,16, 33,16, 35,16, 36,15, 35,12, 36,9, 34,7, 36,5, 34,5, 33,6, 36,4, 33,5, 36,4, 32,2,
			34,0, 27,1, 29,0, 22,0, 19,0, 18,0, 12.5,0, 9,0
		};
		for(int i = 0; i < posC.length; i+=2) l.addObject(new Decoration(t(posC[i]), t(posC[i + 1]), R1, Images.wallCracks[(int)(3452 + i * .5 * 7) % 4]));
		for(int i = 0; i < posB.length; i+=2) l.addObject(new Decoration(t(posB[i]), t(posB[i + 1]), R2, Images.blockCracks[(int)(7051 + i * .5 * 7) % 4]));
		l.addObject(new Decoration(t(17.5), t(1), R1 + 2, Images.trappedFriend[0]));
		
		//boss object
		l.addObject(new FinalBoss(l, p));
	}
	
	/**
	 * @param t
	 * @return The length of t tiles
	 */
	public static double t(double t){
		return t * Tile.TILE_SIZE;
	}
}
