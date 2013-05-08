package Server;

import java.io.BufferedReader;
import java.io.*;

public class CommandListener extends Thread {

	private Server server;
	
	public CommandListener(Server server) {
		this.server = server;
	}
	
	public void run() {
		while(true) {
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			String input = null;
			try {
				input = console.readLine();
				server.exec(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
