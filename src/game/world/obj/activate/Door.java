package game.world.obj.activate;

import java.awt.Graphics2D;

import game.libs.Sounds;
import game.world.levels.Level;
import game.world.obj.GameObject;
import game.world.obj.Tile;

public class Door extends GameObject implements Activatable{
	
	private boolean closing;
	private double fullHeight;
	private double minHeight;
	
	private Tile.Type[][] tiles;
	
	private Level containerLevel;
	
	/**
	 * Creates a closed door, the door opens and closes from the top
	 * @param x
	 * @param y
	 * @param width, must be an integer number of tiles
	 * @param fullHeight The height of the door while closed, must be an integer number of tiles
	 * @param minHeight The minimum height of the door, must be an integer number of tiles
	 */
	public Door(double x, double y, int width, int fullHeight, int minHeight, int tileID, Level containerLevel){
		super(x, y, width * TILE_SIZE, fullHeight * TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .6), true);
		closing = true;
		
		if(minHeight < 1) minHeight = 1;
		
		this.fullHeight = fullHeight * TILE_SIZE;
		this.minHeight = minHeight * TILE_SIZE;
		
		tiles = new Tile.Type[width][fullHeight];
		makeTileGrid(tileID);
		
		this.containerLevel = containerLevel;
	}
	
	private void makeTileGrid(int id){
		boolean length = tiles.length == 1;
		switch(id){
			case Tile.ROCK:
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						tiles[i][j] = Tile.Type.ROCK_FILL;
					}
				}
				for(int j = 0; j < tiles[0].length; j++){
					if(length) tiles[0][j] = Tile.Type.ROCK_EDGE_V;
					else{
						tiles[0][j] = Tile.Type.ROCK_EDGE_L;
						tiles[tiles.length - 1][j] = Tile.Type.ROCK_EDGE_R;
					}
				}
				if(length) tiles[0][0] = Tile.Type.ROCK_TRI_B;
				else{
					for(int i = 0; i < tiles.length; i++){
						if(tiles[i][0] == Tile.Type.ROCK_FILL) tiles[i][0] = Tile.Type.ROCK_EDGE_B;
						else{
							if(i == 0) tiles[i][0] = Tile.Type.ROCK_EXT_BL;
							else if(i == tiles.length - 1) tiles[i][0] = Tile.Type.ROCK_EXT_BR;
						}
					}
				}
				break;
			case Tile.PMETAL:
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						tiles[i][j] = Tile.Type.PMETAL_FILL;
					}
				}
				for(int j = 0; j < tiles[0].length; j++){
					if(length) tiles[0][j] = Tile.Type.PMETAL_EDGE_V;
					else{
						tiles[0][j] = Tile.Type.PMETAL_EDGE_L;
						tiles[tiles.length - 1][j] = Tile.Type.PMETAL_EDGE_R;
					}
				}
				if(length) tiles[0][0] = Tile.Type.PMETAL_TRI_B;
				else{
					for(int i = 0; i < tiles.length; i++){
						if(tiles[i][0] == Tile.Type.PMETAL_FILL) tiles[i][0] = Tile.Type.PMETAL_EDGE_B;
						else{
							if(i == 0) tiles[i][0] = Tile.Type.PMETAL_EXT_BL;
							else if(i == tiles.length - 1) tiles[i][0] = Tile.Type.PMETAL_EXT_BR;
						}
					}
				}
				break;
			case Tile.SHIP:
				for(int i = 0; i < tiles.length; i++){
					for(int j = 0; j < tiles[i].length; j++){
						tiles[i][j] = Tile.Type.SHIP_FILL;
					}
				}
				for(int j = 0; j < tiles[0].length; j++){
					if(length) tiles[0][j] = Tile.Type.SHIP_EDGE_V;
					else{
						tiles[0][j] = Tile.Type.SHIP_EDGE_L;
						tiles[tiles.length - 1][j] = Tile.Type.SHIP_EDGE_R;
					}
				}
				if(length) tiles[0][0] = Tile.Type.SHIP_TRI_B;
				else{
					for(int i = 0; i < tiles.length; i++){
						if(tiles[i][0] == Tile.Type.SHIP_FILL) tiles[i][0] = Tile.Type.SHIP_EDGE_B;
						else{
							if(i == 0) tiles[i][0] = Tile.Type.SHIP_EXT_BL;
							else if(i == tiles.length - 1) tiles[i][0] = Tile.Type.SHIP_EXT_BR;
						}
					}
				}
				break;
		}
	}
	
	public void toggleClosing(){
		closing = !closing;
	}
	
	public void setClose(){
		closing = true;
	}
	
	public void open(){
		closing = false;
	}
	
	@Override
	public void tickOverride(){
		if(closing && getHeight() < fullHeight){
			double add = 1;
			if(add + getHeight() > fullHeight) setHeight(fullHeight);
			else addHeight(add);
			if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.DOOR_OPEN);
		}
		else if(!closing && getHeight() > minHeight){
			double sub = -1;
			if(sub + getHeight() < minHeight) setHeight(minHeight);
			else addHeight(sub);
			if(shouldRender(containerLevel.getCameraX(), containerLevel.getCameraY())) containerLevel.getEffectsPlayer().playSound(Sounds.DOOR_OPEN);
		}
	}
	
	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[0].length; j++){
				double yy = getScreenY(y) + getHeight() - (j + 1) * TILE_SIZE;
				if(yy >= getScreenY(y) - TILE_SIZE) tiles[i][j].drawTile(g, getScreenX(x) + i * TILE_SIZE, yy);
			}
		}
	}
	
	@Override
	public void activate(){
		toggleClosing();
	}
	
}
