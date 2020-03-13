package game.libs;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public final class Sounds{
	
	public static final float DEFAULT_VOLUME = 0;
	
	//music
	public static final String SONG_TEST_1 = Files.MUSIC + "/test1.wav";
	public static final String SONG_TEST_2 = Files.MUSIC + "/test2.wav";
	public static final String FOREIGN_GRAVITY = Files.MUSIC + "/foreignGravity.wav";
	public static final String MAIN_THEME = Files.MUSIC + "/mainTheme.wav";
	public static final String GAME_OVER = Files.MUSIC + "/gameOver.wav";
	public static final String VICTORY = Files.MUSIC + "/victory.wav";
	public static final String BOSS_THEME = Files.MUSIC + "/bossTheme.wav";
	public static final String RAPID_DEPARTURE = Files.MUSIC + "/rapidDeparture.wav";
	
	//effects
	public static final String EFFECT_TEST_1 = Files.EFFECTS + "/test1.wav";
	public static final String EFFECT_TEST_2 = Files.EFFECTS + "/test2.wav";
	public static final String EFFECT_TEST_3 = Files.EFFECTS + "/test3.wav";
	public static final String EFFECT_TEST_4 = Files.EFFECTS + "/test4.wav";
	public static final String SCRATCH = Files.EFFECTS + "/scratch.wav";
	public static final String CLICK = Files.EFFECTS + "/click.wav";
	
	public static final String PLAYER_GUN_SHOT = Files.EFFECTS + "/playerGunShot.wav";
	public static final String PLAYER_DAMAGE = Files.EFFECTS + "/playerDamage.wav";
	public static final String HEALTH_PACK = Files.EFFECTS + "/healthPack.wav";
	
	public static final String FOOT_STEP_1 = Files.EFFECTS + "/footStep1.wav";
	public static final String FOOT_STEP_2 = Files.EFFECTS + "/footStep2.wav";
	public static final String FOOT_STEP_3 = Files.EFFECTS + "/footStep3.wav";
	
	public static final String BOSS_TAKE_DAMAGE = Files.EFFECTS + "/bossTakeDamage.wav";
	public static final String BOSS_SHOOT_1 = Files.EFFECTS + "/bossShoot1.wav";
	public static final String BOSS_SHOOT_2 = Files.EFFECTS + "/bossShoot2.wav";
	public static final String BOSS_SUMMON = Files.EFFECTS + "/bossSummon.wav";
	public static final String BOSS_DASH = Files.EFFECTS + "/bossDash.wav";
	public static final String BOSS_SHIELD_HIT = Files.EFFECTS + "/bossShieldHit.wav";
	public static final String BOSS_NEXT_STAGE = Files.EFFECTS + "/bossNextStage.wav";
	public static final String BOSS_DEATH = Files.EFFECTS + "/bossDeath.wav";
	
	public static final String FLAP_WINGS_1 = Files.EFFECTS + "/flapWings1.wav";
	public static final String FLAP_WINGS_2 = Files.EFFECTS + "/flapWings2.wav";
	public static final String ALIEN_DAMAGE_1 = Files.EFFECTS + "/alienDamage1.wav";
	public static final String ALIEN_DAMAGE_2 = Files.EFFECTS + "/alienDamage2.wav";
	public static final String ALIEN_DAMAGE_3 = Files.EFFECTS + "/alienDamage3.wav";
	public static final String ALIEN_DAMAGE_4 = Files.EFFECTS + "/alienDamage4.wav";
	public static final String ALIEN_SHOOT_1 = Files.EFFECTS + "/alienShoot1.wav";
	public static final String ALIEN_SHOOT_2 = Files.EFFECTS + "/alienShoot2.wav";
	
	public static final String PRESS_GAME_BUTTON = Files.EFFECTS + "/pressGameButton.wav";
	public static final String DOOR_OPEN = Files.EFFECTS + "/doorOpen.wav";
	public static final String LAZER_HUM = Files.EFFECTS + "/lazerHUm.wav";
	public static final String ACID_1 = Files.EFFECTS + "/acid1.wav";
	public static final String ACID_2 = Files.EFFECTS + "/acid2.wav";
	public static final String ACID_3 = Files.EFFECTS + "/acid3.wav";
	public static final String ACID_4 = Files.EFFECTS + "/acid4.wav";
	
	/**
	 * Loads all of the sounds initially so no lag happens later on
	 */
	public static void load(){
		
		loadSound(SONG_TEST_1);
		loadSound(SONG_TEST_2);
		
		loadSound(FOREIGN_GRAVITY);
		loadSound(MAIN_THEME);
		loadSound(GAME_OVER);
		loadSound(VICTORY);
		loadSound(BOSS_THEME);
		loadSound(RAPID_DEPARTURE);
		
		loadSound(EFFECT_TEST_1);
		loadSound(EFFECT_TEST_2);
		loadSound(EFFECT_TEST_3);
		loadSound(EFFECT_TEST_4);
		loadSound(SCRATCH);
		loadSound(CLICK);

		loadSound(PLAYER_GUN_SHOT);
		loadSound(PLAYER_DAMAGE);
		loadSound(HEALTH_PACK);
		
		loadSound(FOOT_STEP_1);
		loadSound(FOOT_STEP_2);
		loadSound(FOOT_STEP_3);
		
		loadSound(BOSS_TAKE_DAMAGE);
		loadSound(BOSS_SHOOT_1);
		loadSound(BOSS_SHOOT_2);
		loadSound(BOSS_SUMMON);
		loadSound(BOSS_DASH);
		loadSound(BOSS_SHIELD_HIT);
		loadSound(BOSS_DEATH);
		
		loadSound(FLAP_WINGS_1);
		loadSound(FLAP_WINGS_2);
		loadSound(ALIEN_DAMAGE_1);
		loadSound(ALIEN_DAMAGE_2);
		loadSound(ALIEN_DAMAGE_3);
		loadSound(ALIEN_DAMAGE_4);
		loadSound(ALIEN_SHOOT_1);
		loadSound(ALIEN_SHOOT_2);
		
		loadSound(PRESS_GAME_BUTTON);
		loadSound(DOOR_OPEN);
		loadSound(LAZER_HUM);
		loadSound(ACID_1);
		loadSound(ACID_2);
		loadSound(ACID_3);
		loadSound(ACID_4);
		
	}
	
	public static Clip loadSound(String path){
		Clip sound = null;
		try{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
			sound = AudioSystem.getClip();
			sound.open(audioStream);
			if(sound.isRunning()) sound.stop();
			sound.setFramePosition(0);
			((FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(DEFAULT_VOLUME);
			
		}catch(Exception e){
			System.out.println("Could not load sound located at: " + path);
			e.printStackTrace();
		}
		
		return sound;
	}
}
