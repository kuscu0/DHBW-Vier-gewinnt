package bean.method;

public class TurnTaken {
    private static final String METHOD = "turn taken";

    private final String method = METHOD;
    private final String gameId;
    private final int[][] board;
    
    public TurnTaken(final String gameId, final int[][] board) {
        this.gameId = gameId;
        this.board = board;
    }

    public String getMethod() {
        return method;
    }

    public String getGameId() {
        return gameId;
    }

    public int[][] getBoard() {
        return board;
    }
}
