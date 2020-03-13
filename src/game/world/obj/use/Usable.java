package game.world.obj.use;

public interface Usable{
	
	/**
	 * This method will be called when this object is used
	 */
	public void use();
	
	public int getUUID();
	
	public void removeActivatable(int UUID);
	
}
