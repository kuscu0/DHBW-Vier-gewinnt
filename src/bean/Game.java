package bean;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private static Game game;
    
    public static Game getInstance() {
        if(game == null) {
            game = new Game();
        }
        
        return game;
    }
    
    private final Set<Match> matches = new HashSet<>();
    private final Set<User> users = new HashSet<>();
    
    private Game() {}
    
    public void addUser(final User user) {
        users.add(user);
        System.out.println("active users" + users);
    }
    
    public void addMatch(final Match match) {
        matches.add(match);
        System.out.println("active matches" + matches);
    }
    
    public User getUser(String clientId) {
        for(User user : users) {
            if(user.getId().equals(clientId)) {
                return user;
            }
        }
        return null;
    }
    
    public Match getMatch(String gameId) {
        for(Match match : matches) {
            if(match.getGameId().equals(gameId)) {
                return match;
            }
        }
        return null;
    }    
}
