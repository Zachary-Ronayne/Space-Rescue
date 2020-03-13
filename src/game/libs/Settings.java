package game.libs;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import game.Main;
import game.util.sound.AudioPlayer;

public final class Settings{

	public static final int DEFAULT_SCREEN_WIDTH = 320;
	public static final int DEFAULT_SCREEN_HEIGHT = 180;
	public static final boolean DEFAULT_FULLSCREEN = false;
	public static final boolean DEFAULT_STRETCH_TO_FIT = false;
	
	public static final double DEFAULT_MASTER_VOLUME = 1;
	public static final double DEFAULT_MUSIC_VOLUME = 1;
	public static final double DEFAULT_EFFECTS_VOLUME = 1;
	
	public static final int DEFAULT_PRESS_LEFT = KeyEvent.VK_LEFT;
	public static final int DEFAULT_PRESS_RIGHT = KeyEvent.VK_RIGHT;
	public static final int DEFAULT_PRESS_JUMP = KeyEvent.VK_UP;
	public static final int DEFAULT_PRESS_DUCK = KeyEvent.VK_DOWN;
	public static final int DEFAULT_PRESS_ATTACK = KeyEvent.VK_Q;
	public static final int DEFAULT_PRESS_USE = KeyEvent.VK_W;
	public static final int DEFAULT_PRESS_PAUSE = KeyEvent.VK_E;
	
	private static int screenWidth;
	private static int screenHeight;
	private static boolean stretchToFit;
	private static boolean fullScreen;
	
	private static double masterVolume;
	private static double musicVolume;
	private static double effectsVolume;
	
	private static int pressLeft;
	private static int pressRight;
	private static int pressJump;
	private static int pressDuck;
	private static int pressAttack;
	private static int pressUse;
	private static int pressPause;
	
	public static void load(){
		try{
			Scanner read = new Scanner(new File(Files.DATA + "/settings.txt"));
			
			read.nextLine();
			read.nextLine();
			read.next();
			setScreenWidth(read.nextInt());
			read.next();
			setScreenHeight(read.nextInt());
			read.next();
			setFullScreen(read.nextBoolean());
			read.next();
			setStretchToFit(read.nextBoolean());
			read.nextLine();
			read.nextLine();
			read.next();
			setMasterVolume(read.nextDouble());
			read.next();
			setMusicVolume( read.nextDouble());
			read.next();
			setEffectsVolume(read.nextDouble());
			read.nextLine();
			read.nextLine();
			read.next();
			setPressLeft(read.nextInt());
			read.next();
			setPressRight(read.nextInt());
			read.next();
			setPressJump(read.nextInt());
			read.next();
			setPressDuck(read.nextInt());
			read.next();
			setPressAttack(read.nextInt());
			read.next();
			setPressUse(read.nextInt());
			read.next();
			setPressPause(read.nextInt());
			
			read.close();
		}catch(Exception e){
			System.err.println("Failed to load settings");
			e.printStackTrace();
			restoreDefaultSettings();
			save();
		}
	}
	
	public static void save(){
		try{
			PrintWriter write = new PrintWriter(new File(Files.DATA + "/settings.txt"));
			
			write.println("Window Settings:");
			write.println("Resolution:");
			write.println("Width:\t" + screenWidth);
			write.println("Height:\t" + screenHeight);
			write.println("FullScreen:\t" + fullScreen);
			write.println("StretchToFit:\t" + stretchToFit);
			write.println("Audio Settings:");
			write.println("MasterVolume:\t" + masterVolume);
			write.println("MusicVolume:\t" + musicVolume);
			write.println("EffectsVolume:\t" + effectsVolume);
			write.println("Controls:");
			write.println("MoveLeft:\t" + pressLeft);
			write.println("MoveRight:\t" + pressRight);
			write.println("Jump:\t" + pressJump);
			write.println("Duck:\t" + pressDuck);
			write.println("Attack:\t" + pressAttack);
			write.println("Use:\t" + pressUse);
			write.println("Pause:\t" + pressPause);
			
			write.close();
		}catch(Exception e){
			System.err.println("Failed to save settings");
			e.printStackTrace();
		}
	}
	
