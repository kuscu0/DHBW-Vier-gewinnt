package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;
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
import bean.method.ErrorJson;
import bean.method.GameStarted;
import bean.method.TurnTaken;
import servlet.Constants;
import servlet.RoundType;
import bean.method.NewGame;

@ServerEndpoint("/socket")
public class WebSocket {
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

        final String method = parseJsonElementToString(methodJson);
        String response;
        
        switch (method) {
        case "connect":
            response = handleConnect(session);
            break;
        case "new game":
            final JsonElement clientIdJson = jsonObject.get("clientId");
            final String clientId = parseJsonElementToString(clientIdJson);

            response = handleNewGame(clientId);
            break;
        case "join game":   
            System.out.println("join game method");
            final JsonElement playerTwoClientIdJson = jsonObject.get("clientId");
            final JsonElement gameIdJson = jsonObject.get("gameId");
            
            final String gameId = parseJsonElementToString(gameIdJson);
            final String playerTwoClientId = parseJsonElementToString(playerTwoClientIdJson);

            response = handleJoinGame(playerTwoClientId, gameId);
            break;
        //case "start game":
            //response = handleGameStart(session);
           // break;
//        case "make turn":
//            response = handleTurn(jsonObject);
//            break;
        default:
            error("method", method);
            return;
        }

        session.getBasicRemote().sendText(response);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
    }

    private String handleConnect(Session session) {
        final String clientId = UUID.randomUUID().toString();
        final User user = new User(session, clientId);

        final Game game = Game.getInstance();
        game.addUser(user);

        final ConnectionEstablished json = new ConnectionEstablished(clientId);
        final String response = gson.toJson(json);
        return response;
    }

    private String handleNewGame(String clientId) {
        final Game game = Game.getInstance();
        final User user = game.getUser(clientId);
        final String gameId;

        if (user != null) {
            gameId = UUID.randomUUID().toString();
            final Match match = new Match(user, gameId);
            game.addMatch(match);
        } else {
            System.out.println("user not found error TODO !! in js abfangen");
            final ErrorJson errorJson = new ErrorJson("User not found.");
            final String error = gson.toJson(errorJson);
            return error;
        }

        final NewGame json = new NewGame(gameId);
        final String response = gson.toJson(json);
        return response;
    }
    
    private String handleJoinGame(String clientId, String gameId) throws IOException {
        System.out.println("handleJoinGame was called");
        final Game game = Game.getInstance();
        final Match match = game.getMatch(gameId);
        final User user = game.getUser(clientId);


        if (match != null) {
            for (User myUser : match.getPlayers()) {
                if (myUser.getId().equals(clientId)) {
                    final ErrorJson errorJson = new ErrorJson("Player already joined.");
          
                    
                    final String error = gson.toJson(errorJson);
                    return error;
                }
            }


            if (match.getPlayers().size() >= 2) {
                System.out.println("Max Player size reached.");
                final ErrorJson errorJson = new ErrorJson("Max players reached.");
                final String error = gson.toJson(errorJson);
                return error;
            }
            match.addPlayer(user);
            
        } else { //if match == null it print match not found
            System.out.println("Match not found.");
            final ErrorJson errorJson = new ErrorJson("Match not found.");
            final String error = gson.toJson(errorJson);
            return error;
        }

        // TODO control in match
        final Control c = new Control();
        final int[][] board = c.getFieldWithNewestChip();
        final GameStarted json = new GameStarted(clientId, gameId, board);
        final String response = gson.toJson(json);
        
        //sends the message to the other player;
        for (User secondPlayer : match.getPlayers()) {
            if (secondPlayer.getId() != clientId) { 
               secondPlayer.getSession().getBasicRemote().sendText(response);
               break;
            } else {
                final ErrorJson errorJson = new ErrorJson("Other User Not Found.");
                final String error = gson.toJson(errorJson);
                return error;
            }

        }
        return response;
    }

//    private String handleTurn(JsonObject jsonObject) {
//        final JsonElement clientId = jsonObject.get("clientId");
//        final JsonElement gameIdJson = jsonObject.get("gameId");
//        final JsonElement boardJson = jsonObject.get("column");
//        
//        if (gameIdJson == null || !gameIdJson.isJsonPrimitive()) {
//            error("method", gameIdJson);
//            return "";
//        }
//        
//        final TurnTaken json = new TurnTaken(gameIdJson, );
//        final String response = gson.toJson(json);
//        return response;
//    }

    private String parseJsonElementToString(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            error("method", element);
            return "";
        }

        final JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();

        if (!jsonPrimitive.isString()) {
            error("method", jsonPrimitive);
            return "";
        }

        final String json = jsonPrimitive.getAsString();
        System.out.println(json + "parse Element");
        return json;
    }

    private static void error(final String message, final Object json) {
        System.out.println("Malformed request (" + message + "): " + json);
    }
}