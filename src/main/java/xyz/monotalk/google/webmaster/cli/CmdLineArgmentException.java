package xyz.monotalk.google.webmaster.cli;

/**
 * CmdLineArgmentException
 */
public class CmdLineArgmentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     * @param msg
     */
    public CmdLineArgmentException(final String msg) {
        super(msg);
    }
}
