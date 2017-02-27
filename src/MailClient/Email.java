package MailClient;

import java.util.ArrayList;

public class Email {
	
	String mensaje;
	ArrayList<String> destinatarios;
	String emisor;

	
	public Email(String mensaje, ArrayList<String> destino, String emisor){
		this.mensaje = mensaje;
		this.destinatarios = destino;
		this.emisor = emisor;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ArrayList<String> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(ArrayList<String> destinatarios) {
		this.destinatarios = destinatarios;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
}
