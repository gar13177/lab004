import java.net.*;
import java.util.*;

import MailClient.Email;
import MailClient.Worker;

import java.io.*;

public class TCPClientAPP {
    public static void main(String args[]) throws IOException {

    	String hostName = "172.20.8.156";
        int serverSocket = 25;
        Socket socket = null;
        
        try {
            socket = new Socket(hostName, serverSocket);
        } catch (UnknownHostException e) {
            System.err.println("Direccion incorrecta");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de interfaz");
            System.exit(1);
        }
		ArrayList<String> to = new ArrayList<String>();
		to.add("willy@correo.com");
		
		Email newEmail = new Email("Esto es una prueba con el cliente ",to,"kevin@correo.com");
		System.out.println("Paso");
		Worker cliente = new Worker(newEmail,socket);
		Thread thread = new Thread(cliente);
		thread.start();
    }
}