package bean;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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

import bean.exception.MatchNotFoundException;
import bean.exception.MaximumPlayersReachedException;
import bean.exception.UserNotFoundException;
import bean.method.ConnectionEstablished;
import bean.method.GameStarted;
import bean.method.TurnTaken;
import bean.method.NewGame;

@ServerEndpoint("/socket")
public class WebSocket {
    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();
    private final Set<Match> matches = new HashSet<>();
    private final Set<User> users = new HashSet<>();

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
            final JsonElement playerTwoClientIdJson = jsonObject.get("clientId");
            final JsonElement gameIdJson = jsonObject.get("gameId");
            final String gameId = parseJsonElementToString(gameIdJson);
            final String playerTwoClientId = parseJsonElementToString(playerTwoClientIdJson);
            try {
                response = handleJoinGame(playerTwoClientId, gameId);
            } catch (MaximumPlayersReachedException e) {
                response = "";
                e.printStackTrace();
            }
            break;
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
        users.add(user);
        System.out.println("active users" + users);
        final ConnectionEstablished json = new ConnectionEstablished(clientId);
        final String response = gson.toJson(json);
        return response;
    }

    private String handleNewGame(String clientId) {
        final String gameId = UUID.randomUUID().toString();
        try {
            final User user = searchForUser(clientId);
            final Match match = new Match(user, gameId);
            matches.add(match);
            System.out.println("active matches" + matches);
        } catch (UserNotFoundException ex) {
            System.out.println(ex);
            return "";
        }

        final NewGame json = new NewGame(gameId);
        final String response = gson.toJson(json);
        return response;
    }

    private String handleJoinGame(String clientId, String gameId) throws MaximumPlayersReachedException {
        try {
            final Match match = searchForMatch(gameId);
            if (match.getPlayers().size() > 2) {
                throw new MaximumPlayersReachedException(
                        "The maximum amount of players for this match has been reached.");
                //TODO do things
            }
        } catch (MatchNotFoundException ex) {
            System.out.println(ex);
            return "";
        }
        
        final GameStarted json = new GameStarted(clientId, gameId);
        final String response = gson.toJson(json);
        return response;
    }

//    private String handleTurn(JsonObject jsonObject) {
//        final JsonElement clientId = jsonObject.get("clientId");
//        final JsonElement gameIdJson = jsonObject.get("gameId");
//        final JsonElement boardJson = jsonObject.get("column");
//        //TODO initialize new Board.
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

    private User searchForUser(final String clientId) throws UserNotFoundException {
        for (User user : users) {
            System.out.println(user.getId() + "----------" + clientId);
            if (user.getId().equals(clientId)) {
                return user;
            }
        }
        throw new UserNotFoundException("The User with the id: " + clientId + " was not found.");
    }

    private Match searchForMatch(final String gameId) throws MatchNotFoundException {
        for (Match match : matches) {
            System.out.println(match.getGameId() + "----------" + gameId);
            if (match.getGameId().equals(gameId)) {
                return match;
            }
        }
        throw new MatchNotFoundException("The Match with the id: " + gameId + " was not found");
    }

    private static void error(final String message, final Object json) {
        System.out.println("Malformed request (" + message + "): " + json);
    }
}