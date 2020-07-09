package bean.method;

/**
 * The json class for a new connection.
 * 
 * @author Fabian Dittebrand
 *
 */
public class ConnectionEstablished {
    private static final String METHOD = "connection established";

    private final String method = METHOD;
    private final String clientId;

    public ConnectionEstablished(final String clientId) {
        this.clientId = clientId;
    }

    public String getMethod() {
        return method;
    }

    public String getClientId() {
        return clientId;
    }
}