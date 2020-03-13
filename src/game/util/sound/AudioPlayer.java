package game.util.sound;

import java.util.ArrayList;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import game.libs.Sounds;

/**
 * Creates a player that will play given sound files. When the sound is finished playing one time, it will stop playing that sound
 */
public class AudioPlayer implements Runnable{
	
	private boolean paused;
	private boolean running;
	
	private float volume;
	
	private ArrayList<Clip> sounds;
	
	public AudioPlayer(boolean paused){
		running = true;
		
		sounds = new ArrayList<Clip>();
		
		this.paused = paused;
		if(!this.paused){
			paused = true;
			unpause();
		}
		setVolume(0);
	}
	
	private void addSound(Clip sound){
		((FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
		sounds.add(sound);
	}
	
	/**
	 * Begins playing the specified sound. Will not play sound if the player is paused. Volume is set to the value of the player
	 * @param sound The sound file to be played, should come from the Sound lib class, the Clip will be copied, and not directly used
	 */
	public void playSound(String sound){
		if(paused) return;
		Clip s = Sounds.loadSound(sound);
		
		((FloatControl)s.getControl(FloatControl.Type.MASTER_GAIN)).setValue(this.volume);
		s.setFramePosition(0);
		s.start();
		
		addSound(s);
	}
	
	/**
	 * Pauses all audio in this player, no more sound will be played until unpause() is called
	 */
	public void pause(){
		if(paused) return;
		paused = true;
		
		for(int i = 0; i < sounds.size(); i++){
			if(sounds.get(i).isRunning()) sounds.get(i).stop();
		}
	}
	
	/**
	 * Resumes playing all sound in this player
	 */
	public void unpause(){
		if(!paused) return;
		paused = false;
		
		for(int i = 0; i < sounds.size(); i++){
			if(!sounds.get(i).isRunning()) sounds.get(i).start();
		}
	}
	
	/**
	 * @return true if there are no sounds either playing or paused playing, false otherwise
	 */
	public boolean empty(){
		return sounds.size() == 0;
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public float getVolume(){
		return volume;
	}
	
	/**
	 * Sets volume of all sounds to a percentage (0-1)
	 * @param volume (0-1), the percentage of volume to use
	 */
	public void setVolume(double volume){
		if(volume > 1) volume = 1;
		else if(volume < 0) volume = 0;
		
		float max = 87.0206f;
		volume = (float)(-Math.pow(max, 1 - volume) + max);
		volume -= 80f;
		
		this.volume = (float)volume;
		
		for(int i = 0; i < sounds.size(); i++){
			((FloatControl)sounds.get(i).getControl(FloatControl.Type.MASTER_GAIN)).setValue(this.volume);
		}
	}
	
	/**
	 * Removes all sounds from the player and stops it from making sound
	 */
	public void stopSounds(){
		try{
			if(sounds == null) return;
			for(Clip c : sounds) if(c != null) c.stop();
			sounds = new ArrayList<Clip>();
		} catch(Exception e){}
	}
	
	public void stop(){
		running = false;
	}
	
	@Override
	public void run(){
		while(running){
			try{
			for(int i = 0; i < sounds.size(); i++){
				if(sounds.get(i).getMicrosecondPosition() == sounds.get(i).getMicrosecondLength()){
					sounds.remove(i);
					i--;
				}
			}
				Thread.sleep(1);
			}catch(Exception e){}
		}
		stopSounds();
	}
	
}
