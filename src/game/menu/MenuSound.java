package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.Main;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.component.MenuButton;
import game.menu.component.MenuComponent;
import game.menu.component.MenuSlider;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;

public class MenuSound implements Menu{
	
	public static final int MASTER_VOLUME = 0;
	public static final int MUSIC_VOLUME = 1;
	public static final int EFFECTS_VOLUME = 2;
	public static final int TEST_EFFECTS = 3;
	
	private Main instance;
	private AudioPlayer musicPlayer;
	private AudioPlayer effectsPlayer;
	
	private MenuComponent[] components;
	private int selectedComponent;
	
	public MenuSound(Main instance, AudioPlayer musicPlayer, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.musicPlayer = musicPlayer;
		this.effectsPlayer = effectsPlayer;
		
		components = new MenuComponent[4];
		for(int i = 0; i < 3; i++) components[i] = new MenuSlider(210, 76 + 24 * i, 100, 20, true, Type.SIZE_100X20, effectsPlayer);
		components[TEST_EFFECTS] = new MenuButton(210, 150, 100, 20, "Test Effect", 15, Type.SIZE_100X20);
		
		((MenuSlider)components[MASTER_VOLUME]).setValue((int)(Settings.getMasterVolume() * 100));
		((MenuSlider)components[MUSIC_VOLUME]).setValue((int)(Settings.getMusicVolume() * 100));
		((MenuSlider)components[EFFECTS_VOLUME]).setValue((int)(Settings.getEffectsVolume() * 100));
		
		setSelectedComponent(0);
	}
	
	private void setSelectedComponent(int b){
		if(b < 0) b = components.length - 1;
		else if(b > components.length - 1) b = 0;
		
		for(int i = 0; i < components.length; i++) components[i].setSelected(false);
		components[b].setSelected(true);
		selectedComponent = b;
	}
	
	
	private void applySound(){
		double master = ((MenuSlider)components[MASTER_VOLUME]).getValue() / 100.0;
		double music = ((MenuSlider)components[MUSIC_VOLUME]).getValue() / 100.0;
		double effects = ((MenuSlider)components[EFFECTS_VOLUME]).getValue() / 100.0;
		Settings.setMasterVolume(master);
		Settings.setMusicVolume(music);
		Settings.setEffectsVolume(effects);
		
		Settings.setVolume(musicPlayer, effectsPlayer);
		
		Settings.save();
	}
	
	@Override
	public void tick(){
		for(int i = 0; i < components.length; i++) components[i].tick();
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 12));
		g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
		g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
		g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " / " + KeyEvent.getKeyText(Settings.getPressDuck()) + " : Up / Down", 2, 164);
		g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Adjust", 2, 177);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Volume", 10, 36);
		
		g.setFont(new Font("Impact", Font.PLAIN, 15));
		g.drawString("Master Volume", components[MASTER_VOLUME].getX() - 100, components[MASTER_VOLUME].getY() + 15);
		g.drawString("Music Volume", components[MUSIC_VOLUME].getX() - 100, components[MUSIC_VOLUME].getY() + 15);
		g.drawString("Effects Volume", components[EFFECTS_VOLUME].getX() - 100, components[EFFECTS_VOLUME].getY() + 15);
		
		for(int i = 0; i < components.length; i++) components[i].render(g);
	}

	@Override
	public void pressLeft(){
		if(selectedComponent >= 0 && selectedComponent <= 2) {
			((MenuSlider)components[selectedComponent]).addValue(-1);
			applySound();
		}
	}

	@Override
	public void pressRight(){
		if(selectedComponent >= 0 && selectedComponent <= 2) {
			((MenuSlider)components[selectedComponent]).addValue(1);
			applySound();
		}
	}

	@Override
	public void pressUp(){
		setSelectedComponent(selectedComponent - 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressDown(){
		setSelectedComponent(selectedComponent + 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressEnter(){
		if(components[TEST_EFFECTS].isSelected()) effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressBack(){
		instance.setGamestate(Main.GameState.MENU_SETTINGS);
		effectsPlayer.playSound(Sounds.CLICK);
	}

}
