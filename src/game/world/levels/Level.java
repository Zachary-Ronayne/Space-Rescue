package game.world.levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.Main;
import game.input.Controlable;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.menu.Menu;
import game.menu.component.MenuButton;
import game.menu.component.MenuComponent.Type;
import game.util.sound.AudioPlayer;
import game.world.SaveFile;
import game.world.obj.GameObject;
import game.world.obj.Tile;
import game.world.obj.activate.Activatable;
import game.world.obj.entity.Player;
import game.world.obj.use.Usable;

public class Level implements Controlable{
	
	public static final int PAUSE_WIDTH = 130;
	public static final int PAUSE_HEIGHT = 160;
	
	private int playTime;
	private int[] speedrunTimes;
	private int totalSpeedrunTime;
	
	private SaveFile loadedFile;
	private Mode playMode;
	private int levelID;
	
	private Main instance;
	private AudioPlayer musicPlayer;
	private AudioPlayer effectsPlayer;
	
	private Point2D.Double cameraPos;
	private GameObject cameraObject;
	
	private ArrayList<GameObject> objects;
	private ArrayList<GameObject> objectBounds;
	private ArrayList<Controlable> controlables;
	private ArrayList<Usable> usables;
	private ArrayList<Activatable> activatables;
	private ArrayList<Integer> removeUUID;
	private Player mainPlayer;
	
	private boolean paused;
	private Menu pauseMenu;
	
	/**
	 * The array of the tiles in this level. (0,0) is the upper left hand corner tile, and tiles will always snap to the rounded down tile position
	 */
	private Tile[][] tiles;
	
	private State levelState;
	private Menu dieMenu;
	private int gameOverTimer;
	private Menu winMenu;
	private int speedrunTimer;
	private boolean newSpeedrunTime;
	
	public static final int GAME_OVER_TIME = 90;
	public static final int SPEEDRUN_TIME = 90;
	
	public enum State{
		PLAY, DIE, GAME_OVER, WIN;
	}
	
	public enum Mode{
		NORMAL, FREE, SPEED_RUN;
	}
	
	public Level(SaveFile loadedFile, int levelID, Mode playMode, Main instance, AudioPlayer musicPlayer, AudioPlayer effectsPlayer){
		this.loadedFile = loadedFile;
		this.levelID = levelID;
		this.playMode = playMode;
		this.instance = instance;
		this.musicPlayer = musicPlayer;
		this.effectsPlayer = effectsPlayer;
		
		cameraPos = new Point2D.Double(0, 0);
		
		clear();
		
		cameraObject = null;
		
		switch(levelID){
			case 0: Level0Tutorial.setLevel(this); break;
			case 1: Level1Outside.setLevel(this); break;
			case 2: Level2Inside.setLevel(this); break;
			case 3: Level3Boss.setLevel(this); break;
			default: throw new IllegalArgumentException("Invalid LevelID of " + levelID);
		}
		
		findCameraObject();
		findPlayer();
		
		centerCamera();
		
		paused = false;
		pauseMenu = getPauseMenu();
		
		levelState = State.PLAY;
		gameOverTimer = GAME_OVER_TIME;
		speedrunTimer = SPEEDRUN_TIME;
		
		playTime = 0;
		speedrunTimes = new int[SaveFile.NUM_LEVELS];
		for(int i = 0; i < speedrunTimes.length; i++) speedrunTimes[i] = -1;
		totalSpeedrunTime = 0;
		newSpeedrunTime = false;
	}
	
	/**
	 * Removes everything in the level, including all tiles and entities, also acts as a reset method
	 */
	public void clear(){
		objects = new ArrayList<GameObject>();
		controlables = new ArrayList<Controlable>();
		removeUUID = new ArrayList<Integer>();
		objectBounds = new ArrayList<GameObject>();
		usables = new ArrayList<Usable>();
		activatables = new ArrayList<Activatable>();
		setTiles(0, 0);
	}
	
	public void addObject(GameObject obj){
		for(GameObject o : objects) if(o.getUUID() == obj.getUUID()) return;
		objects.add(obj);
		if(obj instanceof Controlable) controlables.add((Controlable)obj);
		if(obj instanceof Usable) usables.add((Usable)obj);
		if(obj instanceof Activatable) activatables.add((Activatable)obj);
	}
	
	public void removeObject(GameObject obj){
		removeObject(obj.getUUID());
	}
	
	public void removeObject(int UUID){
		removeUUID.add(UUID);
	}
	
	public void setCameraObject(GameObject o){
		cameraObject = o;
	}
	
	public GameObject getCameraObject(){
		return cameraObject;
	}
	
	public double getCameraX(){
		return cameraPos.x;
	}
	
	public double getCameraY(){
		return cameraPos.y;
	}
	
	private void findCameraObject(){
		findPlayer();
		cameraObject = mainPlayer;
	}
	
