package bean;

import java.io.IOException;
import java.util.*;

import javax.websocket.Session;

import bean.method.ErrorJson;
import bean.method.TurnTaken;
import servlet.RoundType;

public class Match {
    private final Set<User> players = new HashSet<>();
    private final int WHITE = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int lastChipSet = 0;
    private int[][] board = new int[6][7];
    private final String gameId;
    private final Control c = new Control();

    public Match(User user, String gameId) {
        this.gameId = gameId;

        user.setColor(RED);
        players.add(user);

        for (int column = 0; column < 7; column++) {
            for (int row = 0; row < 6; row++) {
                board[row][column] = WHITE;
            }
        }
    }

    public String messagePartner(final String clientId, final String message) {
        boolean succes = false;
        // sends the message to the other player;
        for (User secondPlayer : getPlayers()) {
            if (!secondPlayer.getId().equals(clientId)) {
                succes = true;
                
                try {
                    secondPlayer.getSession().getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    final ErrorJson errorJson = new ErrorJson("second Player couldnt get messsage");
                    final String error = WebSocket.gson.toJson(errorJson);
                    return error;
                }
                break;
            } else {
                continue;
            }
        }
        
        if(!succes) {
            final ErrorJson errorJson = new ErrorJson("Other User Not Found.");
            final String error = WebSocket.gson.toJson(errorJson);
            return error;
        }
        
        return message;
    }

    public void newRound(final RoundType roundType) {
        c.newRound(roundType);
    }

    public void nextRound() {
        c.nextRound();
    }

    public String setChip(final int column, final int color) {
        if(lastChipSet == color) {
            final ErrorJson errorJson = new ErrorJson("The same player can't make 2 turns in a row");
            final String error = WebSocket.gson.toJson(errorJson);
            return error;
        }
        
        for (int row = 0; row < 6; row++) {
            if (board[row][column] == RED || board[row][column] == YELLOW) {
                if (row == 5) {
                    final ErrorJson errorJson = new ErrorJson("The column is full");
                    final String error = WebSocket.gson.toJson(errorJson);
                    return error;
                }
                board[row + 1][column] = color;
                break;
            }
        }
        
        final TurnTaken json = new TurnTaken(gameId, board);
        final String response = WebSocket.gson.toJson(json);
        return response;
    }

    public int[][] getFieldWithNewestChip() {
        return c.getFieldWithNewestChip();
    }

    public int getPlayerWon() {
        return c.getPlayerWon();
    }

    public RoundType getRoundType() {
        return c.getRoundType();
    }

    public String getPlayerWonToString() {
        return c.getPlayerWonToString();
    }

    public void clearField() {
        c.clearField();
    }

    public void checkGewonnen() {
        c.checkGewonnen();
    }

    public boolean checkGewonnen(int x, int y, int chip) {
        return c.checkGewonnen(x, y, chip);
    }

    public boolean checkInARow(int x, int y, int chip, int length) {
        return c.checkInARow(x, y, chip, length);
    }

    public boolean checkInARowWithOffset(int x, int y, int chip, int length, int offset) {
        return c.checkInARowWithOffset(x, y, chip, length, offset);
    }

    public boolean checkHorizontal(int x, int y, int chip, int length, int offset) {
        return c.checkHorizontal(x, y, chip, length, offset);
    }

    public boolean checkVertical(int x, int y, int chip, int length, int offset) {
        return c.checkVertical(x, y, chip, length, offset);
    }

    public boolean checkSidewaysRight(int x, int y, int chip, int length, int offset) {
        return c.checkSidewaysRight(x, y, chip, length, offset);
    }

    public boolean checkSidewaysLeft(int x, int y, int chip, int length, int offset) {
        return c.checkSidewaysLeft(x, y, chip, length, offset);
    }

    public String getGameId() {
        return gameId;
    }

    public Set<User> getPlayers() {
        return players;
    }

    public void addPlayer(User user) {
        user.setColor(YELLOW);
        players.add(user);
    }
}
