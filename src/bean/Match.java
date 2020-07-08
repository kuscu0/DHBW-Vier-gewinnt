package bean;

import java.io.IOException;
import java.util.*;

import bean.method.ErrorJson;
import bean.method.GameWon;
import bean.method.TurnTaken;
import servlet.RoundType;

public class Match {
    private final Set<User> players = new HashSet<>();
    private final int WHITE = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private int lastChipSet = 0;
    private int[][] board = new int[6][7];
    private final String gameId;
    private final Control c = new Control();
    public boolean gameWon = false;
    

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
    
    public void player2isJoining(User user) {
        user.setColor(YELLOW);
        players.add(user);
    }

    public String messagePartner(final String clientId, final String message) {
        System.out.println("§send message was called");
        boolean succes = false;
        // sends the message to the other player;
        for (User secondPlayer : getPlayers()) {
            if (!secondPlayer.getId().equals(clientId)) {
                succes = true;

                try {
                    System.out.println("tried to send message");
                    secondPlayer.getSession().getBasicRemote().sendText(message);
                    System.out.println("after triying");
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

        if (!succes) {
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
        if (lastChipSet == color) {
            final ErrorJson errorJson = new ErrorJson("The same player can't make 2 turns in a row");
            final String error = WebSocket.gson.toJson(errorJson);
            return error;
        }

        for (int row = 5; row >= 0; row--) {
            if (board[row][column] == RED || board[row][column] == YELLOW) {
                if (row == 0) {
                    final ErrorJson errorJson = new ErrorJson("The column is full");
                    final String error = WebSocket.gson.toJson(errorJson);
                    return error;
                }
                
                if(board[row + 1][column] == 0) {
                    board[row + 1][column] = color;
                    break;
                } else {
                    continue;
                }
            }
        }
        
        if(board[5][column] == 0) {
            board[5][column] = color;
        }

        if(checkWin(board)) {
            final GameWon json = new GameWon(gameId);
            final String response = WebSocket.gson.toJson(json);
            return response;
        }
        
        System.out.println("Turn was taken");

        lastChipSet = color;
        final TurnTaken json = new TurnTaken(gameId, board);
        final String response = WebSocket.gson.toJson(json);
        return response;
    }

    public boolean checkWin(int[][] board) {
        if(checkVertical(board) || checkHorizontal(board) || checkDiagonal(board)) {
            gameWon = true;
            return true;
        }
        return false;
    }

    private boolean checkVertical(int[][] board) {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[row][col] != 0) {
                    int color = board[row][col];

                    if (board[row + 1][col] == color && board[row + 2][col] == color && board[row + 3][col] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkHorizontal(int[][] board) {
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] != 0) {
                    int color = board[row][col];
                    
                    if(board[row][col + 1] == color && board[row][col + 2] == color && board[row][col + 3] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkDiagonal(int[][] board) {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[row][col] != 0) {
                    int color = board[row][col];
                    
                    if(col < 4 && board[row + 1][col + 1] == color && board[row + 2][col + 2] == color && board[row + 3][col + 3] == color) {
                        return true;
                    } else if(col > 2 && board[row + 1][col - 1] == color && board[row + 2][col - 2] == color && board[row + 3][col - 3] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
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
