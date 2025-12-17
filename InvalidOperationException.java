package smarthome;

public class InvalidOperationException extends Exception {

    private final String operation;
    private final String reason;

    public InvalidOperationException(String operation, String reason) {
        super("Invalid operation '" + operation + "': " + reason);
        this.operation = operation;
        this.reason = reason;
    }

    public String getOperation() {
        return operation;
    }

    public String getReason() {
        return reason;
    }
}
