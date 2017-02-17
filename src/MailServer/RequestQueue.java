package MailServer;
import java.net.Socket;
import java.util.ArrayList;

public class RequestQueue {
	
	private ArrayList<Socket> _queue = new ArrayList<Socket>();
	private ThreadPool _pool;
	
	public RequestQueue(){
		
	}
	
	public ThreadPool getThreadPool(){
		return this._pool;
	}
	
	public void setThreadPool(ThreadPool pool){
		this._pool = pool;
	}
	
	public synchronized void insert(Socket socket){
		_queue.add(socket);
		if (this._pool.getFreeThreads() > 0)
			notify();
		else
			this._pool.newPanic();
	}
	
	public synchronized Socket get(){
		if (_queue.size() == 0){
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
		return _queue.remove(0);
	}

	public synchronized Socket getForPanic(){
		if (_queue.size() != 0)
			return _queue.remove(0);
		return null;
	}
}
