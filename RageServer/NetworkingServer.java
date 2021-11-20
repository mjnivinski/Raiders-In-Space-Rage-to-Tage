package RageServer;

import java.io.IOException;
import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer {
	private GameServerUDP thisUDPServer;
	
	public NetworkingServer(int serverPort) {
		try {
			thisUDPServer = new GameServerUDP(serverPort);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length > 0) {
			NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]));
		}
	}
}
