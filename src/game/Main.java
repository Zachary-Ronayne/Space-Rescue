package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.input.Controller;
import game.libs.Files;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.MenuControls;
import game.menu.MenuCrash;
import game.menu.MenuCredits;
import game.menu.MenuGraphics;
import game.menu.MenuIntroCutscene;
import game.menu.MenuLevel;
import game.menu.MenuLoad;
import game.menu.MenuMain;
import game.menu.MenuSettings;
import game.menu.MenuSound;
import game.util.screen.ResizableScreen;
import game.util.sound.AudioPlayer;
import game.util.thread.ThreadPool;
import game.world.SaveFile;
import game.world.levels.Level;

public class Main extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public static final String FRAME_NAME = "Space Rescue";
	
	private static Main instance;
	private static AudioPlayer musicPlayer;
	private static AudioPlayer effectsPlayer;
	
	private static ThreadPool threadPool;
	private static JFrame frame;
	
	private ResizableScreen screen;
	private boolean running;
	
	private MenuMain mainMenu;
	private MenuSettings settingsMenu;
	private MenuGraphics graphicsMenu;
	private MenuSound soundMenu;
	private MenuControls controlsMenu;
	private MenuLoad loadMenu;
	private MenuLevel levelSelectMenu;
	private MenuCredits creditsMenu;
	private MenuIntroCutscene introCutsceneMenu;
	private MenuCrash crashMenu;
	private boolean fromMain;
	
	private Controller controls;
	private KeyInput keyInput;
	
	public enum GameState{
		MENU_MAIN, MENU_LOAD, MENU_SETTINGS, MENU_SOUND, MENU_GRAPHICS, MENU_CONTROLS, MENU_LEVEL, PLAY, CREDITS, INTRO, CRASH;
	}
	
	private GameState state;
	
	private Level loadedLevel;
	
	public void setGamestate(GameState s){
		if(state == GameState.MENU_GRAPHICS) Settings.save();
		if(state == GameState.PLAY && s != GameState.MENU_SETTINGS){
			musicPlayer.stopSounds();
			effectsPlayer.stopSounds();
		}
		
		switch(s){
			case CREDITS:
				creditsMenu.reset();
				musicPlayer.stopSounds();
				effectsPlayer.stopSounds();
				musicPlayer.playSound(Sounds.RAPID_DEPARTURE);
				break;
			case MENU_CONTROLS:
				controlsMenu.openSettings();
				controlsMenu.resetMenu();
				break;
			case MENU_GRAPHICS:
				graphicsMenu.openSettings();
				break;
			case MENU_LEVEL:
				levelSelectMenu.resetMenu(null);
				break;
			case MENU_LOAD:
				loadMenu.resetMenu();
				break;
			case MENU_MAIN:
				if(state == GameState.MENU_SETTINGS && !fromMain) s = GameState.PLAY;
				else mainMenu.resetMenu();
				break;
			case MENU_SETTINGS:
				break;
			case MENU_SOUND:
				break;
			case PLAY:
				musicPlayer.stopSounds();
				if(loadedLevel == null) return;
				break;
			case INTRO:
				musicPlayer.stopSounds();
				effectsPlayer.stopSounds();
				break;
			case CRASH:
				break;
		}
		state = s;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param changeSettings the settings will only be updated if true
	 */
	public static void setFrameSize(int width, int height, boolean changeSettings){
		frame.setSize(width + 6, height + 29);
		instance.screen.setCurrentWidth(width);
		instance.screen.setCurrentHeight(height);
		if(changeSettings){
			Settings.setScreenWidth(width);
			Settings.setScreenHeight(height);
		}
	}
	
	public static void updateResolution(){
		setFrameSize(Settings.getScreenWidth(), Settings.getScreenHeight(), false);
	}
	
	public static void updateStretchFit(){
		instance.screen.setStretchToFit(Settings.getStretchToFit());
		frame.setLocationRelativeTo(null);
	}
	
	/**
	 * Opens the specified SaveFile and puts the game in the level select screen using the given SaveFile
	 * @param file
	 */
	public void startNewGame(SaveFile file){
		musicPlayer.stopSounds();
		musicPlayer.playSound(Sounds.FOREIGN_GRAVITY);
		introCutsceneMenu.reset();
		introCutsceneMenu.setSaveFile(file);
		setGamestate(GameState.INTRO);
	}
	
	/**
	 * Opens the specified SaveFile and puts the game in the level select screen using the given SaveFile
	 * @param file
	 */
	public void openSaveFile(SaveFile file){
		setGamestate(GameState.MENU_LEVEL);
		levelSelectMenu.resetMenu(file);
		loadedLevel = null;
	}
	
	/**
	 * @param fromMain true if you are coming from the main menu, false if you are coming from the game
	 */
	public void openSettingsMenu(boolean fromMain){
		setGamestate(GameState.MENU_SETTINGS);
		this.fromMain = fromMain;
	}
	
	public void playLevel(int levelID, Level.Mode playMode, SaveFile save){
		loadedLevel = new Level(save, levelID, playMode, instance, musicPlayer, effectsPlayer);
		setGamestate(GameState.PLAY);
	}
	
	/**
	 * This should be called when times for a speedrun need to be sent over
	 * @param levelID
	 * @param playMode
	 * @param save
	 * @param times
	 */
	public void playLevel(int levelID, Level.Mode playMode, SaveFile save, int[] times){
		loadedLevel = new Level(save, levelID, playMode, instance, musicPlayer, effectsPlayer);
		loadedLevel.setSpeedrunTimes(times);
		setGamestate(GameState.PLAY);
	}

	public void init(){
		screen = new ResizableScreen(createVolatileImage(Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT));
		
		Settings.load();
		Sounds.load();
		Images.load();
		
		screen.setStretchToFit(Settings.getStretchToFit());
		setFrameSize(Settings.getScreenWidth(), Settings.getScreenHeight(), true);
		setFullScreen(Settings.getFullScreen(), false, true);
		
		running = true;
		
		controls = createControler();
		
		state = GameState.MENU_MAIN;
		
		mainMenu = new MenuMain(this, effectsPlayer);
		settingsMenu = new MenuSettings(this, effectsPlayer);
		graphicsMenu = new MenuGraphics(this, effectsPlayer);
		soundMenu = new MenuSound(this, musicPlayer, effectsPlayer);
		controlsMenu = new MenuControls(this, keyInput, effectsPlayer);
		loadMenu = new MenuLoad(this, effectsPlayer);
		levelSelectMenu = new MenuLevel(this, effectsPlayer);
		creditsMenu = new MenuCredits(this, musicPlayer, effectsPlayer);
		introCutsceneMenu = new MenuIntroCutscene(this, musicPlayer, effectsPlayer, null);
		crashMenu = new MenuCrash(this, effectsPlayer);
		fromMain = true;
		
		frame.requestFocus();
		frame.toFront();
		
		Settings.setVolume(musicPlayer, effectsPlayer);
	}
	
	public void tick(){
		try{
			switch(state){
				case CREDITS:
					creditsMenu.tick();
					break;
				case MENU_CONTROLS:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					controlsMenu.tick();
					break;
				case MENU_GRAPHICS:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					graphicsMenu.tick();
					break;
				case MENU_LEVEL:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					levelSelectMenu.tick();
					break;
				case MENU_LOAD:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					loadMenu.tick();
					break;
				case MENU_MAIN:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					mainMenu.tick();
					break;
				case MENU_SETTINGS:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					settingsMenu.tick();
					break;
				case MENU_SOUND:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					soundMenu.tick();
					break;
				case PLAY:
					if(loadedLevel != null) loadedLevel.tick();
					break;
				case INTRO:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					introCutsceneMenu.tick();
					break;
				case CRASH:
					if(musicPlayer.empty()) musicPlayer.playSound(Sounds.MAIN_THEME);
					crashMenu.tick();
					break;
			}
		}catch(Exception e){
			System.err.println("GAME HAS CRASHED\n");
			e.printStackTrace();
			setGamestate(GameState.CRASH);
		}
	}
	
	public void render(Graphics2D g2){
		try{
			Graphics2D g = (Graphics2D)screen.getGraphics();
			
			switch(state){
				case CREDITS:
					creditsMenu.render(g, 0, 0);
					break;
				case MENU_CONTROLS:
					controlsMenu.render(g, 0, 0);
					break;
				case MENU_GRAPHICS:
					graphicsMenu.render(g, 0, 0);
					break;
				case MENU_LEVEL:
					levelSelectMenu.render(g, 0, 0);
					break;
				case MENU_LOAD:
					loadMenu.render(g, 0, 0);
					break;
				case MENU_MAIN:
					mainMenu.render(g, 0, 0);
					break;
				case MENU_SETTINGS:
					settingsMenu.render(g, 0, 0);
					break;
				case MENU_SOUND:
					soundMenu.render(g, 0, 0);
					break;
				case PLAY:
					if(loadedLevel != null) loadedLevel.render(g);
					break;
				case INTRO:
					introCutsceneMenu.render(g, 0, 0);
					break;
				case CRASH:
					crashMenu.render(g, 0, 0);
					break;
			}
			
			g.dispose();
			
			screen.drawToComponent(this, g2);
		}
		catch(NullPointerException e){
			System.err.println("Failed to render");
		}
		catch(Exception e){
			System.err.println("Failed to render");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		if(running) render((Graphics2D)g);
	}
	
	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double numTicks = 60;
		final int nanoSecond = 1000000000;
		final double nanoTicks = nanoSecond / numTicks;
		double nanoTime = 0;
		int frames = 0;
		int ticks = 0;
		long timer = System.currentTimeMillis();
		
		long currentTime;
		while(running){
			//general update
			currentTime = System.nanoTime();
			nanoTime += (currentTime - lastTime) / nanoTicks;
			lastTime = currentTime;
			
			//ticks statements
			if(nanoTime >= 1){
				tick();
				ticks++;
				nanoTime--;
				
				repaint();
				frames++;
			}
			
			if(ticks > 61){
				nanoTime = 0;
			}
			
			//console output
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println(ticks + " Ticks\tFPS: " + frames);
				ticks = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public synchronized void stop(){
		if(!running) return;
		running = false;
		terminate();
	}
	
	public void stopSounds(){
		musicPlayer.stopSounds();
		effectsPlayer.stopSounds();
	}
	
	public void setUpKeyInput(){
		keyInput = new KeyInput();
	}
	
	public KeyInput getKeyInput(){
		return keyInput;
	}
	
	public static void main(String[] args){
		
		instance = new Main();
		musicPlayer = new AudioPlayer(false);
		effectsPlayer = new AudioPlayer(false);
		
		setUpFrame();
		
		threadPool = new ThreadPool(3);
		threadPool.runTask(instance);
		threadPool.runTask(musicPlayer);
		threadPool.runTask(effectsPlayer);
		threadPool.join();
	}
	
	private static void setUpFrame(){
		frame = new JFrame(FRAME_NAME);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Files.IMAGES + "/icon.png"));
		instance.setUpKeyInput();
		frame.add(instance);
		frame.addKeyListener(instance.getKeyInput());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				terminate();
			}
		});
		frame.setSize(Settings.DEFAULT_SCREEN_WIDTH + 6, Settings.DEFAULT_SCREEN_HEIGHT + 29);
		frame.setUndecorated(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.pack();
	}
	
	public static void setMouseHidden(boolean hide){
		if(hide) frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage(Files.IMAGES + "/cursor.gif"),new Point(frame.getX(), frame.getY()), "mouseIcon"));
		else frame.setCursor(null);
	}
	
	/**
	 * Ends the game, should prevent the game from closing acendintally, keeps track of unexpected closing, saves data before the program ends
	 */
	public static void terminate(){
		threadPool.close();
		frame.dispose();
		
		Settings.save();
		
		System.out.println("Exit and Save successful");
		System.exit(1);
		return;
	}
	
	public static void toggleFullScreen(){
		setFullScreen(!Settings.getFullScreen());
	}
	
	public static void setFullScreen(boolean full){
		setFullScreen(full, true, true);
	}
	
	public static void setFullScreen(boolean full, boolean updateSettings, boolean forceUpdate){
		if(!forceUpdate && full == Settings.getFullScreen()) return;
		if(full){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setFrameSize((int)screenSize.getWidth(), (int)screenSize.getHeight(), false);
			frame.dispose();
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			if(updateSettings) Settings.setFullScreen(true);
			frame.requestFocus();
			frame.toFront();
			setMouseHidden(true);
		}
		else{
			frame.dispose();
			setUpFrame();
			setFrameSize(Settings.getScreenWidth(), Settings.getScreenHeight(), true);
			if(updateSettings) Settings.setFullScreen(false);
			frame.setLocationRelativeTo(null);
			frame.requestFocus();
			frame.toFront();
			setMouseHidden(false);
		}
	}
	
	public class KeyInput extends KeyAdapter{
		
		private int lastKeyPressed;
		private boolean keyDown;
		
		public KeyInput(){
			super();
			lastKeyPressed = -1;
			keyDown = false;
		}
		
		@Override
		public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();
			
			lastKeyPressed = key;
			keyDown = true;
			
			if(key == Settings.getPressLeft()) controls.pressLeft();
			else if(key == Settings.getPressRight()) controls.pressRight();
			else if(key == Settings.getPressJump()) controls.pressJump();
			else if(key == Settings.getPressDuck()) controls.pressDuck();
			else if(key == Settings.getPressAttack()) controls.pressAttack();
			else if(key == Settings.getPressUse()) controls.pressUse();
			else if(key == Settings.getPressPause()) controls.pressPause();
		}
		
		@Override
		public void keyReleased(KeyEvent e){
			int key = e.getKeyCode();
			
			keyDown = false;
			
			if(key == Settings.getPressLeft()) controls.releaseLeft();
			else if(key == Settings.getPressRight()) controls.releaseRight();
			else if(key == Settings.getPressJump()) controls.releaseJump();
			else if(key == Settings.getPressDuck()) controls.releaseDuck();
			else if(key == Settings.getPressAttack()) controls.releaseAttack();
			else if(key == Settings.getPressUse()) controls.releaseUse();
			else if(key == Settings.getPressPause()) controls.releasePause();
		}
		
		public boolean isKeyDown(){
			return keyDown;
		}
		
		public int getLastKeyPressed(){
			return lastKeyPressed;
		}
	}
	
	/**
	 * Yeah I know this is really sloppy, but I swear it will make my life easier later on
	 * @return
	 */
	private Controller createControler(){
		return new Controller(){
			@Override
			public void pressLeft(){
				super.pressLeft();
				switch(state){
					case CREDITS:
						creditsMenu.pressLeft();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressLeft();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressLeft();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressLeft();
						break;
					case MENU_LOAD:
						loadMenu.pressLeft();
						break;
					case MENU_MAIN:
						mainMenu.pressLeft();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressLeft();
						break;
					case MENU_SOUND:
						soundMenu.pressLeft();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressLeft();
						break;
					case INTRO:
						introCutsceneMenu.pressLeft();
						break;
					case CRASH:
						crashMenu.pressLeft();
						break;
				}
			}
			@Override
			public void pressRight(){
				super.pressRight();
				switch(state){
					case CREDITS:
						creditsMenu.pressRight();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressRight();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressRight();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressRight();
						break;
					case MENU_LOAD:
						loadMenu.pressRight();
						break;
					case MENU_MAIN:
						mainMenu.pressRight();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressRight();
						break;
					case MENU_SOUND:
						soundMenu.pressRight();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressRight();
						break;
					case INTRO:
						introCutsceneMenu.pressRight();
						break;
					case CRASH:
						crashMenu.pressRight();
						break;
				}
			}
			@Override
			public void pressJump(){
				super.pressJump();
				switch(state){
					case CREDITS:
						creditsMenu.pressUp();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressUp();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressUp();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressUp();
						break;
					case MENU_LOAD:
						loadMenu.pressUp();
						break;
					case MENU_MAIN:
						mainMenu.pressUp();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressUp();
						break;
					case MENU_SOUND:
						soundMenu.pressUp();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressJump();
						break;
					case INTRO:
						introCutsceneMenu.pressUp();
						break;
					case CRASH:
						crashMenu.pressUp();
						break;
				}
			}
			@Override
			public void pressDuck(){
				super.pressDuck();
				switch(state){
					case CREDITS:
						creditsMenu.pressDown();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressDown();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressDown();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressDown();
						break;
					case MENU_LOAD:
						loadMenu.pressDown();
						break;
					case MENU_MAIN:
						mainMenu.pressDown();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressDown();
						break;
					case MENU_SOUND:
						soundMenu.pressDown();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressDuck();
						break;
					case INTRO:
						introCutsceneMenu.pressDown();
						break;
					case CRASH:
						crashMenu.pressDown();
						break;
				}
			}
			@Override
			public void pressAttack(){
				super.pressAttack();
				switch(state){
					case CREDITS:
						creditsMenu.pressEnter();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressEnter();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressEnter();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressEnter();
						break;
					case MENU_LOAD:
						loadMenu.pressEnter();
						break;
					case MENU_MAIN:
						mainMenu.pressEnter();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressEnter();
						break;
					case MENU_SOUND:
						soundMenu.pressEnter();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressAttack();
						break;
					case INTRO:
						introCutsceneMenu.pressEnter();
						break;
					case CRASH:
						crashMenu.pressEnter();
						break;
				}
			}
			@Override
			public void pressUse(){
				super.pressUse();
				switch(state){
					case CREDITS:
						creditsMenu.pressBack();
						break;
					case MENU_CONTROLS:
						controlsMenu.pressBack();
						break;
					case MENU_GRAPHICS:
						graphicsMenu.pressBack();
						break;
					case MENU_LEVEL:
						levelSelectMenu.pressBack();
						break;
					case MENU_LOAD:
						loadMenu.pressBack();
						break;
					case MENU_MAIN:
						mainMenu.pressBack();
						break;
					case MENU_SETTINGS:
						settingsMenu.pressBack();
						break;
					case MENU_SOUND:
						soundMenu.pressBack();
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressUse();
						break;
					case INTRO:
						introCutsceneMenu.pressBack();
						break;
					case CRASH:
						crashMenu.pressBack();
						break;
				}
			}
			@Override
			public void pressPause(){
				super.pressPause();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.pressPause();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releaseLeft(){
				super.releaseLeft();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseLeft();
						break;
					case INTRO:
						break;
					case CRASH:
						crashMenu.pressLeft();
						break;
				}
			}
			@Override
			public void releaseRight(){
				super.releaseRight();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseRight();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releaseJump(){
				super.releaseJump();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseJump();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releaseDuck(){
				super.releaseDuck();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseDuck();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releaseAttack(){
				super.releaseAttack();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseAttack();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releaseUse(){
				super.releaseUse();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releaseUse();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			@Override
			public void releasePause(){
				super.releasePause();
				switch(state){
					case CREDITS:
						break;
					case MENU_CONTROLS:
						break;
					case MENU_GRAPHICS:
						break;
					case MENU_LEVEL:
						break;
					case MENU_LOAD:
						break;
					case MENU_MAIN:
						break;
					case MENU_SETTINGS:
						break;
					case MENU_SOUND:
						break;
					case PLAY:
						if(loadedLevel != null) loadedLevel.releasePause();
						break;
					case INTRO:
						break;
					case CRASH:
						break;
				}
			}
			
			@Override
			public int getUUID(){
				return -1;
			}
		};
	}
	
}