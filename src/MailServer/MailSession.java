package MailServer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.UUID;

public class MailSession {

	private final String DOMINIO = "mi_dominio";
	
	private File file;
	private PrintWriter writer;
	private String fileName;
	private String data;
	private int data_len;

	private String msg;
	private int status;
	private int rcptCount;
	private ClientMailAddress fromAddress;
	private HashSet<ClientMailAddress> toAddress;
	
	
	public MailSession( ){
		this.status = 1;
		this.data_len = 0;
		this.rcptCount = 0;	
	}
	
	private int ProcessHELO ( String buf, int len ){
		System.out.println("HELO recibido");
		//buf = buf.substring(5);//lectura HELO_
		
		this.status = 2;//indicamos que estamos en helo
		if ( fromAddress == null ) fromAddress = new ClientMailAddress();
		this.fromAddress.setAddress("");
		this.rcptCount = 0;
		CreateNewMessage();
		return SendResponse(250); 
	}
	
	private int ProcessRCPT ( String buf, int len ){ 
		
		if ( this.status != 2 ) return SendResponse(503);//no se ha hecho HELO
		
		//if ( this.rcptCount > MAX_COUNT ) return SendResponse(552);
		String addr = buf.substring(buf.indexOf('<')+1, buf.indexOf('>'));
		
		if ( !ClientMailAddress.AddressValid(addr) ) return SendResponse(501);//problemas con addr
		
		ClientMailAddress rcpt = new ClientMailAddress(addr);
		
		System.out.println (String.format("RCPT [%s] User: [%s] Domain: [%s]",rcpt.getAddress(), rcpt.getUser(), rcpt.getDomain()));
		
		File f = new File("server/"+rcpt.getDomain().toLowerCase());
		if ( f.isDirectory() ){
			f = new File("server/"+rcpt.getDomain().toLowerCase()+"/"+rcpt.getUser().toLowerCase());
			if ( !f.isDirectory() ){
				System.out.println("User no se encuentra en este dominio");
				return SendResponse(550);//responder 550
			}
		}else{
			return SendResponse(551);//no se encuentra este dominio
			
		}
		
		if ( this.toAddress == null ) this.toAddress = new HashSet<ClientMailAddress>();
		
		this.toAddress.add(rcpt);
		this.rcptCount++;
		
		return SendResponse(250); 
	}
	
	private int ProcessMAIL ( String buf, int len ){ 
		if ( this.status != 2 ) return SendResponse(503);//no se ha hecho HELO
		
		String addr = buf.substring(buf.indexOf('<')+1, buf.indexOf('>'));
		
		System.out.println (String.format("FROM [%s]",addr));
		
		if ( !ClientMailAddress.AddressValid(addr) ) return SendResponse(501);//problemas con addr
		
		this.fromAddress.setAddress(addr);
		return SendResponse(250);
	}
	
	private int ProcessRSET ( String buf, int len ){
		System.out.println("RSET");
		this.rcptCount = 0;
		this.status = 2;
		
		File f = new File(this.fileName);
		if ( f.isFile() ) f.delete();
		
		this.fileName = null;
		
		CreateNewMessage();
				
		return SendResponse(220); 
	}
	
	private int ProcessNOOP ( String buf, int len ){
		System.out.println("NOOP");
		return SendResponse(220); 
	}
	
	private int ProcessQUIT ( String buf, int len ){
		System.out.println("Sesion terminada");
		return SendResponse(221); 
	}
	
	private int ProcessDATA ( String buf, int len ){ 
		if ( this.status != 16 ) {
			this.status = 16;
			return SendResponse(354);
		}
		
		if ( buf.contains("finaldelmensaje..") ){//"\r\n.\r\n"
			System.out.println("Fin de mensaje");
			this.status = 32;//final de data
			writer.close();
			writer = null;
			return ProcessDATAEnd();
		}
		
		if ( writer == null )
			try {
				writer = new PrintWriter( file.getName(), "UTF-8" );
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if ( writer != null ) writer.println(buf);
		
		return 220;//O return 250??
		
	}
	
	private int ProcessNotImplemented ( boolean param ){
		if ( param ) return SendResponse(504);
		return SendResponse(502); 
	}
	
	public int ProcessCMD ( String buf, int len ){
		if ( this.status == 16 ) return ProcessDATA( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("HELO")) return ProcessHELO( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("EHLO")) return ProcessHELO( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("MAIL")) return ProcessMAIL( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("RCPT")) return ProcessRCPT( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("DATA")) return ProcessDATA( buf, len );
		else if ( buf.substring(0, 4).toUpperCase().equals("QUIT")) return ProcessQUIT( buf, len );
		else return ProcessNotImplemented(false);
	}
	
	public int SendResponse ( int responseType ){
		int len;
		
		switch ( responseType ){
			case 220:
				msg = String.format("220 %s Welcome\r\n", DOMINIO);
				break;
			case 221:
				msg = "221 Service closing transmission channel\r\n";
				break;
			case 250:
				msg = "250 OK\r\n";
				break;
			case 354:
				msg = "354 Start mail input; end width <CRLF>.<CRLF>\r\n";
				break;
			case 501:
				msg = "501 Syntax error in parameters or arguments\r\n";
				break;
			case 502:
				msg = "502 Command not implemented\r\n";
				break;
			case 503:
				msg = "503 Bad sequence of commands\r\n";
				break;
			case 550:
				msg = "550 No such user\r\n";
				break;
			case 551:
				msg = "551 User not local. Can not forward the mail\r\n";
				break;
			default:
				msg = String.format("%d No description\r\n", responseType);
				break;
		}
		
		len = msg.length();
		System.out.println("Enviando: "+msg);
		
		//Falta implementar send
		//send(this.socket, msg, len, 0);
		
		return responseType;
	}
	public int ProcessDATAEnd ( ){
		this.status = 1;
		File f;
		String name;		
		
		for ( ClientMailAddress c: this.toAddress ){
			System.out.println("Sending to "+c.getAddress()+" "+c.getPath());
			 
			name = c.getPath()+"/"+UUID.randomUUID().toString()+".eml";		
			
			while ( (f = new File(name)).exists() )
				name = c.getPath()+"/"+UUID.randomUUID().toString()+".eml";
			
			try {
				Files.copy(file.toPath(), f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error: ");e.printStackTrace();
			}
			
		}
		
		file.delete();
		file = null;
		
		return SendResponse(250); 
	}
	
	public boolean CreateNewMessage() { 
		String name = UUID.randomUUID().toString()+".eml";		
		
		while ( (file = new File(name)).exists() )
			name = UUID.randomUUID().toString()+".eml";
				
		return true;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getData_len() {
		return data_len;
	}

	public void setData_len(int data_len) {
		this.data_len = data_len;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRcptCount() {
		return rcptCount;
	}

	public void setRcptCount(int rcptCount) {
		this.rcptCount = rcptCount;
	}

	public ClientMailAddress getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(ClientMailAddress fromAddress) {
		this.fromAddress = fromAddress;
	}

	public HashSet<ClientMailAddress> getToAddress() {
		return toAddress;
	}

	public void setToAddress(HashSet<ClientMailAddress> toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getMessage(){
		return this.msg;
	}
	
}
