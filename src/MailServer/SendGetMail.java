package MailServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

public class SendGetMail {
	
	Socket cliente;
	
	public SendGetMail(Socket cliente){
		this.cliente = cliente;
	}

	public void run(){
		try {
			method();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void method() throws ClassNotFoundException, IOException{
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
        try {
        	out = new ObjectOutputStream(cliente.getOutputStream());
			in = new ObjectInputStream(cliente.getInputStream());
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
        
        ClientMailAddress client = (ClientMailAddress) in.readObject();//obtengo el cliente
        File file = new File(client.getPath());
        ArrayList<String> mail = new ArrayList<String>();
        if (file.exists()){
        	
        	File[] files = file.listFiles();//obtengo todos los files
        	for (int i = 0; i < files.length; i++ ){
        		FileInputStream fis;
    			byte[] data = new byte[(int) files[i].length()];
    			try {
    				fis = new FileInputStream(files[i]);
    				fis.read(data);
    				fis.close();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			String str = "";
    			try {
    				str = new String(data, "UTF-8");
    			} catch (UnsupportedEncodingException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			mail.add(str);
    			files[i].delete();//elimino el mensaje del server
        	}
        }
        
        out.writeObject(mail);
        
        try {
			out.close();
		} catch (IOException e1) {}
		try {
			in.close();
		} catch (IOException e1) {}      
		System.out.println( "comunicacion finalizada" );
	}
}
