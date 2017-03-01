package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	public JPanel contentPane;
	public JTextField usuario;
	public MailCenter mailCenter;

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 125);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCorreo = new JLabel("Correo");
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCorreo.setBounds(12, 27, 56, 16);
		contentPane.add(lblCorreo);
		
		usuario = new JTextField();
		usuario.setBounds(88, 25, 229, 22);
		contentPane.add(usuario);
		usuario.setColumns(10);
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(usuario.getText() != "" && usuario.getText().contains("@")){
					mailCenter = new MailCenter();
					mailCenter.setVisible(true);
					mailCenter.setUsuario(usuario.getText());
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(null, "Debe ingresar un usuario valido");
				}
			}
		});
		btnIngresar.setBounds(360, 24, 97, 25);
		contentPane.add(btnIngresar);
	}
}
