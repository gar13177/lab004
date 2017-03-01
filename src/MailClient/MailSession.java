package MailClient;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

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
	private Email newEmail;
	
	public MailSession(Email newEmail ){
		//State 0 escribiendo mensaje
		//State 1 esperando al server
		//State 2 error
		this.status = 0;
		this.data_len = 0;
		this.rcptCount = 0;	
		this.newEmail = newEmail;
	}
	
	public int ProcessHELO(String buf, int len){
		status = 2;
		return SendResponse(220);
	}
	
	public int ProcessMAIL(String buf, int len){
		if(rcptCount >= newEmail.destinatarios.size()-1){
			status = 3;
		}
		return SendResponse(250);
	}
	
	public int ProcessRCPT(String buf, int len){
		return SendResponse(250);
	}
	
	public int ProcessDATA(String buf, int len){
		status = 4;
		return SendResponse(354);
	}
	
	public int ProcessQUIT(String buf, int len){
		return SendResponse(221);
	}
	
	private int ProcessNotImplemented ( boolean param ){
		if ( param ) return SendResponse(504);
		return SendResponse(502); 
	}
	


	public int ProcessServer ( String buf, int len ){
		if ( buf.contains("250 OK") && status == 1 ) return ProcessHELO( buf, len );
		else if (buf.contains("250 OK") && status == 2) return ProcessMAIL( buf, len );
		else if ( status == 3 && buf.contains("250") ) return ProcessRCPT( buf, len );
		else if ( buf.contains("354")) return ProcessDATA( buf, len );
		else if ( buf.contains("250") && status == 5) return ProcessQUIT( buf, len );
		else return ProcessNotImplemented(false);
	}
	
	public int SendResponse ( int responseType ){
		int len;
		
		switch ( responseType ){
			case 220:
				msg = String.format("MAIL FROM: <" + newEmail.emisor + ">" );
				break;
			case 221:
				msg = "221 Service closing transmission channel\r\n";
				break;
			case 250:
				if(rcptCount>newEmail.destinatarios.size()-1 && status == 3){
					msg = "DATA";
					status=3;
					break;
				}
				String rcp = newEmail.destinatarios.get(rcptCount);
				rcptCount += 1;
				String rcpt = "RCPT TO: <" + rcp + ">";
				msg = rcpt;
				break;
			case 354:
				msg = newEmail.mensaje;
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
	
	public String getMessage(){
		return this.msg;
	}
}
