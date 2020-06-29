package bean.method;

public class TurnTaken {
    private static final String METHOD = "turn taken";

    private final String method = METHOD;
    private final String gameId;
    private final Integer[][] board;
    
    public TurnTaken(final String gameId, final Integer[][] board) {
        this.gameId = gameId;
        this.board = board;
    }
}
