package game.libs;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import game.util.SpriteSheet;

public final class Images{
	
	//menu items
	public static BufferedImage[] button90X20;
	public static BufferedImage[] button100X20;
	public static BufferedImage[] button100X15;
	public static BufferedImage[] button120X20;
	public static BufferedImage[] buttonSlider20X20;
	public static BufferedImage menuBackground;
	public static BufferedImage menuPause;
	public static BufferedImage menuHud;
	public static BufferedImage[][] thumbnails;
	public static BufferedImage thumbnailSelect;
	
	//tile lists
	public static BufferedImage[] rock;
	public static BufferedImage[] rockBG;
	
	public static BufferedImage[] pmetal;
	public static BufferedImage[] pmetalBG;
	
	public static BufferedImage[] ship;
	public static BufferedImage[] shipBG;
	
	//objects
	public static BufferedImage[] pmetalSlope;
	public static BufferedImage[] shipSlope;
	
	public static BufferedImage[][][] leverFrames;
	public static BufferedImage[] introSignTiles;
	public static BufferedImage[] spikes;
	public static BufferedImage[] healthPacks;
	
	public static BufferedImage[] acidPool;
	
	public static BufferedImage[][] lazerHorizontal;
	public static BufferedImage[][] lazerVertical;
	
	public static BufferedImage[] gameButton;
	
	public static BufferedImage trainingDummmy;
	
	public static BufferedImage playerAttack;
	public static BufferedImage bounceAttack;
	public static BufferedImage normalAttack;
	
	public static BufferedImage[] finalBossShield;
	
	public static BufferedImage[] flyingAlienFrames;
	public static BufferedImage[] minionAlienFrames;
	public static BufferedImage[] meleeAlienFrames;
	public static BufferedImage[] bounceAlienFrames;
	
	public static BufferedImage[][] playerFrames;
	public static BufferedImage[][] finalBossFrames;
	
	public static BufferedImage finalBossNormalShot;
	public static BufferedImage finalBossSpreadShot4;
	public static BufferedImage finalBossSpreadShot8;
	public static BufferedImage finalBossExplode;
	
	public static BufferedImage[] plants;
	public static BufferedImage[] stalagtite;
	public static BufferedImage[] wallCracks;
	public static BufferedImage[] blockCracks;
	public static BufferedImage[] pMoss;
	
	public static BufferedImage alienDoor;
	public static BufferedImage alienWindow;
	
	public static BufferedImage[] trappedFriend;
	public static BufferedImage[] shipExterior;
	
	//background
	public static BufferedImage backgroundStars;
	public static BufferedImage backgroundPlanets;
	public static BufferedImage backgroundHills;
	public static BufferedImage backgroundShip;
	public static BufferedImage backgroundBossChamber;
	
