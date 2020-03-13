package game.world.obj.use;

import java.awt.Graphics2D;
import java.util.ArrayList;

import game.libs.Images;
import game.libs.Sounds;
import game.util.sound.AudioPlayer;
import game.world.obj.GameObject;
import game.world.obj.activate.Activatable;

public class Lever extends GameObject implements Usable{
	
	public static final int LEVER_LENGTH = 13;
	public static final int LEVER_WIDTH = 3;
	private boolean neg;
	private boolean vert;
	private boolean movingForward;
	private int stage;
	
	private int style;
	
	private ArrayList<Activatable> activatables;
	
	private AudioPlayer effectsPlayer;
	
	/**
	 * Creates a lever
	 * @param x the x position of the upper left hand corner
	 * @param y the y position of the upper left hand corner
	 * @param neg true if this lever should face the negative direction
	 * @param vert true if this lever should be vertical
	 */
	public Lever(double x, double y, boolean neg, boolean vert, int style, AudioPlayer effectsPlayer){
		super(x, y, TILE_SIZE, TILE_SIZE, (int)(MAX_RENDER_PRIORITY * 0.5), false);
		this.effectsPlayer = effectsPlayer;
		this.neg = neg;
		this.vert = vert;
		this.movingForward = false;
		
		this.style = style;
		
		stage = 0;
		activatables = new ArrayList<Activatable>();
	}
	
	private void nextStage(boolean next){
		if(next) stage++;
		else stage--;
		if(stage < 0) stage = 0;
		else if(stage > getMaxFrame()) stage = getMaxFrame();
	}
	
	private int getStageFrame(){
		return stage / 2;
	}
	
	private int getMaxFrame(){
		return Images.leverFrames[0].length * 2 - 1;
	}
	
	@Override
	protected void tickOverride(){
		if(!movingForward && stage > 0) nextStage(false);
		else if(movingForward && stage < getMaxFrame()) nextStage(true);
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		if(vert){
			if(neg) g.drawImage(Images.leverFrames[style][getStageFrame()][1], (int)Math.round(getScreenX(x) + getWidth()), (int)Math.round(getScreenY(y)), -(int)getWidth(), (int)getHeight(), null);
			else g.drawImage(Images.leverFrames[style][getStageFrame()][1], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight(), null);
		}
		else{
			if(neg) g.drawImage(Images.leverFrames[style][getStageFrame()][0], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y)), (int)getWidth(), (int)getHeight(), null);
			else g.drawImage(Images.leverFrames[style][getStageFrame()][0], (int)Math.round(getScreenX(x)), (int)Math.round(getScreenY(y) + getHeight()), (int)Math.round(getWidth()), -(int)getHeight(), null);
		}
	}
	
	public void addActivatable(Activatable a){
		activatables.add(a);
	}
	
	public void removeActivatable(int UUID){
		for(int i = 0; i < activatables.size(); i++){
			if(activatables.get(i).getUUID() == UUID){
				activatables.remove(i);
				return;
			}
		}
	}
	
	@Override
	public void use(){
		if(stage == 0 || stage == getMaxFrame()){
			effectsPlayer.playSound(Sounds.PRESS_GAME_BUTTON);
			movingForward = !movingForward;
			for(Activatable a : activatables) a.activate();
		}
	}
	
}
