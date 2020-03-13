package game.input;

public interface Controlable{
	
	public void pressLeft();
	public void pressRight();
	public void pressJump();
	public void pressDuck();
	public void pressAttack();
	public void pressUse();
	public void pressPause();
	
	public void releaseLeft();
	public void releaseRight();
	public void releaseJump();
	public void releaseDuck();
	public void releaseAttack();
	public void releaseUse();
	public void releasePause();
	
	public int getUUID();
}
