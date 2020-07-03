package bean.exception;

public class MaximumPlayersReachedException extends Exception {
    public MaximumPlayersReachedException(String message) {
        super(message);
    }
}
