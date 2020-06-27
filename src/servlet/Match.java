package servlet;

import java.util.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public class Match {
    Set<HttpSession> players = new HashSet<>();

    public Match(HttpSession session) {
        players.add(session);
    }
}
