package game.util;

import java.awt.image.BufferedImage;

public class SpriteSheet{
	
	private int w;
	private int h;
	
	private BufferedImage sheet;
	
	/**
	 * @param w the width of each sprite
	 * @param h the height of each sprite
	 * @param sheet the image of the spritesheet
	 */
	public SpriteSheet(int w, int h, BufferedImage sheet){
		this.w = w;
		this.h = h;
		this.sheet = sheet;
	}
	
	public BufferedImage getSprite(int x, int y){
		return sheet.getSubimage(x * w, y * h, w, h);
	}
	
}
