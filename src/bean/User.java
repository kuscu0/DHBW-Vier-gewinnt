package bean;

import javax.websocket.Session;

public class User {
    private Session session;
    private String id;

    public User(Session session, String id) {
        this.session = session;
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}
