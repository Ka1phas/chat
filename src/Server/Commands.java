package Server;

public class Commands {

	private Server server;
	
	public Commands(Server server) {
		this.server = server;
	}
	
	public void parse(String cmd) {
		String[] cmdArr = cmd.split("-");
		String baseCMD = cmdArr[0];
		String[] params = new String[cmdArr.length - 1];
		for(int i = 1; i < cmdArr.length; i++) {
			params[i-1] = cmdArr[i];
		}
		executeCMD(baseCMD, params);
	}
	
	public void executeCMD(String cmd, String[] params) {
		if(cmd.equals("version")) {
			System.out.println("Server is currently running on version "+ server.version);
		}
	}
	
}
