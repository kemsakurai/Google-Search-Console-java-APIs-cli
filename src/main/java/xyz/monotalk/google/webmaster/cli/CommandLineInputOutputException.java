package xyz.monotalk.google.webmaster.cli;

import java.io.IOException;

/**
 * コマンドライン入出力例外クラスです。
 */
public class CommandLineInputOutputException extends IOException {

    /**
     * デフォルトコンストラクタです。
     */
    public CommandLineInputOutputException() {
        super();
    }

    /**
     * 例外メッセージを指定するコンストラクタです。
     *
     * @param message 例外メッセージ。
     */
    public CommandLineInputOutputException(String message) {
        super(message);
    }

    /**
     * 例外メッセージと原因となる例外を指定するコンストラクタです。
     *
     * @param message 例外メッセージ。
     * @param cause 原因となる例外。
     */
    public CommandLineInputOutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
