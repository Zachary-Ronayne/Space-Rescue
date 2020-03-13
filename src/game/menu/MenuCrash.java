package game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.Main;
import game.libs.Images;
import game.libs.Settings;
import game.libs.Sounds;
import game.util.sound.AudioPlayer;

public class MenuCrash implements Menu{
	
	private Main instance;
	private AudioPlayer effectsPlayer;
	
	public MenuCrash(Main instance, AudioPlayer effectsPlayer){
		this.instance = instance;
		this.effectsPlayer = effectsPlayer;
	}
	
	@Override
	public void tick(){}
	
	@Override
	public void render(Graphics2D g, int x, int y){
		g.drawImage(Images.menuBackground, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 16));
		g.drawString("An unexpected crash has happened and the", 4, 60);
		g.drawString("game was forced to quit.", 4, 80);
		
		g.setFont(new Font("Impact", Font.PLAIN, 12));
		g.drawString("Press " + KeyEvent.getKeyText(Settings.getPressAttack()) + " to exit to the main menu.", 2, 177);
		
		g.setFont(new Font("Impact", Font.PLAIN, 24));
		g.drawString("Game has crashed", 10, 36);
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
	public void pressEnter(){
		instance.setGamestate(Main.GameState.MENU_MAIN);
		instance.stopSounds();
		effectsPlayer.playSound(Sounds.CLICK);
	}
	@Override
	public void pressBack(){}
	
}