	public static void load(){
		try{
			SpriteSheet sheet;
			
			//load menu items
			menuBackground = ImageIO.read(new File(Files.IMAGES + "/menuBackground.png"));
			menuPause = ImageIO.read(new File(Files.IMAGES + "/menuPause.png"));
			menuHud = ImageIO.read(new File(Files.IMAGES + "/menuHud.png"));
			
			sheet = new SpriteSheet(90, 20, ImageIO.read(new File(Files.SPRITE_SHEETS + "/button90x20.png")));
			button90X20 = new BufferedImage[2];
			for(int i = 0; i < 2; i++) button90X20[i] = sheet.getSprite(0, i);
			
			sheet = new SpriteSheet(100, 20, ImageIO.read(new File(Files.SPRITE_SHEETS + "/button100x20.png")));
			button100X20 = new BufferedImage[2];
			for(int i = 0; i < 2; i++) button100X20[i] = sheet.getSprite(0, i);
			
			sheet = new SpriteSheet(100, 15, ImageIO.read(new File(Files.SPRITE_SHEETS + "/button100x15.png")));
			button100X15 = new BufferedImage[2];
			for(int i = 0; i < 2; i++) button100X15[i] = sheet.getSprite(0, i);
			
			sheet = new SpriteSheet(120, 20, ImageIO.read(new File(Files.SPRITE_SHEETS + "/button120x20.png")));
			button120X20 = new BufferedImage[2];
			for(int i = 0; i < 2; i++) button120X20[i] = sheet.getSprite(0, i);
			
			sheet = new SpriteSheet(20, 20, ImageIO.read(new File(Files.SPRITE_SHEETS + "/buttonSlider20x20.png")));
			buttonSlider20X20 = new BufferedImage[2];
			for(int i = 0; i < 2; i++) buttonSlider20X20[i] = sheet.getSprite(0, i);
			
			//load level thumbnails
			sheet = new SpriteSheet(70, 70, ImageIO.read(new File(Files.SPRITE_SHEETS + "/levelThumbnails.png")));
			thumbnails = new BufferedImage[5][2];
			for(int i = 0; i < thumbnails.length; i++){
				for(int j = 0; j < thumbnails[i].length; j++){
					thumbnails[i][j] = sheet.getSprite(i, j);
				}
			}
			thumbnailSelect = ImageIO.read(new File(Files.IMAGES + "/thumbnailSelect.png"));
			
			//load tiles
			sheet = new SpriteSheet(16, 16, ImageIO.read(new File(Files.SPRITE_SHEETS + "/tiles.png")));
			rock = new BufferedImage[18];
			rockBG = new BufferedImage[18];
			for(int i = 0; i < 18; i++){
				rock[i] = sheet.getSprite(i, 0);
				rockBG[i] = sheet.getSprite(i, 1);
			}
			
			pmetal = new BufferedImage[18];
			pmetalBG = new BufferedImage[18];
			for(int i = 0; i < 18; i++){
				pmetal[i] = sheet.getSprite(i, 2);
				pmetalBG[i] = sheet.getSprite(i, 3);
			}
			
			ship = new BufferedImage[18];
			shipBG = new BufferedImage[18];
			for(int i = 0; i < 18; i++){
				ship[i] = sheet.getSprite(i, 4);
				shipBG[i] = sheet.getSprite(i, 5);
			}
			
			//load objects in the tile sprite sheet

			//pmetal slope
			pmetalSlope = new BufferedImage[2];
			for(int i = 0; i < pmetalSlope.length; i++) pmetalSlope[i] = sheet.getSprite(i, 10);
			
			//ship slope
			shipSlope = new BufferedImage[2];
			for(int i = 0; i < shipSlope.length; i++) shipSlope[i] = sheet.getSprite(i + 2, 10);
			
			//spikes
			spikes = new BufferedImage[4];
			for(int i = 0; i < spikes.length; i++) spikes[i] = sheet.getSprite(i, 8);
			
			//acid pool
			acidPool = new BufferedImage[8];
			for(int i = 0; i < acidPool.length; i++) acidPool[i] = sheet.getSprite(i + 4, 8);
			
			//game button
			gameButton = new BufferedImage[2];
			for(int i = 0; i < gameButton.length; i++) gameButton[i] = sheet.getSprite(i + 12, 8);
			
			//plants
			plants = new BufferedImage[3];
			for(int i = 0; i < plants.length; i++) plants[i] = sheet.getSprite(i + 14, 8);
			
			//wall cracks
			wallCracks = new BufferedImage[4];
			for(int i = 0; i < wallCracks.length; i++) wallCracks[i] = sheet.getSprite(i, 9);

			//block cracks
			blockCracks = new BufferedImage[4];
			for(int i = 0; i < blockCracks.length; i++) blockCracks[i] = sheet.getSprite(i + 4, 9);
			//p moss
			pMoss = new BufferedImage[4];
			for(int i = 0; i < pMoss.length; i++) pMoss[i] = sheet.getSprite(i + 8, 9);
			
			//stalagtite
			stalagtite = new BufferedImage[2];
			stalagtite[0] = ImageIO.read(new File(Files.SPRITE_SHEETS + "/stalagtite.png"));
			sheet = new SpriteSheet(16, 16, stalagtite[0]);
			stalagtite[1] = sheet.getSprite(0, 1);
			
			//alien door
			alienDoor = ImageIO.read(new File(Files.IMAGES + "/alienDoor.png"));
			
			//alien window
			alienWindow = ImageIO.read(new File(Files.IMAGES + "/alienWindow.png"));
			
			//ship exterior
			sheet = new SpriteSheet(64, 48, ImageIO.read(new File(Files.SPRITE_SHEETS + "/ship.png")));
			shipExterior = new BufferedImage[2];
			for(int i = 0; i < shipExterior.length; i++) shipExterior[i] = sheet.getSprite(0, i);
			
			//load lever
			sheet = new SpriteSheet(16, 16, ImageIO.read(new File(Files.SPRITE_SHEETS + "/lever.png")));
			leverFrames = new BufferedImage[3][9][2];
			for(int i = 0; i < leverFrames.length; i++){
				for(int j = 0; j < leverFrames[0].length; j++){
					leverFrames[i][j][0] = sheet.getSprite(j, i * 2);
					leverFrames[i][j][1] = sheet.getSprite(j, i * 2 + 1);
				}
			}
			
			//load parts for signs in the beginning
			sheet = new SpriteSheet(16, 16, ImageIO.read(new File(Files.SPRITE_SHEETS + "/introSign.png")));
			introSignTiles = new BufferedImage[21];
			for(int i = 0; i < introSignTiles.length; i++){
				introSignTiles[i] = sheet.getSprite(i % 3, i / 3);
			}
			
			//load health packs
			sheet = new SpriteSheet(12, 12, ImageIO.read(new File(Files.SPRITE_SHEETS + "/healthPack.png")));
			healthPacks = new BufferedImage[4];
			for(int i = 0; i < healthPacks.length; i++){
				healthPacks[i] = sheet.getSprite(i, 0);
			}
			
			//load lazers
			sheet = new SpriteSheet(1, 14, ImageIO.read(new File(Files.SPRITE_SHEETS + "/lazerH.png")));
			lazerHorizontal = new BufferedImage[40][2];
			for(int i = 0; i < lazerHorizontal.length; i++){
				for(int j = 0; j < lazerHorizontal[i].length; j++){
					lazerHorizontal[i][j] = sheet.getSprite(i, j);
				}
			}
			sheet = new SpriteSheet(14, 1, ImageIO.read(new File(Files.SPRITE_SHEETS + "/lazerV.png")));
			lazerVertical = new BufferedImage[2][40];
			for(int i = 0; i < lazerVertical.length; i++){
				for(int j = 0; j < lazerVertical[i].length; j++){
					lazerVertical[i][j] = sheet.getSprite(i, j);
				}
			}
			
			//training dummy
			trainingDummmy = ImageIO.read(new File(Files.IMAGES + "/trainingDummy.png"));
			
			//attack projectiles
			playerAttack = ImageIO.read(new File(Files.IMAGES + "/playerAttack.png"));
			bounceAttack = ImageIO.read(new File(Files.IMAGES + "/bounceAttack.png"));
			normalAttack = ImageIO.read(new File(Files.IMAGES + "/normalAttack.png"));
			
			//load background
			backgroundStars = ImageIO.read(new File(Files.IMAGES + "/backgroundStars.png"));
			backgroundPlanets = ImageIO.read(new File(Files.IMAGES + "/backgroundPlanets.png"));
			backgroundHills = ImageIO.read(new File(Files.IMAGES + "/backgroundHills.png"));
			backgroundShip = ImageIO.read(new File(Files.IMAGES + "/backgroundShip.png"));
			backgroundBossChamber = ImageIO.read(new File(Files.IMAGES + "/backgroundBossChamber.png"));
			
			//load flying alien frames
			sheet = new SpriteSheet(18, 22, ImageIO.read(new File(Files.SPRITE_SHEETS + "/flyingAlien.png")));
			flyingAlienFrames = new BufferedImage[4];
			for(int i = 0; i < flyingAlienFrames.length; i++){
				flyingAlienFrames[i] = sheet.getSprite(i, 0);
			}
			
			//load minion alien frames
			sheet = new SpriteSheet(10, 10, ImageIO.read(new File(Files.SPRITE_SHEETS + "/minionAlien.png")));
			minionAlienFrames = new BufferedImage[4];
			for(int i = 0; i < minionAlienFrames.length; i++){
				minionAlienFrames[i] = sheet.getSprite(i, 0);
			}
			
			//load melee alien frames
			sheet = new SpriteSheet(18, 28, ImageIO.read(new File(Files.SPRITE_SHEETS + "/meleeAlien.png")));
			meleeAlienFrames = new BufferedImage[4];
			for(int i = 0; i < meleeAlienFrames.length; i++){
				meleeAlienFrames[i] = sheet.getSprite(i, 0);
			}
			
			//load bounce alien frames
			sheet = new SpriteSheet(16, 32, ImageIO.read(new File(Files.SPRITE_SHEETS + "/bounceAlien.png")));
			bounceAlienFrames = new BufferedImage[4];
			for(int i = 0; i < bounceAlienFrames.length; i++){
				bounceAlienFrames[i] = sheet.getSprite(i, 0);
			}
			
			//load player frames
			sheet = new SpriteSheet(16, 24, ImageIO.read(new File(Files.SPRITE_SHEETS + "/player.png")));
			playerFrames = new BufferedImage[4][4];
			for(int i = 0; i < playerFrames.length; i++){
				for(int j = 0; j < playerFrames[i].length; j++){
					playerFrames[i][j] = sheet.getSprite(i, j);
				}
			}
			
			//load final boss frames
			sheet = new SpriteSheet(40, 56, ImageIO.read(new File(Files.SPRITE_SHEETS + "/finalBoss.png")));
			finalBossFrames = new BufferedImage[4][2];
			for(int i = 0; i < finalBossFrames.length; i++){
				for(int j = 0; j < finalBossFrames[i].length; j++){
					finalBossFrames[i][j] = sheet.getSprite(i, j);
				}
			}
			
			//load final boss shield frames
			sheet = new SpriteSheet(6, 68, ImageIO.read(new File(Files.SPRITE_SHEETS + "/shield.png")));
			finalBossShield = new BufferedImage[4];
			for(int i = 0; i < finalBossShield.length; i++){
				finalBossShield[i] = sheet.getSprite(i, 0);
			}
			
			//trapped fFriend
			sheet = new SpriteSheet(32, 32, ImageIO.read(new File(Files.SPRITE_SHEETS + "/trappedFriend.png")));
			trappedFriend = new BufferedImage[3];
			for(int i = 0; i < trappedFriend.length; i++){
				trappedFriend[i] = sheet.getSprite(i, 0);
			}
			//load final boss attacks
			finalBossNormalShot = ImageIO.read(new File(Files.IMAGES + "/finalBossNormalShot.png"));
			finalBossSpreadShot4 = ImageIO.read(new File(Files.IMAGES + "/finalBossSpreadShot4.png"));
			finalBossSpreadShot8 = ImageIO.read(new File(Files.IMAGES + "/finalBossSpreadShot8.png"));
			finalBossExplode = ImageIO.read(new File(Files.IMAGES + "/explode.png"));
			
		}catch(Exception e){
			System.err.println("Failed to load Images");
			e.printStackTrace();
		}
	}
	
}
