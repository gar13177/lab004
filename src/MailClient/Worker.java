package MailClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Worker implements Runnable{
	private MailSession session = null;
	private Socket cliente = null;
	private Email newEmail;
	
	public Worker(Email newEmail, Socket cliente){
		this.newEmail = newEmail;
		this.cliente = cliente;
		this.session = new MailSession(this.newEmail);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean condition = true;
		
		try{
			method();
			cliente.close();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void method() throws IOException {
		DataOutputStream out = null;
		DataInputStream in = null;
        try {
        	out = new DataOutputStream(cliente.getOutputStream());
			in = new DataInputStream(cliente.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("SIIIII");
		}
        
        String inputLine, outputLine;
        byte[] inputArray, outputArray;
        int length;       
        boolean condition = true;
        
        outputLine = "Helo";
    	session.setStatus(1);
    	outputArray = outputLine.getBytes();
    	System.out.println(outputArray);
    	out.writeInt(outputArray.length);
    	out.write(outputArray);
    	System.out.print("mensaje enviado" + outputArray.toString());
    	
        while (((length = in.readInt()) > 0) && condition ){	
        	inputArray = new byte[length];
        	in.readFully(inputArray, 0, inputArray.length);
        	System.out.println("Recibido: " + (inputLine = new String(inputArray)));
        	
        	if ( session.ProcessServer(inputLine, length) == 221 ) break;
        	
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
