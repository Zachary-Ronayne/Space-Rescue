package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;

import game.Main;
import game.Main.GameState;
import game.libs.Files;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.component.MenuButton;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;

public class MenuMain implements Menu{
	
	private static final int LOAD_GAME = 0;
	private static final int NEW_GAME = 1;
	private static final int SETTINGS = 2;
	private static final int QUIT = 3;
	
	private MenuButton[] buttons;
	private int selectedButton;
	
	private boolean allowLoad;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	
	public MenuMain(Main instance, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
		
		resetMenu();
	}
	
	public void resetMenu(){
		buttons = new MenuButton[4];
		buttons[LOAD_GAME] = new MenuButton(210, 76, 100, 20, "Load Game", 15, Type.SIZE_100X20);
		buttons[NEW_GAME] = new MenuButton(210, 100, 100, 20, "New Game", 15, Type.SIZE_100X20);
		buttons[SETTINGS] = new MenuButton(210, 124, 100, 20, "Settings", 15, Type.SIZE_100X20);
		buttons[QUIT] = new MenuButton(210, 150, 100, 20, "Quit", 15, Type.SIZE_100X20);
		
		allowLoad = false;
		File[] files = new File(Files.SAVES).listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].isFile()) allowLoad = true;
		}
		
		if(allowLoad) setSelectedButton(0);
		else setSelectedButton(1);
	}
	
	private void setSelectedButton(int b){
		if(allowLoad){
			if(b < 0) b = buttons.length - 1;
			else if(b > buttons.length - 1) b = 0;
		}
		else{
			if(b < 1) b = buttons.length - 1;
			else if(b > buttons.length - 1) b = 1;
		}
		
		for(int i = 0; i < buttons.length; i++) buttons[i].setSelected(false);
		buttons[b].setSelected(true);
		selectedButton = b;
	}
	
	@Override
	public void tick(){
		if(allowLoad) for(int i = 0; i < buttons.length; i++) buttons[i].tick();
		else for(int i = 1; i < buttons.length; i++) buttons[i].tick();
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 12));
		g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
		g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " : Up", 2, 151);
		g.drawString(KeyEvent.getKeyText(Settings.getPressDuck()) + " : Down", 2, 164);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Space Rescue", 10, 36);
		
		if(allowLoad) for(int i = 0; i < buttons.length; i++) buttons[i].render(g);
		else for(int i = 1; i < buttons.length; i++) buttons[i].render(g);
	}
	
	@Override
	public void pressLeft(){
	}
	
	@Override
	public void pressRight(){
	}
	
	@Override
	public void pressUp(){
		setSelectedButton(selectedButton - 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}
	
	@Override
	public void pressDown(){
		setSelectedButton(selectedButton + 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}
	
	@Override
	public void pressEnter(){
		if(buttons[LOAD_GAME].isSelected()){
			instance.setGamestate(GameState.MENU_LOAD);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[NEW_GAME].isSelected()){
			instance.setGamestate(Main.GameState.MENU_LEVEL);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[SETTINGS].isSelected()){
			instance.openSettingsMenu(true);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[QUIT].isSelected()){
			Main.terminate();
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressBack(){
	}

}
