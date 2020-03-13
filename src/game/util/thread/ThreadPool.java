package game.util.thread;

import java.util.LinkedList;
import java.util.List;

import game.util.IDAssigner;

/**
 * Code from bossletsplay
 * @author Owner
 *
 */
public class ThreadPool extends ThreadGroup{

	private static IDAssigner poolID = new IDAssigner(1);
	
	private boolean alive;
	private static List<Runnable> taskQ;
	private int id;
	
	public ThreadPool(int numThreads) {
		super("ThreadPool " + poolID.next());
		this.id = poolID.getCurrent();
		setDaemon(true);
		taskQ = new LinkedList<Runnable>();
		alive = true;
		for(int i = 0; i < numThreads; i++){
			new PooledThread(this, "Pooled Thread").start();
		}
	}

	public synchronized void runTask(Runnable task){
		if(!alive) throw new IllegalStateException("Threadpool " + id + " is dead");
		if(task != null){
			taskQ.add(task);
			notify();
		}
	}
	
	public synchronized void close(){
		if(!alive) return;
		alive = false;
		taskQ.clear();
		interrupt();
	}
	
	public void join(){
		synchronized(this){
			alive = false;
			notifyAll();
		}
		
		Thread[] threads = new Thread[activeCount()];
		int cnt = enumerate(threads);
		for(int i = 0; i < cnt; i++){
			try{
				threads[i].join();
			}catch(InterruptedException e){e.printStackTrace();}
		}
	}
	
	public synchronized Runnable getTask() throws InterruptedException{
		while(taskQ.size() == 0){
			if(!alive) return null;
			wait();
		}
		return taskQ.remove(0);
	}
	
}
