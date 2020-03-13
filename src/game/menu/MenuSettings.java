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
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;

public class MenuSettings implements Menu{
	
	private static final int GRAPHICS = 0;
	private static final int SOUND = 1;
	private static final int CONTROLS = 2;
	
	private MenuButton[] buttons;
	private int selectedButton;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	
	public MenuSettings(Main instance, AudioPlayer effectsPlayer){
		buttons = new MenuButton[3];
		buttons[GRAPHICS] = new MenuButton(210, 100, 100, 20, "Graphics", 15, Type.SIZE_100X20);
		buttons[SOUND] = new MenuButton(210, 124, 100, 20, "Volume", 15, Type.SIZE_100X20);
		buttons[CONTROLS] = new MenuButton(210, 150, 100, 20, "Controls", 15, Type.SIZE_100X20);
		setSelectedButton(0);
		
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
	}
	
	private void setSelectedButton(int b){
		if(b < 0) b = 2;
		else if(b > 2) b = 0;
		
		for(int i = 0; i < buttons.length; i++) buttons[i].setSelected(false);
		buttons[b].setSelected(true);
		selectedButton = b;
	}
	
	@Override
	public void tick(){
		for(int i = 0; i < buttons.length; i++) buttons[i].tick();
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 12));
		g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
		g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
		g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " : Up", 2, 164);
		g.drawString(KeyEvent.getKeyText(Settings.getPressDuck()) + " : Down", 2, 177);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Settings", 10, 36);
		
		for(int i = 0; i < buttons.length; i++) buttons[i].render(g);
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
		if(buttons[GRAPHICS].isSelected()){
			instance.setGamestate(Main.GameState.MENU_GRAPHICS);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[SOUND].isSelected()){
			instance.setGamestate(Main.GameState.MENU_SOUND);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[CONTROLS].isSelected()){
			instance.setGamestate(Main.GameState.MENU_CONTROLS);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressBack(){
		instance.setGamestate(Main.GameState.MENU_MAIN);
		effectsPlayer.playSound(Sounds.CLICK);
	}

}
