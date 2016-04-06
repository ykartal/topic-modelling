package tr.com.ykartal.exception;

/**
 * To throw internal warning exceptions.
 * 
 * @author Yusuf KARTAL
 *
 */
@SuppressWarnings("serial")
public class WarningException extends Exception {

    /**
     * To throw internal warning exceptions with given message.
     * 
     * @param message
     *            text about the exception
     */
    public WarningException(final String message) {
        super(message);
    }

}
