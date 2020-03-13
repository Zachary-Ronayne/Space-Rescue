package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;

import game.Main;
import game.libs.Files;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.component.MenuButton;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;
import game.world.SaveFile;
import game.world.levels.Level;

public class MenuLevel implements Menu{
	
	public static final int PLAY_NORMAL = 0;
	public static final int PLAY_SPEED_RUN = 1;
	
	private Main instance;
	protected AudioPlayer effectsPlayer;
	
	private boolean confirmCreate;
	private int newFile;
	private SaveFile openedFile;
	
	private MenuButton[] confirmCreateButtons;
	
	private MenuButton[] levelButtons;
	private int selectedLevelButton;
	private boolean loaded;
	
	private MenuButton[] selectButtons;
	private int selectedSelectButton;
	
	public MenuLevel(Main instance, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
		
		resetMenu(null);
	}
	
	/**
	 * Resets this menu to its default state
	 * @param file Use null for this parameter if a new file should be created, otherwise provide a valid save file to open
	 */
	public void resetMenu(SaveFile file){
		loaded = false;
		
		confirmCreate = file == null;
		openedFile = null;
		newFile = 0;
		File[] files = new File(Files.SAVES).listFiles();
		for(int i = 0; i < files.length; i++){
			String s = files[i].getName();
			if(files[i].isFile()){
				try{
					int n = Integer.parseInt(s.substring(0, s.indexOf('.')));
					if(n >= newFile) newFile = n + 1;
				}catch(Exception e){}
			}
		}
		
		confirmCreateButtons = new MenuButton[2];
		confirmCreateButtons[0] = new MenuButton(50, 90, 100, 20, "Okay", 15, Type.SIZE_100X20);
		confirmCreateButtons[1] = new MenuButton(170, 90, 100, 20, "Cancel", 15, Type.SIZE_100X20);
		
		confirmCreateButtons[1].setSelected(true);
		
		selectButtons = new MenuButton[2];
		selectButtons[PLAY_NORMAL] = new MenuButton(214, 132, 100, 20, "Play level", 15, Type.SIZE_100X20);
		selectButtons[PLAY_SPEED_RUN] = new MenuButton(214, 156, 100, 20, "Speedrun", 15, Type.SIZE_100X20);
		
		if(file != null){
			openSaveFile(file);
			setSelectedSelectButton(PLAY_NORMAL);
			levelButtons = new MenuButton[SaveFile.NUM_LEVELS];
			for(int i = 0; i < levelButtons.length; i++){
				final int ii = i;
				levelButtons[i] = new MenuButton(5 + i * 80, 40, 70, 70, "", 50, Type.DEFAULT){
					@Override
					public void render(Graphics2D g){
						if(openedFile.getUnlockedLevel() <= ii - 1){
							g.drawImage(Images.thumbnails[0][0], getX(), getY(), null);
						}
						else{
							if(openedFile.getUnlockedLevel() == ii) g.drawImage(Images.thumbnails[ii + 1][1], getX(), getY(), null);
							else g.drawImage(Images.thumbnails[ii + 1][0], getX(), getY(), null);
						}
						if(isSelected()) g.drawImage(Images.thumbnailSelect, getX() - 2, getY() - 2, null);
					}
				};
			}
			
			setSelectedLevelButton(0);
		}
	}
	
	private void openSaveFile(SaveFile file){
		loaded = false;
		if(file == null){
			System.err.println("Cannot open a null SaveFile");
			return;
		}
		openedFile = file;
		file.load();
		loaded = true;
	}
	
	private void setSelectedLevelButton(int b){
		if(openedFile == null) return;
		if(b > openedFile.getUnlockedLevel() || b > SaveFile.NUM_LEVELS - 1) b = 0;
		else if(b < 0){
			b = openedFile.getUnlockedLevel();
			if(b >= SaveFile.NUM_LEVELS) b = SaveFile.NUM_LEVELS - 1;
		}
		
		for(int i = 0; i < levelButtons.length; i++) levelButtons[i].setSelected(false);
		levelButtons[b].setSelected(true);
		
		selectedLevelButton = b;
	}
	
	private void setSelectedSelectButton(int b){
		if(openedFile.getUnlockedLevel() == SaveFile.NUM_LEVELS) {
			if(b < 0) b = selectButtons.length - 1;
			else if(b > selectButtons.length - 1) b = 0;
		}
		else{
			if(b < 0) b = selectButtons.length - 2;
			else if(b > selectButtons.length - 2) b = 0;
		}
		
		for(int i = 0; i < selectButtons.length; i++) selectButtons[i].setSelected(false);
		selectButtons[b].setSelected(true);
		selectedSelectButton = b;
	}
	
