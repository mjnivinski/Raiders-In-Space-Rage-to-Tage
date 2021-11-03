package RageServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> {
	
	public GameServerUDP(int localPort) throws IOException{
		super(localPort, ProtocolType.UDP);
	}
	
	@Override
	public void processPacket(Object o, InetAddress senderIP, int senderPort) {
		
		String message = (String)o;
		
		String[] msgTokens = message.split(",");
		
		String msg = new String("");
		
		for(String s : msgTokens) {
			msg += s + ",";
		}
		
		//System.out.println(msg);
		
		if(msgTokens.length>0) {
			//case where server receives a JOIN message
			//format: join,localid
			if(msgTokens[0].compareTo("join") == 0) {
				
				System.out.println("join packet");
				
				try {
					IClientInfo ci;
					ci = getServerSocket().createClientInfo(senderIP, senderPort);
					UUID clientID = UUID.fromString(msgTokens[1]);
					addClient(ci, clientID);
					sendJoinedMessages(clientID, true);
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			//case where server receives a CREATE message
			//format: create, localid,x,y,z
			if(msgTokens[0].compareTo("create") == 0) {
				
				System.out.println("create packet");
				
				UUID clientID = UUID.fromString(msgTokens[1]);
				String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
				String[] linear = {msgTokens[5], msgTokens[6], msgTokens[7]};
				int team = Integer.parseInt(msgTokens[8]);
				sendCreateMessages(clientID,pos,linear,team);
				//sendWantsDetailsMessages(clientID);
			}
			
			//format bye,localid
			if(msgTokens[0].compareTo("bye") == 0) {
				
				System.out.println("bye packet");
				
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			
			if(msgTokens[0].compareTo("dsfr") == 0) {
				System.out.println("dsfr packet");
				
				UUID clientID = UUID.fromString(msgTokens[1]);
				String[] position = new String[] { msgTokens[2],msgTokens[3],msgTokens[4] };
				String[] linear = {msgTokens[5], msgTokens[6], msgTokens[7]};
				sendDetailsMessage(clientID, position,linear);
			}
			
			if(msgTokens[0].compareTo("move") == 0) {
				UUID clientID = UUID.fromString(msgTokens[1]);
				String[] position = new String[] { msgTokens[2],msgTokens[3],msgTokens[4] };
				String[] linear = {msgTokens[5], msgTokens[6], msgTokens[7]};
				sendMoveMessage(clientID,position,linear);
			}
			
			if(msgTokens[0].compareTo("shoot") == 0) {
				System.out.println(msg);
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendShootMessage(clientID);
			}
		}
	}
	
	
	public void sendJoinedMessages(UUID clientID, boolean success) {
		//format: join,success or join,failure
		try {
			String message = new String("join,");
			if(success) message += "success";
			else message += "failure";
			sendPacket(message,clientID);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void sendCreateMessages(UUID clientID, String[] position, String[] linear, int team) {
		//format: create,remoteId,x,y,z
		System.out.println("send create messages");
		try {
			String message=  new String("create," + clientID.toString());
			message+= "," + position[0];
			message+= "," + position[1];
			message+= "," + position[2];
			
			message+= "," + linear[0];
			message+= "," + linear[1];
			message+= "," + linear[2];
			
			message+= "," + team;
			forwardPacketToAll(message,clientID);
		}
		
		catch(IOException e) {e.printStackTrace();}
	}
	
	public void sendDetailsMessage(UUID remID, String[] position, String[] linear) {
		try {
			String message = new String("dsfr," + remID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			
			message+= "," + linear[0];
			message+= "," + linear[1];
			message+= "," + linear[2];
			sendPacket(message,remID);
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	
	public void sendWantsDetailsMessages(UUID clientID) {
		
		try {
			String message = new String("wants," + clientID.toString());
			sendPacket(message,clientID);
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	private void sendMoveMessage(UUID clientID, String[] position, String[] linear) {
		
		try {
			String message = new String("move," + clientID);
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			
			message+= "," + linear[0];
			message+= "," + linear[1];
			message+= "," + linear[2];
			
			forwardPacketToAll(message,clientID);
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	public void sendByeMessages(UUID clientID) {
		try {
			String message = new String("bye," + clientID);
			forwardPacketToAll(message, clientID);
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	private void sendShootMessage(UUID clientID) {
		try {
			String message = new String("shoot," + clientID);
			forwardPacketToAll(message,clientID);
		}
		catch(IOException e ) {e.printStackTrace();}
	}
}
