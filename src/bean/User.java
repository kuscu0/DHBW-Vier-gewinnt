package bean;

import javax.websocket.Session;

public class User {
    private Session session;
    private final String id;
    private int color = 0;

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