	@Override
	public void tick(){
		try{
			if(confirmCreate){
				for(int i = 0; i < confirmCreateButtons.length; i++) confirmCreateButtons[i].tick();
			}
			else if(loaded){
				for(int i = 0; i < levelButtons.length; i++) levelButtons[i].tick();
				
				if(openedFile.getUnlockedLevel() == SaveFile.NUM_LEVELS) for(int i = 0; i < selectButtons.length; i++) selectButtons[i].tick();
				else for(int i = 0; i < selectButtons.length - 1; i++) selectButtons[i].tick();
			}
		}catch(Exception e){System.err.println("Error in MenuLevel");}
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		
		if(confirmCreate){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 12));
			g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
			g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
			g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " : Left", 2, 164);
			g.drawString(KeyEvent.getKeyText(Settings.getPressRight()) + " : Right", 2, 177);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 24));
			String s;
			if(newFile < 10) s = "with the name: 0" + newFile;
			else s = "with the name: " + newFile;
			g.drawString("This will create a new save", 10, 36);
			g.drawString(s, 10, 58);
			
			for(int i = 0; i < confirmCreateButtons.length; i++) confirmCreateButtons[i].render(g);
		}
		else{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 24));
			g.drawString("Level Select", 10, 36);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 12));
			g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
			g.drawString(KeyEvent.getKeyText(Settings.getPressUse()) + " : Back", 2, 151);
			if(openedFile.getUnlockedLevel() == SaveFile.NUM_LEVELS){
				g.drawString(KeyEvent.getKeyText(Settings.getPressJump()) + " / " + KeyEvent.getKeyText(Settings.getPressDuck()) + " : Up / Down", 2, 164);
				g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Left / Right", 2, 177);
			}
			else g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " / " + KeyEvent.getKeyText(Settings.getPressRight()) + " : Left / Right", 2, 164);
			
			if(loaded){
				for(int i = 0; loaded && i < levelButtons.length; i++){
					levelButtons[i].render(g);
					g.setColor(Color.WHITE);
					g.setFont(new Font("Impact", Font.PLAIN, 13));
					int time = openedFile.getLevelTime(i);
					if(time < 0) g.drawString("?", levelButtons[i].getX(), levelButtons[i].getY() + levelButtons[i].getHeight() + 15);
					else g.drawString(SaveFile.getTime(time), levelButtons[i].getX(), levelButtons[i].getY() + levelButtons[i].getHeight() + 15);
				}
				
				if(openedFile.getUnlockedLevel() == SaveFile.NUM_LEVELS){
					for(int i = 0; i < selectButtons.length; i++) selectButtons[i].render(g);
					if(openedFile.getSpeedrunTime() >= 0){
						g.setColor(Color.WHITE);
						g.setFont(new Font("Impact", Font.PLAIN, 15));
						String s = "Best speedrun: " + SaveFile.getTime(openedFile.getSpeedrunTime());
						g.drawString(s, (int)(316 - g.getFontMetrics().stringWidth(s)), 34);
					}
				}
				else for(int i = 0; i < selectButtons.length - 1; i++) selectButtons[i].render(g);
				
				g.setColor(Color.WHITE);
				g.setFont(new Font("Impact", Font.PLAIN, 15));
				String s = "Lives left : " + openedFile.getLives();
				g.drawString(s, (int)(316 - g.getFontMetrics().stringWidth(s)), 17);
			}
		}
	}
	
	@Override
	public void pressLeft(){
		if(confirmCreate){
			boolean s0 = confirmCreateButtons[0].isSelected();
			boolean s1 = confirmCreateButtons[1].isSelected();
			
			if(s0 && !s1){
				confirmCreateButtons[0].setSelected(false);
				confirmCreateButtons[1].setSelected(true);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else if(!s0 && s1){
				confirmCreateButtons[0].setSelected(true);
				confirmCreateButtons[1].setSelected(false);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else{
				confirmCreateButtons[0].setSelected(true);
				confirmCreateButtons[1].setSelected(false);
				effectsPlayer.playSound(Sounds.CLICK);
			}
		}
		else{
			setSelectedLevelButton(selectedLevelButton - 1);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}
	
	@Override
	public void pressRight(){
		if(confirmCreate){
			boolean s0 = confirmCreateButtons[0].isSelected();
			boolean s1 = confirmCreateButtons[1].isSelected();
			
			if(s0 && !s1) {
				confirmCreateButtons[0].setSelected(false);
				confirmCreateButtons[1].setSelected(true);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else if(!s0 && s1) {
				confirmCreateButtons[0].setSelected(true);
				confirmCreateButtons[1].setSelected(false);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else{
				confirmCreateButtons[0].setSelected(false);
				confirmCreateButtons[1].setSelected(true);
				effectsPlayer.playSound(Sounds.CLICK);
			}
		}
		else{
			setSelectedLevelButton(selectedLevelButton + 1);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}
	
	@Override
	public void pressUp(){
		if(!confirmCreate){
			setSelectedSelectButton(selectedSelectButton - 1);
			if(openedFile.getUnlockedLevel() >= 4) effectsPlayer.playSound(Sounds.CLICK);
		}
	}
	
	@Override
	public void pressDown(){
		if(!confirmCreate){
			setSelectedSelectButton(selectedSelectButton + 1);
			if(openedFile.getUnlockedLevel() >= 4) effectsPlayer.playSound(Sounds.CLICK);
		}
	}

	@Override
	public void pressEnter(){
		if(confirmCreate){
			if(confirmCreateButtons[1].isSelected()){
				instance.setGamestate(Main.GameState.MENU_MAIN);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else if(confirmCreateButtons[0].isSelected()){
				confirmCreate = false;
				String file = newFile + "";
				if(file.length() <= 1) file = "0" + file;
				SaveFile save = new SaveFile(file);
				save.save();
				instance.startNewGame(save);
			}
		}
		else{
			if(selectButtons[PLAY_NORMAL].isSelected()){
				if(openedFile.getUnlockedLevel() > selectedLevelButton) instance.playLevel(selectedLevelButton, Level.Mode.FREE, openedFile);
				else instance.playLevel(selectedLevelButton, Level.Mode.NORMAL, openedFile);
				effectsPlayer.playSound(Sounds.CLICK);
			}
			else if(selectButtons[PLAY_SPEED_RUN].isSelected()){
				instance.playLevel(0, Level.Mode.SPEED_RUN, openedFile);
				effectsPlayer.playSound(Sounds.CLICK);
			}
		}
	}

	@Override
	public void pressBack(){
		if(!confirmCreate){
			openedFile.save();
			instance.setGamestate(Main.GameState.MENU_MAIN);
			effectsPlayer.playSound(Sounds.CLICK);
		}
	}

}
