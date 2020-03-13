package game.world.levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.libs.Files;
import game.libs.Images;
import game.libs.Settings;
import game.world.obj.Decoration;
import game.world.obj.GameObject;
import game.world.obj.Tile;
import game.world.obj.WinLevel;
import game.world.obj.activate.Door;
import game.world.obj.entity.Player;
import game.world.obj.entity.enemy.TrainingDummy;
import game.world.obj.use.Lever;

public final class Level0Tutorial{
	
	/**
	 * Sets the given level to the tutorial level
	 * @param l
	 */
	public static void setLevel(Level l){
		l.clear();
		final int R = GameObject.MIN_RENDER_PRIORITY + 1;
		
		//player
		Player p = new Player(t(1.5), t(9), l);
//		Player p = new Player(t(138), t(8), l);
		l.addObject(p);
		
		//training dummy
		l.addObject(new TrainingDummy(t(19), t(19), l));
		
		//explanation signs
		l.addObject(new Sign(t(2.5), t(8.5), t(5), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + "", (int)Math.round(getScreenX(x) + 4), (int)Math.round(getScreenY(y)) + 26);
				String s = KeyEvent.getKeyText(Settings.getPressRight()) + "";
				g.drawString(s, (int)Math.round((getScreenX(x) + getWidth()) - 5 - g.getFontMetrics().stringWidth(s)), (int)Math.round(getScreenY(y) + 26));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 27), (int)Math.round(getScreenY(y) + 14));
				g.drawImage(Images.introSignTiles[14], (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 3), null);
				g.drawImage(Images.introSignTiles[13], (int)Math.round(getScreenX(x) + getWidth() - TILE_SIZE - 5), (int)Math.round(getScreenY(y) + 3), null);
			}
		});
		
		l.addObject(new Sign(t(5.5), t(13.5), t(3), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 26));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 18), (int)Math.round(getScreenY(y) + 14));
				g.drawImage(Images.introSignTiles[15], (int)Math.round(getScreenX(x) + 3), (int)Math.round(getScreenY(y) + 3), null);
			}
		});
		
		l.addObject(new Sign(t(4.5), t(18.5), t(4), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressDuck()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 26));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 18), (int)Math.round(getScreenY(y) + 14));
				g.drawImage(Images.introSignTiles[16], (int)Math.round(getScreenX(x) + 3), (int)Math.round(getScreenY(y) + 3), null);
			}
		});
		
		l.addObject(new Sign(t(12.5), t(18.5), t(3), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 26));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 19), (int)Math.round(getScreenY(y) + 14));
				g.drawImage(Images.introSignTiles[17], (int)Math.round(getScreenX(x) + 4), (int)Math.round(getScreenY(y) + 2), null);
			}
		});
		
		l.addObject(new Sign(t(17.6), t(14.5), t(5), t(3)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 45), (int)Math.round(getScreenY(y) + 16));
				g.drawImage(Images.introSignTiles[16], (int)Math.round(getScreenX(x) + 3), (int)Math.round(getScreenY(y) + 3), null);
				g.drawImage(Images.introSignTiles[20], (int)Math.round(getScreenX(x) + 14), (int)Math.round(getScreenY(y) + 3), null);
				g.drawImage(Images.introSignTiles[17], (int)Math.round(getScreenX(x) + 27), (int)Math.round(getScreenY(y) + 3), null);
				g.drawString(KeyEvent.getKeyText(Settings.getPressDuck()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 28));
				g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 40));
			}
		});
		
		l.addObject(new Sign(t(24.5), t(14.5), t(3), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 27));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 20), (int)Math.round(getScreenY(y) + 15));
				g.drawImage(Images.introSignTiles[18], (int)Math.round(getScreenX(x) + 4), (int)Math.round(getScreenY(y) + 3), null);
			}
		});
		
		l.addObject(new Sign(t(24.5), t(18.5), t(3), t(2)){
			@Override
			protected void renderOveride(Graphics2D g, double x, double y){
				super.renderOveride(g, x, y);
				g.setColor(new Color(0, 0, 0, 151));
				g.setFont(new Font("Impact", Font.PLAIN, 10));
				g.drawString(KeyEvent.getKeyText(Settings.getPressPause()) + "", (int)Math.round(getScreenX(x) + 5), (int)Math.round(getScreenY(y) + 26));
				g.drawString("Press", (int)Math.round(getScreenX(x) + 19), (int)Math.round(getScreenY(y) + 14));
				g.drawImage(Images.introSignTiles[19], (int)Math.round(getScreenX(x) + 4), (int)Math.round(getScreenY(y) + 2), null);
			}
		});
		
		//door objects
		Door door = new Door(t(29), t(18), 1, 3, 1, Tile.SHIP, l);
		Lever lever = new Lever(t(28), t(15.5), true, true, 2, l.getEffectsPlayer());
		lever.addActivatable(door);
		l.addObject(door);
		l.addObject(lever);
		
		//win level object
		l.addObject(new WinLevel(t(30), t(19.5), l, p));
		l.addObject(new Decoration(t(34), t(20), R, Images.plants[0]));
		l.addObject(new Decoration(t(10), t(5), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(13), t(6), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(16), t(7), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(19), t(8), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(22), t(9), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(25), t(10), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(28), t(11), R, Images.shipSlope[1]));
		l.addObject(new Decoration(t(29), t(12), R, Images.shipSlope[1]));
		
		//tiles
		try{
			BufferedImage grid = ImageIO.read(new File(Files.NEW_LEVELS + "/0Tutorial.png"));
			l.setTiles(Level.loadTileFromImage(grid));
		}catch(IOException e){
			System.out.println("Failed to create: level 0 tutorial");
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
	
	private static BufferedImage getSignBackground(int w, int h){
		int size = (int)Tile.TILE_SIZE;
		
		BufferedImage img = new BufferedImage(w * size, h * size, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		
		for(int i = 1; i < w - 1; i++){
			g.drawImage(Images.introSignTiles[1], i * size, 0, null);
			g.drawImage(Images.introSignTiles[7], i * size, size * (h - 1), null);
		}
		for(int i = 1; i < w - 1; i++){
			g.drawImage(Images.introSignTiles[3], 0, i * size, null);
			g.drawImage(Images.introSignTiles[5], size * (w - 1), i * size, null);
		}
		g.drawImage(Images.introSignTiles[0], 0, 0, null);
		g.drawImage(Images.introSignTiles[2], size * (w - 1), 0, null);
		g.drawImage(Images.introSignTiles[6], 0, size * (h - 1), null);
		g.drawImage(Images.introSignTiles[8], size * (w - 1), size * (h - 1), null);
		
		return img;
	}
	
	private static class Sign extends Decoration{
		public Sign(double x, double y, double width, double height){
			super(x, y, width, height, (int)(Tile.MAX_RENDER_PRIORITY * .2));
		}
		private BufferedImage background;
		private int frame;
		@Override
		protected void setup(){
			background = getSignBackground((int)(getWidth() / TILE_SIZE), (int)(getHeight() / TILE_SIZE));
			frame = (int)(79 * Math.random());
		}
		@Override
		protected void tickOverride(){
			frame--;
			if(frame < 0) frame = 79;
		}
		@Override
		protected void renderOveride(Graphics2D g, double x, double y){
			for(int i = 0; i < getWidth(); i += TILE_SIZE){
				for(int j = 0; j < getHeight(); j += TILE_SIZE){
					g.drawImage(Images.introSignTiles[12 - frame / 20], (int)Math.round(getScreenX(x) + i), (int)Math.round(getScreenY(y) + j), null);
				}
			}
			g.drawImage(background, (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
		}
	}
}
