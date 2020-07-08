package bean.method;

public class GameLost {
    private static final String METHOD = "game lost";

    private final String method = METHOD;
    private final String gameId;

    public GameLost(String gameId) {
        this.gameId = gameId;
    }

    public String getMethod() {
        return method;
    }

    public String getGameId() {
        return gameId;
    }
}
