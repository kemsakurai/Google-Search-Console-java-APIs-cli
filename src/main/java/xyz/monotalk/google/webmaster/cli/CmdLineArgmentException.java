package xyz.monotalk.google.webmaster.cli;

/**
 * コマンドライン引数に関する例外クラスです。
 */
public class CmdLineArgmentException extends Exception {

    /**
     * デフォルトコンストラクタです。
     */
    public CmdLineArgmentException() {
        super();
    }

    /**
     * 例外メッセージを指定するコンストラクタです。
     *
     * @param message 例外メッセージ。
     */
    public CmdLineArgmentException(String message) {
        super(message);
    }
}
