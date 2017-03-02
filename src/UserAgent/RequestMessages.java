package UserAgent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import MailServer.ClientMailAddress;

public class RequestMessages implements Runnable {

	ClientMailAddress client;
	ArrayList<String> msgs;
	
	public RequestMessages(ClientMailAddress client){
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			method();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void method() throws IOException, ClassNotFoundException {
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        String hostName = "localhost";
        int serverSocket = 2020;

	    try {
	        socket = new Socket(hostName, serverSocket);
	        out = new ObjectOutputStream(socket.getOutputStream());
	        in = new ObjectInputStream(socket.getInputStream());
	    } catch (UnknownHostException e) {
	        System.err.println("Direccion incorrecta");
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Error de interfaz");
	        System.exit(1);
	    }
	    
	    out.writeObject(client);
	    
	    msgs = (ArrayList<String>)in.readObject();
	    
	    out.close();
	    in.close();
	    socket.close();
	}
}
