package bean.method;

/**
 * The json class for a won game.
 * 
 * @author Fabian Dittebrand
 *
 */
public class GameWon {
    private static final String METHOD = "game won";

    private final String method = METHOD;
    private final String gameId;

    public GameWon(String gameId) {
        this.gameId = gameId;
    }

    public String getMethod() {
        return method;
    }

    public String getGameId() {
        return gameId;
    }
}
