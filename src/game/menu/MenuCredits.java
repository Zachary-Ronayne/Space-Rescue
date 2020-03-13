package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Main;
import game.libs.Images;
import game.libs.Settings;
import game.util.sound.AudioPlayer;
import game.world.obj.entity.GameEntity;

public class MenuCredits implements Menu{
	
	private static final int MAX_STATE = 12;
	private static final int[] STATE_TIME = new int[]{60, 90, 120, 240, 60, 30, 120, 60, 60, 150, 120, 4000, 60};
	
	private Main instance;
	protected AudioPlayer musicPlayer;
	protected AudioPlayer effectsPlayer;
	
	private int timer;
	private int state;
	
	//state 2 variables
	private double friendFall;
	private double friendFallChange;
	private double camera;
	
	//credits variable
	private final String[] CREDITS = new String[]{
		"Space Rescue",
		"A game by Zachary Ronayne",
		"",
		"",
		"Lead Programmer:",
		"Zachary Ronayne",
		"",
		"Lead Developer:",
		"Zachary Ronayne",
		"",
		"Pixel Art Design:",
		"Zachary Ronayne",
		"",
		"Music Design:",
		"Zachary Ronayne",
		"",
		"Sound Effect Design:",
		"Zachary Ronayne",
		"",
		"Everything else:",
		"Zachary Ronayne",
		"",
		"",
		"Programs and libraries used:",
		"",
		"Coded in java using the Eclipse IDE",
		"(Yes I use Eclipse, deal with it",
		" r/ProgrammerHumor)",
		"",
		"All images created with primarily",
		"Paint.net, and some MS Paint",
		"",
		"Music created with the Processing IDE",
		"using the FunWithSounds library made",
		"by David Hovemeyer.",
		"",
		"Music was rendered by the library and",
		"recorded with Open Brodcast Software",
		"(OBS), then converted to .wav format",
		"via the video edditing software",
		"DaVinchi Resolve",
		"",
		"Yes this is a pretty backwards way",
		"but it was the easiest thing to do",
		"Sue me",
		"",
		"Sound effects were recorded via a",
		"headset mic and then distored in",
		"Audacity",
		"",
		"The sources of sounds varry from",
		"stomping my carpet, to tapping my",
		"window, to making noises with my",
		"mouth (It was actually mostly my,",
		"mouth pretty strange I know)",
		"",
		"",
		"Inspiration and Influences:",
		"(Video games)",
		"Cuphead",
		"Hollow Knight",
		"Undertale",
		"Super Mario World",
		"Minecraft",
		"Trap Adventure 2",
		"",
		"",
		"Special thanks to David Hovemeyer",
		"for creating the FunWithSounds",
		"Processing library",
		"",
		"",
		"Thanks for playing!"
	};
	
	public MenuCredits(Main instance, AudioPlayer musicPlayer, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.musicPlayer = musicPlayer;
		this.effectsPlayer = effectsPlayer;
		
		reset();
	}
	
	public void reset(){
		state = 0;
		timer = STATE_TIME[state];
		
		camera = 0;
		
		friendFall = 0;
		friendFallChange = 0;
	}
	
	private void endCutScene(){
		instance.setGamestate(Main.GameState.MENU_MAIN);
	}
	
	@Override
	public void tick(){
		if(timer <= 0){
			if(state >= MAX_STATE) endCutScene();
			else{
				state++;
				if(state < STATE_TIME.length) timer = STATE_TIME[state];
			}
		}
		if(timer > 0) timer--;
		
		if(state == 2){
			friendFall += friendFallChange += GameEntity.GRAVITY_CONSTANT;
			if(friendFall > 120){
				friendFall = 120;
				friendFallChange = 0;
			}
			camera = friendFall;
			if(camera > 92) camera = 92;
		}
	}

