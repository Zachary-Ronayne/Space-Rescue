package game.world.levels;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.libs.Files;
import game.libs.Images;
import game.libs.Sounds;
import game.world.obj.Decoration;
import game.world.obj.GameObject;
import game.world.obj.PlayerHealthPack;
import game.world.obj.Tile;
import game.world.obj.WinLevel;
import game.world.obj.activate.Door;
import game.world.obj.activate.OneUseMovePlatform;
import game.world.obj.activate.ScreenShift;
import game.world.obj.entity.ActivateMovingPlatform;
import game.world.obj.entity.MovingPlatform;
import game.world.obj.entity.Player;
import game.world.obj.entity.enemy.AlienMinion;
import game.world.obj.entity.enemy.BounceRangedAlien;
import game.world.obj.entity.enemy.FlyingAlien;
import game.world.obj.entity.enemy.MeleeAlien;
import game.world.obj.hazzard.AcidPool;
import game.world.obj.hazzard.Lazer;
import game.world.obj.use.Lever;
import game.world.obj.use.PlayerButton;

public final class Level2Inside{
	
	/**
	 * Sets the given level to the inside level
	 * @param l
	 */
	public static void setLevel(Level l){
		l.clear();
		
		//player
		Player p = new Player(t(13.6), t(22), l);
		l.addObject(p);
		
		//objects from the previous level for continuity
		final int R1 = GameObject.MIN_RENDER_PRIORITY + 1;
		final int R2 = (int)(GameObject.MAX_RENDER_PRIORITY * 0.8);
		l.addObject(new Decoration(t(1), t(33), R1, Images.plants[2]));
		l.addObject(new Decoration(t(4), t(31), R1, Images.plants[1]));
		l.addObject(new Decoration(t(8), t(33), R1, Images.plants[0]));
		l.addObject(new Decoration(t(2), t(22), R1, Images.plants[1]));
		l.addObject(new Decoration(t(7), t(23), R1, Images.plants[0]));
		l.addObject(new Decoration(t(2), t(28), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(4), t(30), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(8), t(32), R1, Images.stalagtite[1]));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(9 + i), t(3 - i), R1, Images.pmetalSlope[0]));
		
		//start room
		l.addObject(new MovingPlatform(2, 1, 1, new Point2D.Double(t(16), t(25)), new Point2D.Double(t(28), t(25))));
		l.addObject(new AcidPool(t(18), t(29), t(9), t(3), p, l));
		Lever lever1 = new Lever(t(28.5), t(31), true, false, 1, l.getEffectsPlayer());
		Door door1 = new Door(t(32), t(21), 1, 3, 1, Tile.PMETAL, l);
		lever1.addActivatable(door1);
		l.addObject(lever1);
		l.addObject(door1);
		l.addObject(new MeleeAlien(t(34), t(23), l, p, false));
		l.addObject(new Decoration(t(9) + 5, t(21) + 8, R1, Images.alienDoor));
		l.addObject(new Decoration(t(13) + 5, t(21) + 8, R1, Images.alienDoor));
		for(int i = 0; i < 5; i++) l.addObject(new Decoration(t(16 + i * 3), t(21), R1, Images.alienWindow));
		
		//room after start room
		l.addObject(new MovingPlatform(4, 2, 1, new Point2D.Double(t(47), t(26)), new Point2D.Double(t(47), t(33))));
		l.addObject(new FlyingAlien(t(47), t(22), l, p));
		l.addObject(new PlayerHealthPack(t(48) + 10, t(34.5), 3, p, l));
		for(int i = 0; i < 7; i++) l.addObject(new Decoration(t(36 + i * 3), t(21), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(45), t(24 + i * 3), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(51), t(24 + i * 3), R1, Images.alienWindow));
		
		//upper elevator and adjoining sections
		l.addObject(new ActivateMovingPlatform(3, 1, 1, new Point2D.Double(t(59), t(27)), new Point2D.Double(t(59), t(16)), p));
		l.addObject(new AlienMinion(t(53), t(14), l, p, false));
		l.addObject(new AlienMinion(t(53), t(15), l, p, false));
		l.addObject(new FlyingAlien(t(51), t(7), l, p));
		l.addObject(new AlienMinion(t(49), t(11), l, p, false));
		l.addObject(new AlienMinion(t(55), t(10), l, p, false));
		l.addObject(new FlyingAlien(t(64), t(5), l, p));
		l.addObject(new ActivateMovingPlatform(3, 1, 1, new Point2D.Double(t(63), t(9)), new Point2D.Double(t(26), t(9)), p){
			@Override
			protected void tickOverride(){
				super.tickOverride();
				if(start.x == t(63) && getX() < t(43)) start = new Point2D.Double(t(46), t(9));
			}
		});
		l.addObject(new AcidPool(t(27), t(13), t(20), t(3), p, l));
		l.addObject(new PlayerHealthPack(t(25.5), t(12), 3, p, l));
		l.addObject(new ActivateMovingPlatform(4, 1, 1, new Point2D.Double(t(16), t(12)), new Point2D.Double(t(16), t(7)), p));
		l.addObject(new BounceRangedAlien(t(13), t(10), l, p));
		l.addObject(new FlyingAlien(t(14), t(6), l, p));
		PlayerButton button1 = new PlayerButton(t(17.5), t(14), l.getEffectsPlayer());
		Door door2 = new Door(t(40), t(31), 1, 3, 1, Tile.PMETAL, l){
			private int wait = 0;
			@Override
			public void tickOverride(){
				super.tickOverride();
				if(wait > 0){
					wait--;
					if(wait <= 0){
						open();
					}
				}
			}
			@Override
			public void activate(){
				wait = 80;
			}
		};
		button1.addActivatable(door2);
		ScreenShift shift1 = new ScreenShift(new Point2D.Double(t(17.5), t(14)), new Point2D.Double(t(41), t(34)), 10, 90, l, p);
		button1.addActivatable(shift1);
		l.addObject(button1);
		l.addObject(door2);
		l.addObject(shift1);
		for(int i = 0; i < 6; i++) l.addObject(new Decoration(t(47 + i * 3), t(6), R1, Images.alienWindow));
		l.addObject(new Decoration(t(14), t(7), R1, Images.alienWindow));
		l.addObject(new Decoration(t(20 ), t(7), R1, Images.alienWindow));
		
		//middle section and staircase
		l.addObject(new PlayerHealthPack(t(23.5), t(37.5), 3, p, l));
		l.addObject(new MeleeAlien(t(26), t(37), l, p));
		Lever lever4 = new Lever(t(44), t(39.5), true, true, 1, l.getEffectsPlayer());
		Door door4 = new Door(t(29), t(40), 2, 4, 1, Tile.PMETAL, l);
		lever4.addActivatable(door4);
		l.addObject(lever4);
		l.addObject(door4);
		l.addObject(new BounceRangedAlien(t(32), t(42), l, p));
		for(int i = 0; i < 2; i++) l.addObject(new Decoration(t(36 + i * 3), t(40), R1, Images.alienWindow));
		
		//after staircase
		l.addObject(new AcidPool(t(19), t(54), t(103), t(3), p, l));
		l.addObject(new Lazer((int)t(21), (int)t(50), (int)t(5), 60, 90, false, true, p, l){
			@Override
			protected void tickOverride(){
				if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.LAZER_HUM);
				super.tickOverride();
			}
		});
		l.addObject(new Lazer((int)t(26), (int)t(49), (int)t(4), 60, 60, true, true, p, l));
		l.addObject(new Lazer((int)t(30), (int)t(50), (int)t(3), 60, 60, false, true, p, l));
		l.addObject(new Lazer((int)t(30), (int)t(53), (int)t(3), 0, 60, true, true, p, l));
		l.addObject(new Lazer((int)t(36), (int)t(51), (int)t(5), 0, 60, false, false, p, l));
		l.addObject(new Lazer((int)t(36), (int)t(49), (int)t(5), 60, 60, false, false, p, l));
		l.addObject(new Lazer((int)t(43), (int)t(50), (int)t(5), 30, 60, true, false, p, l){
			@Override
			protected void tickOverride(){
				if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.LAZER_HUM);
				super.tickOverride();
			}
		});
		l.addObject(new BounceRangedAlien(t(10), t(53), l, p, false));
		l.addObject(new PlayerHealthPack(t(14.5), t(48), 1, p, l));
		l.addObject(new FlyingAlien(t(55), t(52), l, p));
		l.addObject(new PlayerHealthPack(t(44.5), t(47.5), 4, p, l));
		
		//after lazers
		int[] t = new int[]{20, 12, 4};
		for(int i = 0; i < 3; i++){
			PlayerButton liftButton = new PlayerButton(t(95.5 - 8 * i), t(53), l.getEffectsPlayer());
			OneUseMovePlatform liftPlatform1 = new OneUseMovePlatform(4, 1, 4, new Point2D.Double(t(59 + 8 * i), t(57)), new Point2D.Double(t(59 + 8 * i), t(50 - i * 8)));
			OneUseMovePlatform liftPlatform2 = new OneUseMovePlatform(4, 1, 4, new Point2D.Double(t(67 + 8 * i), t(57)), new Point2D.Double(t(67 + 8 * i), t(46 - i * 8)));
			ScreenShift shift = new ScreenShift(new Point2D.Double(t(95.5 - 8 * i), t(53)), new Point2D.Double(t(63.5 + 8 * i), t(49 - i * 8)), t[i], 65 + 3 * i, l, p);
			liftButton.addActivatable(liftPlatform1);
			liftButton.addActivatable(liftPlatform2);
			liftButton.addActivatable(shift);
			l.addObject(liftButton);
			l.addObject(liftPlatform1);
			l.addObject(liftPlatform2);
			l.addObject(shift);
		}
		l.addObject(new PlayerHealthPack(t(70), t(7), 2, p, l));
		l.addObject(new FlyingAlien(t(62), t(40), l, p));
		l.addObject(new FlyingAlien(t(73), t(39), l, p));
		l.addObject(new FlyingAlien(t(77), t(32), l, p));
		l.addObject(new FlyingAlien(t(72), t(33), l, p));
		l.addObject(new AlienMinion(t(74), t(24), l, p, false));
		l.addObject(new AlienMinion(t(84), t(24), l, p, false));
		l.addObject(new MeleeAlien(t(71), t(20), l, p, false));
		l.addObject(new FlyingAlien(t(78), t(11), l, p));
		l.addObject(new BounceRangedAlien(t(87), t(10), l, p, false));
		l.addObject(new Lazer((int)t(85), (int)t(9), (int)t(3), 0, 60, false, true, p, l){
			@Override
			protected void tickOverride(){
				if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.LAZER_HUM);
				super.tickOverride();
			}
		});
		for(int i = 0; i < 6; i++) l.addObject(new Decoration(t(68 + i * 4), t(39), R1, Images.alienWindow));
		for(int i = 0; i < 6; i++) l.addObject(new Decoration(t(68 + i * 4), t(35), R1, Images.alienWindow));
		for(int i = 0; i < 5; i++) l.addObject(new Decoration(t(72 + i * 4), t(31), R1, Images.alienWindow));
		for(int i = 0; i < 4; i++) l.addObject(new Decoration(t(72 + i * 4), t(27), R1, Images.alienWindow));
		for(int i = 0; i < 3; i++) l.addObject(new Decoration(t(76 + i * 4), t(21), R1, Images.alienWindow));
		for(int i = 0; i < 2; i++) l.addObject(new Decoration(t(68 + i * 4), t(17), R1, Images.alienWindow));
		for(int i = 0; i < 1; i++) l.addObject(new Decoration(t(76 + i * 4), t(13), R1, Images.alienWindow));
		for(int i = 0; i < 2; i++) l.addObject(new Decoration(t(76 + i * 4), t(9), R1, Images.alienWindow));
		
		//far right wall after elevator
		l.addObject(new PlayerHealthPack(t(92), t(26), 3, p, l));
		l.addObject(new FlyingAlien(t(90), t(26), l, p));
		
		//next to elevator
		Lever lever3 = new Lever(t(67), t(28), true, false, 1, l.getEffectsPlayer());
		Door door3 = new Door(t(63), t(22), 3, 5, 1, Tile.PMETAL, l);
		lever3.addActivatable(door3);
		l.addObject(lever3);
		l.addObject(door3);
		
		//win location
		l.addObject(new WinLevel(t(95.5), t(11.5), l, p));
		l.addObject(new Decoration(t(94) + 5, t(9) + 8, R1, Images.alienDoor));
		
		//tile crack decorations
		double[] posC = new double[]{
			14,20, 23.5,19.25, 20,19, 18,24.5, 27,24.5, 23,25, 20,24, 28,28, 22.5,28, 18,27.25, 26,20, 31.5,22, 24,26, 24,22.5, 29,30,
			
			33,21, 38,19.5, 45,18.5, 45,20, 49,19, 52.5,20, 56,19, 56.5,23.5, 48,24, 49,24, 47,26, 49,28, 48,31, 48,32, 41,26, 43,24, 39,23.5, 36,23, 41,30, 42,33, 53,24, 56,28, 54.25,32.5, 48,32,
			42,27, 43,40, 55,25, 54,31,
			
			58,26, 59,24, 56,24.5, 59,27, 62,25, 63,23.5, 59.5,22, 61,19, 59,18, 59,16.5, 60,14, 57,13, 56,14.25, 54,13,
			
			52.5,14, 53,11, 50,12, 53,8.5, 60,8, 65,6, 63.5,5, 62,5, 59,4, 57,4, 57,5, 52.25,5, 48,4, 49,5, 45,5, 45.5,7.5, 45,11, 45,13, 48,10, 48,8, 51,9, 54,9, 63,9,
			
			42,4, 43,7, 41,11, 41,12.5, 43,13, 40.5,6, 37,5, 38,6, 37,5, 39,10, 37,11, 38,12, 39,14, 37,14, 35,5, 33,6, 35,8, 34,9.25, 35,11, 33,12, 34,13, 34,15.5, 31,8, 30,5, 30,9, 31,11, 30,13,
			31,15, 27,5, 28,7, 26,8, 26,10, 27,12, 28,14, 25,12, 24,8,
			
			21,9, 19,6, 22,5, 16,7, 16,5, 13.5,6, 13.5,9.5, 17,9, 18,7, 19,11, 15,10.5, 16,13, 18,14,
			
			38,22, 34.5,33, 33,32.5, 32,35, 29,37, 25,37, 23,38, 32.5,38, 34,29, 37,39, 40,38, 42,40, 44,39, 40,42, 42,42, 37,42.5, 34,41, 32,39, 30,41, 28,43, 23,41, 21.5,43, 21,45, 16,47, 14,46,
			31,43, 25,42, 16.5,44, 16.25,49,
			
			14,51, 12,53, 13.5,53, 10,53.5, 16,53.25, 17,51, 20,50, 20,53, 19,54, 22,55, 23,54, 24,51, 26,49, 27,51, 29,51.5, 29,54, 32,53, 34,51, 34,49, 37,50, 36,51, 40,51, 38,54, 36,51, 40.5,53,
			42,52, 44,55, 45,52, 44,50, 47,49, 45,57, 48,51, 53,53, 54,51, 55,50, 57,52.5, 22,52, 31,50, 52,50, 44,47,
			
			58,54, 59,53, 61,55, 59,56, 66,54, 68,56, 70,54, 74,55, 75,55, 77,54, 78,56, 82,54, 84,55.5, 86,54, 90,55, 92,56, 93,54.5, 57,51, 62,51, 56,52, 66,51, 64,50.5, 69,50, 72,52, 71,49, 76,50,
			74,51, 75,53, 77,53, 79,50, 80,52, 78,53, 82,51, 84,53, 86,52.5, 82,50, 86,52.5, 88.5,51, 93,52, 90,53, 95,50, 98,52, 58,49, 58.5,45, 61,48, 67,46, 70,48, 66,49, 73,48, 75,45, 73,44, 77,48,
			79,46, 82,49, 84,47, 87,49, 89,48, 92,49, 95,47, 95,48.5, 61,43, 63,45, 64.5,43, 71,43, 69,45, 74,43, 75,45, 78,44, 80,45.5, 82,44, 82,45, 86,45, 88,47, 91,45, 93,47, 95,44, 65,39, 67,40.5,
			71,41, 74,43, 75,39, 78,41, 79,39, 82,42, 85,41, 87,39, 89,41, 91,42, 92,39, 66,36, 70,34, 74,36, 75.5,34, 77,33, 80,34, 83,33, 86,33, 87,33.5, 90,32, 91,35, 69,30, 71.5,29, 74,30, 77,29,
			81,30, 83,29, 86,28, 90,30, 88,30, 89,27, 92,25, 67,24, 69,26, 70,23, 65,26, 65,48, 59,46, 68,38, 71,37, 87,44, 84,37, 87,36, 
			
			70,23, 72,22, 74,24, 75.5,24, 78,23, 82,22, 84,24, 86,22, 88,25, 87,20, 83,20, 78,20, 70,18.5, 67,17, 71,16, 75,17, 80,18, 82,24, 87,22, 86,20, 83,19, 80,17, 78,18, 74,17, 71,19, 68,16, 
			68,13, 71,12, 72,13, 75,12, 77,15, 80,14, 84,15, 84,13.5, 78,11, 82.5,12, 72,9, 75,8, 71,7, 69,6, 73,5, 76,6, 80,7, 83,8, 86,9, 88,11, 89,10, 91,10, 93,11, 92,9
		};
		double[] posB = new double[]{
			10,25, 13,25, 10,27, 16,27, 14,29, 17,30, 13,33, 16,35, 20,34, 24,32, 26.5,34, 26,36, 30,35, 30,32, 31,29, 34.5,30, 37,29, 36,26, 31,25.5, 30.5,27, 37,26, 39,27, 38,29, 38,31, 13,40, 9,42,
			6,43, 9,46, 10,44, 13,47, 11,45, 9,49, 8,47.5, 10,49, 6,50, 11,51, 8,52, 6,50, 7,54, 6.5,55, 10,57, 13,55, 12.5,59, 14,57, 17,56, 16,59, 20,59, 22,57, 25,58, 25,55, 27,56, 27,53, 29,59,
			31,57, 33,59, 35,56, 36,54, 37,57, 39,58.5, 41,57, 40,55, 40,54, 43,57, 44,59, 45,58, 47,57, 48,55, 46,54, 48,53.5, 50,55, 49,57, 51,55, 53,54, 53.5,57, 52,59, 56,57, 58,58.5, 62,57, 65,59,
			62,66, 65,59, 68.5,67, 70,58, 72,56.5, 71,54, 74,58, 76.5,59, 78,57, 80,54, 81,57.5, 82,59, 84,57, 84,58, 87,58, 88,56, 90,59, 92,57, 95,59, 96,57, 95,54, 98,59, 99,56, 99,53, 98,50, 97,48,
			99,46, 96,45, 97,43, 99,44, 96,42, 99,40, 96,38, 93.5,36, 93,34, 97,35, 99,32, 96,31, 94,29, 91,29, 92,28, 95,28, 98,26.5, 96,25, 94,23, 98,22, 95,20, 90,21, 89,19, 87,17, 95,16, 99,18,
			98,14, 93,13, 97,11, 95,7, 97.5,3, 95,1, 93,2, 90,5, 90,8, 88,7, 85,4, 84,5, 81,5, 82,2, 79,0, 78,2, 76,3, 74,1, 74,4, 71,2, 73,0.5, 72,72, 68,3, 66,1, 66,5, 69,7, 67,9, 70,10, 74,25,
			84,25, 85,25, 77,18, 73,12, 84,12, 66,11, 61,9, 57,10, 56,12, 59,11, 63,13, 66,12, 67,13, 65,15, 66,18, 62,19, 64,21, 68,20, 70,21, 61,10, 64,0, 64.5,2, 62,2, 59,1, 57,3, 55,1, 54,1.5,
			53,3, 50,1, 48,0, 47,3, 46,1, 44,1, 43,0, 41,3, 40,1, 37,3, 35,1, 33,0, 33,3, 32,2, 32,5, 36,4, 32,2, 30,0, 29,3, 25,2, 25,6, 21,0, 20,2, 18,4, 16,2, 14,0, 12,2, 13,4, 10,4, 9,6, 13,7,
			9,11, 11.5,14, 14,12, 15,14, 15.5,16, 20,15, 21,14, 23,11, 25,10, 25,9, 25,14, 25,17.5, 28,17, 31,18, 33,16, 33,19, 36,17, 39,16, 29,14, 29,10, 33,13, 36,10, 36,11, 40,18, 40,15, 40,9,
			44,11, 45,14, 42,18, 45,17, 47,15, 48,13, 51,14, 51,16, 52,18, 54,16, 55,17, 57,18, 57,20, 58,17, 11,8, 89,13, 10,18, 12,15, 16,17, 12,21,
			
			58,28, 60,31.5, 64,29, 66,28, 67,30, 65,32, 63,33, 61,31, 58,33, 57,31, 58,34, 61,35, 65,34, 66,32, 64,35, 63,36, 60,37, 58,36.5, 55,36, 53,34, 53,37, 58,38, 63,37, 65,35, 62,40, 55,40,
			51,38, 52,39, 49,37, 51,34, 45,36, 41,34, 40,36, 37,37, 35,35, 36,37, 39,34, 44,35, 46,39, 50,39, 52,42, 54,44, 58,44, 55,46, 52,48, 50,44, 48,46, 46,45, 44,43, 42,46, 41,49, 45,49, 38,47,
			36,45, 34,44, 34,46, 31,48, 31,46, 29,45.5, 27,47, 26,45, 24,47, 21,49, 19,47, 24,45
		};
		double[] moss = new double[]{
			18,45, 29,42, 30,41, 23,44, 19.5,44, 17,46, 14,44, 15.5,44, 14,49, 17,50.5, 14,53, 11,53, 15,54, 19,53, 22,51.5, 26,49,27,51, 30,53, 32,50, 35,49, 37,51, 38,53, 40,51, 43,52, 44,50, 44,47,
			47,48, 46,51, 49.5,52, 51,50, 53,52, 54,51, 58,52, 61.5,50.5, 65,52, 68,52, 67,49, 71,51, 70,49.5, 74,51, 78,49, 80,51, 82,50, 84,50, 87,51, 85,53, 89,52.5, 92,51, 94,52, 97,50, 96,52,
			93,48, 91,47, 94,44, 91,42, 87,44, 85,43, 83,46, 81,47, 79,43, 76,45, 78,47, 76,45, 77,43, 74,44.5, 71,45, 69,43, 66,44, 64,42, 61,44, 61,47, 32,53, 23,49, 17,62
		};
		for(int i = 0; i < posC.length; i+=2) l.addObject(new Decoration(t(posC[i]), t(posC[i + 1]), R1, Images.wallCracks[(int)(3452 + i * .5 * 7) % 4]));
		for(int i = 0; i < posB.length; i+=2) l.addObject(new Decoration(t(posB[i]), t(posB[i + 1]), R2, Images.blockCracks[(int)(7051 + i * .5 * 7) % 4]));
		for(int i = 0; i < moss.length; i+=2) l.addObject(new Decoration(t(moss[i]), t(moss[i + 1]), R1, Images.pMoss[(int)(2976 + i * .5 * 7) % 4]));
		
		//tiles
		try{
			BufferedImage grid = ImageIO.read(new File(Files.NEW_LEVELS + "/2Inside.png"));
			l.setTiles(Level.loadTileFromImage(grid));
		}catch(IOException e){
			System.out.println("Failed to create: level 2 inside");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param t
	 * @return The length of t tiles
	 */
	public static double t(double t){
		return t * Tile.TILE_SIZE;
	}
	
}