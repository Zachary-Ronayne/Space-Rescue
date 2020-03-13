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
import game.menu.component.MenuListSelect;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;

public class MenuGraphics implements Menu{
	
	private static final int RESOLUTION = 0;
	private static final int FULL_SCREEN = 1;
	private static final int STRETCH_FILL = 2;
	private static final int APPLY = 3;
	
	private MenuComponent[] components;
	private int selectedComponent;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	
	private boolean setFullScreen;
	private boolean setStretchFit;
	
	public MenuGraphics(Main instance, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
		
		components = new MenuComponent[4];
		components[RESOLUTION] = new MenuListSelect(190, 76, 120, 20, 15, Type.SIZE_120X20, effectsPlayer,
				"1920x1080 (16:9)", "1280x720 (16:9)", "960x540 (16:9)", "640x360 (16:9)", "320x180 (16:9)",
				"1280x960 (4:3)", "960x720 (4:3)", "640x480 (4:3)", "320x240 (4:3)",
				"960x960 (1:1)", "640x640 (1:1)", "320x320 (1:1)",
				"unknown");
		components[FULL_SCREEN] = new MenuButton(190, 100, 120, 20, "Full Screen", 15, Type.SIZE_120X20);
		components[STRETCH_FILL] = new MenuButton(190, 124, 120, 20, "Stretch To Fill", 15, Type.SIZE_120X20);
		components[APPLY] = new MenuButton(190, 148, 120, 20, "Apply", 15, Type.SIZE_120X20);
		
		setSelectedComponent(0);
		
		openSettings();
	}
	
	public void openSettings(){
		MenuListSelect s = ((MenuListSelect)components[RESOLUTION]);
		String load = Settings.getScreenWidth() + "x" + Settings.getScreenHeight();
		int found = -1;
		for(int i = 0; i < s.getOptionsLength(); i++){
			String option = s.getOption(i);
			if(option.contains(load)){
				found = i;
				break;
			}
		}
		if(found != -1) s.setSelectedOption(found);
		else s.setSelectedOption(s.getOptionsLength() - 1);
		setFullScreen = Settings.getFullScreen();
		setStretchFit = Settings.getStretchToFit();
	}
	
	private void setSelectedComponent(int b){
		if(b < 0) b = components.length - 1;
		else if(b > components.length - 1) b = 0;
		
		for(int i = 0; i < components.length; i++) components[i].setSelected(false);
		components[b].setSelected(true);
		selectedComponent = b;
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
		g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Swap Option", 2, 177);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Graphics", 10, 36);
		
		g.setFont(new Font("Impact", Font.PLAIN, 15));
		g.drawString("Screen Size", 108, 91);
		if(setFullScreen) g.drawString("Currently On", 108, 114);
		else g.drawString("Currently Off", 108, 114);
		if(setStretchFit) g.drawString("Currently On", 108, 138);
		else g.drawString("Currently Off", 108, 138);
		
		for(int i = 0; i < components.length; i++) components[i].render(g);
	}

	@Override
	public void pressLeft(){
		if(components[RESOLUTION].isSelected()){
			((MenuListSelect)components[RESOLUTION]).moveSelectedOption(-1);
		}
		else if(components[FULL_SCREEN].isSelected()){
			setFullScreen = !setFullScreen;
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(components[STRETCH_FILL].isSelected()){
			setStretchFit = !setStretchFit;
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressRight(){
		if(components[RESOLUTION].isSelected()){
			((MenuListSelect)components[RESOLUTION]).moveSelectedOption(1);
		}
		else if(components[FULL_SCREEN].isSelected()){
			setFullScreen = !setFullScreen;
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(components[STRETCH_FILL].isSelected()){
			setStretchFit = !setStretchFit;
			effectsPlayer.playSound(Sounds.CLICK);
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
		if(components[FULL_SCREEN].isSelected()){
			setFullScreen = !setFullScreen;
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(components[STRETCH_FILL].isSelected()){
			setStretchFit = !setStretchFit;
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(components[APPLY].isSelected()){
			String s = ((MenuListSelect)components[RESOLUTION]).getSelectedOption();
			if(s.contains(" ") && s.contains("x")){
				Settings.setScreenWidth(Integer.parseInt(s.substring(0, s.indexOf('x'))));
				Settings.setScreenHeight(Integer.parseInt(s.substring(s.indexOf('x') + 1, s.indexOf(' '))));
			}
			if(setFullScreen != Settings.getFullScreen()) Settings.setFullScreen(setFullScreen);
			Settings.setStretchToFit(setStretchFit);
			Settings.save();
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressBack(){
		instance.setGamestate(Main.GameState.MENU_SETTINGS);
		effectsPlayer.playSound(Sounds.CLICK);
	}

}
