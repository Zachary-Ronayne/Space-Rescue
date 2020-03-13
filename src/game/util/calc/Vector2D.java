package game.util.calc;

import game.util.IDAssigner;

public class Vector2D{
	
	private static final IDAssigner UUID_ASSIGNER = new IDAssigner(0);
	
	private int UUID;
	
	/**
	 * The magnitude of this vector, will always be a positive number
	 */
	private double amount;
	/**
	 * The direction of this vector, in degrees, not radians, always stored as 0 <= angle < 360
	 */
	private double angle;
	
	/**
	 * @param amount
	 * @param angle is in degrees
	 */
	public Vector2D(double amount, double angle){
		setAmount(amount);
		setAngleDegrees(angle);
		
		UUID = UUID_ASSIGNER.next();
	}
	
	/**
	 * 
	 * @param amount
	 * @param angle
	 * @param degrees true if angle is in degrees, false if in radians
	 */
	public Vector2D(double amount, double angle, boolean degrees){
		this(amount, angle);
		if(!degrees) setAngleDegrees(Math.toDegrees(angle));
	}
	
	/**
	 * @return The distance this vector is on the x axis
	 */
	public double getX(){
		return amount * Math.cos(Math.toRadians(angle));
	}
	
	/**
	 * @return The distance this vector is on the y axis
	 */
	public double getY(){
		return amount * Math.sin(Math.toRadians(angle));
	}
	
	public double getAmount(){
		return amount;
	}
	
	public void setAmount(double amount){
		if(amount < 0){
			amount = -amount;
			addAngleDegrees(180);
		}
		this.amount = amount;
	}
	
	public void addAmount(double amount){
		setAmount(this.amount + amount);
	}
	
	/**
	 * @return The angle of this vector in degrees
	 */
	public double getDegrees(){
		return angle;
	}
	
	/**
	 * @return The angle of this vector in radians
	 */
	public double getRadians(){
		return Math.toRadians(angle);
	}
	
	/**
	 * @param angle The angle in degrees this vector will be set to
	 */
	public void setAngleDegrees(double angle){
		if(angle >= 0) this.angle = angle % 360;
		else this.angle = angle % 360 + 360;
	}
	
	/**
	 * @param angle The angle in radians this vector will be set to
	 */
	public void setAngleRadians(double angle){
		setAngleDegrees(Math.toDegrees(angle));
	}
	
	/**
	 * @param angle Add this amount to the angle of this vector
	 */
	public void addAngleDegrees(double angle){
		setAngleDegrees(this.angle + angle);
	}
	
	/**
	 * @param angle Add this amount to the angle of this vector
	 */
	public void addAngleRadians(double angle){
		addAngleDegrees(Math.toDegrees(angle));
	}
	
	public int getUUID(){
		return UUID;
	}
	
	/**
	 * @param v1
	 * @param v2
	 * @return The vector of v1 and v2 added together
	 */
	public static Vector2D addVectors(Vector2D v1, Vector2D v2){
		double x = v1.getX() + v2.getX();
		double y = v1.getY() + v2.getY();
		double m = Math.sqrt(x * x + y * y);
		double a = Math.toDegrees(Math.atan2(y, x));
		return new Vector2D(m, a);
	}
	
}
