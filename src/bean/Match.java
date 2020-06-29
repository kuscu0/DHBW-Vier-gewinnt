package bean;

import java.util.*;

import javax.websocket.Session;

public class Match {
    private final Set<Session> players = new HashSet<>();
    private final int WHITE = 0;
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int[][] board = new int[7][6];

    public Match(Session session) {
        players.add(session);
        
        for(int column = 0; column < 7; column++) {
            for(int row = 0; row < 6; row ++) {
                board[column][row] = WHITE;
            }
        }
    }
}
