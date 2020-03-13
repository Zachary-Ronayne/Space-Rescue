package game.util.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;

public class ResizableScreen{
	
	private Image screen;
	
	private int screenWidth;
	private int screenHeight;
	
	private int currentWidth;
	private int currentHeight;
	
	/**
	 * If true, the screen will be resized and stretched to fit the aspect ratio of the current size. 
	 * If false, the screen will be stretched to fill the screen in the same aspect ratio, and empty space will be filled in with black bars
	 */
	private boolean stretchToFit;
	
	public ResizableScreen(Image screen){
		this.screen = screen;
		
		screenWidth = screen.getWidth(null);
		screenHeight = screen.getHeight(null);
		
		currentWidth = screenWidth;
		currentHeight = screenHeight;
		
		stretchToFit = false;
	}
	
	/**
	 * Draws the screen to a component, resizing or drawing black bars depending on stretchToFit
	 */
	public void drawToComponent(Component c, Graphics2D g){
		if(stretchToFit) g.drawImage(getImage(), 0, 0, currentWidth, currentHeight, null);
		else{
			double cRatio = (double)currentWidth / currentHeight;
			double rRatio = (double)screenWidth / screenHeight;
			
			g.setColor(Color.BLACK);
			if(cRatio > rRatio){
				//vertical bars
				int dWidth = (int)(currentHeight * rRatio);
				int diff = Math.abs(currentWidth - dWidth) / 2;
				g.drawImage(getImage(), diff, 0, dWidth, currentHeight, null);
				g.fillRect(0, 0, diff, currentHeight);
				g.fillRect(diff + dWidth, 0, diff + 10, currentHeight);
			}
			else{
				//horizontal bars
				int dHeight = (int)(currentWidth / rRatio);
				int diff = Math.abs(currentHeight - dHeight) / 2;
				g.drawImage(getImage(), 0, diff, currentWidth, dHeight, null);
				g.fillRect(0, 0, currentWidth, diff);
				g.fillRect(0, diff + dHeight, currentWidth, diff + 10);
			}
		}
	}
	
	public Graphics2D getGraphics() {
		return (Graphics2D)screen.getGraphics();
	}
	
	public Image getImage(){
		return screen;
	}
	
	public int getCurrentWidth(){
		return currentWidth;
	}
	
	public void setCurrentWidth(int width){
		currentWidth = width;
	}
	
	public int getCurrentHeight(){
		return currentHeight;
	}
	
	public void setCurrentHeight(int height){
		currentHeight = height;
	}
	
	public boolean getStretchToFit(){
		return stretchToFit;
	}
	
	public void setStretchToFit(boolean s){
		stretchToFit = s;
	}
}
