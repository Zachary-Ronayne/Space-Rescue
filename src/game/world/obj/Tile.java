package game.world.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.libs.Images;

public class Tile extends GameObject{
	
	//tiles edge ids for lists
	public static final int FILL = 0;
	public static final int EDGE_T = 1;
	public static final int EDGE_R = 2;
	public static final int EDGE_B = 3;
	public static final int EDGE_L = 4;
	public static final int EXT_TL = 5;
	public static final int EXT_TR = 6;
	public static final int EXT_BR = 7;
	public static final int EXT_BL = 8;
	public static final int INT_TL = 9;
	public static final int INT_TR = 10;
	public static final int INT_BR = 11;
	public static final int INT_BL = 12;
	public static final int TRI_T = 13;
	public static final int TRI_R = 14;
	public static final int TRI_B = 15;
	public static final int TRI_L = 16;
	public static final int FULL = 17;
	
	//ids for each solid tile type
	public static final int ROCK = 0;
	public static final int PMETAL = 1;
	public static final int SHIP = 2;
	
	/**
	 * Fill = inside block
	 * T = top
	 * R = right
	 * B = bottom
	 * L = left
	 * H = horizontal
	 * V = vertical
	 * N = an interior corner that faces the negative direction
	 * P = an interior corner that faces the positive direction
	 * EXT = exterior corner
	 * INT = interior corner
	 * BOTH = tile that has both an interior and exterior angle
	 * TL = top left
	 * TR = top right
	 * BL = bottom left
	 * BR = bottom right
	 * NOT (corner specification) = all 4 corners except for that corner
	 * @author Owner
	 */
	public enum Type{
		//test tiles
		TEST_BLOCK(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(0, 0, 0).getRGB(), null),
		TEST_WALL(false, MIN_RENDER_PRIORITY, new Color(100, 100, 100).getRGB(), null),
		TEST_WALL2(false, MIN_RENDER_PRIORITY, new Color(110, 110, 110).getRGB(), null),
		
		//invisible tiles
		INVISIBLE_WALL(true, MIN_RENDER_PRIORITY, new Color(40, 40, 40).getRGB(), null),
		AIR(false, MIN_RENDER_PRIORITY, new Color(255, 255, 255).getRGB(), null),
		
