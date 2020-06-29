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
import bean.method.TurnTaken;
import bean.method.NewGame;

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

        if (!json.isJsonObject()) {
            error("not an object", json);
            return;
        }

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonElement methodJson = jsonObject.get("method");

        if (methodJson == null || !methodJson.isJsonPrimitive()) {
            error("method", methodJson);
            return;
        }

        final JsonPrimitive methodPrimitive = methodJson.getAsJsonPrimitive();

        if (!methodPrimitive.isString()) {
            error("method", methodPrimitive);
            return;
        }

        final String method = methodPrimitive.getAsString();
        final String response;

        switch (method) {
        case "connect":
            response = handleConnect();
            break;
        case "new game":
            response = handleNewGame();
            break;
        case "make turn":
            response = handleTurn(jsonObject);
            break;
        default:
            error("method", method);
            return;
        }

        session.getBasicRemote().sendText(response);
    }

    private String handleConnect() {
        final String clientId = UUID.randomUUID().toString();
        final ConnectionEstablished json = new ConnectionEstablished(clientId);
        final String response = gson.toJson(json);
        return response;
    }
    
    private String handleNewGame() {
        final String gameId = UUID.randomUUID().toString();
        final NewGame json = new NewGame(gameId);
        final String response = gson.toJson(json);
        return response;
    }
    
    private String handleTurn(JsonObject jsonObject) {
        final JsonElement gameIdJson = jsonObject.get("gameId");
        if (gameIdJson == null || !gameIdJson.isJsonPrimitive()) {
            error("method", gameIdJson);
            return "";
        }
        
        final TurnTaken json = null;//new TurnTaken(gameIdJson, );
        final String response = gson.toJson(json);
        return response;
    }

    @OnClose
    public void onClose(Session session) {
        // peers.remove(session);
        System.out.println("Session " + session.getId() + " has ended");
    }

    private static void error(final String message, final Object json) {
        System.out.println("Malformed request (" + message + "): " + json);
    }
}