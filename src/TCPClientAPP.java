import java.net.*;
import java.util.*;

import MailClient.Email;
import MailClient.Worker;
import UI.Login;
import UI.MailCenter;

import java.awt.EventQueue;
import java.io.*;

public class TCPClientAPP {
	public static String usuario = "";
    public static void main(String args[]) throws IOException {
    	
    	try {
			Login frame = new Login();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}