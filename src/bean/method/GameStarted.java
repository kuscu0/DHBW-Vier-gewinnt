package bean.method;

public class GameStarted {
    private static final String METHOD = "game starting";

    private final String method = METHOD;
    private final String clientId;
    private final String gameId;
    private final int[][] board;

    public GameStarted(final String clientId, final String gameId, final int[][]board) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.board= board;
    }

    public String getMethod() {
        return method;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGameId() {
        return gameId;
    }
    
    public int[][] getBoard() {
        return board;
    }

}
