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

public class MenuControls implements Menu{
	
	public static final int MOVE_LEFT = 0;
	public static final int MOVE_RIGHT = 1;
	public static final int JUMP = 2;
	public static final int DUCK = 3;
	public static final int ATTACK = 4;
	public static final int USE = 5;
	public static final int PAUSE = 6;
	public static final int SET_DEFAULT = 7;
	public static final int APPLY = 8;
	
	private static final int NOTICE_DISPLAY_TIME = 120;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	private Main.KeyInput keyInput;
	
	private MenuButton[] buttons;
	private int selectedButton;
	
	private int settingBind;
	private int newBind;
	private int setControlsDisplayTime;
	private boolean[] bindError;
	private int[] setControls;
	
	public MenuControls(Main instance, Main.KeyInput keyInput, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.keyInput = keyInput;
		
		resetMenu();
		this.effectsPlayer = effectsPlayer;
	}
	
	public void resetMenu(){
		buttons = new MenuButton[9];
		buttons[MOVE_LEFT] = new MenuButton(210, 5, 100, 15, "Move Left", 12, Type.SIZE_100X15);
		buttons[MOVE_RIGHT] = new MenuButton(210, 24, 100, 15, "Move Right", 12, Type.SIZE_100X15);
		buttons[JUMP] = new MenuButton(210, 43, 100, 15, "Jump", 12, Type.SIZE_100X15);
		buttons[DUCK] = new MenuButton(210, 62, 100, 15, "Duck", 12, Type.SIZE_100X15);
		buttons[ATTACK] = new MenuButton(210, 81, 100, 15, "Attack", 12, Type.SIZE_100X15);
		buttons[USE] = new MenuButton(210, 100, 100, 15, "Use", 12, Type.SIZE_100X15);
		buttons[PAUSE] = new MenuButton(210, 119, 100, 15, "Pause", 12, Type.SIZE_100X15);
		buttons[SET_DEFAULT] = new MenuButton(210, 138, 100, 15, "Select Defaults", 12, Type.SIZE_100X15);
		buttons[APPLY] = new MenuButton(210, 157, 100, 15, "Apply", 12, Type.SIZE_100X15);
		
		settingBind = -1;
		newBind = -1;
		setControlsDisplayTime = 0;
		bindError = new boolean[]{false, false, false, false, false, false, false};
		
		setSelectedButton(0);
		
		openSettings();
	}
	
	public void openSettings(){
		setControls = new int[7];
		setControls[0] = Settings.getPressLeft();
		setControls[1] = Settings.getPressRight();
		setControls[2] = Settings.getPressJump();
		setControls[3] = Settings.getPressDuck();
		setControls[4] = Settings.getPressAttack();
		setControls[5] = Settings.getPressUse();
		setControls[6] = Settings.getPressPause();
	}
	
	private void setSelectedButton(int b){
		if(b < 0) b = buttons.length - 1;
		else if(b > buttons.length - 1) b = 0;
		
		if(isBindError() && b == APPLY){
			if(selectedButton == 0) b = SET_DEFAULT;
			else b = MOVE_LEFT;
		}
		
		for(int i = 0; i < buttons.length; i++) buttons[i].setSelected(false);
		buttons[b].setSelected(true);
		selectedButton = b;
	}
	
	public boolean isBindError(){
		for(int i = 0; i < bindError.length; i++) if(bindError[i]) return true;
		return false;
	}
	