	private void findPlayer(){
		mainPlayer = null;
		
		for(int i = 0; i < objects.size(); i++){
			if(objects.get(i) instanceof Player){
				mainPlayer = (Player)(objects.get(i));
			}
		}
	}
	
	/**
	 * Sets the tile array to a rectangular array of x and y size, each tile is by default air
	 * @param x the width of the array
	 * @param y the height of the array
	 */
	public void setTiles(int x, int y){
		tiles = new Tile[x][y];
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j] = new Tile(i * Tile.TILE_SIZE, j * Tile.TILE_SIZE, Tile.Type.AIR);
			}
		}
	}
	
	/**
	 * Sets the specific tile to the specified tile
	 * @param x the width index of the tile
	 * @param y the height index of the tile
	 * @param tile the type of the tile to be set to
	 */
	public void setTile(int x, int y, Tile.Type tile){
		tiles[x][y].setTile(tile);
	}
	
	private void setPaused(boolean paused){
		this.paused = paused;
		if(paused) pauseMenu = getPauseMenu();
		else for(GameObject obj : objects) obj.pause();
	}
	
	/**
	 * @return true if the game is currently being played, i.e. not paused and not in a death/victory screen
	 */
	public boolean playingGame(){
		return levelState == State.PLAY;
	}
	
	public void winLevel(){
		
		boolean newBestTme = false;
		int levelTime = (int)(playTime / 60.0 * 1000.0);
		int oldTime = loadedFile.getLevelTime(levelID);
		if(oldTime < 0 || levelTime < oldTime){
			loadedFile.setLevelTime(levelID, levelTime);
			newBestTme = true;
		}
		if(levelID == loadedFile.getUnlockedLevel()) loadedFile.setUnlockedLevel(loadedFile.getUnlockedLevel() + 1);
		loadedFile.save();
		if(playMode == Mode.SPEED_RUN){
			setSpeedRunTime(playTime, levelID);
			if(levelID + 1 < SaveFile.NUM_LEVELS) instance.playLevel(levelID + 1, Mode.SPEED_RUN, loadedFile, speedrunTimes);
			else{
				totalSpeedrunTime = 0;
				for(int i = 0; i < speedrunTimes.length; i++){
					speedrunTimes[i] = (int)(speedrunTimes[i] / 60.0 * 1000.0);
					totalSpeedrunTime += speedrunTimes[i];
				}
				int newS = totalSpeedrunTime;
				int oldS = loadedFile.getSpeedrunTime();
				if(oldS < 0 || newS < oldS){
					loadedFile.setSpeedrunTime(newS);
					loadedFile.save();
					newSpeedrunTime = true;
				}
				else newSpeedrunTime = false;
				levelState = State.WIN;
			}
		}
		else{
			winMenu = getWinMenu(newBestTme);
			levelState = State.WIN;
		}
		
		if(playMode != Mode.SPEED_RUN || playMode == Mode.SPEED_RUN && inBossFight()){
			musicPlayer.stopSounds();
			musicPlayer.playSound(Sounds.VICTORY);
		}
	}
	
	public void die(){
		musicPlayer.stopSounds();
		musicPlayer.playSound(Sounds.GAME_OVER);
		
		switch(playMode){
			case FREE:
				levelState = State.DIE;
				dieMenu = getDieMenu();
				break;
			case NORMAL:
				loadedFile.setLives(loadedFile.getLives() - 1);
				loadedFile.save();
				if(loadedFile.getLives() > 0){
					levelState = State.DIE;
					dieMenu = getDieMenu();
				}
				else gameOver();
				break;
			case SPEED_RUN:
				levelState = State.GAME_OVER;
				break;
		}
	}
	
	private void gameOver(){
		loadedFile.delete();
		levelState = State.GAME_OVER;
	}
	
	public void setSpeedRunTime(int time, int index){
		speedrunTimes[index] = time;
	}
	
	public void setSpeedrunTimes(int[] times){
		speedrunTimes = times;
	}
	
	public void tick(){
		switch(levelState){
			case DIE:
				dieMenu.tick();
				break;
			case GAME_OVER:
				if(gameOverTimer > 0) gameOverTimer--;
				break;
			case PLAY:
				
				if(playingGame() && musicPlayer.empty()){
					if(levelID == 3) musicPlayer.playSound(Sounds.BOSS_THEME);
					else musicPlayer.playSound(Sounds.FOREIGN_GRAVITY);
				}
				
				if(paused) pauseMenu.tick();
				else{
					playTime++;
					
					for(int i = 0; i < objects.size(); i++) objects.get(i).tick();
					
					for(Tile[] ts : tiles){
						for(Tile t : ts){
							t.tick();
						}
					}
					
					for(int j = 0; j < removeUUID.size(); j++){
						boolean removed = false;
						for(int i = 0; i < objects.size(); i++){
							if(objects.get(i).getUUID() == removeUUID.get(j)){
								objects.remove(i);
								removed = true;
								break;
							}
						}
						
						for(int i = 0; i < controlables.size(); i++){
							if(controlables.get(i).getUUID() == removeUUID.get(j)){
								controlables.remove(i);
								removed = true;
								break;
							}
						}
						
						for(int i = 0; i < usables.size(); i++){
							if(usables.get(i).getUUID() == removeUUID.get(j)){
								usables.remove(i);
								removed = true;
								break;
							}
						}
						
						for(int i = 0; i < activatables.size(); i++){
							int UUID = activatables.get(i).getUUID();
							if(UUID == removeUUID.get(j)){
								for(Usable u : usables) u.removeActivatable(UUID);
								activatables.remove(i);
								removed = true;
								break;
							}
						}
						
						if(removed){
							removeUUID.remove(j);
							j--;
						}
					}
					
					objectBounds = new ArrayList<GameObject>();
					for(int i = 0; i < objects.size(); i++) objectBounds.add(objects.get(i));
					
					if(mainPlayer != null && mainPlayer.getHealth() <= 0) die();
					
					centerCamera();
				}
				break;
			case WIN:
				if(playMode == Mode.SPEED_RUN){
					if(speedrunTimer > 0) speedrunTimer--;
				}
				else winMenu.tick();
				break;
			
		}
	}
	
	public void render(Graphics2D g){
		switch(levelState){
			case GAME_OVER:
			{
				double ratio;
				boolean renderGame = gameOverTimer > GAME_OVER_TIME / 2;
				
				if(renderGame) ratio = (gameOverTimer - (GAME_OVER_TIME / 2.0)) / (GAME_OVER_TIME / 2.0);
				else ratio = 1 - gameOverTimer / (GAME_OVER_TIME / 2.0);
				
				if(renderGame) renderGame(g);
				else g.drawImage(Images.menuBackground, 0, 0, null);
				
				if(renderGame) g.setColor(new Color((int)(100 * ratio), 0, 0, 255 - (int)(255 * ratio)));
				else g.setColor(new Color(0, 0, 0, 255 - (int)(255 * ratio)));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				
				ratio = gameOverTimer / (double)GAME_OVER_TIME;
				
				int y = 0;
				String s;
				
				if(playMode == Mode.SPEED_RUN){
					g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
					g.setFont(new Font("Impact", Font.PLAIN, 15));
					for(int i = 0; i < speedrunTimes.length; i++){
						s = "Level " + (i + 1) + " time: " + SaveFile.getTime((int)(speedrunTimes[i] / 60.0 * 1000.0));
						if(loadedFile.getLevelTime(i) == speedrunTimes[i] / 60.0 * 1000.0) s += " (Best time!)";
						if(speedrunTimes[i] < 0) s = "Level " + (i + 1) + " incomplete";
						g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 120 + i * 17);
						y = -34;
					}
					
					g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
					g.setFont(new Font("Impact", Font.PLAIN, 40));
					s = "Speedrun over";
					g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 42);
				}
				else{
					g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
					g.setFont(new Font("Impact", Font.PLAIN, 40));
					g.drawString("GAME OVER", 72, 76);
				}
				if(gameOverTimer <= 0){
					g.setFont(new Font("Impact", Font.PLAIN, 20));
					s = "Press '" + KeyEvent.getKeyText(Settings.getPressAttack());
					if(playMode == Mode.SPEED_RUN) s += "' to exit to level menu";
					else s += "' to exit to main menu";
					double d = g.getFontMetrics().stringWidth(s);
					g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - d) / 2), 120 + y);
				}
				break;
			}
			case PLAY:
				renderGame(g);
				break;
			case DIE:
				dieMenu.render(g, 0, 0);
				break;
			case WIN:
			{
				if(playMode == Mode.SPEED_RUN){
					
					double ratio;
					boolean renderGame = speedrunTimer > SPEEDRUN_TIME / 2;
					
					if(renderGame) ratio = (speedrunTimer - (SPEEDRUN_TIME / 2.0)) / (SPEEDRUN_TIME / 2.0);
					else ratio = 1 - speedrunTimer / (SPEEDRUN_TIME / 2.0);
					
					if(renderGame) renderGame(g);
					else g.drawImage(Images.menuBackground, 0, 0, null);
					
					if(renderGame) g.setColor(new Color(0, 0, (int)(100 * ratio), 255 - (int)(255 * ratio)));
					else g.setColor(new Color(0, 0, 0, 255 - (int)(255 * ratio)));
					g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
					
					ratio = speedrunTimer / (double)SPEEDRUN_TIME;
					
					g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
					
					g.setFont(new Font("Impact", Font.PLAIN, 35));
					g.drawString("Speedrun Complete", 18, 37);
					
					g.setFont(new Font("Impact", Font.PLAIN, 15));
					String s = "Completed in: " + SaveFile.getTime((int)(totalSpeedrunTime));
					if(newSpeedrunTime) s += " (New best time!)";
					g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 59);

					for(int i = 0; i < speedrunTimes.length; i++){
						s = "Level " + (i + 1) + " time: " + SaveFile.getTime(speedrunTimes[i]);
						if(loadedFile.getLevelTime(i) == speedrunTimes[i]) s += " (Best time!)";
						g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 81 + i * 17);
					}
					
					g.setFont(new Font("Impact", Font.PLAIN, 11));
					s = "Press '" + KeyEvent.getKeyText(Settings.getPressAttack()) + "' to exit to level menu";
					if(speedrunTimer <= 0) g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2), 173);
				}
				else winMenu.render(g, 0, 0);
				break;
			}
		}
		
	}
	
	private void renderGame(Graphics2D g){
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_WIDTH);
		double bx = 0;
		double by = 0;
		if(cameraPos != null){
			bx = -(cameraPos.x / 6.0);
			by = -(cameraPos.y / 6.0);
		}
		try{
			g.drawImage(Images.backgroundStars, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
					(int)Math.round(bx), (int)Math.round(by), (int)Math.round(bx + Settings.DEFAULT_SCREEN_WIDTH + 2), (int)Math.round(by + Settings.DEFAULT_SCREEN_HEIGHT + 2), null);
			g.drawImage(Images.backgroundPlanets, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
					(int)Math.round(bx * 1.3), (int)Math.round(by * 1.3), (int)Math.round(bx * 1.3 + Settings.DEFAULT_SCREEN_WIDTH + 2), (int)Math.round(by * 1.3 + Settings.DEFAULT_SCREEN_HEIGHT + 2), null);
			g.drawImage(Images.backgroundHills, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
					(int)Math.round(bx * 1.5), (int)Math.round(by * 2.0), (int)Math.round(bx * 1.5 + Settings.DEFAULT_SCREEN_WIDTH + 2), (int)Math.round(by * 2.0 + Settings.DEFAULT_SCREEN_HEIGHT + 2), null);
		}catch(Exception e){}

		//foreground
		for(int r = GameObject.MIN_RENDER_PRIORITY; r <= GameObject.MAX_RENDER_PRIORITY; r++){
			for(GameObject obj : objects){
				if(obj.getRenderPriority() == r) obj.render(g, cameraPos.x, cameraPos.y);
			}
			
			for(Tile[] ts : tiles) for(Tile t : ts){
				if(t.getRenderPriority() == r) t.render(g, cameraPos.x, cameraPos.y);
			}
		}
		
		//HUD
		int hx = 274;
		int hy = 2;
		
		g.drawImage(Images.menuHud, hx, hy, null);
		
		int health;
		if(levelState != State.DIE) health = mainPlayer.getHealth();
		else health = 0;

		g.setColor(new Color(0, 0, 20));
		g.setFont(new Font("Impact", Font.PLAIN, 10));
		g.drawString("" + health, hx + 30, hy + 10);
		g.drawString("" + loadedFile.getLives(), hx + 25, hy + 20);
		
		//pause menu
		if(paused){
			pauseMenu.render(g, (Settings.DEFAULT_SCREEN_WIDTH - PAUSE_WIDTH) / 2, (Settings.DEFAULT_SCREEN_HEIGHT - PAUSE_HEIGHT) / 2);
		}
	}
	
	public void centerCamera(){
		if(cameraObject != null){
			Point2D.Double p = cameraObject.getNormalCenter();
			double x = p.x - Settings.DEFAULT_SCREEN_WIDTH / 2;
			double y = p.y - Settings.DEFAULT_SCREEN_HEIGHT / 2;
			
			if(x < 0) x = 0;
			if(y < 0) y = 0;
			double a = tiles.length * Tile.TILE_SIZE - Settings.DEFAULT_SCREEN_WIDTH;
			if(x > a) x = a;
			a = tiles[0].length * Tile.TILE_SIZE - Settings.DEFAULT_SCREEN_HEIGHT;
			if(y > a) y = a;
			
			cameraPos.x = -x;
			cameraPos.y = -y;
		}
	}
	
	public void setTiles(Tile[][] tiles){
		this.tiles = tiles;
	}
	
	/**
	 * @param r
	 * @return A list of all the tiles an object with the bounds r intersects
	 */
	public ArrayList<Tile> getCollideTiles(Rectangle2D.Double r){
		ArrayList<Tile> arr = new ArrayList<Tile>();
		
		int startI = (int)((r.x - Tile.TILE_SIZE) / Tile.TILE_SIZE);
		int endI = (int)((r.x + r.width + Tile.TILE_SIZE) / Tile.TILE_SIZE);
		int startJ = (int)((r.y - Tile.TILE_SIZE) / Tile.TILE_SIZE);
		int endJ = (int)((r.y + r.height + Tile.TILE_SIZE) / Tile.TILE_SIZE);
		
		if(startI < 0) startI = 0;
		if(endI > tiles.length) endI = tiles.length;
		if(startJ < 0) startJ = 0;
		if(endJ > tiles[0].length) endJ = tiles[0].length;
		
		for(int i = startI; i < endI; i++){
			for(int j = startJ; j < endJ; j++){
				arr.add(tiles[i][j]);
			}
		}
		
		return arr;
	}
	
	/**
	 * @param UUID
	 * @return A list of all the bounds of all the objects in the list. Does not include the object with the given UUID.
	 */
	public ArrayList<Rectangle2D.Double> getObjectBoundsList(int UUID){
		ArrayList<Rectangle2D.Double> bounds = new ArrayList<Rectangle2D.Double>();
		for(int i = 0; i < objectBounds.size(); i++) if(objectBounds.get(i).getUUID() != UUID) bounds.add(objectBounds.get(i).getBounds());
		
		return bounds;
	}
	
	/**
	 * @param obj
	 * @return A list of all the objects (not tiles) that currently intersect the given GameObject. Objects with the same UUID as the given GameObject are not included.
	 */
	public ArrayList<GameObject> getIntersectingObjects(GameObject obj){
		ArrayList<GameObject> objs = new ArrayList<GameObject>();
		for(int i = 0; i < objects.size(); i++){
			if(objects.get(i).getUUID() != obj.getUUID() && objects.get(i).getBounds().intersects(obj.getBounds())) objs.add(objects.get(i));
		}
		return objs;
	}
	
	/**
	 * @return All objects in the list
	 */
	public ArrayList<GameObject> getObjects(){
		return objects;
	}
	
	/**
	 * Uses all usable objects within range of the the center of the given GameObject. If the usable is not a GameObject it is used regardless
	 * @param obj
	 */
	public void useUsables(GameObject obj, double distance){
		for(Usable u : usables){
			if(u instanceof GameObject){
				GameObject o = (GameObject)u;
				if(o.getCenter().distance(obj.getCenter()) <= distance) u.use();
			}
			else u.use();
		}
	}
	
	public double getLevelWidth(){
		return tiles.length * Tile.TILE_SIZE;
	}
	
	public double getLevelHeight(){
		return tiles[0].length * Tile.TILE_SIZE;
	}
	
	public SaveFile getSaveFile(){
		return loadedFile;
	}
	
	public void pressLeft(){
		switch(levelState){
			case DIE:
				dieMenu.pressLeft();
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) pauseMenu.pressLeft();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressLeft();
				break;
			case WIN:
				if(playMode != Mode.SPEED_RUN) winMenu.pressLeft();
				break;
		}
	}
	public void pressRight(){
		switch(levelState){
			case DIE:
				dieMenu.pressRight();
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) pauseMenu.pressRight();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressRight();
				break;
			case WIN:
				if(playMode != Mode.SPEED_RUN) winMenu.pressRight();
				break;
		}
	}
	public void pressJump(){
		switch(levelState){
			case DIE:
				dieMenu.pressUp();
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) pauseMenu.pressUp();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressJump();
				break;
			case WIN:
				if(playMode != Mode.SPEED_RUN) winMenu.pressUp();
				break;
		}
	}
	public void pressDuck(){
		switch(levelState){
			case DIE:
				dieMenu.pressDown();
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) pauseMenu.pressDown();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressDuck();
				break;
			case WIN:
				if(playMode != Mode.SPEED_RUN) winMenu.pressDown();
				break;
		}
	}
	public void pressAttack(){
		switch(levelState){
			case DIE:
				dieMenu.pressEnter();
				break;
			case GAME_OVER:
				if(gameOverTimer <= 0){
					if(playMode == Mode.SPEED_RUN) instance.openSaveFile(loadedFile);
					else instance.setGamestate(Main.GameState.MENU_MAIN);
				}
				break;
			case PLAY:
				if(paused) pauseMenu.pressEnter();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressAttack();
				break;
			case WIN:
				if(playMode == Mode.SPEED_RUN && speedrunTimer <= 0) instance.openSaveFile(loadedFile);
				else winMenu.pressEnter();
				break;
		}
	}
	public void pressUse(){
		switch(levelState){
			case DIE:
				dieMenu.pressBack();
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) pauseMenu.pressBack();
				else for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressUse();
				break;
			case WIN:
				if(playMode != Mode.SPEED_RUN) winMenu.pressBack();
				break;
		}
	}
	public void pressPause(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(paused) setPaused(false);
				else{
					setPaused(true);
					for(int i = 0; i < controlables.size(); i++) controlables.get(i).pressPause();
				}
				break;
			case WIN:
				break;
		}
	}
	
	public void releaseLeft(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseLeft();
				break;
			case WIN:
				break;
		}
	}
	public void releaseRight(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseRight();
				break;
			case WIN:
				break;
		}
	}
	public void releaseJump(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseJump();
				break;
			case WIN:
				break;
		}
	}
	public void releaseDuck(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseDuck();
				break;
			case WIN:
				break;
		}
	}
	public void releaseAttack(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseAttack();
				break;
			case WIN:
				break;
		}
	}
	public void releaseUse(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releaseUse();
				break;
			case WIN:
				break;
		}
	}
	public void releasePause(){
		switch(levelState){
			case DIE:
				break;
			case GAME_OVER:
				break;
			case PLAY:
				if(!paused) for(int i = 0; i < controlables.size(); i++) controlables.get(i).releasePause();
				break;
			case WIN:
				break;
		}
	}
	
	public AudioPlayer getMusicPlayer(){
		return musicPlayer;
	}
	
	public AudioPlayer getEffectsPlayer(){
		return effectsPlayer;
	}
	
	@Override
	public int getUUID(){
		return -2;
	}
	
	/**
	 * Creates a tile grid, where each pixel in grid is a different tile for the returned grid. The colors used for the image are defined in Tile.Type
	 * @param grid
	 * @return A tile grid for a level based on the given BufferedImage
	 */
	public static Tile[][] loadTileFromImage(BufferedImage grid){
		Tile[][] tiles = new Tile[grid.getWidth()][grid.getHeight()];
		
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j] = new Tile(i * Tile.TILE_SIZE, j * Tile.TILE_SIZE, Tile.Type.getTileFromColor(grid.getRGB(i, j)));
			}
		}
		
		return tiles;
	}
	
	private Menu getPauseMenu(){
		return new Menu(){
			public static final int RESUME = 0;
			public static final int SETTINGS = 1;
			public static final int EXIT = 2;
			
			private MenuButton[] buttons;
			private int selectedButton;
			
			private MenuButton[] confirm;
			
			private boolean started = false;
			private boolean confirmExit	;
			
			@Override
			public void tick(){
				if(!started){
					started = true;
					confirmExit = false;
					
					buttons = new MenuButton[3];
					buttons[RESUME] = new MenuButton(Settings.DEFAULT_SCREEN_WIDTH / 2 - 50, Settings.DEFAULT_SCREEN_HEIGHT / 2 - 16, 100, 20, "Resume Game", 15, Type.SIZE_100X20);
					buttons[SETTINGS] = new MenuButton(Settings.DEFAULT_SCREEN_WIDTH / 2 - 50, Settings.DEFAULT_SCREEN_HEIGHT / 2 + 10, 100, 20, "Settings", 15, Type.SIZE_100X20);
					buttons[EXIT] = new MenuButton(Settings.DEFAULT_SCREEN_WIDTH / 2 - 50, Settings.DEFAULT_SCREEN_HEIGHT / 2 + 34, 100, 20, "Exit Level", 15, Type.SIZE_100X20);
					setSelectedButton(RESUME);
					
					confirm = new MenuButton[2];
					confirm[0] = new MenuButton(Settings.DEFAULT_SCREEN_WIDTH / 2 - 50, 110, 100, 20, "Yes", 15, Type.SIZE_100X20);
					confirm[1] = new MenuButton(Settings.DEFAULT_SCREEN_WIDTH / 2 - 50, 135, 100, 20, "No", 15, Type.SIZE_100X20);
					confirm[1].setSelected(true);
				}
				else{
					for(MenuButton b : buttons) b.tick();
					for(MenuButton b : confirm) b.tick();
				}
			}
			
			@Override
			public void render(Graphics2D g, int x, int y){
				if(started){
					g.drawImage(Images.menuPause, x, y, null);
					
					if(confirmExit){
						g.setColor(Color.BLACK);
						g.setFont(new Font("Impact", Font.PLAIN, 18));
						g.drawString("Are you sure you", x + 4, y + 20);
						g.drawString("want to exit? All", x + 4, y + 40);
						g.drawString("progress in this", x + 4, y + 60);
						g.drawString("level will be lost!", x + 4, y + 80);
						for(MenuButton b : confirm) b.render(g);
					}
					else{
						g.setColor(Color.BLACK);
						g.setFont(new Font("Impact", Font.PLAIN, 30));
						g.drawString("PAUSED", x + 20, y + 46);
						for(MenuButton b : buttons) b.render(g);
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
			
			@Override
			public void pressLeft(){
				if(confirmExit){
					if(confirm[0].isSelected()){
						confirm[0].setSelected(false);
						confirm[1].setSelected(true);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(confirm[1].isSelected()){
						confirm[0].setSelected(true);
						confirm[1].setSelected(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
				else{
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressRight(){
				if(confirmExit){
					if(confirm[0].isSelected()){
						confirm[0].setSelected(false);
						confirm[1].setSelected(true);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(confirm[1].isSelected()){
						confirm[0].setSelected(true);
						confirm[1].setSelected(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
				else{
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressUp(){
				if(confirmExit){
					if(confirm[0].isSelected()){
						confirm[0].setSelected(false);
						confirm[1].setSelected(true);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(confirm[1].isSelected()){
						confirm[0].setSelected(true);
						confirm[1].setSelected(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
				else{
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressDown(){
				if(confirmExit){
					if(confirm[0].isSelected()){
						confirm[0].setSelected(false);
						confirm[1].setSelected(true);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(confirm[1].isSelected()){
						confirm[0].setSelected(true);
						confirm[1].setSelected(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
				else{
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressEnter(){
				if(confirmExit){
					if(confirm[0].isSelected()) instance.openSaveFile(loadedFile);
					else confirmExit = false;
					effectsPlayer.playSound(Sounds.CLICK);
				}
				else{
					if(buttons[RESUME].isSelected()){
						setPaused(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(buttons[SETTINGS].isSelected()){
						instance.openSettingsMenu(false);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(buttons[EXIT].isSelected()){
						confirmExit = true;
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
			}
			@Override
			public void pressBack(){}
		};
	}
	
	private Menu getDieMenu(){
		return new Menu(){
			public static final int DEATH_TIME = 91;
			
			public static final int RETRY = 0;
			public static final int MENU = 1;
			
			private MenuButton[] buttons;
			private int selectedButton;
			
			private int deathTimer = DEATH_TIME;
			
			@Override
			public void tick(){
				if(playMode == Mode.SPEED_RUN){
					loadedFile.setLives(0);
					die();
					return;
				}
				if(deathTimer == DEATH_TIME){
					deathTimer--;
					
					buttons = new MenuButton[2];
					buttons[RETRY] = new MenuButton(55, 100, 100, 20, "Retry Level", 15, Type.SIZE_100X20);
					buttons[MENU] = new MenuButton(165, 100, 100, 20, "Level Menu", 15, Type.SIZE_100X20);
					setSelectedButton(RETRY);
				}
				else{
					if(deathTimer > 0) deathTimer--;
					for(MenuButton b : buttons) b.tick();
				}
			}
			
			@Override
			public void render(Graphics2D g, int x, int y){
				if(deathTimer <= DEATH_TIME){
					double ratio;
					boolean renderGame = deathTimer > DEATH_TIME / 2;
					
					if(renderGame) ratio = (deathTimer - (DEATH_TIME / 2.0)) / (DEATH_TIME / 2.0);
					else ratio = 1 - deathTimer / (DEATH_TIME / 2.0);
					
					if(renderGame) renderGame(g);
					else g.drawImage(Images.menuBackground, 0, 0, null);
					
					if(renderGame) g.setColor(new Color((int)(100 * ratio), 0, 0, 255 - (int)(255 * ratio)));
					else g.setColor(new Color(0, 0, 0, 255 - (int)(255 * ratio)));
					g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
					
					ratio = deathTimer / (double)DEATH_TIME;
					
					g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
					g.setFont(new Font("Impact", Font.PLAIN, 30));
					g.drawString("YOU DIED", 106, 46);
					g.setFont(new Font("Impact", Font.PLAIN, 20));
					switch(playMode){
						case FREE:
							g.drawString("Lives are not lost in free play mode", 25, 76);
							break;
						case NORMAL:
							g.drawString("Lives Remaining: " + loadedFile.getLives(), 85, 76);
							break;
						case SPEED_RUN:
							break;
					}
					if(deathTimer <= 0){
						for(MenuButton b : buttons) b.render(g);
						
						g.setColor(Color.WHITE);
						g.setFont(new Font("Impact", Font.PLAIN, 12));
						g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
						g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " : Left", 2, 151);
						g.drawString(KeyEvent.getKeyText(Settings.getPressRight()) + " : Right", 2, 164);
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
			
			@Override
			public void pressLeft(){
				if(deathTimer <= 0){
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressRight(){
				if(deathTimer <= 0){
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressUp(){
				if(deathTimer <= 0){
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressDown(){
				if(deathTimer <= 0){
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressEnter(){
				if(deathTimer <= 0){
					if(buttons[RETRY].isSelected()){
						instance.playLevel(levelID, playMode, loadedFile);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(buttons[MENU].isSelected()){
						instance.openSaveFile(loadedFile);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
			}
			@Override
			public void pressBack(){}
		};
	}
	
	private Menu getWinMenu(boolean time){
		return new Menu(){
			public static final int WIN_TIME = 91;
			
			public static final int NEXT = 0;
			public static final int MENU = 1;
			
			private MenuButton[] buttons;
			private int selectedButton;
			
			private int winTimer = WIN_TIME;
			
			private boolean newBestTime;
			
			private int bossTimer;
			
			@Override
			public void tick(){
				if(winTimer == WIN_TIME){
					winTimer--;
					
					buttons = new MenuButton[2];
					buttons[NEXT] = new MenuButton(55, 105, 100, 20, "Next Level", 15, Type.SIZE_100X20);
					buttons[MENU] = new MenuButton(165, 105, 100, 20, "Level Menu", 15, Type.SIZE_100X20);
					setSelectedButton(MENU);
					
					newBestTime = time;
					
					bossTimer = 420;
				}
				else{
					if(bossTimer == 0 && inBossFight()) instance.setGamestate(Main.GameState.CREDITS);
					
					if(bossTimer > 0) bossTimer--;
					if(winTimer > 0) winTimer--;
					if(!inBossFight()) for(MenuButton b : buttons) b.tick();
				}
			}
			
			@Override
			public void render(Graphics2D g, int x, int y){
				if(winTimer <= WIN_TIME){
					
					double ratio;
					boolean renderGame = winTimer > WIN_TIME / 2;
					
					if(renderGame) ratio = (winTimer - (WIN_TIME / 2.0)) / (WIN_TIME / 2.0);
					else ratio = 1 - winTimer / (WIN_TIME / 2.0);
					
					if(inBossFight()){
						renderGame(g);
						ratio = winTimer / (double)WIN_TIME;
						g.setColor(new Color(0, 0, 0, 127 - (int)(127 * ratio)));
						g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
						g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
						g.setFont(new Font("Impact", Font.PLAIN, 30));
						g.drawString("You Win!", 105, 46);
						g.setFont(new Font("Impact", Font.PLAIN, 20));
						String s = "Level completed in: " + SaveFile.getTime((int)(playTime / 60.0 * 1000.0));
						g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 86);
						if(newBestTime){
							g.setFont(new Font("Impact", Font.PLAIN, 15));
							g.drawString("New best time!", 115, 101);
						}
						
						if(bossTimer < 120){
							g.setColor(new Color(0, 0, 0, 255 - (int)(255 * bossTimer / 120.0)));
							g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
						}
					}
					else{
						if(renderGame) renderGame(g);
						else g.drawImage(Images.menuBackground, 0, 0, null);
						
						if(renderGame) g.setColor(new Color(0, 0, (int)(100 * ratio), 255 - (int)(255 * ratio)));
						else g.setColor(new Color(0, 0, 0, 255 - (int)(255 * ratio)));
						g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
						
						ratio = winTimer / (double)WIN_TIME;
						
						g.setColor(new Color(255, 255, 255, 255 - (int)(255 * ratio)));
						g.setFont(new Font("Impact", Font.PLAIN, 30));
						g.drawString("Level Complete!", 65, 46);
						g.setFont(new Font("Impact", Font.PLAIN, 20));
						String s = "Level completed in: " + SaveFile.getTime((int)(playTime / 60.0 * 1000.0));
						g.drawString(s, (int)((Settings.DEFAULT_SCREEN_WIDTH - g.getFontMetrics().stringWidth(s)) / 2.0), 86);
						if(newBestTime){
							g.setFont(new Font("Impact", Font.PLAIN, 15));
							g.drawString("New best time!", 115, 101);
						}
						if(winTimer <= 0){
							for(MenuButton b : buttons) b.render(g);
							g.setColor(Color.WHITE);
							g.setFont(new Font("Impact", Font.PLAIN, 12));
							g.drawString(KeyEvent.getKeyText(Settings.getPressAttack()) + " : Enter", 2, 138);
							g.drawString(KeyEvent.getKeyText(Settings.getPressLeft()) + " : Left", 2, 151);
							g.drawString(KeyEvent.getKeyText(Settings.getPressRight()) + " : Right", 2, 164);
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
			
			@Override
			public void pressLeft(){
				if(winTimer <= 0 && !inBossFight()){
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressRight(){
				if(winTimer <= 0 && !inBossFight()){
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressUp(){
				if(winTimer <= 0 && !inBossFight()){
					setSelectedButton(selectedButton - 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressDown(){
				if(winTimer <= 0 && !inBossFight()){
					setSelectedButton(selectedButton + 1);
					effectsPlayer.playSound(Sounds.CLICK);
				}
			}
			@Override
			public void pressEnter(){
				if(winTimer <= 0 && !inBossFight()){
					if(buttons[NEXT].isSelected()){
						instance.playLevel(levelID + 1, playMode, loadedFile);
						effectsPlayer.playSound(Sounds.CLICK);
					}
					else if(buttons[MENU].isSelected()){
						instance.openSaveFile(loadedFile);
						effectsPlayer.playSound(Sounds.CLICK);
					}
				}
			}
			@Override
			public void pressBack(){}
			
		};
	}

	private boolean inBossFight(){
		return levelID == 3;
	}
}
