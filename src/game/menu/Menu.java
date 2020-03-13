package game.menu;

import java.awt.Graphics2D;

public interface Menu{
	
	public abstract void tick();
	/**
	 * Renders the menu at the specificed coordinates
	 * @param g
	 * @param x
	 * @param y
	 */
	public abstract void render(Graphics2D g, int x, int y);
	
	public abstract void pressLeft();
	public abstract void pressRight();
	public abstract void pressUp();
	public abstract void pressDown();
	public abstract void pressEnter();
	public abstract void pressBack();
	
}
