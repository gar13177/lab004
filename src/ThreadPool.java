
public class ThreadPool {
	
	private RequestQueue _queue;
	private int _size,_available = 0, _available_panic;	
	
	public ThreadPool(int size,int panic_threads, RequestQueue queue){
		this._size = size;
		this._available_panic = panic_threads;	
		this._queue = queue;
	}
	
	public void init(){
		for (int i = 0; i < this._size; i++)
			new Thread(new Worker(this._queue,false)).start();
			
	}
	
	public synchronized void busyThread(){
		_available--;
	}
	
	public synchronized void freeThread(){
		_available++;
	}
	
	public synchronized int getFreeThreads(){
		//System.out.println("Threads libres: "+_available);
		return _available;
	}
	
	public synchronized void newPanic(){
		if (_available_panic > 0){
			//System.out.println("nuevo panico. Disponibles: "+_available_panic);
			_available_panic--;
			new Thread(new Worker(this._queue,true)).start();
		}
	}
	
	public synchronized void freePanicThread(){
		_available_panic++;
	}
	

}
