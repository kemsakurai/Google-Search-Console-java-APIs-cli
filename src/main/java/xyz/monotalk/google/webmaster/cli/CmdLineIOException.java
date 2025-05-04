package xyz.monotalk.google.webmaster.cli;

/**
 * コマンドライン入出力例外クラスです。
 * 
 * @deprecated {@link CommandLineInputOutputException}に置き換えられました。
 */
public class CmdLineIOException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    /**
     * デフォルトコンストラクタです。
     */
    public CmdLineIOException() {
        super();
    }

    /**
     * 例外メッセージを指定するコンストラクタです。
     *
     * @param message 例外メッセージ。
     */
    public CmdLineIOException(final String message) {
        super(message);
    }

    /**
     * 例外メッセージと原因となる例外を指定するコンストラクタです。
     *
     * @param message 例外メッセージ。
     * @param cause 原因となる例外。
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
