package bean.method;

public class GameStarted {
    private static final String METHOD = "game starting";

    private final String method = METHOD;
    private final String clientId;
    private final String gameId;

    public GameStarted(final String clientId, final String gameId) {
        this.clientId = clientId;
        this.gameId = gameId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGameId() {
        return gameId;
    }
}
