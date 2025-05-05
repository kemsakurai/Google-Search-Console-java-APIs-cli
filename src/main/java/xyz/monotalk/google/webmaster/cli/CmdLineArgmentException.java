package xyz.monotalk.google.webmaster.cli;

/**
 * {@summary コマンドライン引数に関する例外クラスです。}
 * Java 21の機能を活用して、より詳細な例外処理をサポートします。
 */
public class CmdLineArgmentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     * {@summary デフォルトコンストラクタです。}
     * 
     * @param msg エラーメッセージ
     */
    public CmdLineArgmentException(final String msg) {
        super(msg);
    }
    
    /**
     * {@summary 原因となる例外を指定するコンストラクタです。}
     * Java 21の例外処理パターンをサポートするために追加されました。
     * 
     * @param msg エラーメッセージ
     * @param cause 原因となる例外
     */
    public CmdLineArgmentException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
