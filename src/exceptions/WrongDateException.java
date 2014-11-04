package exceptions;

/**
 * Created by Wilson on 04/11/2014.
 */
public class WrongDateException extends Exception {
    private String message;

    public WrongDateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
