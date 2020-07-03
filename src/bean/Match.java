package bean;

import java.util.*;

import javax.websocket.Session;

public class Match {
    private final Set<User> players = new HashSet<>();
    private final int WHITE = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private int[][] board = new int[7][6];
    private final String gameId;

    public Match(User user, String gameId) {
        this.gameId = gameId;
        
        players.add(user);
        
        for(int column = 0; column < 7; column++) {
            for(int row = 0; row < 6; row ++) {
                board[column][row] = WHITE;
            }
        }
    }

    public String getGameId() {
        return gameId;
    }
    
    public Set<User> getPlayers() {
        return players;
    }
}
