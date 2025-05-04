package xyz.monotalk.google.webmaster.cli;

/**
 * コマンドライン処理中の入出力に関連する例外をハンドリングするための例外クラスです。
 * ファイル操作やネットワーク通信など、入出力処理中のエラーに使用されます。
 */
public class CommandLineInputOutputException extends Exception {
    
    private static final long serialVersionUID = 1L;

    /**
     * エラーメッセージを指定して例外を生成します。
     *
     * @param message エラーメッセージ
     */
    public CommandLineInputOutputException(final String message) {
        super(message);
    }

    /**
     * エラーメッセージと原因となった例外を指定して例外を生成します。
     *
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public CommandLineInputOutputException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * 原因となった例外を指定して例外を生成します。
     *
     * @param cause 原因となった例外
     */
    public CommandLineInputOutputException(final Throwable cause) {
        super(cause);
    }
}
