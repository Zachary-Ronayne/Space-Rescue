package game.world.obj.use;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.libs.Images;
import game.libs.Sounds;
import game.util.sound.AudioPlayer;
import game.world.obj.GameObject;
import game.world.obj.activate.Activatable;

public class PlayerButton extends GameObject implements Usable{
	
	private ArrayList<Activatable> activatables;
	
	private boolean pressed;
	
	private AudioPlayer effectsPlayer;
	
	public PlayerButton(double x, double y, AudioPlayer effectsPlayer){
		super(x, y, TILE_SIZE, TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .5), false);
		
		activatables = new ArrayList<Activatable>();
		
		pressed = false;
		
		this.effectsPlayer = effectsPlayer;
	}
	
	public void addActivatable(Activatable a){
		activatables.add(a);
	}
	
	@Override
	public void removeActivatable(int UUID){
		for(int i = 0; i < activatables.size(); i++){
			if(activatables.get(i).getUUID() == UUID){
				activatables.remove(i);
				return;
			}
		}
	}
	
	@Override
	protected void tickOverride(){}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(pressed) g.drawImage(Images.gameButton[1], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
		else g.drawImage(Images.gameButton[0], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), null);
	}
	
	@Override
	public void use(){
		if(!pressed){
			effectsPlayer.playSound(Sounds.PRESS_GAME_BUTTON);
			pressed = true;
			for(Activatable a : activatables) a.activate();
		}
	}
	
}
