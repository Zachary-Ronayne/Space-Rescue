package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import game.Main;
import game.Main.GameState;
import game.libs.Files;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.component.MenuButton;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;
import game.world.SaveFile;

public class MenuLoad implements Menu{
	
	public static final int DELETE = 0;
	public static final int OPEN = 1;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	
	private ArrayList<MenuButton> loadButtons;
	private int selectedLoadButton;
	
	private MenuButton[] buttons;
	private int selectedButton;
	
	private SaveFile[] saveFiles;
	
	private boolean inDelete;
	private int selectedDeleteButton;
	
	private MenuButton[] confirmDeleteButtons;
	
	public MenuLoad(Main instance, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
		
		resetMenu();
	}
	
	public void resetMenu(){
		
		loadButtons = new ArrayList<MenuButton>();
		
		File[] files = new File(Files.SAVES).listFiles();
		saveFiles = new SaveFile[files.length];
		for(int i = 0; i < files.length; i++){
			if(files[i].isFile()){
				String name = files[i].getName().substring(0, files[i].getName().indexOf('.'));
				loadButtons.add(new MenuButton(210, 80 + i * 24, 100, 20, "file: " + name, 15, Type.SIZE_100X20));
				saveFiles[i] = new SaveFile(name);
			}
		}
		
		setSelectedLoadButton(0);
		
		buttons = new MenuButton[2];
		buttons[DELETE] = new MenuButton(10, 80, 90, 20, "Delete File", 15, Type.SIZE_90X20);
		buttons[OPEN] = new MenuButton(115, 80, 90, 20, "Open File", 15, Type.SIZE_90X20);
		setSelectedButton(OPEN);
		
		inDelete = false;
		
		confirmDeleteButtons = new MenuButton[2];
		confirmDeleteButtons[0] = new MenuButton(50, 95, 100, 20, "Yes", 15, Type.SIZE_100X20);
		confirmDeleteButtons[1] = new MenuButton(170, 95, 100, 20, "No", 15, Type.SIZE_100X20);
		setSelectedDeleteButton(1);
	}
	
	private void setSelectedLoadButton(int s){
		if(loadButtons.size() == 0) return;
		
		if(s > loadButtons.size() - 1) s = 0;
		else if(s < 0) s = loadButtons.size() - 1; 
		
		selectedLoadButton = s;
		
		for(int i = 0; i < loadButtons.size(); i++) loadButtons.get(i).setSelected(false);
		
		loadButtons.get(selectedLoadButton).setSelected(true);
		loadButtons.get(selectedLoadButton).setY(80);
		for(int i = 0; i < loadButtons.size(); i++){
			if(i < selectedLoadButton) loadButtons.get(i).setY(80 - (selectedLoadButton - i) * 24);
			else if(i > selectedLoadButton) loadButtons.get(i).setY(80 + (i - selectedLoadButton) * 24);
		}
		
		if(loadButtons.size() > 4){
			if(selectedLoadButton < 4 && loadButtons.size() - 1 - selectedLoadButton > 4){
				for(int i = 0; i < loadButtons.size() - 5 - selectedLoadButton && i < 4; i++){
					loadButtons.get(loadButtons.size() - 1 - i).setY(80 - (i + 1 + selectedLoadButton) * 24);
				}
			}
			
			if(selectedLoadButton > loadButtons.size() - 5 && loadButtons.size() - 1 - selectedLoadButton < 4){
				for(int i = 0; i <= 3 - (loadButtons.size() - 1 - selectedLoadButton) && i < 4; i++){
					loadButtons.get(i).setY(80 + (i + loadButtons.size() - selectedLoadButton) * 24);
				}
			}
		}
	}
	
	private void setSelectedButton(int b){
		if(b < 0) b = buttons.length - 1;
		else if(b > buttons.length - 1) b = 0;
		
		for(int i = 0; i < buttons.length; i++) buttons[i].setSelected(false);
		buttons[b].setSelected(true);
		selectedButton = b;
	}
	
	private void setSelectedDeleteButton(int b){
		if(b < 0) b = confirmDeleteButtons.length - 1;
		else if(b > confirmDeleteButtons.length - 1) b = 0;
		
		for(int i = 0; i < confirmDeleteButtons.length; i++) confirmDeleteButtons[i].setSelected(false);
		confirmDeleteButtons[b].setSelected(true);
		selectedDeleteButton = b;
	}
	
