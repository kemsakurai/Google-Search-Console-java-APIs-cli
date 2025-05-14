package xyz.monotalk.google.webmaster.cli;

/**
 * カスタム例外クラス。
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ。
     *
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public CustomException(final String message, final Throwable cause) {
        super(message, cause);
    }
}