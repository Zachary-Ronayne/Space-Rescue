package game.menu.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Sounds;
import game.util.sound.AudioPlayer;

public class MenuListSelect extends MenuComponent{
	
	private int width;
	private int height;
	
	private String[] options;
	private int textSize;
	
	private int selectedOption;
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param options Must contain at least one option
	 */
	public MenuListSelect(int x, int y, int width, int height, int textSize, Type type, AudioPlayer effectsPlayer, String... options){
		super(x, y, type, effectsPlayer);
		
		if(options.length == 0) throw new IllegalArgumentException("The amount of Options in a MenuListSelect Must be at least 1");
		
		this.width = width;
		this.height = height;
		
		this.options = options;
		this.textSize = textSize;
		
		selectedOption = 0;
	}
	
	public void moveSelectedOption(int amount){
		setSelectedOption(selectedOption + amount);
		effectsPlayer.playSound(Sounds.CLICK);
	}
	
	public void setSelectedOption(int option){
		if(option < 0) option = options.length - 1;
		if(option >= options.length) option = 0;
		
		selectedOption = option;
	}
	
	public String getSelectedOption(){
		return options[selectedOption];
	}
	
	public int getOptionsLength(){
		return options.length;
	}
	
	public String getOption(int i) {
		return options[i];
	}

	@Override
	public void tick(){}

	@Override
	public void render(Graphics2D g){
		BufferedImage img = getTypeImage(isSelected());
		if(img == null){
			if(selected){
				g.setColor(new Color(0, 0, 80, 128));
				g.fillRect(x - 2, y - 2, width + 4, height + 4);
				g.setColor(new Color(0, 0, 120));
			}
			else g.setColor(new Color(0, 0, 80));
			g.fillRect(x, y, width, height);
		}
		else g.drawImage(img, x, y, null);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, textSize));
		g.drawString(options[selectedOption], x + 5, y + textSize);
	}

}
