package MailServer;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Worker implements Runnable{
	
	private RequestQueue _queue;
	private boolean _isPanic;
	private Socket cliente = null;
	private MailSession session = null;
	
	public Worker(RequestQueue queue, boolean isPanic){
		this._queue = queue;
		this._isPanic = isPanic;
		this.session = new MailSession();
		//System.out.println("Nuevo Thread: "+_isPanic);
		//System.out.println(_queue+"\n");
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean condition = true;
		while (condition){
			if (_isPanic) cliente = this._queue.getForPanic();
			else {
				this._queue.getThreadPool().freeThread();
				cliente = this._queue.get();
				this._queue.getThreadPool().busyThread();
			}
			
			if (_isPanic) condition = false;
			
			if (cliente != null){
				
				try{
					method(cliente);
					cliente.close();
				}catch (Exception e){
					System.out.println(e.getMessage());
				}
				condition = true;
			}
			//if(!_isPanic) this._queue.getThreadPool().freeThread();
		
			
		}
		if (_isPanic)
			this._queue.getThreadPool().freePanicThread();
	}
	
	public void method(Socket cliente) throws IOException {
		DataOutputStream out = null;
		DataInputStream in = null;
        try {
        	out = new DataOutputStream(cliente.getOutputStream());
			in = new DataInputStream(cliente.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				out.close();
			} catch (IOException e1) {}
			try {
				in.close();
			} catch (IOException e1) {}
			return;
		}
        
        String inputLine, outputLine;
        byte[] inputArray, outputArray;
        int length;       
        boolean condition = true;
        
        while (((length = in.readInt()) > 0) && condition ){
        	inputArray = new byte[length];
        	in.readFully(inputArray, 0, inputArray.length);
        	System.out.println("Recibido: " + (inputLine = new String(inputArray)));
        	
        	if ( session.ProcessCMD(inputLine, length) == 221 ) break;
        	
        	outputLine = session.getMessage();
        	outputArray = outputLine.getBytes();
        	out.writeInt(outputArray.length);
        	out.write(outputArray);
        	System.out.println("Enviado: "+ outputLine);
        }
        
        try {
			out.close();
		} catch (IOException e1) {}
		try {
			in.close();
		} catch (IOException e1) {}      
		System.out.println( "comunicacion finalizada" );
        
	}

}