		//rocks, solid
		ROCK_FILL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(50, 0, 50).getRGB(), Images.rock),
		ROCK_EDGE_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(55, 0, 55).getRGB(), Images.rock, EDGE_T),
		ROCK_EDGE_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(60, 0, 60).getRGB(), Images.rock, EDGE_R),
		ROCK_EDGE_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(65, 0, 65).getRGB(), Images.rock, EDGE_B),
		ROCK_EDGE_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(70, 0, 70).getRGB(), Images.rock, EDGE_L),
		ROCK_EDGE_H(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(75, 0, 75).getRGB(), Images.rock, EDGE_T, EDGE_B),
		ROCK_EDGE_V(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(80, 0, 80).getRGB(), Images.rock, EDGE_L, EDGE_R),
		ROCK_EDGE_T_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(85, 0, 85).getRGB(), Images.rock, EDGE_T, INT_BL),
		ROCK_EDGE_R_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(90, 0, 90).getRGB(), Images.rock, EDGE_R, INT_TL),
		ROCK_EDGE_B_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(95, 0, 95).getRGB(), Images.rock, EDGE_B, INT_TL),
		ROCK_EDGE_L_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(100, 0, 100).getRGB(), Images.rock, EDGE_L, INT_TR),
		ROCK_EDGE_T_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(105, 0, 105).getRGB(), Images.rock, EDGE_T, INT_BR),
		ROCK_EDGE_R_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(110, 0, 110).getRGB(), Images.rock, EDGE_R, INT_BL),
		ROCK_EDGE_B_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(115, 0, 115).getRGB(), Images.rock, EDGE_B, INT_TR),
		ROCK_EDGE_L_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(120, 0, 120).getRGB(), Images.rock, EDGE_L, INT_BR),
		ROCK_EDGE_T_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(125, 0, 125).getRGB(), Images.rock, EDGE_T, INT_BL, INT_BR),
		ROCK_EDGE_R_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(130, 0, 130).getRGB(), Images.rock, EDGE_R, INT_TL, INT_BL),
		ROCK_EDGE_B_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(135, 0, 135).getRGB(), Images.rock, EDGE_B, INT_TL, INT_TR),
		ROCK_EDGE_L_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(140, 0, 140).getRGB(), Images.rock, EDGE_L, INT_TR, INT_BR),
		ROCK_EXT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(145, 0, 145).getRGB(), Images.rock, EXT_TL),
		ROCK_EXT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(150, 0, 150).getRGB(), Images.rock, EXT_TR),
		ROCK_EXT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(155, 0, 155).getRGB(), Images.rock, EXT_BR),
		ROCK_EXT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(160, 0, 160).getRGB(), Images.rock, EXT_BL),
		ROCK_INT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(165, 0, 165).getRGB(), Images.rock, INT_TL),
		ROCK_INT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(170, 0, 170).getRGB(), Images.rock, INT_TR),
		ROCK_INT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(175, 0, 175).getRGB(), Images.rock, INT_BR),
		ROCK_INT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 180).getRGB(), Images.rock, INT_BL),
		ROCK_INT_TL_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 175).getRGB(), Images.rock, INT_TL, INT_TR),
		ROCK_INT_TL_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 170).getRGB(), Images.rock, INT_TL, INT_BR),
		ROCK_INT_TL_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 165).getRGB(), Images.rock, INT_TL, INT_BL),
		ROCK_INT_TR_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 160).getRGB(), Images.rock, INT_TR, INT_BR),
		ROCK_INT_TR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 155).getRGB(), Images.rock, INT_TR, INT_BL),
		ROCK_INT_BR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 150).getRGB(), Images.rock, INT_BR, INT_BL),
		ROCK_INT_NOT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 145).getRGB(), Images.rock, INT_TR, INT_BR, INT_BL),
		ROCK_INT_NOT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 140).getRGB(), Images.rock, INT_TL, INT_BR, INT_BL),
		ROCK_INT_NOT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 135).getRGB(), Images.rock, INT_TL, INT_TR, INT_BL),
		ROCK_INT_NOT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 130).getRGB(), Images.rock, INT_TL, INT_TR, INT_BR),
		ROCK_INT_ALL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 0, 125).getRGB(), Images.rock, INT_TL, INT_TR, INT_BR, INT_BL),
		ROCK_BOTH_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(185, 0, 185).getRGB(), Images.rock, INT_BR, EXT_TL),
		ROCK_BOTH_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(190, 0, 190).getRGB(), Images.rock, INT_BL, EXT_TR),
		ROCK_BOTH_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(195, 0, 195).getRGB(), Images.rock, INT_TL, EXT_BR),
		ROCK_BOTH_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(200, 0, 200).getRGB(), Images.rock, INT_TR, EXT_BL),
		ROCK_TRI_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(205, 0, 205).getRGB(), Images.rock, TRI_T),
		ROCK_TRI_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(210, 0, 210).getRGB(), Images.rock, TRI_R),
		ROCK_TRI_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(215, 0, 215).getRGB(), Images.rock, TRI_B),
		ROCK_TRI_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(220, 0, 220).getRGB(), Images.rock, TRI_L),
		ROCK_FULL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(225, 0, 225).getRGB(), Images.rock, FULL),
		
		//rocks, background
		ROCK_BG_FILL(false, MIN_RENDER_PRIORITY, new Color(20, 0, 20).getRGB(), Images.rockBG),
		ROCK_BG_EDGE_T(false, MIN_RENDER_PRIORITY, new Color(25, 0, 20).getRGB(), Images.rockBG, EDGE_T),
		ROCK_BG_EDGE_R(false, MIN_RENDER_PRIORITY, new Color(30, 0, 20).getRGB(), Images.rockBG, EDGE_R),
		ROCK_BG_EDGE_B(false, MIN_RENDER_PRIORITY, new Color(35, 0, 20).getRGB(), Images.rockBG, EDGE_B),
		ROCK_BG_EDGE_L(false, MIN_RENDER_PRIORITY, new Color(40, 0, 20).getRGB(), Images.rockBG, EDGE_L),
		ROCK_BG_EDGE_H(false, MIN_RENDER_PRIORITY, new Color(45, 0, 20).getRGB(), Images.rockBG, EDGE_T, EDGE_B),
		ROCK_BG_EDGE_V(false, MIN_RENDER_PRIORITY, new Color(50, 0, 20).getRGB(), Images.rockBG, EDGE_L, EDGE_R),
		ROCK_BG_EDGE_T_N(false, MIN_RENDER_PRIORITY, new Color(55, 0, 20).getRGB(), Images.rockBG, EDGE_T, INT_BL),
		ROCK_BG_EDGE_R_N(false, MIN_RENDER_PRIORITY, new Color(60, 0, 20).getRGB(), Images.rockBG, EDGE_R, INT_TL),
		ROCK_BG_EDGE_B_N(false, MIN_RENDER_PRIORITY, new Color(65, 0, 20).getRGB(), Images.rockBG, EDGE_B, INT_TL),
		ROCK_BG_EDGE_L_N(false, MIN_RENDER_PRIORITY, new Color(70, 0, 20).getRGB(), Images.rockBG, EDGE_L, INT_TR),
		ROCK_BG_EDGE_T_P(false, MIN_RENDER_PRIORITY, new Color(75, 0, 20).getRGB(), Images.rockBG, EDGE_T, INT_BR),
		ROCK_BG_EDGE_R_P(false, MIN_RENDER_PRIORITY, new Color(80, 0, 20).getRGB(), Images.rockBG, EDGE_R, INT_BL),
		ROCK_BG_EDGE_B_P(false, MIN_RENDER_PRIORITY, new Color(85, 0, 20).getRGB(), Images.rockBG, EDGE_B, INT_TR),
		ROCK_BG_EDGE_L_P(false, MIN_RENDER_PRIORITY, new Color(90, 0, 20).getRGB(), Images.rockBG, EDGE_L, INT_BR),
		ROCK_BG_EDGE_T_NP(false, MIN_RENDER_PRIORITY, new Color(95, 0, 20).getRGB(), Images.rockBG, EDGE_T, INT_BL, INT_BR),
		ROCK_BG_EDGE_R_NP(false, MIN_RENDER_PRIORITY, new Color(100, 0, 20).getRGB(), Images.rockBG, EDGE_R, INT_TL, INT_BL),
		ROCK_BG_EDGE_B_NP(false, MIN_RENDER_PRIORITY, new Color(105, 0, 20).getRGB(), Images.rockBG, EDGE_B, INT_TL, INT_TR),
		ROCK_BG_EDGE_L_NP(false, MIN_RENDER_PRIORITY, new Color(110, 0, 20).getRGB(), Images.rockBG, EDGE_L, INT_TR, INT_BR),
		ROCK_BG_EXT_TL(false, MIN_RENDER_PRIORITY, new Color(115, 0, 20).getRGB(), Images.rockBG, EXT_TL),
		ROCK_BG_EXT_TR(false, MIN_RENDER_PRIORITY, new Color(120, 0, 20).getRGB(), Images.rockBG, EXT_TR),
		ROCK_BG_EXT_BR(false, MIN_RENDER_PRIORITY, new Color(125, 0, 20).getRGB(), Images.rockBG, EXT_BR),
		ROCK_BG_EXT_BL(false, MIN_RENDER_PRIORITY, new Color(130, 0, 20).getRGB(), Images.rockBG, EXT_BL),
		ROCK_BG_INT_TL(false, MIN_RENDER_PRIORITY, new Color(135, 0, 20).getRGB(), Images.rockBG, INT_TL),
		ROCK_BG_INT_TR(false, MIN_RENDER_PRIORITY, new Color(140, 0, 20).getRGB(), Images.rockBG, INT_TR),
		ROCK_BG_INT_BR(false, MIN_RENDER_PRIORITY, new Color(145, 0, 20).getRGB(), Images.rockBG, INT_BR),
		ROCK_BG_INT_BL(false, MIN_RENDER_PRIORITY, new Color(150, 0, 20).getRGB(), Images.rockBG, INT_BL),
		ROCK_BG_INT_TL_TR(false, MIN_RENDER_PRIORITY, new Color(155, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_TR),
		ROCK_BG_INT_TL_BR(false, MIN_RENDER_PRIORITY, new Color(160, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_BR),
		ROCK_BG_INT_TL_BL(false, MIN_RENDER_PRIORITY, new Color(165, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_BL),
		ROCK_BG_INT_TR_BR(false, MIN_RENDER_PRIORITY, new Color(170, 0, 20).getRGB(), Images.rockBG, INT_TR, INT_BR),
		ROCK_BG_INT_TR_BL(false, MIN_RENDER_PRIORITY, new Color(175, 0, 20).getRGB(), Images.rockBG, INT_TR, INT_BL),
		ROCK_BG_INT_BR_BL(false, MIN_RENDER_PRIORITY, new Color(180, 0, 20).getRGB(), Images.rockBG, INT_BR, INT_BL),
		ROCK_BG_INT_NOT_TL(false, MIN_RENDER_PRIORITY, new Color(185, 0, 20).getRGB(), Images.rockBG, INT_TR, INT_BR, INT_BL),
		ROCK_BG_INT_NOT_TR(false, MIN_RENDER_PRIORITY, new Color(190, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_BR, INT_BL),
		ROCK_BG_INT_NOT_BR(false, MIN_RENDER_PRIORITY, new Color(195, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_TR, INT_BL),
		ROCK_BG_INT_NOT_BL(false, MIN_RENDER_PRIORITY, new Color(200, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_TR, INT_BR),
		ROCK_BG_INT_ALL(false, MIN_RENDER_PRIORITY, new Color(205, 0, 20).getRGB(), Images.rockBG, INT_TL, INT_TR, INT_BR, INT_BL),
		ROCK_BG_BOTH_TL(false, MIN_RENDER_PRIORITY, new Color(210, 0, 20).getRGB(), Images.rockBG, INT_BR, EXT_TL),
		ROCK_BG_BOTH_TR(false, MIN_RENDER_PRIORITY, new Color(215, 0, 20).getRGB(), Images.rockBG, INT_BL, EXT_TR),
		ROCK_BG_BOTH_BR(false, MIN_RENDER_PRIORITY, new Color(220, 0, 20).getRGB(), Images.rockBG, INT_TL, EXT_BR),
		ROCK_BG_BOTH_BL(false, MIN_RENDER_PRIORITY, new Color(225, 0, 20).getRGB(), Images.rockBG, INT_TR, EXT_BL),
		ROCK_BG_TRI_T(false, MIN_RENDER_PRIORITY, new Color(230, 0, 20).getRGB(), Images.rockBG, TRI_T),
		ROCK_BG_TRI_R(false, MIN_RENDER_PRIORITY, new Color(235, 0, 20).getRGB(), Images.rockBG, TRI_R),
		ROCK_BG_TRI_B(false, MIN_RENDER_PRIORITY, new Color(240, 0, 20).getRGB(), Images.rockBG, TRI_B),
		ROCK_BG_TRI_L(false, MIN_RENDER_PRIORITY, new Color(245, 0, 20).getRGB(), Images.rockBG, TRI_L),
		ROCK_BG_FULL(false, MIN_RENDER_PRIORITY, new Color(250, 0, 20).getRGB(), Images.rockBG, FULL),
		
		//pmetal, solid
		PMETAL_FILL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(20, 5, 20).getRGB(), Images.pmetal),
		PMETAL_EDGE_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(25, 10, 25).getRGB(), Images.pmetal, EDGE_T),
		PMETAL_EDGE_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(30, 15, 30).getRGB(), Images.pmetal, EDGE_R),
		PMETAL_EDGE_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(35, 20, 35).getRGB(), Images.pmetal, EDGE_B),
		PMETAL_EDGE_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(40, 25, 40).getRGB(), Images.pmetal, EDGE_L),
		PMETAL_EDGE_H(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(45, 30, 45).getRGB(), Images.pmetal, EDGE_T, EDGE_B),
		PMETAL_EDGE_V(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(50, 35, 50).getRGB(), Images.pmetal, EDGE_L, EDGE_R),
		PMETAL_EDGE_T_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(55, 40, 55).getRGB(), Images.pmetal, EDGE_T, INT_BL),
		PMETAL_EDGE_R_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(60, 45, 60).getRGB(), Images.pmetal, EDGE_R, INT_TL),
		PMETAL_EDGE_B_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(65, 50, 65).getRGB(), Images.pmetal, EDGE_B, INT_TL),
		PMETAL_EDGE_L_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(70, 55, 70).getRGB(), Images.pmetal, EDGE_L, INT_TR),
		PMETAL_EDGE_T_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(75, 60, 75).getRGB(), Images.pmetal, EDGE_T, INT_BR),
		PMETAL_EDGE_R_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(80, 65, 80).getRGB(), Images.pmetal, EDGE_R, INT_BL),
		PMETAL_EDGE_B_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(85, 70, 85).getRGB(), Images.pmetal, EDGE_B, INT_TR),
		PMETAL_EDGE_L_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(90, 75, 90).getRGB(), Images.pmetal, EDGE_L, INT_BR),
		PMETAL_EDGE_T_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(95, 80, 95).getRGB(), Images.pmetal, EDGE_T, INT_BL, INT_BR),
		PMETAL_EDGE_R_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(100, 85, 100).getRGB(), Images.pmetal, EDGE_R, INT_TL, INT_BL),
		PMETAL_EDGE_B_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(105, 90, 105).getRGB(), Images.pmetal, EDGE_B, INT_TL, INT_TR),
		PMETAL_EDGE_L_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(110, 95, 110).getRGB(), Images.pmetal, EDGE_L, INT_TR, INT_BR),
		PMETAL_EXT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(115, 100, 115).getRGB(), Images.pmetal, EXT_TL),
		PMETAL_EXT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(120, 105, 120).getRGB(), Images.pmetal, EXT_TR),
		PMETAL_EXT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(125, 110, 125).getRGB(), Images.pmetal, EXT_BR),
		PMETAL_EXT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(130, 115, 130).getRGB(), Images.pmetal, EXT_BL),
		PMETAL_INT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(135, 120, 135).getRGB(), Images.pmetal, INT_TL),
		PMETAL_INT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(140, 125, 140).getRGB(), Images.pmetal, INT_TR),
		PMETAL_INT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(145, 130, 145).getRGB(), Images.pmetal, INT_BR),
		PMETAL_INT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(150, 135, 150).getRGB(), Images.pmetal, INT_BL),
		PMETAL_INT_TL_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(155, 140, 155).getRGB(), Images.pmetal, INT_TL, INT_TR),
		PMETAL_INT_TL_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(160, 145, 160).getRGB(), Images.pmetal, INT_TL, INT_BR),
		PMETAL_INT_TL_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(165, 150, 165).getRGB(), Images.pmetal, INT_TL, INT_BL),
		PMETAL_INT_TR_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(170, 155, 170).getRGB(), Images.pmetal, INT_TR, INT_BR),
		PMETAL_INT_TR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(175, 160, 175).getRGB(), Images.pmetal, INT_TR, INT_BL),
		PMETAL_INT_BR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 165, 180).getRGB(), Images.pmetal, INT_BR, INT_BL),
		PMETAL_INT_NOT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(185, 170, 185).getRGB(), Images.pmetal, INT_TR, INT_BR, INT_BL),
		PMETAL_INT_NOT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(190, 175, 190).getRGB(), Images.pmetal, INT_TL, INT_BR, INT_BL),
		PMETAL_INT_NOT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(195, 180, 195).getRGB(), Images.pmetal, INT_TL, INT_TR, INT_BL),
		PMETAL_INT_NOT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(200, 185, 200).getRGB(), Images.pmetal, INT_TL, INT_TR, INT_BR),
		PMETAL_INT_ALL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(205, 190, 205).getRGB(), Images.pmetal, INT_TL, INT_TR, INT_BR, INT_BL),
		PMETAL_BOTH_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(210, 195, 210).getRGB(), Images.pmetal, INT_BR, EXT_TL),
		PMETAL_BOTH_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(215, 200, 215).getRGB(), Images.pmetal, INT_BL, EXT_TR),
		PMETAL_BOTH_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(220, 205, 220).getRGB(), Images.pmetal, INT_TL, EXT_BR),
		PMETAL_BOTH_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(225, 210, 225).getRGB(), Images.pmetal, INT_TR, EXT_BL),
		PMETAL_TRI_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(230, 215, 230).getRGB(), Images.pmetal, TRI_T),
		PMETAL_TRI_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(235, 220, 235).getRGB(), Images.pmetal, TRI_R),
		PMETAL_TRI_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(240, 225, 240).getRGB(), Images.pmetal, TRI_B),
		PMETAL_TRI_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(245, 230, 245).getRGB(), Images.pmetal, TRI_L),
		PMETAL_FULL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(250, 235, 250).getRGB(), Images.pmetal, FULL),
		
		//pmetal, background
		PMETAL_BG_FILL(false, MIN_RENDER_PRIORITY, new Color(100, 240, 255).getRGB(), Images.pmetalBG),
		PMETAL_BG_EDGE_T(false, MIN_RENDER_PRIORITY, new Color(100, 225, 255).getRGB(), Images.pmetalBG, EDGE_T),
		PMETAL_BG_EDGE_R(false, MIN_RENDER_PRIORITY, new Color(100, 220, 255).getRGB(), Images.pmetalBG, EDGE_R),
		PMETAL_BG_EDGE_B(false, MIN_RENDER_PRIORITY, new Color(100, 215, 255).getRGB(), Images.pmetalBG, EDGE_B),
		PMETAL_BG_EDGE_L(false, MIN_RENDER_PRIORITY, new Color(100, 210, 255).getRGB(), Images.pmetalBG, EDGE_L),
		PMETAL_BG_EDGE_H(false, MIN_RENDER_PRIORITY, new Color(100, 205, 255).getRGB(), Images.pmetalBG, EDGE_T, EDGE_B),
		PMETAL_BG_EDGE_V(false, MIN_RENDER_PRIORITY, new Color(100, 200, 255).getRGB(), Images.pmetalBG, EDGE_L, EDGE_R),
		PMETAL_BG_EDGE_T_N(false, MIN_RENDER_PRIORITY, new Color(100, 195, 255).getRGB(), Images.pmetalBG, EDGE_T, INT_BL),
		PMETAL_BG_EDGE_R_N(false, MIN_RENDER_PRIORITY, new Color(100, 190, 255).getRGB(), Images.pmetalBG, EDGE_R, INT_TL),
		PMETAL_BG_EDGE_B_N(false, MIN_RENDER_PRIORITY, new Color(100, 185, 255).getRGB(), Images.pmetalBG, EDGE_B, INT_TL),
		PMETAL_BG_EDGE_L_N(false, MIN_RENDER_PRIORITY, new Color(100, 180, 255).getRGB(), Images.pmetalBG, EDGE_L, INT_TR),
		PMETAL_BG_EDGE_T_P(false, MIN_RENDER_PRIORITY, new Color(100, 175, 255).getRGB(), Images.pmetalBG, EDGE_T, INT_BR),
		PMETAL_BG_EDGE_R_P(false, MIN_RENDER_PRIORITY, new Color(100, 170, 255).getRGB(), Images.pmetalBG, EDGE_R, INT_BL),
		PMETAL_BG_EDGE_B_P(false, MIN_RENDER_PRIORITY, new Color(100, 165, 255).getRGB(), Images.pmetalBG, EDGE_B, INT_TR),
		PMETAL_BG_EDGE_L_P(false, MIN_RENDER_PRIORITY, new Color(100, 160, 255).getRGB(), Images.pmetalBG, EDGE_L, INT_BR),
		PMETAL_BG_EDGE_T_NP(false, MIN_RENDER_PRIORITY, new Color(100, 155, 255).getRGB(), Images.pmetalBG, EDGE_T, INT_BL, INT_BR),
		PMETAL_BG_EDGE_R_NP(false, MIN_RENDER_PRIORITY, new Color(100, 150, 255).getRGB(), Images.pmetalBG, EDGE_R, INT_TL, INT_BL),
		PMETAL_BG_EDGE_B_NP(false, MIN_RENDER_PRIORITY, new Color(100, 145, 255).getRGB(), Images.pmetalBG, EDGE_B, INT_TL, INT_TR),
		PMETAL_BG_EDGE_L_NP(false, MIN_RENDER_PRIORITY, new Color(100, 140, 255).getRGB(), Images.pmetalBG, EDGE_L, INT_TR, INT_BR),
		PMETAL_BG_EXT_TL(false, MIN_RENDER_PRIORITY, new Color(100, 135, 255).getRGB(), Images.pmetalBG, EXT_TL),
		PMETAL_BG_EXT_TR(false, MIN_RENDER_PRIORITY, new Color(100, 130, 255).getRGB(), Images.pmetalBG, EXT_TR),
		PMETAL_BG_EXT_BR(false, MIN_RENDER_PRIORITY, new Color(100, 125, 255).getRGB(), Images.pmetalBG, EXT_BR),
		PMETAL_BG_EXT_BL(false, MIN_RENDER_PRIORITY, new Color(100, 120, 255).getRGB(), Images.pmetalBG, EXT_BL),
		PMETAL_BG_INT_TL(false, MIN_RENDER_PRIORITY, new Color(100, 115, 255).getRGB(), Images.pmetalBG, INT_TL),
		PMETAL_BG_INT_TR(false, MIN_RENDER_PRIORITY, new Color(100, 110, 255).getRGB(), Images.pmetalBG, INT_TR),
		PMETAL_BG_INT_BR(false, MIN_RENDER_PRIORITY, new Color(100, 105, 255).getRGB(), Images.pmetalBG, INT_BR),
		PMETAL_BG_INT_BL(false, MIN_RENDER_PRIORITY, new Color(100, 100, 255).getRGB(), Images.pmetalBG, INT_BL),
		PMETAL_BG_INT_TL_TR(false, MIN_RENDER_PRIORITY, new Color(100, 95, 255).getRGB(), Images.pmetalBG, INT_TL, INT_TR),
		PMETAL_BG_INT_TL_BR(false, MIN_RENDER_PRIORITY, new Color(100, 90, 255).getRGB(), Images.pmetalBG, INT_TL, INT_BR),
		PMETAL_BG_INT_TL_BL(false, MIN_RENDER_PRIORITY, new Color(100, 85, 255).getRGB(), Images.pmetalBG, INT_TL, INT_BL),
		PMETAL_BG_INT_TR_BR(false, MIN_RENDER_PRIORITY, new Color(100, 80, 255).getRGB(), Images.pmetalBG, INT_TR, INT_BR),
		PMETAL_BG_INT_TR_BL(false, MIN_RENDER_PRIORITY, new Color(100, 75, 255).getRGB(), Images.pmetalBG, INT_TR, INT_BL),
		PMETAL_BG_INT_BR_BL(false, MIN_RENDER_PRIORITY, new Color(100, 70, 255).getRGB(), Images.pmetalBG, INT_BR, INT_BL),
		PMETAL_BG_INT_NOT_TL(false, MIN_RENDER_PRIORITY, new Color(100, 65, 255).getRGB(), Images.pmetalBG, INT_TR, INT_BR, INT_BL),
		PMETAL_BG_INT_NOT_TR(false, MIN_RENDER_PRIORITY, new Color(100, 60, 255).getRGB(), Images.pmetalBG, INT_TL, INT_BR, INT_BL),
		PMETAL_BG_INT_NOT_BR(false, MIN_RENDER_PRIORITY, new Color(100, 55, 255).getRGB(), Images.pmetalBG, INT_TL, INT_TR, INT_BL),
		PMETAL_BG_INT_NOT_BL(false, MIN_RENDER_PRIORITY, new Color(100, 50, 255).getRGB(), Images.pmetalBG, INT_TL, INT_TR, INT_BR),
		PMETAL_BG_INT_ALL(false, MIN_RENDER_PRIORITY, new Color(100, 45, 255).getRGB(), Images.pmetalBG, INT_TL, INT_TR, INT_BR, INT_BL),
		PMETAL_BG_BOTH_TL(false, MIN_RENDER_PRIORITY, new Color(100, 40, 255).getRGB(), Images.pmetalBG, INT_BR, EXT_TL),
		PMETAL_BG_BOTH_TR(false, MIN_RENDER_PRIORITY, new Color(100, 35, 255).getRGB(), Images.pmetalBG, INT_BL, EXT_TR),
		PMETAL_BG_BOTH_BR(false, MIN_RENDER_PRIORITY, new Color(100, 30, 255).getRGB(), Images.pmetalBG, INT_TL, EXT_BR),
		PMETAL_BG_BOTH_BL(false, MIN_RENDER_PRIORITY, new Color(100, 25, 255).getRGB(), Images.pmetalBG, INT_TR, EXT_BL),
		PMETAL_BG_TRI_T(false, MIN_RENDER_PRIORITY, new Color(100, 20, 255).getRGB(), Images.pmetalBG, TRI_T),
		PMETAL_BG_TRI_R(false, MIN_RENDER_PRIORITY, new Color(100, 15, 255).getRGB(), Images.pmetalBG, TRI_R),
		PMETAL_BG_TRI_B(false, MIN_RENDER_PRIORITY, new Color(100, 10, 255).getRGB(), Images.pmetalBG, TRI_B),
		PMETAL_BG_TRI_L(false, MIN_RENDER_PRIORITY, new Color(100, 5, 255).getRGB(), Images.pmetalBG, TRI_L),
		PMETAL_BG_FULL(false, MIN_RENDER_PRIORITY, new Color(100, 0, 255).getRGB(), Images.pmetalBG, FULL),
		
		//space ship, solid
		SHIP_FILL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(0, 40, 40).getRGB(), Images.ship),
		SHIP_EDGE_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(5, 50, 50).getRGB(), Images.ship, EDGE_T),
		SHIP_EDGE_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(10, 50, 50).getRGB(), Images.ship, EDGE_R),
		SHIP_EDGE_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(15, 50, 50).getRGB(), Images.ship, EDGE_B),
		SHIP_EDGE_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(20, 50, 50).getRGB(), Images.ship, EDGE_L),
		SHIP_EDGE_H(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(25, 50, 50).getRGB(), Images.ship, EDGE_T, EDGE_B),
		SHIP_EDGE_V(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(30, 50, 50).getRGB(), Images.ship, EDGE_L, EDGE_R),
		SHIP_EDGE_T_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(35, 50, 50).getRGB(), Images.ship, EDGE_T, INT_BL),
		SHIP_EDGE_R_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(40, 50, 50).getRGB(), Images.ship, EDGE_R, INT_TL),
		SHIP_EDGE_B_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(45, 50, 50).getRGB(), Images.ship, EDGE_B, INT_TL),
		SHIP_EDGE_L_N(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(50, 50, 50).getRGB(), Images.ship, EDGE_L, INT_TR),
		SHIP_EDGE_T_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(55, 50, 50).getRGB(), Images.ship, EDGE_T, INT_BR),
		SHIP_EDGE_R_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(60, 50, 50).getRGB(), Images.ship, EDGE_R, INT_BL),
		SHIP_EDGE_B_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(65, 50, 50).getRGB(), Images.ship, EDGE_B, INT_TR),
		SHIP_EDGE_L_P(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(70, 50, 50).getRGB(), Images.ship, EDGE_L, INT_BR),
		SHIP_EDGE_T_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(75, 50, 50).getRGB(), Images.ship, EDGE_T, INT_BL, INT_BR),
		SHIP_EDGE_R_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(80, 50, 50).getRGB(), Images.ship, EDGE_R, INT_TL, INT_BL),
		SHIP_EDGE_B_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(85, 50, 50).getRGB(), Images.ship, EDGE_B, INT_TL, INT_TR),
		SHIP_EDGE_L_NP(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(90, 50, 50).getRGB(), Images.ship, EDGE_L, INT_TR, INT_BR),
		SHIP_EXT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(95, 50, 50).getRGB(), Images.ship, EXT_TL),
		SHIP_EXT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(100, 50, 50).getRGB(), Images.ship, EXT_TR),
		SHIP_EXT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(105, 50, 50).getRGB(), Images.ship, EXT_BR),
		SHIP_EXT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(110, 50, 50).getRGB(), Images.ship, EXT_BL),
		SHIP_INT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(115, 50, 50).getRGB(), Images.ship, INT_TL),
		SHIP_INT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(120, 50, 50).getRGB(), Images.ship, INT_TR),
		SHIP_INT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(125, 50, 50).getRGB(), Images.ship, INT_BR),
		SHIP_INT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(130, 50, 50).getRGB(), Images.ship, INT_BL),
		SHIP_INT_TL_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(135, 50, 50).getRGB(), Images.ship, INT_TL, INT_TR),
		SHIP_INT_TL_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(140, 50, 50).getRGB(), Images.ship, INT_TL, INT_BR),
		SHIP_INT_TL_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(145, 50, 50).getRGB(), Images.ship, INT_TL, INT_BL),
		SHIP_INT_TR_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(150, 50, 50).getRGB(), Images.ship, INT_TR, INT_BR),
		SHIP_INT_TR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(155, 50, 50).getRGB(), Images.ship, INT_TR, INT_BL),
		SHIP_INT_BR_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(160, 50, 50).getRGB(), Images.ship, INT_BR, INT_BL),
		SHIP_INT_NOT_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(165, 50, 50).getRGB(), Images.ship, INT_TR, INT_BR, INT_BL),
		SHIP_INT_NOT_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(170, 50, 50).getRGB(), Images.ship, INT_TL, INT_BR, INT_BL),
		SHIP_INT_NOT_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(175, 50, 50).getRGB(), Images.ship, INT_TL, INT_TR, INT_BL),
		SHIP_INT_NOT_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(180, 50, 50).getRGB(), Images.ship, INT_TL, INT_TR, INT_BR),
		SHIP_INT_ALL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(185, 50, 50).getRGB(), Images.ship, INT_TL, INT_TR, INT_BR, INT_BL),
		SHIP_BOTH_TL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(190, 50, 50).getRGB(), Images.ship, INT_BR, EXT_TL),
		SHIP_BOTH_TR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(195, 50, 50).getRGB(), Images.ship, INT_BL, EXT_TR),
		SHIP_BOTH_BR(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(200, 50, 50).getRGB(), Images.ship, INT_TL, EXT_BR),
		SHIP_BOTH_BL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(205, 50, 50).getRGB(), Images.ship, INT_TR, EXT_BL),
		SHIP_TRI_T(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(210, 50, 50).getRGB(), Images.ship, TRI_T),
		SHIP_TRI_R(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(215, 50, 50).getRGB(), Images.ship, TRI_R),
		SHIP_TRI_B(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(220, 50, 50).getRGB(), Images.ship, TRI_B),
		SHIP_TRI_L(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(225, 50, 50).getRGB(), Images.ship, TRI_L),
		SHIP_FULL(true, (int)(MAX_RENDER_PRIORITY * 0.7), new Color(230, 50, 50).getRGB(), Images.ship, FULL),
		
		//space ship, background
		SHIP_BG_FILL(false, MIN_RENDER_PRIORITY, new Color(0, 7, 255).getRGB(), Images.shipBG);
		
		private boolean collide;
		private int renderPriority;
		private int loadColor;
		private BufferedImage[] sprites;
		private int[] edges;
		
		private Type(boolean collide, int renderPriority, int loadColor, BufferedImage[] sprites, int... edges){
			this.collide = collide;
			this.renderPriority = renderPriority;
			this.loadColor = loadColor;
			this.sprites = sprites;
			this.edges = edges;
		}
		
		public boolean getCollide(){
			return collide;
		}
		
		public int getRenderPriority(){
			return renderPriority;
		}
		
		public int getLoadColor(){
			return loadColor;
		}
		
		public static Type getTileFromColor(int c){
			Type[] ts = Type.values();
			for(Type t : ts) {
				if(c == t.getLoadColor()) return t;
			}
			return AIR;
		}
		
		public void drawTile(Graphics2D g, double x, double y){
			switch(this){
				case TEST_WALL: g.setColor(new Color(200, 200, 200)); break;
				case TEST_WALL2: g.setColor(new Color(220, 220, 220)); break;
				case TEST_BLOCK: g.setColor(new Color(100, 100, 100)); break;
				case INVISIBLE_WALL: return;
				case AIR: return;
				default:{
					g.drawImage(sprites[FILL], (int)Math.round(x), (int)Math.round(y), null);
					for(int i = 0; i < edges.length; i++) g.drawImage(sprites[edges[i]], (int)Math.round(x), (int)Math.round(y), null);
					return;
				}
			}
			
			g.fillRect((int)x, (int)y, (int)TILE_SIZE, (int)TILE_SIZE);
		}
	}
	
	private Type tile;
	
	public Tile(double x, double y, Type tile){
		super(x , y, TILE_SIZE, TILE_SIZE, tile.getRenderPriority(), tile.getCollide());
		this.tile = tile;
	}
	
	@Override
	protected void tickOverride(){}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		tile.drawTile(g, getScreenX(x), getScreenY(y));
	}
	
	@Override
	public void setX(double x){
		super.setX((int)((int)(x / TILE_SIZE) * TILE_SIZE));
	}
	
	@Override
	public void setY(double y){
		super.setY((int)((int)(y / TILE_SIZE) * TILE_SIZE));
	}
	
	@Override
	public void setWidth(double width){
		super.setWidth(TILE_SIZE);
	}
	
	@Override
	public void setHeight(double height){
		super.setHeight(TILE_SIZE);
	}
	
	public Type getTile(){
		return tile;
	}
	
	public void setTile(Type tile){
		this.tile = tile;
	}
	
}
