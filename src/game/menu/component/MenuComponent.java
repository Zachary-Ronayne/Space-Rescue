package game.menu.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;
import game.util.sound.AudioPlayer;

public abstract class MenuComponent{
	
	protected int x;
	protected int y;
	
	protected boolean selected;
	
	protected Type type;
	
	protected AudioPlayer effectsPlayer;
	
	public enum Type{
		DEFAULT, SIZE_90X20, SIZE_100X20, SIZE_100X15, SIZE_120X20;
	}
	
	public MenuComponent(int x, int y, Type type, AudioPlayer effectsPlayer){
		this.x = x;
		this.y = y;
		selected = false;
		
		this.type = type;
		
		this.effectsPlayer = effectsPlayer;
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public abstract void tick();
	public abstract void render(Graphics2D g);
	
	public BufferedImage getTypeImage(boolean selected){
		switch(type){
			case SIZE_90X20:
				if(selected) return Images.button90X20[1];
				else return Images.button90X20[0];
			case SIZE_100X15:
				if(selected) return Images.button100X15[1];
				else return Images.button100X15[0];
			case SIZE_100X20:
				if(selected) return Images.button100X20[1];
				else return Images.button100X20[0];
			case SIZE_120X20:
				if(selected) return Images.button120X20[1];
				else return Images.button120X20[0];
			default: return null;
		}
	}
	
}
