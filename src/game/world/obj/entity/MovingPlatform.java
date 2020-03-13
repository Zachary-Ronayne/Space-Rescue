package game.world.obj.entity;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.util.calc.Vector2D;
import game.world.obj.GameObject;
import game.world.obj.Tile;

public class MovingPlatform extends GameEntity{
	
	private ArrayList<GameEntity> movedObjs;
	private ArrayList<Integer> moveTime;
	
	protected Vector2D move;
	
	protected Point2D.Double start;
	protected Point2D.Double end;
	
	protected Tile.Type[][] tiles;
	
	/**
	 * @param width the number of tiles this platform is wide
	 * @param height the number of tiles this platform is tall
	 * @param speed
	 * @param start
	 * @param end
	 */
	public MovingPlatform(int width, int height, double speed, Point2D.Double start, Point2D.Double end){
		super(start.x, start.y, width * TILE_SIZE, height * TILE_SIZE, (int)(MAX_RENDER_PRIORITY * .7), true);
		
		if(width <= 0 || height <= 0) throw new IllegalArgumentException("Width (" + width + ") and height (\" + height + \") must both be at least 1");
		
		tiles = new Tile.Type[width][height];
		createTiles();
		
		removeVector(gravity);
		
		move = new Vector2D(speed, Math.toDegrees(Math.atan2(start.y - end.y, start.x - end.x) + Math.PI));
		addVector(move);
		
		movedObjs = new ArrayList<GameEntity>();
		moveTime = new ArrayList<Integer>();
		
		this.start = start;
		this.end = end;
	}
	
	private void createTiles(){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j] = Tile.Type.PMETAL_FILL;
			}
		}
		tiles[0][0] = Tile.Type.PMETAL_EXT_TL;
		tiles[tiles.length - 1][0] = Tile.Type.PMETAL_EXT_TR;
		
		Tile.Type t = tiles[tiles.length - 1][tiles[0].length - 1];
		if(t == Tile.Type.PMETAL_FILL) tiles[tiles.length - 1][tiles[0].length - 1] = Tile.Type.PMETAL_EXT_BR;
		else tiles[tiles.length - 1][tiles[0].length - 1] = Tile.Type.PMETAL_TRI_R;
		
		t = tiles[0][tiles[0].length - 1];
		if(t == Tile.Type.PMETAL_FILL) tiles[0][tiles[0].length - 1] = Tile.Type.PMETAL_EXT_BL;
		else tiles[0][tiles[0].length - 1] = Tile.Type.PMETAL_TRI_L;
		
		for(int i = 1; i < tiles.length - 1; i++){
			tiles[i][tiles[0].length - 1] = Tile.Type.PMETAL_EDGE_B;
			if(tiles[i][0] == Tile.Type.PMETAL_FILL) tiles[i][0] = Tile.Type.PMETAL_EDGE_T;
			else tiles[i][0] = Tile.Type.PMETAL_EDGE_H;
		}
	}
	
	@Override
	protected void tickOverride(){
		super.tickOverride();
		
		if(willMove()) move.addAngleDegrees(180);
		
		for(int i = 0; i < movedObjs.size(); i++){
			GameEntity e = movedObjs.get(i);
			
			if(moveTime.get(i) <= 0){
				e.removeVector(move);
				e.resetGravity();
				movedObjs.remove(i);
				moveTime.remove(i);
				i--;
			}
			else if(moveTime.get(i) > 0) moveTime.set(i, moveTime.get(i) - 1);
		}
	}

	@Override
	protected void renderOveride(Graphics2D g, double x, double y){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				tiles[i][j].drawTile(g, getScreenX(x) + i * TILE_SIZE, getScreenY(y) + j * TILE_SIZE);
			}
		}
	}
	
	@Override
	public void topHit(GameObject obj){
		super.topHit(obj);
		if(obj instanceof GameEntity){
			GameEntity e = ((GameEntity)obj);
			int time = 3;
			if(!e.hasVector(move)){
				e.addVector(move);
				movedObjs.add(e);
				moveTime.add(time);
			}
			else{
				int i = 0;
				for(int j = 0; j < movedObjs.size(); j++){
					if(movedObjs.get(i).getUUID() == e.getUUID()){
						i = j;
						break;
					}
				}
				moveTime.set(i, time);
			}
		}
	}
	
	protected boolean willMove(){
		return getLoc().distance(start) < move.getAmount() * .5 || getLoc().distance(end) < move.getAmount() * .5;
	}
}
