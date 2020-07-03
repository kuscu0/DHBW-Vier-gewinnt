package bean;

import javax.websocket.Session;

public class User {
    private final Session session;
    private final String id;

    public User(final Session session, final String id) {
        this.id = id;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public String getId() {
        return id;
    }
}
