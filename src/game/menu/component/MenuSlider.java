package game.menu.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.libs.Sounds;
import game.util.sound.AudioPlayer;

public class MenuSlider extends MenuComponent{
	
	private int width;
	private int height;
	
	private int value;
	
	private boolean displayPerc;
	
	/**
	 * If height is bigger it scrolls vertical, if width is bigger it scrolls horizontal
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public MenuSlider(int x, int y, int width, int height, boolean displayPerc, Type type, AudioPlayer effectsPlayer){
		super(x, y, type, effectsPlayer);
		this.width = width;
		this.height = height;
		
		value = 100;
		
		this.displayPerc = displayPerc;
	}
	
	public int getValue(){
		return value;
	}
	public void setValue(int value){
		if(value > 100) value = 100;
		else if(value < 0) value = 0;
		
		this.value = value;
	}
	public void addValue(int value){
		setValue(this.value + value);
		effectsPlayer.playSound(Sounds.CLICK);
	}
	
	@Override
	public void tick(){
	}

	@Override
	public void render(Graphics2D g){
		BufferedImage img = getTypeImage(isSelected());
		if(img == null){
			if(selected){
				g.setColor(new Color(0, 0, 80, 128));
				g.fillRect(x - 2, y - 2, width + 4, height + 4);
				g.setColor(new Color(0, 0, 150));
			}
			else g.setColor(new Color(0, 0, 80));
			g.fillRect(x, y, width, height);
		}
		else g.drawImage(img, x, y, null);
		
		if(height > width){
			if(img == null){
				g.setColor(new Color(0, 0, 150));
				g.fillRect(x, (int)(y + (value / 100.0) * (height - width)), width, width);
				g.setColor(new Color(10, 10, 255));
				g.fillRect(x + 2, (int)(y + (value / 100.0) * (height - width) + 2), width - 4, width - 4);
			}
			else{
				int index;
				if(isSelected()) index = 1;
				else index = 0;
				g.drawImage(Images.buttonSlider20X20[index], x, (int)(y + (value / 100.0) * (height - width)), null);
			}
		}
		else{
			if(img == null){
				g.setColor(new Color(0, 0, 150));
				g.fillRect((int)(x + (value / 100.0) * (width - height)), y, height, height);
				g.setColor(new Color(10, 10, 255));
				g.fillRect((int)(x + (value / 100.0) * (width - height) + 2), y + 2, height - 4, height - 4);
			}
			else{
				int index;
				if(isSelected()) index = 1;
				else index = 0;
				g.drawImage(Images.buttonSlider20X20[index], (int)(x + (value / 100.0) * (width - height)), y, null);
			}
		}
		
		if(displayPerc){
			int size;
			if(height > width) size = width - 5;
			else size = height - 4;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, size));
			g.drawString(value + "%", x + 5, y + size);
		}
	}

}
