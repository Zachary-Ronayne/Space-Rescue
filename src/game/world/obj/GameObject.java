package game.world.obj;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import game.libs.Settings;
import game.util.IDAssigner;
import game.world.levels.Level;

public abstract class GameObject{
	
	public static final double TILE_SIZE = 16;
	
	public static final int MIN_RENDER_PRIORITY = 0;
	public static final int MAX_RENDER_PRIORITY = 10;
	
	private static final IDAssigner UUID_ASSIGNER = new IDAssigner(0);
	
	private int UUID;
	
	private double x;
	private double y;
	private double width;
	private double height;
	
	private int renderPriority;
	
	/**
	 * true if this GameObject should collide with other GameObject and move them around and if this object should be moved by other GameObject. false otherwise
	 */
	protected boolean collide;
	
	/**
	 * Value from 0-2. It is 2 if this object just hit a wall and decreases down to 0 every tick
	 */
	private int hitWallTime;
	/**
	 * Value from 0-2. It is 2 if this object just hit a ceiling and decreases down to 0 every tick
	 */
	private int hitCeilingTime;
	/**
	 * Value from 0-2. It is 2 if this object just hit a floor and decreases down to 0 every tick
	 */
	private int hitFloorTime;
	
	public GameObject(double x, double y, double width, double height, int renderPriority, boolean collide){
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		
		setRenderPriority(renderPriority);
		
		this.collide = collide;
		
		UUID = UUID_ASSIGNER.next();
	}
	
	public final void tick(){
		tickOverride();
		if(hitWallTime > 0) hitWallTime--;
		if(hitCeilingTime > 0) hitCeilingTime--;
		if(hitFloorTime > 0) hitFloorTime--;
	}

	/**
	 * Override this method to do things this object should do
	 */
	protected abstract void tickOverride();
	
	/**
	 * @param g
	 * @param x the x position of the screen
	 * @param y the y position of the screen
	 */
	public final void render(Graphics2D g, double x, double y){
		if(shouldRender(x, y)) renderOveride(g, x, y);
	}
	
	/**
	 * Override this method to render this object on the screen based on the x and y position of the camera. 
	 * If this object has different conditions as to if it should be rendered, then override the shouldRender() method.
	 * @param g
	 * @param x the x position of the screen
	 * @param y the y position of the screen
	 */
	protected abstract void renderOveride(Graphics2D g, double x, double y);
	
	/**
	 * @param x the x position of the screen
	 * @param y the y position of the screen
	 * @return true if this object will be on the screen if it is rendered, false if this object is off screen
	 */
	public boolean shouldRender(double x, double y){
		return getScreenBounds(x, y).intersects(Settings.getScreenBounds());
	}
	
	public final int getUUID(){
		return UUID;
	}
	
	public double getX(){
		return x;
	}
	
	public double getCenterX(){
		return getX() + getWidth() / 2;
	}
	
	public double getScreenCenterX(double x){
		return getScreenX(x) + getWidth() / 2;
	}
	
