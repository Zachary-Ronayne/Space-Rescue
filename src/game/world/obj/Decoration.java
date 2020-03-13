package game.world.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Decoration extends GameObject{
	
	private BufferedImage render;
	boolean resize;
	
	public Decoration(double x, double y, double width, double height, int renderPriority, BufferedImage render){
		super(x, y, width, height, renderPriority, false);
		this.render = render;
		resize = true;
		setup();
	}
	
	public Decoration(double x, double y, int renderPriority, BufferedImage render){
		super(x, y, render.getWidth(), render.getHeight(), renderPriority, false);
		this.render = render;
		resize = false;
		setup();
	}
	
	public Decoration(double x, double y, double width, double height, int renderPriority){
		super(x, y, width, height, renderPriority, false);
		this.render = null;
		setup();
	}
	
	/**
	 * Called when this object is constructed, use this to initialize variables
	 */
	protected void setup(){}
	
	@Override
	protected void tickOverride(){}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(render != null){
			if(resize) g.drawImage(render, (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight(), null);
			else g.drawImage(render, (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
		}
		else{
			g.setColor(Color.BLACK);
			g.fillRect((int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight());
		}
	}
}
