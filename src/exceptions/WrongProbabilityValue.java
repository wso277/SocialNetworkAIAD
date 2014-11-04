package exceptions;

/**
 * Created by Wilson on 04/11/2014.
 */
public class WrongProbabilityValue extends Exception {
    private String message;

    public WrongProbabilityValue(String message) {
        this.message = message;
    }
}
