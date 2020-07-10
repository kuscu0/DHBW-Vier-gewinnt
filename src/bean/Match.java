package bean;

import java.io.IOException;
import java.util.*;

import bean.method.ErrorJson;
import bean.method.TurnTaken;

/**
 * The Match class represents a created match.
 * 
 * @author Fabian Dittebrand
 *
 */
public class Match {
    private final Set<User> players = new HashSet<>();
    private final int WHITE = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int animationOffset = 2;
    private int lastChipSet = 0;
    private int[][] board = new int[6][7];
    private final String gameId;
    private final Control c = new Control();
    private boolean gameWon = false;

    /**
     * The Match is created with a User so players can't be empty. A game id has to be given.
     * @param user
     * @param gameId
     */
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

    /**
     * Adds a user to the match.
     * 
     * @param user
     */
    public void player2isJoining(User user) {
        user.setColor(YELLOW);
        players.add(user);
    }

    /**
     * Messages the other player of the given client id. Returns message, if both
     * should get the same message it can be returned at the end of a method.
     * 
     * @param clientId
     * @param message
     * @return message or error.
     */
    public String messagePartner(final String clientId, final String message) {
        System.out.println("send message was called");
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

    /**
     * Sets the chip into the board by the given column. Saves the last turn by
     * saving the color of the player in lastChipSet. Adds animation offset to the chip
     * and subtracts it again at the beginning.
     * 
     * @param column
     * @param color
     * @return Json TurnTaken.
     */
    public String setChip(final int column, final int color) {
        if (lastChipSet == color) {
            final ErrorJson errorJson = new ErrorJson("The same player can't make 2 turns in a row");
            final String error = WebSocket.gson.toJson(errorJson);
            return error;
        }
        
        eraseAnimationOffset(board);

        for (int row = 5; row >= 0; row--) {
            if (row == 5) {
                if (board[row][column] == 0) {
                    board[row][column] = color + animationOffset;
                    break;
                }
            }

            if (board[row][column] == RED || board[row][column] == YELLOW) {
                if (row == 0) {
                    final ErrorJson errorJson = new ErrorJson("The column is full");
                    final String error = WebSocket.gson.toJson(errorJson);
                    return error;
                }

                if (board[row - 1][column] == 0) {
                    board[row - 1][column] = color + animationOffset;
                    break;
                }
            }
        }

        checkWin(board);

        lastChipSet = color;
        final TurnTaken json = new TurnTaken(gameId, board);
        final String response = WebSocket.gson.toJson(json);
        return response;
    }

    /**
     * Checks if the game has been won and returns true or false; If the game is
     * won, the gameWon variable is set to true.
     * erasing animation offset and adding it again for win condition.
     * 
     * @param board
     * @return true or false
     */
    private boolean checkWin(int[][] board) {
        int[][] animatedBoardTmp = board;
        eraseAnimationOffset(board);
        
        if (checkVertical(board) || checkHorizontal(board) || checkDiagonal(board)) {
            gameWon = true;
            board = animatedBoardTmp;
            return true;
        }
        board = animatedBoardTmp;
        return false;
    }

    /**
     * Checks if the match has been won by 4 vertical touching chips of the same
     * color.
     * 
     * @param board
     * @return
     */
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

    /**
     * Checks if the match has been won by 4 horizontal touching chips of the same
     * color.
     * 
     * @param board
     * @return
     */
    private boolean checkHorizontal(int[][] board) {
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 6; row++) {
                if (board[row][col] != 0) {
                    int color = board[row][col];

                    if (board[row][col + 1] == color && board[row][col + 2] == color && board[row][col + 3] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the match has been won by 4 diagonal touching chips of the same
     * color.
     * 
     * @param board
     * @return
     */
    private boolean checkDiagonal(int[][] board) {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[row][col] != 0) {
                    int color = board[row][col];

                    if (col < 4 && board[row + 1][col + 1] == color && board[row + 2][col + 2] == color
                            && board[row + 3][col + 3] == color) {
                        return true;
                    } else if (col > 2 && board[row + 1][col - 1] == color && board[row + 2][col - 2] == color
                            && board[row + 3][col - 3] == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Erases the animation offset of the board.
     * @param board
     * @return board
     */
    private int[][] eraseAnimationOffset(int[][] board) {
        //Subtracts the animationOffset.
        for(int col = 0; col < 7; col++) {
            for(int row = 0; row < 6; row++) {
                if(board[row][col] == 3 || board[row][col] == 4) {
                    board[row][col] -=  animationOffset;
                }
            }
        }
        return board;
    }

    /**
     * Gets the board.
     * @return board array
     */
    public int[][] getFieldWithNewestChip() {
        return c.getFieldWithNewestChip();
    }

    public String getGameId() {
        return gameId;
    }

    public Set<User> getPlayers() {
        return players;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getRed() {
        return RED;
    }

    public int getYellow() {
        return YELLOW;
    }

    public int getLastSetChip() {
        return lastChipSet;
    }
}
