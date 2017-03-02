package UserAgent;

import java.util.HashSet;
import java.util.Iterator;

import MailServer.ClientMailAddress;

public class MailSession {
	
	String message, enddata = "\r\n.\r\n";
	int status;//status helo
	
	ClientMailAddress from;
	HashSet<ClientMailAddress> rcpt;
	Iterator iterRcpt;
	String data;
	
	public MailSession(){
	}
	
	public MailSession(ClientMailAddress from, HashSet<ClientMailAddress> rcpt, String data){
		this.from = from;
		this.rcpt = rcpt;
		this.data = data;
		this.status = 0;// status helo
		this.iterRcpt = rcpt.iterator();
	}
		
	public int ProcessCMD(String msg, int length){
		
		if (msg.replaceAll(" ","").startsWith("250")){// si no hubo error
			if (status == 0 ) {
				message = String.format("MAIL FROM <%S>",from.getAddress());
				status = 1;//status 1 = 
				return 1;//1 va a ser estado ok;
			} else if ( status == 1){//quiere decir que ya se mando el from correctamente
				if (iterRcpt.hasNext()){
					ClientMailAddress nextRcpt = (ClientMailAddress) iterRcpt.next();
					message = String.format("RCPT TO <%s>", nextRcpt.getAddress());
					return 1;
				}else{
					status = 2;//ya termino de enviar los destinatarios
					return ProcessCMD(msg, length);//repito la llamada pero con el nuevo codigo
				}
			} else if ( status == 2 ){//ya se terminaron de mandar los rcpt
				message = "DATA";//se indica que se va a mandar data
				return 1;
			} else if ( status == 4 ){
				message = "QUIT";
				return 1;
			}
		} else if (msg.replaceAll(" ","").startsWith("354")){//ya estoy preparado para mandar la data
			if ( status == 2 ){//puedo mandar la data
				message = data;
				status = 3;//quiere decir que ya mande la data
				return 1;//envio la data
			} else if ( status == 3 ){
				message = enddata;
				status = 4;
				return 1;
			}
		}
		
		return 0;
	}
	
	
	public String getMessage(){
		return this.message;
	}
	
}
