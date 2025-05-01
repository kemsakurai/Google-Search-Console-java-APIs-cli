package xyz.monotalk.google.webmaster.cli;

/**
 * CmdLineIOException
 */
public class CmdLineIOException extends IllegalStateException {
    /**
     * @param message エラーメッセージ
     */
    public CmdLineIOException(String message) {
        super(message);
    }

    /**
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public CmdLineIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause 原因となった例外
     */
    public CmdLineIOException(Throwable cause) {
        super(cause);
    }
}
