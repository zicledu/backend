package ssac.LMS.exception;

public class CustomUsernameExistsException extends RuntimeException {
    public CustomUsernameExistsException(String message) {
        super(message);
    }
}
