package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.event.ActionListener;

public class MailCenter extends JFrame {
	public String usuario = "";
	

	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	/**
	 * Create the frame.
	 */
	public MailCenter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 447);
		getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(mnMenu);
		
		JMenuItem mntmBandejaDeEntrada = new JMenuItem("Bandeja de Entrada");
		mnMenu.add(mntmBandejaDeEntrada);
		
		mnMenu.addSeparator();
		
		JMenuItem mntmEnviarEmail = new JMenuItem("Enviar Email");
		mntmEnviarEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				Correo newCorreo = new Correo();
				newCorreo.setUsuario(usuario);
				newCorreo.setVisible(true);
			}
		});
		mnMenu.add(mntmEnviarEmail);
		mnMenu.addSeparator();
		
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnMenu.add(mntmSalir);
	}
	
}
