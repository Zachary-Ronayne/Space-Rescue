package game.world;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import game.libs.Files;

public class SaveFile {
	
	public static final int NUM_LEVELS = 4;
	public static final int STARTING_LIVES = 5;
	
	/**
	 * 0 = no levels unlocked
	 * 1 = first level unlocked
	 * 2 = second level unlocked
	 * etc...
	 * 
	 * A level is considered unlocked when it has been completed once, playing an unlocked level does not use lives up
	 */
	private int unlockedLevel;
	
	/**
	 * The number of lives remaining for this run, lives are only lost when playing an incomplete level
	 */
	private int livesLeft;
	
	/**
	 * The time it took to complete a level, in milliseconds, a number less than 0 means no time exists for the level
	 */
	private int[] levelTime;
	
	/**
	 * The time it took to complete a speedrun, in milliseconds, a number less than 0 means no time exists for the save file
	 */
	private int speedrunTime;
	
	/**
	 * The name of this file, does not include file path or file extension
	 */
	private String fileName;
	
	/**
	 * fileName cannot be empty
	 */
	public SaveFile(String fileName){
		if(fileName.equals("")) throw new IllegalArgumentException("fileName cannot be empty");
		unlockedLevel = 0;
		livesLeft = STARTING_LIVES;
		levelTime = new int[NUM_LEVELS];
		speedrunTime = -1;
		for(int i = 0; i < NUM_LEVELS; i++) levelTime[i] = -1;
		this.fileName = fileName;
		
		File[] files = new File(Files.SAVES).listFiles();
		boolean found = false;
		for(int i = 0; i < files.length && !found; i++){
			if(files[i].getName().equals(fileName + ".txt")) found = true;
		}
		if(found) load();
		else{
			save();
			load();
		}
	}
	
	public int getUnlockedLevel(){
		return unlockedLevel;
	}
	
	public void setUnlockedLevel(int l){
		if(l > NUM_LEVELS) l = NUM_LEVELS;
		unlockedLevel = l;
	}
	
	public int getLives(){
		return livesLeft;
	}
	
	public void setLives(int l){
		if(l < 0) l = 0;
		livesLeft = l;
	}
	
	public int getLevelTime(int index){
		return levelTime[index];
	}
	
	public void setLevelTime(int index, int time){
		levelTime[index] = time;
	}
	
	public int getSpeedrunTime(){
		return speedrunTime;
	}
	
	public void setSpeedrunTime(int speedrunTime){
		this.speedrunTime = speedrunTime;
	}
	
	public boolean save(){
		try{
			
			PrintWriter write = new PrintWriter(new File(Files.SAVES + "/" + fileName + ".txt"));
			
			write.print(unlockedLevel + " ");
			write.print(livesLeft + " ");
			write.print(levelTime.length + " ");
			for(int i = 0; i < levelTime.length; i++) write.print(levelTime[i] + " ");
			write.print(speedrunTime + " ");
			
			write.close();
			
			return true;
			
		}catch(Exception e){
			System.err.println("Failed to save a saved game with the name: " + fileName);
			e.printStackTrace();
			
			return false;
		}
	}
	
	public boolean load(){
		try{
			
			Scanner read = new Scanner(new File(Files.SAVES + "/" + fileName + ".txt"));
			
			unlockedLevel = read.nextInt();
			livesLeft = read.nextInt();
			levelTime = new int[read.nextInt()];
			for(int i = 0; i < levelTime.length; i++) levelTime[i] = read.nextInt();
			speedrunTime = read.nextInt();
			
			read.close();
			
			return true;
			
		}catch(Exception e){
			System.err.println("Failed to load a saved game with the name: " + fileName);
			e.printStackTrace();
			
			return false;
		}
	}
	
	public void delete(){
		new File(Files.SAVES + "/" + fileName + ".txt").delete();
	}
	
	@Override
	public String toString(){
		return "SaveFile: " + fileName + ", Unlocked Level: " + unlockedLevel + ", Lives left: " + livesLeft;
	}
	
	/**
	 * Takes an integer of miliseconds and converts it into minutes:seconds.decimalplaceseconds
	 * @param time
	 * @return
	 */
	public static String getTime(int time){
		int minutes = (int)(time / 60000.0);
		time %= 60000;
		int seconds = (int)(time / 1000.0);
		time %= 1000;
		String s = minutes + ":";
		if(seconds >= 10) s += seconds + ".";
		else s += "0" + seconds + ".";
		String sec = "" + time;
		if(sec.length() == 1) s += "00" + sec;
		else if(sec.length() == 2) s += "0" + sec;
		else s += sec;
		return s;
	}
}
