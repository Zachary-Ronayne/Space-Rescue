package game.util.thread;

import game.util.IDAssigner;

/**
 * Code from bossletsplay
 * @author Owner
 *
 */
public class PooledThread extends Thread{

	private static IDAssigner threadID = new IDAssigner(1); 
	private ThreadPool pool;
	
	public PooledThread(ThreadPool pool, String name){
		super(pool, name + " " + threadID.next());
		this.pool = pool;
	}
	
	@Override
	public void run() {
		while(!interrupted()){
			Runnable task = null;
			try {
				task = pool.getTask();
			}catch(InterruptedException e) {e.printStackTrace();}
			if(task == null) return;
			try{
				task.run();
			}catch(Throwable t){
				pool.uncaughtException(this, t);
			}
		}
	}
}
