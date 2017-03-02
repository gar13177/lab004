package UserAgent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

import MailServer.ClientMailAddress;

public class MailSender implements Runnable {
	
	MailSession session;
	
	
	public MailSender(){
		session = new MailSession();
	}
	
	public MailSender(MailSession ms){
		
	}
	
	public MailSender (ClientMailAddress from, HashSet<ClientMailAddress> rcpt, String data ){
		this.session = new MailSession(from, rcpt, data);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			method();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void method() throws IOException {
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        String hostName = "localhost";
        int serverSocket = 2020;

	    try {
	        socket = new Socket(hostName, serverSocket);
	        out = new DataOutputStream(socket.getOutputStream());
	        in = new DataInputStream(socket.getInputStream());
	    } catch (UnknownHostException e) {
	        System.err.println("Direccion incorrecta");
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Error de interfaz");
	        System.exit(1);
	    }
	    
	    
	    String inputLine, outputLine = "HELO\n";	    
	    byte[] outputArray = outputLine.getBytes();
	    out.writeInt(outputArray.length);
	    out.write(outputArray);//siempre se va a mandar primero el HELO
	    System.out.println("Enviado: "+outputLine);
	
	    int length;
	    
	    while ((length = in.readInt()) > 0){//aqui ya suponemos que se envio el helo
	    	byte[] inputArray = new byte[length];
	    	in.readFully(inputArray, 0, inputArray.length);
	    	System.out.println("Recibido: "+ (inputLine = new String(inputArray)));
	    	
	    	if ( session.ProcessCMD(inputLine, length) == 0 ) break;
        	
        	outputLine = session.getMessage();
	        
	        outputArray = outputLine.getBytes();
	        out.writeInt(outputArray.length);
	        out.write(outputArray);
	        System.out.println("Enviado: "+outputLine);
	    }
	    
	    out.close();
	    in.close();
	    socket.close();
        
	}	
	
}
