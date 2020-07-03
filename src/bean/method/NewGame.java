package bean.method;

public class NewGame {
    private static final String METHOD = "game created";

    private final String method = METHOD;
    private final String gameId;
    
    public NewGame(final String gameId) {
        this.gameId = gameId;
    }


    public String getMethod() {
        return method;
    }
    
    public String getGameId() {
        return gameId;
    }
    
}
