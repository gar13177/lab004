package MailServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GetMail {
	
	public GetMail(){
		
		int port = 2020;
        ServerSocket socket = null;

        try {
            socket = new ServerSocket(port);
            System.out.println("Puerto actual:" + port + "\nPresiona Crtl-C para terminar");
        } catch (IOException e) {
            System.err.println("Mala conexion");
            System.exit(1);
        } 
		
		while (true){
			Socket cliente = null;
	        try {
	            cliente = socket.accept();
	            
	        } catch (IOException e) {
	            System.err.println("No se puedo conectar.");
	            try{
	            	socket.close();
	            }catch (Exception f){}
	            System.exit(1);
	        }	        
		}
	}

}
