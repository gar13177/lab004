import java.net.*;
import java.util.*;
import java.io.*;

public class TCPClientAPP {
    public static void main(String args[]) throws IOException {

            Scanner sc = new Scanner(System.in);
            Socket socket = null;
            DataInputStream in = null;
            DataOutputStream out = null;
            String hostName = "186.151.48.9";
            int serverSocket = 2407;

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
        

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Ingrese mensaje a servidor: ");
        String msg =  sc.nextLine();
        
        byte[] outputArray = msg.getBytes();
        out.writeInt(outputArray.length);
        out.write(outputArray);
        System.out.println("Enviado: "+msg);

        int length;
        while ((length = in.readInt()) > 0){
        	byte[] inputArray = new byte[length];
        	in.readFully(inputArray, 0, inputArray.length);
        	System.out.println("Recibido: "+ new String(inputArray));
        	
        	System.out.print("Ingrese mensaje a servidor: ");
            msg =  sc.nextLine();
            
            outputArray = msg.getBytes();
            out.writeInt(outputArray.length);
            out.write(outputArray);
            System.out.println("Enviado: "+msg);
        }
        
        sc.close();
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}