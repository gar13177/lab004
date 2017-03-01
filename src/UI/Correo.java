package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import MailClient.Email;
import MailClient.SendEmail;

import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Correo extends JFrame {

	public JPanel contentPane;
	public JTextField textField;
	public String usuario;
	public SendEmail sendEmail;
	public Email newEmail;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Correo frame = new Correo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Correo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPara = new JLabel("Para");
		lblPara.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPara.setBounds(12, 13, 60, 22);
		contentPane.add(lblPara);
		
		textField = new JTextField();
		textField.setBounds(59, 15, 427, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblMensaje = new JLabel("Mensaje");
		lblMensaje.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMensaje.setBounds(12, 65, 71, 16);
		contentPane.add(lblMensaje);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(59, 94, 451, 247);
		contentPane.add(textArea);
		
		//Enviar Correo
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> destino = new ArrayList<String>();
				destino.add(textField.getText());
				
				newEmail = new Email(textArea.getText(),destino,usuario);
				System.out.println(newEmail.getDestinatarios());
				System.out.println(newEmail.getEmisor());
				System.out.println(newEmail.getMensaje());
				sendEmail = new SendEmail(newEmail);
				try {
					sendEmail.Send();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnEnviar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnEnviar.setBounds(160, 354, 97, 25);
		contentPane.add(btnEnviar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnCancelar.setBounds(292, 354, 106, 25);
		contentPane.add(btnCancelar);
	}
}
