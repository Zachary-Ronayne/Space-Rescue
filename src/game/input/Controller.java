package game.input;

public abstract class Controller implements Controlable{
	
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean jumpPressed;
	private boolean duckPressed;
	private boolean attackPressed;
	private boolean usePressed;
	private boolean pausePressed;
	
	protected Controller(){
		leftPressed = false;
		rightPressed = false;
		jumpPressed = false;
		duckPressed = false;
		attackPressed = false;
		usePressed = false;
		pausePressed = false;
	}
	
	public void pressLeft(){
		leftPressed = true;
	}
	public void pressRight(){
		rightPressed = true;
	}
	public void pressJump(){
		jumpPressed = true;
	}
	public void pressDuck(){
		duckPressed = true;
	}
	public void pressAttack(){
		attackPressed = true;
	}
	public void pressUse(){
		usePressed = true;
	}
	public void pressPause(){
		pausePressed = true;
	}
	
	public void releaseLeft(){
		leftPressed = false;
	}
	public void releaseRight(){
		rightPressed = false;
	}
	public void releaseJump(){
		jumpPressed = false;
	}
	public void releaseDuck(){
		duckPressed = false;
	}
	public void releaseAttack(){
		attackPressed = false;
	}
	public void releaseUse(){
		usePressed = false;
	}
	public void releasePause(){
		pausePressed = false;
	}
	
	public boolean getLeftPressed(){
		return leftPressed;
	}
	public boolean getRightPressed(){
		return rightPressed;
	}
	public boolean getJumpPressed(){
		return jumpPressed;
	}
	public boolean getDuckPressed(){
		return duckPressed;
	}
	public boolean getAttackPressed(){
		return attackPressed;
	}
	public boolean getUsePressed(){
		return usePressed;
	}
	public boolean getPausePressed(){
		return pausePressed;
	}
	
}
