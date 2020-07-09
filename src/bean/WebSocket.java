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
import bean.method.GameLost;
import bean.method.GameStarted;
import bean.method.GameWon;
import bean.method.TurnTaken;
import servlet.Constants;
import servlet.RoundType;
import bean.method.NewGame;

/**
 * The web socket end point for communicating with the client.
 * 
 * @author Fabian Dittebrand.
 *
 */
@ServerEndpoint("/socket")
public class WebSocket {
    static final Gson gson = new Gson();
    static final JsonParser parser = new JsonParser();

    /**
     * Executes this method when the web socket is being opened.
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Session " + session.getId() + " has opened");
    }

    /**
     * Executes this method when the web socket is receiving messages and returns a message back.
     * @param message
     * @param session
     * @throws IOException
     * @throws InterruptedException
     */
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
        case "connect game":
            response = handleConnectGame(session, jsonObject);
            break;
        case "new game":
            response = handleNewGame(jsonObject);
            break;
        case "join game":
            response = handleJoinGame(jsonObject);
            break;
        case "make turn":
            response = handleTurn(jsonObject, session);
            break;
        case "check win":
            response = handleWin(jsonObject);
            break;
        default:
            error("method", method);
            return;
        }

        session.getBasicRemote().sendText(response);
    }

    /**
     * Executes this method when the web socket is closing.
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
    }

    /**
     * Connecting the user and creating the clientId as well as adding him to the game.
     * @param session
     * @return Json ConnectionEstablished or error.
     */
    private String handleConnect(Session session) {
        final String clientId = UUID.randomUUID().toString();
        final User user = new User(session, clientId);

        final Game game = Game.getInstance();
        game.addUser(user);

        final ConnectionEstablished json = new ConnectionEstablished(clientId);
        final String response = gson.toJson(json);
        return response;
    }
    
    /**
     * Connects the user to the game. Resets his session, because the web site is reloading,
     * and creating each time a new web socket session. The client Id has to be given each time,
     * for the socket to know which player is making the requests.
     * @param session
     * @param jsonObject
     * @return Json Connection Established or error.
     */
    private String handleConnectGame(Session session, JsonObject jsonObject) {
        final JsonElement clientIdJson = jsonObject.get("clientId");
        
        final String clientId = parseJsonElementToString(clientIdJson);
        
        final Game game = Game.getInstance();
        game.getUser(clientId).setSession(session);
        
        final ConnectionEstablished json = new ConnectionEstablished(clientId);
        final String response = gson.toJson(json);
        return response;
    }

    /**
     * Creates a new match with a random game id. Adds the user the to match.
     * @param jsonObject
     * @return Json new game or error.
     */
    private String handleNewGame(JsonObject jsonObject) {
        final JsonElement clientIdJson = jsonObject.get("clientId");
        final String clientId = parseJsonElementToString(clientIdJson);
        
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

    /**
     * User joins an existing game with the corresponding game id. Adds the user to the existing match.
     * Starts the game by returning json. Needs to return the board.
     * @param jsonObject
     * @return Json Game Started or error.
     * @throws IOException
     */
    private String handleJoinGame(JsonObject jsonObject) throws IOException {
        final JsonElement clientIdJson = jsonObject.get("clientId");
        final JsonElement gameIdJson = jsonObject.get("gameId");

        final String gameId = parseJsonElementToString(gameIdJson);
        final String clientId = parseJsonElementToString(clientIdJson);
        
        final Game game = Game.getInstance();
        final Match match = game.getMatch(gameId);
        final User user = game.getUser(clientId);

        if (match == null) {
            System.out.println("Match not found.");
            final ErrorJson errorJson = new ErrorJson("Match not found.");
            final String error = gson.toJson(errorJson);
            return error;
        }

        for (User myUser : match.getPlayers()) {
            if (myUser.getId().equals(clientId)) {
                final ErrorJson errorJson = new ErrorJson("Player already joined.");
                final String error = gson.toJson(errorJson);
                return error;
            }
        }

        if (match.getPlayers().size() >= 2) {
            final ErrorJson errorJson = new ErrorJson("Max players reached.");
            final String error = gson.toJson(errorJson);
            return error;
        }
        match.player2isJoining(user);

        final int[][] board = match.getFieldWithNewestChip();
        final GameStarted json = new GameStarted(clientId, gameId, board);
        final String response = gson.toJson(json);

        return match.messagePartner(clientId, response);
    }

    /**
     * Handles the turns made by the Players and resets the session for each turn.
     * Sends the response to both players by returning messagePartner().
     * @param jsonObject
     * @param session
     * @return Json TurnTaken or error. 
     */
    private String handleTurn(JsonObject jsonObject, Session session) {
        final JsonElement clientIdJson = jsonObject.get("clientId");
        final JsonElement gameIdJson = jsonObject.get("gameId");
        final JsonElement columnJson = jsonObject.get("column");
        
        String clientId = parseJsonElementToString(clientIdJson);
        String gameId = parseJsonElementToString(gameIdJson);
        int column = parseJsonElementToInt(columnJson);        
        
        if(column == -1) {
            final ErrorJson errorJson = new ErrorJson("column doesn't exist.");
            final String error = gson.toJson(errorJson);
            return error;
        }       
        
        final Game game = Game.getInstance();
        final Match match = game.getMatch(gameId);
        final User user = game.getUser(clientId);
        user.setSession(session);
        
        if(match == null) {
            System.out.println("Match not found.");
            final ErrorJson errorJson = new ErrorJson("Match not found.");
            final String error = gson.toJson(errorJson);
            return error;
        }
        
        String response = match.setChip(column, user.getColor());       
        
        return match.messagePartner(clientId, response); 
    }
    
    /**
     * Handles the Win. Is called each time after a User in a match reconnects.
     * Sends the win and lost json to the players. The Game Lost json needs to be called separately
     * and only the Game Won json is being returned, to the actual player that has won.
     * @param jsonObject
     * @return Json Game Won or an empty string which does nothing.
     */
    private String handleWin(JsonObject jsonObject) {
        final JsonElement gameIdJson = jsonObject.get("gameId");
        String gameId = parseJsonElementToString(gameIdJson);
        final JsonElement clientIdJson = jsonObject.get("clientId");
        String clientId = parseJsonElementToString(clientIdJson);
        
        final Game game = Game.getInstance();
        final Match match = game.getMatch(gameId);
        final User user = game.getUser(clientId);
        
        if(match.isGameWon()) {
            int lastChipSet = match.getLastSetChip();
            int red = match.getRed();
            int yellow = match.getYellow();
            
            final GameWon gameWon = new GameWon(gameId);
            final String wonJson = WebSocket.gson.toJson(gameWon);
            final GameLost gameLost = new GameLost(gameId);
            final String lostJson = gson.toJson(gameLost);
            
            if(lastChipSet == red && user.getColor() == red) {               
                match.messagePartner(clientId, lostJson);
                String response = wonJson;
                return response;
            }
            
            if(lastChipSet == yellow && user.getColor() == yellow) {                
                match.messagePartner(clientId, lostJson);
                String response = wonJson;
                return response;
            }
        }
        return "";
    }
    
    /**
     * Parses a Json Element to a int
     * @param element
     * @return int
     */
    private int parseJsonElementToInt(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            error("method", element);
            return -1;
        }

        final JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();

        if (!jsonPrimitive.isNumber()) {
            error("method", jsonPrimitive);
            return -1;
        }

        final int json = jsonPrimitive.getAsNumber().intValue();
        System.out.println(json + "parse Element");
        return json;
    }

    /**
     * Parses a Json Element to a String
     * @param element
     * @return String
     */
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

    /**
     * Prints out errors in the console.
     * @param message
     * @param json
     */
    private static void error(final String message, final Object json) {
        System.out.println("Malformed request (" + message + "): " + json);
    }
}