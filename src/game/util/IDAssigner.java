package game.util;

public class IDAssigner {
	
	private int id;
	
	public IDAssigner(int id){
		this.id = id;
	}
	
	public int next(){
		return id++;
	}
	
	public int getCurrent(){
		return id;
	}
	
	public void set(int id){
		this.id = id;
	}
}
