package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class Client extends JFrame implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private byte[] buffer;
	private JTextField input;
	private JTextPane output;
	private JButton sendButton;
	private ArrayList<String> userNames;
	private String name;
	
	public Client(){
		//Kommentar
		name = "YOU (no name choosen)";
		userNames = new ArrayList<String>();
		addKeyListener(this);
		input = new JTextField();
		input.addKeyListener(this);
		output = new JTextPane();
		sendButton = new JButton("send");
		sendButton.addActionListener(this);
		JScrollPane sp = new JScrollPane(output);
		JPanel UserPanel = new JPanel();
		JRadioButton onlineB = new JRadioButton(name, true);
		Box userBox = Box.createVerticalBox();
		userBox.add(onlineB);
		
		Box box = Box.createHorizontalBox();
		box.add(input);
		box.add(sendButton);
		setTitle("ChattieChat");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		//add(output, BorderLayout.CENTER);
		add(sp, BorderLayout.CENTER);
		add(box, BorderLayout.SOUTH);
		add(userBox, BorderLayout.EAST);
		setVisible(true);
		
		
		
		try {
			socket = new DatagramSocket();
			new ClientThread(output, socket, this).start();
			
		}catch(SocketException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Client();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		send(input.getText());
		
	}
	
	public void send(String message) {
		buffer = message.getBytes();
		InetAddress address;
		System.out.println("Sende das hier: " + new String(buffer) + " mit der laenge: " + buffer.length);
		try {
			//address = InetAddress.getLocalHost();
			address = InetAddress.getByName("smartdot.de");
			packet = new DatagramPacket(buffer, buffer.length,address, 4711);
			socket.send(packet);
			input.setText("");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void addUserName(String user) {
		userNames.add(user);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ENTER)send(input.getText());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}

class ClientThread extends Thread {
	
	JTextPane output;
	DatagramSocket socket;
	StyledDocument doc;
	Style style;
	private Client client;
	
	public ClientThread(JTextPane output, DatagramSocket socket, Client client) {
		this.output = output;
		this.socket = socket;
		this.client = client;
		doc = output.getStyledDocument();
		style = output.addStyle("styloo", null);
	}
	
	public void run(){
		while(true){
			System.out.println("in while-schleife");
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
				System.out.println("Oha da ist was angekommen");
				String message = new String(packet.getData());
				Scanner sc = new Scanner(message).useDelimiter(">");
				String tag = sc.next();
				String other = sc.next();
				other = other.concat("\n");
				tag = tag.concat(">");
				if(tag.equals("<server>")) {
					StyleConstants.setForeground(style, Color.RED);
				} else {
					StyleConstants.setForeground(style, Color.BLUE);
				}
				doc.insertString(doc.getLength(), tag, style);
				StyleConstants.setForeground(style, Color.BLACK);
				doc.insertString(doc.getLength(), other, style);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

