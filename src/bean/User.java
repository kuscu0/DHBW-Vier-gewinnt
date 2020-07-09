package bean;

import javax.websocket.Session;

/**
 * Represents a User.
 * @author Fabian Dittebrand
 *
 */
public class User {
    private Session session;
    private final String id;
    private int color = 0;

    /**
     * Creates a new User with a Session so u can communicate with him.
     * Gets a unique id.
     * @param session
     * @param id
     */
    public User(final Session session, final String id) {
        this.id = id;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
    
    public void setSession(Session session) {
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
        
}
