package bean;

import java.io.IOException;
import java.util.Set;

import javax.websocket.Session;

public class Service {

    public static void broadcast(Set<Session> peers) {
        for (Session peer : peers) {
            try {
                ping(peer);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error while boradcast");
            }
        }
    }

    private static void ping(Session peer) throws IOException {
        peer.getBasicRemote().sendText("ping");
    }
}
