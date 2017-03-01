package MailClient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SendEmail {
	public Email newEmail;
	public SendEmail(Email newEmail){
		this.newEmail = newEmail;
	}
	public void Send()  throws IOException{
		String hostName = "localhost";
		int serverSocket = 2407;
		Socket socket = null;

		try
		{
			socket = new Socket(hostName, serverSocket);
		}catch(UnknownHostException e)
		{
			System.err.println("Direccion incorrecta");
			System.exit(1);
		}catch(IOException e)
		{
			System.err.println("Error de interfaz");
			System.exit(1);
		}
		ArrayList<String> to = new ArrayList<String>();to.add("willy@correo.com");

		Worker cliente = new Worker(newEmail, socket);
		Thread thread = new Thread(cliente);thread.start();
	}
	
}