	@Override
	public void tick(){
		if(inDelete){
			for(int i = 0; i < confirmDeleteButtons.length; i++) confirmDeleteButtons[i].tick();
		}
		else{
			for(int i = 0; i < loadButtons.size(); i++) loadButtons.get(i).tick();
			
			for(int i = 0; i < buttons.length; i++) buttons[i].tick();
			
			if(loadButtons.size() == 0) instance.setGamestate(Main.GameState.MENU_MAIN);
		}
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		
		if(inDelete){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 20));
			g.drawString("Are you sure you want to delete this", 10, 36);
			g.drawString("file? (" + loadButtons.get(selectedLoadButton).getText() + ")", 10, 60);
			g.drawString("It will be permanently removed.", 10, 84);
			

			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 12));
			g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
			g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Left / Right", 2, 151);
			
			for(int i = 0; i < confirmDeleteButtons.length; i++) confirmDeleteButtons[i].render(g);
		}
		else{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 12));
			g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
			g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
			g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " / " + KeyEvent.getKeyText(Settings.getPressDuck()) + " : Up / Down", 2, 164);
			g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Choose Select / Delete", 2, 177);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 24));
			g.drawString("Load Save File", 10, 36);
			
			g.setFont(new Font("Impact", Font.PLAIN, 15));
			
			if(saveFiles.length > 0){
				String s = "Lives: " + saveFiles[selectedLoadButton].getLives();
				g.drawString(s, (int)(206 - g.getFontMetrics().stringWidth(s)), 120);
				
				int l = saveFiles[selectedLoadButton].getUnlockedLevel();
				if(l <= 1) l = 1;
				if(l >= SaveFile.NUM_LEVELS) l = SaveFile.NUM_LEVELS;
				s = "Unlocked Level: " + l;
				g.drawString(s, (int)(206 - g.getFontMetrics().stringWidth(s)), 140);
			}
			
			for(int i = 0; i < loadButtons.size(); i++){
				MenuButton b = loadButtons.get(i);
				if(b.getY() > -b.getHeight() && b.getY() < Settings.DEFAULT_SCREEN_HEIGHT) b.render(g);
			}
			
			for(int i = 0; i < buttons.length; i++) buttons[i].render(g);
		}
	}

	@Override
	public void pressLeft(){
		if(inDelete) setSelectedDeleteButton(selectedDeleteButton - 1);
		else setSelectedButton(selectedButton - 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressRight(){
		if(inDelete) setSelectedDeleteButton(selectedDeleteButton - 1);
		else setSelectedButton(selectedButton - 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressUp(){
		if(inDelete) setSelectedDeleteButton(selectedDeleteButton - 1);
		else setSelectedLoadButton(selectedLoadButton - 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressDown(){
		if(inDelete) setSelectedDeleteButton(selectedDeleteButton - 1);
		else setSelectedLoadButton(selectedLoadButton + 1);
		effectsPlayer.playSound(Sounds.CLICK);
	}

	@Override
	public void pressEnter(){
		String s = loadButtons.get(selectedLoadButton).getText();
		String name = s.substring("file: ".length());
		String path = Files.SAVES + "/" + name + ".txt";
		File f = new File(path);
		
		if(inDelete){
			if(confirmDeleteButtons[0].isSelected()){
				System.err.println("Attempting to delete file at: " + path);
				
				if(f.delete()) System.err.println(s + " deleted succesfully");
				else System.err.println("failed to delete " + s);
				
				loadButtons.remove(selectedLoadButton);
				setSelectedLoadButton(selectedLoadButton - 1);
				
				resetMenu();
				setSelectedButton(DELETE);
			}
			inDelete = false;
			setSelectedButton(OPEN);
			effectsPlayer.playSound(Sounds.CLICK);
		}
		else{
			if(loadButtons.size() != 0){
				
				if(buttons[DELETE].isSelected()){
					inDelete = true;
					effectsPlayer.playSound(Sounds.CLICK);
				}
				else if(buttons[OPEN].isSelected()){
					instance.openSaveFile(new SaveFile(name));
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
		}
		
	}

	@Override
	public void pressBack(){
		if(!inDelete){
			instance.setGamestate(GameState.MENU_MAIN);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

}