	/**
	 * @param x the x position of the screen
	 * @return the place this object should be rendered if it is drawn on the screen
	 */
	public double getScreenX(double x){
		return getX() + x;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void addX(double x){
		setX(this.x + x);
	}

	public double getY(){
		return y;
	}
	
	public double getCenterY(){
		return getY() + getHeight() / 2;
	}
	
	public double getScreenCenterY(double y){
		return getScreenY(y) + getHeight() / 2;
	}
	
	/**
	 * @param y the y position of the screen
	 * @return the place this object should be rendered if it is drawn on the screen
	 */
	public double getScreenY(double y){
		return getY() + y;
	}

	public void setY(double y){
		this.y = y;
	}
	
	public void addY(double y){
		setY(this.y + y);
	}
	
	public Point2D.Double getCenter(){
		return new Point2D.Double(getCenterX(), getCenterY());
	}
	
	/**
	 * @return the upper left hand corner of this object
	 */
	public Point2D.Double getLoc(){
		return new Point2D.Double(getX(), getY());
	}
	
	public void keepInLevelBounds(Level l){
		if(getX() < 0) setX(0);
		if(getY() < 0) setY(0);
		if(getX() > l.getLevelWidth() - getWidth()) setX(l.getLevelWidth() - getWidth());
		if(getY() > l.getLevelHeight() - getHeight()) setX(l.getLevelHeight() - getHeight());
	}

	public double getWidth(){
		return width;
	}

	public void setWidth(double width){
		this.width = width;
	}
	
	public void addWidth(double width){
		setWidth(getWidth() + width); 
	}
	
	public double getHeight(){
		return height;
	}

	public void setHeight(double height){
		this.height = height;
	}
	
	public void addHeight(double height){
		setHeight(getHeight() + height); 
	}
	
	public Rectangle2D.Double getBounds(){
		return new Rectangle2D.Double(x, y, width, height);
	}
	
	/**
	 * @param n
	 * @return The bounds of this object, but increased in width and height on all sides by n. Can also be used to get smaller bounds with negative n
	 */
	public Rectangle2D.Double getBiggerBounds(double n){
		return new Rectangle2D.Double(getX() - n, getY() - n, getWidth() + n * 2, getHeight() + n * 2);
	}
	
	public Point2D.Double getNormalCenter(){
		return getCenter();
	}
	
	/**
	 * @param x the x position of the screen
	 * @param y the y position of the screen
	 * @return The bounds of this object of where it should be rendered on the screen
	 */
	public Rectangle2D.Double getScreenBounds(double x, double y){
		return new Rectangle2D.Double(getScreenX(x), getScreenY(y), width, height);
	}
	
	/**
	 * @param obj
	 * @return the angle between the centers of this object and obj, in radians
	 */
	public double getAngleTo(GameObject obj){
		return Math.atan2(getCenterY() - obj.getCenterY(), getCenterX() - obj.getCenterX()) + Math.PI;
	}
	
	/**
	 * This should not be called if the objects do not inersect. Also this is a very experimental method, it is very unreliable
	 * @param obj
	 * @return The angle of where this object intersects obj
	 */
	public double getAngleSlope(GameObject obj){
		if(isLeft(obj.getBounds()) || isRight(obj.getBounds())) return 180;
		else if(isAbove(obj.getBounds()) || isBelow(obj.getBounds())) return 90;
		else return 0;
	}
	
	public boolean getCollide(){
		return collide;
	}
	
	/**
	 * Collides this object with o, this object will move, and o will not move
	 * @param r
	 */
	public void collideWith(GameObject obj){
		Rectangle2D.Double r = obj.getBounds();
		if(!r.intersects(getBounds())) return;
		
		double xDis = 0;
		double yDis = 0;
		if(isLeft(r)) xDis = getX() + getWidth() - r.x;
		else if(isRight(r)) xDis = r.x + r.width - getX();
		if(isAbove(r)) yDis = getY() + getHeight() - r.y;
		else if(isBelow(r)) yDis = r.y + r.height - getY();
		
		if(yDis < xDis){
			if(isAbove(r)){
				setY(r.y - getHeight());
				hitFloor();
				obj.topHit(this);
			}
			else if(isBelow(r)){
				setY(r.y + r.height);
				hitCeiling();
				obj.botHit(this);
			}
			if(r.intersects(getBounds())){
				if(isLeft(r)){
					setX(r.x - getWidth());
					hitWall();
					obj.sideHit(this);
				}
				else if(isRight(r)){
					setX(r.x + r.width);
					hitWall();
					obj.sideHit(this);
				}
			}
		}
		else{
			if(isLeft(r)){
				setX(r.x - getWidth());
				hitWall();
				obj.sideHit(this);
			}
			else if(isRight(r)){
				setX(r.x + r.width);
				hitWall();
				obj.sideHit(this);
			}
			if(r.intersects(getBounds())){
				if(isAbove(r)){
					setY(r.y - getHeight());
					hitFloor();
					obj.topHit(this);
				}
				else if(isBelow(r)){
					setY(r.y + r.height);
					hitCeiling();
					obj.botHit(this);
				}
			}
		}
	}
	
	protected void collideWithTiles(Level l){
		ArrayList<Tile> ts = l.getCollideTiles(getBounds());
		for(int j = 0; j < ts.size(); j++){
			if(ts.get(j).getTile().getCollide()) collideWith(ts.get(j));
		}
	}
	
	protected void collideWithObjects(Level l){
		ArrayList<GameObject> objs = l.getObjects();
		for(int i = 0; i < objs.size(); i++) if(objs.get(i).getCollide()) collideWith(objs.get(i));
	}
	
	/**
	 * @param l
	 * @return True if this object is in a solid object like a door, or a collidable tile
	 */
	public boolean inSolidObject(Level l){
		return getInSolidObject(l) != null;
	}
	
	/**
	 * @param l
	 * @return a solid object from the level l that this object is in. If this object is not in an object, returns null
	 */
	public GameObject getInSolidObject(Level l){
		ArrayList<Tile> ts = l.getCollideTiles(getBounds());
		for(int j = 0; j < ts.size(); j++){
			if(ts.get(j).getUUID() != getUUID() && ts.get(j).getTile().getCollide() && ts.get(j).getBounds().intersects(getBounds())) return ts.get(j); 
		}
		
		ArrayList<GameObject> objs = l.getObjects();
		for(int j = 0; j < objs.size(); j++){
			if(objs.get(j).getUUID() != getUUID() && objs.get(j).getCollide() && objs.get(j).getBounds().intersects(getBounds())) return objs.get(j); 
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param r
	 * @return true if this object is to the left of an object with the bounds of r, false otherwise
	 */
	public boolean isLeft(Rectangle2D.Double r){
		return getX() < r.x;
	}
	/**
	 * 
	 * @param r
	 * @return true if this object is to the right of an object with the bounds of r, false otherwise
	 */
	public boolean isRight(Rectangle2D.Double r){
		return getX() + getWidth() > r.x + r.width;
	}
	/**
	 * 
	 * @param r
	 * @return true if this object is above an object with the bounds of r, false otherwise
	 */
	public boolean isAbove(Rectangle2D.Double r){
		return getY() < r.y;
	}
	/**
	 * 
	 * @param r
	 * @return true if this object is below an object with the bounds of r, false otherwise
	 */
	
	/**
	 * Override and call supe this method if this object needs to do something when it hits a wall
	 */
	public void hitWall(){
		updateHitWall();
	}
	/**
	 * Override and call super this method if this object needs to do something when it hits a floor
	 */
	public void hitFloor(){
		updateHitFloor();
	}
	/**
	 * Override and call super this method if this object needs to do something when it hits a ceiling
	 */
	public void hitCeiling(){
		updateHitCeiling();
	}

	protected final void updateHitWall(){
		hitWallTime = 3;
	}
	protected final void updateHitFloor(){
		hitFloorTime = 3;
	}
	protected final void updateHitCeiling(){
		hitCeilingTime = 3;
	}
	
	/**
	 * Override and call super if an action needs to be performed on an object when it hits the side of this object
	 * @param obj the object that hit the side of this object
	 */
	public void sideHit(GameObject obj){}
	/**
	 * Override and call super if an action needs to be performed on an object when it hits the bottom of this object
	 * @param obj the object that hit the bottom of this object
	 */
	public void botHit(GameObject obj){}
	/**
	 * Override and call super if an action needs to be performed on an object when it hits the top of this object
	 * @param obj the object that hit the top of this object
	 */
	public void topHit(GameObject obj){}
	
	/**
	 * Value from 0-2. It is 2 if this object just hit a wall and decreases down to 0 every tick
	 */
	public int getHitWallTime(){
		return hitWallTime;
	}
	
	/**
	 * Value from 0-2. It is 2 if this object just hit a ceiling and decreases down to 0 every tick
	 */
	public int getHitCeilingTime(){
		return hitCeilingTime;
	}
	
	/**
	 * Value from 0-2. It is 2 if this object just hit a floor and decreases down to 0 every tick
	 */
	public int getHitFloorTime(){
		return hitFloorTime;
	}
	
	public boolean isBelow(Rectangle2D.Double r){
		return getY() + getHeight() > r.y + r.height;
	}
	
	public int getRenderPriority(){
		return renderPriority;
	}
	
	public void setRenderPriority(int r){
		if(r > MAX_RENDER_PRIORITY) r = MAX_RENDER_PRIORITY;
		else if(r < MIN_RENDER_PRIORITY) r = MIN_RENDER_PRIORITY;
		renderPriority = r;
	}
	
	/**
	 * Override this method if this object needs to do something when the game is paused
	 */
	public void pause(){}
	
}
