package game.world.levels;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.libs.Files;
import game.libs.Images;
import game.world.obj.Decoration;
import game.world.obj.GameObject;
import game.world.obj.PlayerHealthPack;
import game.world.obj.Tile;
import game.world.obj.WinLevel;
import game.world.obj.activate.Door;
import game.world.obj.entity.Player;
import game.world.obj.entity.enemy.AlienMinion;
import game.world.obj.entity.enemy.BounceRangedAlien;
import game.world.obj.entity.enemy.FlyingAlien;
import game.world.obj.entity.enemy.MeleeAlien;
import game.world.obj.entity.projectile.EnemyBounceProjectile;
import game.world.obj.hazzard.Spike;
import game.world.obj.use.Lever;

public final class Level1Outside{
	
	/**
	 * Sets the given level to the outside level
	 * @param l
	 */
	public static void setLevel(Level l){
		l.clear();
		
		final int R1 = GameObject.MIN_RENDER_PRIORITY + 1;
		final int R2 = (int)(GameObject.MAX_RENDER_PRIORITY * 0.8);
		
		//player
		Player p = new Player(t(5.5), t(29), l);
		
		l.addObject(p);
		
		//before enter cave
		l.addObject(new MeleeAlien(t(16), t(32), l, p));
		l.addObject(new Spike(t(11), t(29), 2, p));
		l.addObject(new Spike(t(17), t(33), 2, p));
		l.addObject(new Spike(t(20), t(26), 2, p));
		l.addObject(new Decoration(t(2), t(29), R1, Images.plants[0]));
		l.addObject(new Decoration(t(19), t(32), R1, Images.plants[1]));
		l.addObject(new Decoration(t(10), t(31), R1, Images.plants[0]));
		l.addObject(new Decoration(t(22), t(25), R1, Images.plants[2]));
		l.addObject(new Decoration(t(20), t(21), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(21), t(21), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(18), t(17), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(25), t(23), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(28), t(27), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(30), t(25), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(31), t(25), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(19), t(29), R1, Images.stalagtite[0]));
		
		//in cave before door
		l.addObject(new PlayerHealthPack(t(32), t(26.5), 2, p, l));
		l.addObject(new Spike(t(26), t(35), 1, p));
		l.addObject(new MeleeAlien(t(15), t(43), l, p));
		Lever lever1 = new Lever(t(9), t(43), true, false, 0, l.getEffectsPlayer());
		Door door1 = new Door(t(29), t(40), 1, 2, 1, Tile.ROCK, l);
		lever1.addActivatable(door1);
		l.addObject(lever1);
		l.addObject(door1);
		l.addObject(new Spike(t(24), t(42), 3, p));
		l.addObject(new Spike(t(15), t(44), 5, p));
		l.addObject(new Decoration(t(30), t(35), R1, Images.plants[1]));
		l.addObject(new Decoration(t(28), t(41), R1, Images.plants[0]));
		l.addObject(new Decoration(t(20), t(43), R1, Images.plants[0]));
		l.addObject(new Decoration(t(12), t(43), R1, Images.plants[2]));
		l.addObject(new Decoration(t(11), t(43), R1, Images.plants[1]));
		l.addObject(new Decoration(t(25), t(37), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(20), t(39), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(19), t(39), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(12), t(41), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(10), t(41), R1, Images.stalagtite[0]));
		
		//in cave after door
		l.addObject(new BounceRangedAlien(t(37), t(43), l, p));
		l.addObject(new Spike(t(36), t(46), 3, p));
		l.addObject(new Spike(t(40), t(45), 2, p));
		l.addObject(new PlayerHealthPack(t(39.25), t(38.5), 2, p, l));
		l.addObject(new MeleeAlien(t(44), t(36), l, p, false));
		l.addObject(new Decoration(t(31), t(43), R1, Images.plants[2]));
		l.addObject(new Decoration(t(35), t(45), R1, Images.plants[0]));
		l.addObject(new Decoration(t(39), t(45), R1, Images.plants[1]));
		l.addObject(new Decoration(t(44), t(43), R1, Images.plants[1]));
		l.addObject(new Decoration(t(47), t(39), R1, Images.plants[2]));
		l.addObject(new Decoration(t(43), t(36), R1, Images.plants[2]));
		l.addObject(new Decoration(t(40), t(35), R1, Images.plants[0]));
		l.addObject(new Decoration(t(31), t(39), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(35), t(40), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(36), t(41), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(39), t(41), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(41), t(39), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(43), t(39), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(45), t(34), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(40), t(34), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(40), t(30), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(42), t(29), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(43), t(28), R1, Images.stalagtite[0]));
		
		//exit of cave
		l.addObject(new MeleeAlien(t(44), t(31), l, p));
		l.addObject(new Spike(t(47), t(39), 1, p));
		l.addObject(new Spike(t(44), t(32), 3, p));
		l.addObject(new Decoration(t(47), t(31), R1, Images.plants[1]));
		l.addObject(new Decoration(t(49), t(30), R1, Images.plants[0]));
		l.addObject(new Decoration(t(50), t(30), R1, Images.plants[2]));
		l.addObject(new Decoration(t(45), t(27), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(46), t(27), R1, Images.stalagtite[0]));
		
		//right after cave
		l.addObject(new PlayerHealthPack(t(45) + 2, t(21.5), 3, p, l));
		l.addObject(new BounceRangedAlien(t(52), t(28), l, p));
		l.addObject(new BounceRangedAlien(t(46), t(21), l, p){
			@Override
			protected void attack(){
				if(attackTimer > 0) return;
				
				attackTimer = (int)(ATTACK_TIME * 1.5);
				double a = Math.toDegrees(getAngleTo(player));
				containerLevel.addObject(new EnemyBounceProjectile(getX() + getWidth(), getY() + 22, a, containerLevel, player));
			}
			
			@Override
			protected double getRange(){
				return 10000;
			}
			
			@Override
			protected double getSpeed(){
				return 0;
			}
		});
		Lever lever2 = new Lever(t(59), t(19), true, false, 0, l.getEffectsPlayer());
		Door door2 = new Door(t(57), t(28), 1, 4, 1, Tile.ROCK, l);
		lever2.addActivatable(door2);
		l.addObject(lever2);
		l.addObject(door2);
		l.addObject(new Decoration(t(53), t(29), R1, Images.plants[2]));
		l.addObject(new Decoration(t(59), t(26), R1, Images.plants[0]));
		l.addObject(new Decoration(t(57), t(26), R1, Images.plants[1]));
		l.addObject(new Decoration(t(49), t(24), R1, Images.plants[1]));
		l.addObject(new Decoration(t(45), t(22), R1, Images.plants[2]));
		l.addObject(new Decoration(t(56), t(20), R1, Images.plants[1]));
		l.addObject(new Decoration(t(58), t(19), R1, Images.plants[0]));
		l.addObject(new Decoration(t(57), t(14), R1, Images.plants[0]));
		l.addObject(new Decoration(t(58), t(14), R1, Images.plants[2]));
		l.addObject(new Decoration(t(48), t(28), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(50), t(27), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(47), t(22), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(56), t(22), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(48), t(19), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(49), t(17), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(61), t(11), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(58), t(9), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(57), t(8), R1, Images.stalagtite[1]));
		
		//secret above cave
		l.addObject(new Spike(t(41), t(14), 5, p));
		l.addObject(new PlayerHealthPack(t(37.5), t(19.5), 3, p, l));
		l.addObject(new Decoration(t(46), t(13), R1, Images.plants[2]));
		l.addObject(new Decoration(t(48), t(12), R1, Images.plants[1]));
		l.addObject(new Decoration(t(38), t(13), R1, Images.plants[2]));
		l.addObject(new Decoration(t(34), t(18), R1, Images.plants[0]));
		l.addObject(new Decoration(t(37), t(20), R1, Images.plants[1]));
		l.addObject(new Decoration(t(38), t(20), R1, Images.plants[2]));
		l.addObject(new Decoration(t(30), t(13), R1, Images.plants[1]));
		l.addObject(new Decoration(t(28), t(12), R1, Images.plants[0]));
		l.addObject(new Decoration(t(24), t(11), R1, Images.plants[2]));
		l.addObject(new Decoration(t(18), t(14), R1, Images.plants[2]));
		l.addObject(new Decoration(t(37), t(17), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(39), t(18), R1, Images.stalagtite[1]));
		
		//after second door
		l.addObject(new AlienMinion(t(63), t(34), l, p, true));
		l.addObject(new AlienMinion(t(64.5), t(34), l, p, true));
		l.addObject(new AlienMinion(t(70), t(32), l, p, false));
		l.addObject(new AlienMinion(t(71), t(32), l, p, false));
		l.addObject(new Spike(t(63), t(34), 3, p));
		l.addObject(new Decoration(t(59), t(32), R1, Images.plants[1]));
		l.addObject(new Decoration(t(62), t(33), R1, Images.plants[2]));
		l.addObject(new Decoration(t(67), t(33), R1, Images.plants[1]));
		l.addObject(new Decoration(t(70), t(32), R1, Images.plants[0]));
		l.addObject(new Decoration(t(73), t(31), R1, Images.plants[2]));
		l.addObject(new Decoration(t(76), t(30), R1, Images.plants[0]));
		l.addObject(new Decoration(t(82), t(32), R1, Images.plants[1]));
		l.addObject(new Decoration(t(75), t(22), R1, Images.plants[2]));
		l.addObject(new Decoration(t(72), t(16), R1, Images.plants[2]));
		l.addObject(new Decoration(t(71), t(12), R1, Images.plants[0]));
		l.addObject(new Decoration(t(69), t(8), R1, Images.plants[1]));
		l.addObject(new Decoration(t(64), t(6), R1, Images.plants[1]));
		l.addObject(new Decoration(t(65), t(6), R1, Images.plants[0]));
		l.addObject(new Decoration(t(59), t(4), R1, Images.plants[2]));
		l.addObject(new Decoration(t(60), t(30), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(62), t(31), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(66), t(31), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(69), t(29), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(70), t(28), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(71), t(27), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(73), t(26), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(74), t(26), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(75), t(26), R1, Images.stalagtite[1]));
		
		//mountain with little platforms stand on
		l.addObject(new MeleeAlien(t(79), t(30), l, p, false));
		l.addObject(new BounceRangedAlien(t(92), t(32), l, p, false));
		l.addObject(new BounceRangedAlien(t(102), t(28), l, p, false));
		l.addObject(new BounceRangedAlien(t(99), t(16), l, p, false));
		l.addObject(new AlienMinion(t(107), t(34), l, p, false));
		l.addObject(new AlienMinion(t(85), t(34), l, p, false));
		l.addObject(new AlienMinion(t(90), t(25), l, p, false));
		l.addObject(new AlienMinion(t(101), t(28), l, p, false));
		l.addObject(new AlienMinion(t(93), t(18), l, p, false));
		l.addObject(new Spike(t(87), t(35), 3, p));
		l.addObject(new Spike(t(98), t(37), 3, p));
		l.addObject(new PlayerHealthPack(t(107.5), t(33.5), 4, p, l));
		l.addObject(new Decoration(t(85), t(34), R1, Images.plants[2]));
		l.addObject(new Decoration(t(90), t(34), R1, Images.plants[1]));
		l.addObject(new Decoration(t(96), t(36), R1, Images.plants[2]));
		l.addObject(new Decoration(t(103), t(36), R1, Images.plants[0]));
		l.addObject(new Decoration(t(106), t(34), R1, Images.plants[2]));
		l.addObject(new Decoration(t(107), t(34), R1, Images.plants[1]));
		l.addObject(new Decoration(t(103), t(30), R1, Images.plants[1]));
		l.addObject(new Decoration(t(100), t(28), R1, Images.plants[0]));
		l.addObject(new Decoration(t(97), t(31), R1, Images.plants[2]));
		l.addObject(new Decoration(t(93), t(26), R1, Images.plants[0]));
		l.addObject(new Decoration(t(90), t(25), R1, Images.plants[1]));
		l.addObject(new Decoration(t(89), t(24), R1, Images.plants[0]));
		l.addObject(new Decoration(t(91), t(19), R1, Images.plants[2]));
		l.addObject(new Decoration(t(95), t(19), R1, Images.plants[1]));
		l.addObject(new Decoration(t(99), t(17), R1, Images.plants[1]));
		l.addObject(new Decoration(t(102), t(18), R1, Images.plants[0]));
		l.addObject(new Decoration(t(104), t(18), R1, Images.plants[2]));
		l.addObject(new Decoration(t(105), t(19), R1, Images.plants[1]));
		l.addObject(new Decoration(t(87), t(29), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(89), t(31), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(93), t(31), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(94), t(30), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(97), t(33), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(101), t(34), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(102), t(33), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(108), t(32), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(105), t(28), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(103), t(25), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(101), t(24), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(99), t(22), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(98), t(20), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(95), t(23), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(92), t(24), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(90), t(23), R1, Images.stalagtite[1]));
		
		//after mountain
		l.addObject(new FlyingAlien(t(116), t(26), l, p));
		l.addObject(new FlyingAlien(t(126), t(36), l, p));
		l.addObject(new FlyingAlien(t(134), t(40), l, p));
		l.addObject(new FlyingAlien(t(128), t(19), l, p));
		l.addObject(new FlyingAlien(t(129), t(14), l, p));
		l.addObject(new FlyingAlien(t(134), t(24), l, p));
		l.addObject(new FlyingAlien(t(143), t(17), l, p));
		Lever lever3 = new Lever(t(125), t(11), false, false, 0, l.getEffectsPlayer());
		Door door3 = new Door(t(137), t(15), 1, 3, 1, Tile.ROCK, l);
		lever3.addActivatable(door3);
		l.addObject(lever3);
		l.addObject(door3);
		l.addObject(new Spike(t(106), t(22), 1, p));
		l.addObject(new Spike(t(119), t(31), 2, p));
		l.addObject(new Spike(t(125), t(36), 2, p));
		l.addObject(new Spike(t(132), t(40), 3, p));
		l.addObject(new Spike(t(136), t(40), 2, p));
		l.addObject(new Spike(t(133), t(32), 2, p));
		l.addObject(new Spike(t(128), t(27), 1, p));
		l.addObject(new Spike(t(124), t(21), 1, p));
		l.addObject(new Spike(t(123), t(15), 2, p));
		l.addObject(new Spike(t(131), t(18), 1, p));
		l.addObject(new PlayerHealthPack(t(148) + 2, t(37.5), 3, p, l));
		l.addObject(new PlayerHealthPack(t(133) + 2, t(21.5), 4, p, l));
		l.addObject(new Decoration(t(107), t(22), R1, Images.plants[0]));
		l.addObject(new Decoration(t(109), t(24), R1, Images.plants[2]));
		l.addObject(new Decoration(t(115), t(28), R1, Images.plants[0]));
		l.addObject(new Decoration(t(118), t(30), R1, Images.plants[1]));
		l.addObject(new Decoration(t(123), t(33), R1, Images.plants[1]));
		l.addObject(new Decoration(t(125), t(36), R1, Images.plants[2]));
		l.addObject(new Decoration(t(127), t(36), R1, Images.plants[1]));
		l.addObject(new Decoration(t(130), t(38), R1, Images.plants[0]));
		l.addObject(new Decoration(t(135), t(39), R1, Images.plants[2]));
		l.addObject(new Decoration(t(141), t(38), R1, Images.plants[2]));
		l.addObject(new Decoration(t(144), t(36), R1, Images.plants[1]));
		l.addObject(new Decoration(t(148), t(38), R1, Images.plants[0]));
		l.addObject(new Decoration(t(137), t(33), R1, Images.plants[2]));
		l.addObject(new Decoration(t(133), t(32), R1, Images.plants[0]));
		l.addObject(new Decoration(t(130), t(29), R1, Images.plants[0]));
		l.addObject(new Decoration(t(128), t(27), R1, Images.plants[2]));
		l.addObject(new Decoration(t(125), t(23), R1, Images.plants[1]));
		l.addObject(new Decoration(t(124), t(21), R1, Images.plants[0]));
		l.addObject(new Decoration(t(122), t(14), R1, Images.plants[2]));
		l.addObject(new Decoration(t(125), t(15), R1, Images.plants[1]));
		l.addObject(new Decoration(t(131), t(18), R1, Images.plants[2]));
		l.addObject(new Decoration(t(132), t(17), R1, Images.plants[0]));
		l.addObject(new Decoration(t(139), t(18), R1, Images.plants[1]));
		l.addObject(new Decoration(t(135), t(22), R1, Images.plants[1]));
		l.addObject(new Decoration(t(138), t(13), R1, Images.plants[0]));
		l.addObject(new Decoration(t(135), t(10), R1, Images.plants[2]));
		l.addObject(new Decoration(t(134), t(9), R1, Images.plants[0]));
		l.addObject(new Decoration(t(132), t(8), R1, Images.plants[2]));
		l.addObject(new Decoration(t(127), t(7), R1, Images.plants[1]));
		l.addObject(new Decoration(t(124), t(5), R1, Images.plants[1]));
		l.addObject(new Decoration(t(119), t(4), R1, Images.plants[0]));
		l.addObject(new Decoration(t(116), t(5), R1, Images.plants[2]));
		l.addObject(new Decoration(t(142), t(27), R1, Images.plants[1]));
		l.addObject(new Decoration(t(147), t(28), R1, Images.plants[0]));
		l.addObject(new Decoration(t(116), t(8), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(118), t(13), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(119), t(17), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(120), t(18), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(122), t(23), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(124), t(26), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(126), t(29), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(127), t(31), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(129), t(34), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(131), t(36), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(134), t(37), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(135), t(37), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(138), t(36), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(142), t(33), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(144), t(35), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(148), t(37), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(137), t(29), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(135), t(24), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(131), t(22), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(132), t(22), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(125), t(17), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(123), t(12), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(128), t(12), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(131), t(13), R1, Images.stalagtite[0]));
		l.addObject(new Decoration(t(132), t(13), R1, Images.stalagtite[1]));
		l.addObject(new Decoration(t(134), t(14), R1, Images.stalagtite[1]));
		
		//win location
		l.addObject(new WinLevel(t(150.5), t(28.5), l, p));
		l.addObject(new Decoration(t(149) + 5, t(26) + 8, R1, Images.alienDoor));
		for(int i = 0; i < 2; i++) l.addObject(new Decoration(t(156 + i * 3), t(26), R1, Images.alienWindow));
		l.addObject(new Decoration(t(154), t(12), R1, Images.alienWindow));
		for(int i = 0; i < 6; i++) l.addObject(new Decoration(t(149 + i), t(8 - i), R1, Images.pmetalSlope[0]));
		double[] posB = new double[]{
			155,5, 158,5, 159,7, 153.5,8, 151,10, 151,7, 156,10, 159,7, 159,13, 156,11, 153,12, 151,13, 151.5,15, 154,17, 150,17, 159,15, 156,18, 158,18, 154,18, 151,19, 153,21.5, 157,21, 159,19,
			157,21, 151,21, 154,23, 150,24, 153,25, 158,23, 158,28, 154,29, 152,28, 152,31, 156,31, 160,33, 155,36, 159,37, 154,39, 157,41, 158,41.5, 159,43, 154,43, 156,44, 158,44, 156,46, 154,48,
			159,48, 159,45, 152,47, 150,48, 147,48
		};
		for(int i = 0; i < posB.length; i+=2) l.addObject(new Decoration(t(posB[i]), t(posB[i + 1]), R2, Images.blockCracks[(int)(7051 + i * .5 * 7) % 4]));
		
		//tiles
		try{
			BufferedImage grid = ImageIO.read(new File(Files.NEW_LEVELS + "/1Outside.png"));
			l.setTiles(Level.loadTileFromImage(grid));
		}catch(IOException e){
			System.out.println("Failed to create: level 1 outside");
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
