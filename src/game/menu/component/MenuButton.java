package game.menu.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MenuButton extends MenuComponent{

	protected int width;
	protected int height;
	
	protected String text;
	protected int textSize;
	
	public MenuButton(int x, int y, int width, int height, String text, int textSize, Type type){
		super(x, y, type, null);
		this.width = width;
		this.height = height;
		this.text = text;
		this.textSize = textSize;
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
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, textSize));
		g.drawString(text, x + 3, y + textSize);
	}
	
	public String getText(){
		return text;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