	@Override
	public void render(Graphics2D g, int x, int y){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
		
		switch(state){
			case 0:
				drawBackground(g, 40, 20);
				g.drawImage(Images.backgroundBossChamber, 0, 0, null);
				g.drawImage(Images.trappedFriend[0], 145, 16, null);
				g.setColor(new Color(0, 0, 0, (int)(255 * timer / 60.0)));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 1:
				drawBackground(g, 40, 20);
				g.drawImage(Images.backgroundBossChamber, 0, 0, null);
				g.drawImage(Images.trappedFriend[timer / 10 % 2], 145, 16, null);
				break;
			case 2:
				drawBackground(g, 40, (int)(20 + camera));
				g.drawImage(Images.backgroundBossChamber, 0, (int)(-camera), null);
				g.drawImage(Images.trappedFriend[2], 145, (int)(16 - camera), null);
				BufferedImage img = Images.playerFrames[1][3];
				if(friendFall ==  120) img = Images.playerFrames[0][3];
				g.drawImage(img, 153, (int)(21 + friendFall), null);
				img = Images.playerFrames[0][0];
				g.drawImage(img, 190, (int)(233 - camera), -img.getWidth(), img.getHeight(), null);
				break;
			case 3:
				drawBackground(g, 40, (int)(20 + camera));
				g.drawImage(Images.backgroundBossChamber, 0, (int)(-camera), null);
				img = Images.playerFrames[timer / 5 % 4][3];
				g.drawImage(img, 153 - STATE_TIME[3] + timer + img.getWidth(), (int)(233 - camera), -img.getWidth(), img.getHeight(), null);
				img = Images.playerFrames[(timer / 5 + 6) % 4][0];
				g.drawImage(img, 190 - STATE_TIME[3] + timer, (int)(233 - camera), -img.getWidth(), img.getHeight(), null);
				break;
			case 4:
				drawBackground(g, 40, (int)(20 + camera));
				g.drawImage(Images.backgroundBossChamber, 0, (int)(-camera), null);
				g.setColor(new Color(0, 0, 0, 255 - (int)(255.0 * timer / STATE_TIME[4])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 5:
				drawBackground(g, 40, 20);
				g.drawImage(Images.backgroundShip, 0, 0, null);
				img = Images.playerFrames[timer / 5 % 4][3];
				g.drawImage(img, 80 - STATE_TIME[5] + timer + img.getWidth(), 74, -img.getWidth(), img.getHeight(), null);
				img = Images.playerFrames[(timer / 5 + 6) % 4][0];
				g.drawImage(img, 104 - STATE_TIME[5] + timer + img.getWidth(), 74, -img.getWidth(), img.getHeight(), null);
				g.setColor(new Color(0, 0, 0, (int)(255.0 * timer / STATE_TIME[5])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 6:
				drawBackground(g, 40, 20);
				g.drawImage(Images.backgroundShip, 0, 0, null);
				img = Images.playerFrames[timer / 5 % 4][3];
				g.drawImage(img, 50 - STATE_TIME[6] + timer + img.getWidth(), 74, -img.getWidth(), img.getHeight(), null);
				img = Images.playerFrames[(timer / 5 + 6) % 4][0];
				g.drawImage(img, 74 - STATE_TIME[6] + timer + img.getWidth(), 74, -img.getWidth(), img.getHeight(), null);
				break;
			case 7:
				drawBackground(g, 40, 20);
				g.drawImage(Images.backgroundShip, 0, 0, null);
				g.setColor(new Color(0, 0, 0, 255 - (int)(255.0 * timer / STATE_TIME[7])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 8:
				drawBackground(g, 40, 125 - (STATE_TIME[8] - timer) * .5, 40, 55 - (STATE_TIME[8] - timer) * .5);
				g.setColor(new Color(0, 0, 0, (int)(255.0 * timer / STATE_TIME[8])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 9:
				drawBackground(g, 40, 95 - (STATE_TIME[9] - timer) * .5, 40, 25 - (STATE_TIME[9] - timer) * .5);
				break;
			case 10:
				drawBackground(g, 40, 20, 40, -50);
				g.drawImage(Images.shipExterior[timer / 4 % 2], 128, 180 - (STATE_TIME[10] - timer) * 4, null);
				break;
			case 11:
				drawBackground(g, 40, 20, 40, -50);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Impact", Font.PLAIN, 20));
				for(int i = 0; i < CREDITS.length; i++){
					int textY = 200 + i * 25 - (STATE_TIME[11] - timer) / 2;
					if(textY < 220 && textY > -20) {
						String s = CREDITS[i];
						g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, textY);
					}
				}
				break;
			case 12:
				drawBackground(g, 40, 20, 40, -50);
				g.setColor(new Color(0, 0, 0, 255 - (int)(255.0 * timer / STATE_TIME[12])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
		}
	}
	
	private void drawBackground(Graphics2D g, double x, double y){
		g.drawImage(Images.backgroundStars, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)x, (int)y, (int)x + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)y + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
		g.drawImage(Images.backgroundPlanets, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)(x * 1.3), (int)(y * 1.3), (int)(x * 1.3) + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)(y * 1.3) + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
		g.drawImage(Images.backgroundHills, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)(x * 1.5), (int)(y * 2.0), (int)(x * 1.5) + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)(y * 2.0) + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
	}
	
	private void drawBackground(Graphics2D g, double x, double y, double hillsX, double hillsY){
		g.drawImage(Images.backgroundStars, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)x, (int)y, (int)x + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)y + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
		g.drawImage(Images.backgroundPlanets, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)(x * 1.3), (int)(y * 1.3), (int)(x * 1.3) + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)(y * 1.3) + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
		g.drawImage(Images.backgroundHills, -1, -1, Settings.DEFAULT_SCREEN_WIDTH + 1, Settings.DEFAULT_SCREEN_HEIGHT + 1,
				(int)(hillsX * 1.5), (int)(hillsY * 2.0), (int)(hillsX * 1.5) + Settings.DEFAULT_SCREEN_WIDTH + 2, (int)(hillsY * 2.0) + Settings.DEFAULT_SCREEN_HEIGHT + 2, null);
	}
	
	@Override
	public void pressLeft(){}
	@Override
	public void pressRight(){}
	@Override
	public void pressUp(){}
	@Override
	public void pressDown(){}
	@Override
	public void pressEnter(){}
	@Override
	public void pressBack(){}
}
