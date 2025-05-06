package xyz.monotalk.google.webmaster.cli;

/**
 * コマンドライン引数に関する例外を表します。
 *
 * <p>この例外は、無効な引数が提供された場合にスローされます。</p>
 */
public class CmdLineArgmentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     * コマンドライン引数に関する例外クラスです。
     *
     * @param msg エラーメッセージ
     */
    public CmdLineArgmentException(final String msg) {
        super(msg);
    }
    
    /**
     * コマンドライン引数に関する例外クラスです。
     * Java 21の例外処理パターンをサポートするために追加されました。
     *
     * @param msg エラーメッセージ
     * @param cause 原因となる例外
     */
    public CmdLineArgmentException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
