package game.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Main;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.util.sound.AudioPlayer;
import game.world.SaveFile;

public class MenuIntroCutscene implements Menu{
	
	private static final int MAX_STATE = 8;
	private static final int[] STATE_TIME = new int[]{30, 60, 120, 10, 30, 35, 120, 30, 30};
	
	private Main instance;
	protected AudioPlayer musicPlayer;
	protected AudioPlayer effectsPlayer;
	protected SaveFile openedFile;
	
	private int timer;
	private int state;
	
	public MenuIntroCutscene(Main instance, AudioPlayer musicPlayer, AudioPlayer effectsPlayer, SaveFile openedFile){
		this.instance = instance;
		this.musicPlayer = musicPlayer;
		this.effectsPlayer = effectsPlayer;
		this.openedFile = openedFile;
		
		reset();
	}
	
	public void reset(){
		state = 0;
		timer = STATE_TIME[state];
	}
	
	private void endCutScene(){
		instance.openSaveFile(openedFile);
		musicPlayer.stopSounds();
	}
	
	public void setSaveFile(SaveFile file){
		openedFile = file;
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

		if(state == 4 && timer == STATE_TIME[4]) effectsPlayer.playSound(Sounds.BOSS_SHOOT_1);
		if((state == 5 || state == 6) && timer % 20 == 0) effectsPlayer.playSound(Sounds.FLAP_WINGS_1);
		
		if(timer > 0) timer--;
	}
	
	@Override
	public void render(Graphics2D g, int x, int y){
		switch(state){
			case 0:
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 1:
				drawBackground(g, 40, 20);
				g.setColor(new Color(0, 0, 0, (int)(255.0 * timer / STATE_TIME[1])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 2:
				drawBackground(g, 40, 20);
				g.drawImage(Images.playerFrames[(timer / 5 + 6) % 4][3], -30 + STATE_TIME[2] - timer, 74, null);
				g.drawImage(Images.playerFrames[(timer / 5 + 6) % 4][0], -55 + STATE_TIME[2] - timer, 74, null);
				
				BufferedImage img = Images.finalBossSpreadShot8;
				double bx = 330 - (STATE_TIME[2] - timer) * 2;
				double by = .003 * Math.pow(bx - 230, 2);
				Graphics2D g2 = (Graphics2D)g.create();
				g2.rotate(Math.toRadians((STATE_TIME[2] - timer) * 4), bx + img.getWidth() * .5, by + img.getHeight() * .5);
				g2.drawImage(img, (int)bx, (int)by, null);
				
				break;
			case 3:
				drawBackground(g, 40, 20);
				g.drawImage(Images.playerFrames[0][3], 90, 74, null);
				g.drawImage(Images.playerFrames[0][0], 65, 74, null);
				
				img = Images.finalBossSpreadShot8;
				bx = 330 - (STATE_TIME[3] - timer + STATE_TIME[2]) * 2;
				by = .003 * Math.pow(bx - 230, 2);
				g2 = (Graphics2D)g.create();
				g2.rotate(Math.toRadians((STATE_TIME[3] - timer + STATE_TIME[2]) * 4), bx + img.getWidth() * .5, by + img.getHeight() * .5);
				g2.drawImage(img, (int)bx, (int)by, null);
				break;
			case 4:
				drawBackground(g, 40, 20);
				g.drawImage(Images.playerFrames[0][3], 90 + (STATE_TIME[4] - timer) * 2, (int)(74 - (STATE_TIME[4] - timer) * .9), null);
				g.drawImage(Images.playerFrames[(timer / 5 + 6) % 4][0], 65 - (STATE_TIME[4] - timer) * 3, (int)(74 - (STATE_TIME[4] - timer) * 0.25), null);
				
				img = Images.finalBossExplode;
				double size = Math.pow(STATE_TIME[4] - timer, 2);
				bx = 330 - (STATE_TIME[3] + STATE_TIME[2]) * 2;
				by = .003 * Math.pow(bx - 230, 2);
				g2 = (Graphics2D)g.create();
				g2.rotate(Math.toRadians((STATE_TIME[4] - timer) * 4), bx, by);
				g2.drawImage(img, (int)(bx - size * .5), (int)(by - size * .5), (int)size, (int)size, null);
				break;
			case 5:
				drawBackground(g, 40, 20);
				g.drawImage(Images.playerFrames[0][3], 90 + (STATE_TIME[4] + STATE_TIME[5] - timer) * 2, (int)(74 - (STATE_TIME[4] + STATE_TIME[5] - timer) * .9), null);
				g.drawImage(Images.flyingAlienFrames[timer / 5 % 4], 250 - (STATE_TIME[5] - timer), (int)(-30 + (STATE_TIME[5] - timer) * .5), null);
				break;
			case 6:
				drawBackground(g, 40, 20);
				double diffX = STATE_TIME[6] - timer;
				double diffY = diffX * .5;
				
				g.drawImage(Images.playerFrames[0][3], (int)(221 + diffX), (int)(15 + diffY), null);
				img = Images.flyingAlienFrames[timer / 5 % 4];
				g.drawImage(img, (int)(215 + img.getWidth() + diffX), (int)(-12 + diffY), -img.getWidth(), img.getHeight(), null);
				break;
			case 7:
				drawBackground(g, 40, 20);
				g.setColor(new Color(0, 0, 0, 255 - (int)(255.0 * timer / STATE_TIME[1])));
				g.fillRect(0, 0, Settings.DEFAULT_SCREEN_WIDTH, Settings.DEFAULT_SCREEN_HEIGHT);
				break;
			case 8:
				g.setColor(Color.BLACK);
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
		g.drawImage(Images.backgroundShip, 0, 0, null);
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
