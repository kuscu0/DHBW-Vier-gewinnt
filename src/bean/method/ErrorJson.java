package bean.method;

/**
 * The json class for a error that has occurred.
 * 
 * @author Fabian Dittebrand.
 *
 */
public class ErrorJson {
    private static final String METHOD = "error";

    private final String method = METHOD;
    private final String errorMessage;
    
    public ErrorJson(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMethod() {
        return method;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
