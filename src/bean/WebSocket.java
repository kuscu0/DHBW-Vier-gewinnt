package bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.*;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.google.gson.Gson;
import com.google.gson.*;

@ServerEndpoint("/socket")
public class WebSocket {
	private static Set<Session> peers = new HashSet<>();
	
	

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException {

		// Print the client message for testing purposes
		System.out.println("Received: " + message);

		// Send the first message to the client
		session.getBasicRemote().sendText("This is the first server message");

		// Send 3 messages to the client every 5 seconds
		int sentMessages = 0;
		while (sentMessages < 3) {
			Thread.sleep(5000);
			session.getBasicRemote().sendText("This is an intermediate server message. Count: " + sentMessages);
			sentMessages++;
		}

		// Send a final message to the client
		session.getBasicRemote().sendText("This is the last server message");
		
		
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

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client connected");
		System.out.println(session.getId() + "has opened a connection");

		try {
			session.getBasicRemote().sendText("Connection established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		peers.add(session);
		System.out.println("My peers: " + peers);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Connection closed");
		peers.remove(session);
		System.out.println("Session " + session.getId() + " has ended");
	}

	public void sendMesage(String message) throws IOException {
		for (Session peer : peers) {
			peer.getBasicRemote().sendText(message);
		}
	}
}