package xyz.monotalk.google.webmaster.cli;

/**
 * CmdLineIOException
 */
public class CmdLineIOException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message エラーメッセージ
     */
    public CmdLineIOException(final String message) {
        super(message);
    }

    /**
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public CmdLineIOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause 原因となった例外
     */
    public CmdLineIOException(final Throwable cause) {
        super(cause);
    }
}