	public static void restoreDefaultSettings(){
		setScreenWidth(1280);
		setScreenHeight(720);
		setFullScreen(DEFAULT_FULLSCREEN);
		setStretchToFit(DEFAULT_STRETCH_TO_FIT);
		
		setMasterVolume(DEFAULT_MASTER_VOLUME);
		setMusicVolume(DEFAULT_MUSIC_VOLUME);
		setEffectsVolume(DEFAULT_EFFECTS_VOLUME);
		
		setPressLeft(DEFAULT_PRESS_LEFT);
		setPressRight(DEFAULT_PRESS_RIGHT);
		setPressJump(DEFAULT_PRESS_JUMP);
		setPressDuck(DEFAULT_PRESS_DUCK);
		setPressAttack(DEFAULT_PRESS_ATTACK);
		setPressUse(DEFAULT_PRESS_USE);
		setPressPause(DEFAULT_PRESS_PAUSE);
		
		save();
	}
	
	public static int getScreenWidth(){
		return screenWidth;
	}
	public static void setScreenWidth(int screenWidth){
		Settings.screenWidth = screenWidth;
		if(Settings.screenWidth <= 1) Settings.screenWidth = DEFAULT_SCREEN_WIDTH;
		if(!fullScreen) Main.updateResolution();
	}
	
	public static int getScreenHeight(){
		return screenHeight;
	}
	public static void setScreenHeight(int screenHeight){
		Settings.screenHeight = screenHeight;
		if(Settings.screenHeight < 1) Settings.screenHeight = DEFAULT_SCREEN_HEIGHT;
		if(!fullScreen) Main.updateResolution();
	}
	
	public static boolean getFullScreen(){
		return fullScreen;
	}
	public static void setFullScreen(boolean fullScreen){
		Main.setFullScreen(fullScreen, false, false);
		Settings.fullScreen = fullScreen;
	}
	
	public static boolean getStretchToFit(){
		return stretchToFit;
	}
	public static void setStretchToFit(boolean stretchToFit){
		Settings.stretchToFit = stretchToFit;
		Main.updateStretchFit();
	}

	public static double getMasterVolume(){
		return masterVolume;
	}
	public static void setMasterVolume(double volume){
		if(volume > 1) volume = 1;
		else if(volume < 0) volume = 0;
		masterVolume = volume;
	}
	
	public static double getMusicVolume(){
		return musicVolume;
	}
	public static void setMusicVolume(double volume){
		if(volume > 1) volume = 1;
		else if(volume < 0) volume = 0;
		musicVolume = volume;
	}
	
	public static double getEffectsVolume(){
		return effectsVolume;
	}
	public static void setEffectsVolume(double volume){
		if(volume > 1) volume = 1;
		else if(volume < 0) volume = 0;
		effectsVolume = volume;
	}
	
	public static void setVolume(AudioPlayer musicPlayer, AudioPlayer effectsPlayer){
		setMusicVolume(musicPlayer);
		setEffectsVolume(effectsPlayer);
	}
	
	public static void setMusicVolume(AudioPlayer player){
		player.setVolume(getMusicVolume() * getMasterVolume());
	}
	
	public static void setEffectsVolume(AudioPlayer player){
		player.setVolume(getEffectsVolume() * getMasterVolume());
	}

	public static int getPressLeft(){
		return pressLeft;
	}
	public static void setPressLeft(int pressLeft){
		Settings.pressLeft = pressLeft;
	}

	public static int getPressRight(){
		return pressRight;
	}
	public static void setPressRight(int pressRight){
		Settings.pressRight = pressRight;
	}

	public static int getPressJump(){
		return pressJump;
	}
	public static void setPressJump(int pressJump){
		Settings.pressJump = pressJump;
	}

	public static int getPressDuck(){
		return pressDuck;
	}
	public static void setPressDuck(int pressDuck){
		Settings.pressDuck = pressDuck;
	}

	public static int getPressAttack(){
		return pressAttack;
	}
	public static void setPressAttack(int pressAttack){
		Settings.pressAttack = pressAttack;
	}

	public static int getPressUse(){
		return pressUse;
	}
	public static void setPressUse(int pressUse){
		Settings.pressUse = pressUse;
	}

	public static int getPressPause(){
		return pressPause;
	}
	public static void setPressPause(int pressPause){
		Settings.pressPause = pressPause;
	}
	
	public static Rectangle2D.Double getScreenBounds(){
		return new Rectangle2D.Double(0, 0, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT);
	}
	
}