	@Override
	public void tick(){
		if(settingBind < 0) for(int i = 0; i < buttons.length; i++) buttons[i].tick();
		else{
			newBind++;
			if(newBind > 20 && keyInput.isKeyDown() && keyInput.getLastKeyPressed() != 0){
				setControls[selectedButton] = keyInput.getLastKeyPressed();
				effectsPlayer.playSound(Sounds.CLICK);
				newBind = 0;
				settingBind = -1;
			}
		}
		
		bindError = new boolean[]{false, false, false, false, false, false, false};
		for(int i = 0; i < setControls.length; i++){
			for(int j = i + 1; j < setControls.length; j++){
				if(setControls[i] == setControls[j]) bindError[i] = bindError[j]  = true;
			}
		}
		
		if(setControlsDisplayTime > 0){
			setControlsDisplayTime--;
		}
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 12));
		g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Rebind / Enter", 2, 138);
		g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
		g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " : Up", 2, 164);
		g.drawString(KeyEvent.getKeyText(Settings.getPressDuck()) + " : Down", 2, 177);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Controls", 10, 36);
		
		if(settingBind < 0) {
			if(isBindError()) for(int i = 0; i < buttons.length - 1; i++) buttons[i].render(g);
			else for(int i = 0; i < buttons.length; i++) buttons[i].render(g);
			
			g.setFont(new Font("Impact", Font.PLAIN, 13));
			
			for(int i = 0; i < setControls.length; i++){
				if(bindError[i]) g.setColor(Color.RED);
				else g.setColor(Color.WHITE);
				g.drawString(KeyEvent.getKeyText(setControls[i]), buttons[i].getX() - 80, buttons[i].getY() + 13);
				
				int length = 70 - g.getFontMetrics().stringWidth(KeyEvent.getKeyText(setControls[i]));
				if(length > 0) g.fillRect(buttons[i].getX() - (length + 4), buttons[i].getY() + buttons[i].getHeight() / 2 - 1, length, 2);
			}
			
			g.setColor(Color.RED);
			if(isBindError()) {
				g.drawString("Cannot have more than", 2, 70);
				g.drawString("one control binded to", 2, 85);
				g.drawString("the same key", 2, 100);
			}
			
			if(setControlsDisplayTime > 0){
				if(setControlsDisplayTime > NOTICE_DISPLAY_TIME * .9) g.setColor(new Color(255, 255, 255, (int)(255 * ((NOTICE_DISPLAY_TIME - setControlsDisplayTime) / (NOTICE_DISPLAY_TIME * .1)))));
				else if(setControlsDisplayTime < NOTICE_DISPLAY_TIME * .1) g.setColor(new Color(255, 255, 255, (int)(255 * ((setControlsDisplayTime) / (NOTICE_DISPLAY_TIME * .1)))));
				else g.setColor(Color.WHITE);
				g.drawString("Controls set", 20, 80);
				g.drawString("sucessfully", 20, 95);
			}
		}
		else {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 18));
			g.drawString("Press a key to set a binding for: " + buttons[selectedButton].getText(), 5, 90);
		}
	}

	@Override
	public void pressLeft(){
	}

	@Override
	public void pressRight(){
	}

	@Override
	public void pressUp(){
		if(settingBind < 0){
			setSelectedButton(selectedButton - 1);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressDown(){
		if(settingBind < 0){
			setSelectedButton(selectedButton + 1);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressEnter(){
		if(settingBind < 0 && selectedButton <= 6) {
			settingBind = selectedButton;
			newBind = 0;
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else if(buttons[APPLY].isSelected()){
			if(!isBindError()){
				Settings.setPressLeft(setControls[MOVE_LEFT]);
				Settings.setPressRight(setControls[MOVE_RIGHT]);
				Settings.setPressJump(setControls[JUMP]);
				Settings.setPressDuck(setControls[DUCK]);
				Settings.setPressAttack(setControls[ATTACK]);
				Settings.setPressUse(setControls[USE]);
				Settings.setPressPause(setControls[PAUSE]);
				Settings.save();
				setControlsDisplayTime = NOTICE_DISPLAY_TIME;
				effectsPlayer.playSound(Sounds.CLICK);
			}
		}
		else if(buttons[SET_DEFAULT].isSelected()) {
			setControls[0] = Settings.DEFAULT_PRESS_LEFT;
			setControls[1] = Settings.DEFAULT_PRESS_RIGHT;
			setControls[2] = Settings.DEFAULT_PRESS_JUMP;
			setControls[3] = Settings.DEFAULT_PRESS_DUCK;
			setControls[4] = Settings.DEFAULT_PRESS_ATTACK;
			setControls[5] = Settings.DEFAULT_PRESS_USE;
			setControls[6] = Settings.DEFAULT_PRESS_PAUSE;
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressBack(){
		if(settingBind < 0){
			instance.setGamestate(Main.GameState.MENU_SETTINGS);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

}
