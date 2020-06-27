package bean;

import java.io.IOException;
import java.util.UUID;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import bean.method.ConnectionEstablished;

@ServerEndpoint("/socket")
public class WebSocket {
	// private static Set<Session> peers = new HashSet<>();
	private static final Gson gson = new Gson();
	private static final JsonParser parser = new JsonParser();

	@OnOpen
	public void onOpen(Session session) throws IOException {
		System.out.println("Session " + session.getId() + " has opened");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException {
		final JsonElement json = parser.parse(message);
		
		if(!json.isJsonObject()) {
			error("not an object", json);
			return;
		}
		
		final JsonObject jsonObject = json.getAsJsonObject();
		final JsonElement methodJson = jsonObject.get("method");
		
		if(methodJson == null || !methodJson.isJsonPrimitive()) {
			error("method", methodJson);
			return;
		}
		
		final JsonPrimitive methodPrimitive = methodJson.getAsJsonPrimitive();
		
		if(!methodPrimitive.isString()) {
			error("method", methodPrimitive);
			return;
		}
		
		final String method = methodPrimitive.getAsString();
		final String response;
		
		switch(method) {
		case "connect":
			response = handleConnect();
			break;
		default:
			error("method", method);
			return;
		}
		
		session.getBasicRemote().sendText(response);
		
		
		

//		// Print the client message for testing purposes
//		System.out.println("Received: " + message);
//
//		// Send the first message to the client
//		session.getBasicRemote().sendText("This is the first server message");
//
//		// Send 3 messages to the client every 5 seconds
//		//int sentMessages = 0;
//		while (peers.size() > 0) {
//			Thread.sleep(5000);
//			Service.broadcast(peers);
//			//session.getBasicRemote().sendText("This is an intermediate server message. Count: " + sentMessages);
//			//sentMessages++;
//		}
//
//		// Send a final message to the client
//		session.getBasicRemote().sendText("This is the last server message");

//		//TODO Look for errors
//		JsonParser parser = new JsonParser();
//		JsonObject obj = parser.parse(message).getAsJsonObject();
//		String type = obj.get("type").getAsString();
//		

//		switch(type) {
//		case "newGame":
//			System.out.println("new game was started");
//			break;
//		case "turn":
//			System.out.println("turn was being made.");
//			break;
//		default:
//			System.out.println("Error no explicit type");
//		}
	}
	
	private String handleConnect() {
		final String clientId = UUID.randomUUID().toString();
		final ConnectionEstablished json = new ConnectionEstablished(clientId);
		final String response = gson.toJson(json);
		return response;
	}

	@OnClose
	public void onClose(Session session) {
		// peers.remove(session);
		System.out.println("Session " + session.getId() + " has ended");
	}

//	public void sendMesage(String message) throws IOException {
//      peers.stream().forEach(peer -> peer.getBasicRemote().sendText(message));
//		for (Session peer : peers) {
//			peer.getBasicRemote().sendText(message);
//		}1
//	}
	
	private static void error(final String message, final Object json) {
		System.out.println("Malformed request (" + message + "): " + json);
	}
}