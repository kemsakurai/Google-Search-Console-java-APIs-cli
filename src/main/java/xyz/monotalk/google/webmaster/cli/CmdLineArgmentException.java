package xyz.monotalk.google.webmaster.cli;

/**
 * {@summary コマンドライン引数に関する例外クラスです。}
 */
public class CmdLineArgmentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     * {@summary デフォルトコンストラクタです。}
     */
    public CmdLineArgmentException(final String msg) {
        super(msg);
    }
}
