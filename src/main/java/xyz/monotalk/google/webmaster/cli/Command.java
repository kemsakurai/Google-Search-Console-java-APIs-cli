package xyz.monotalk.google.webmaster.cli;

/**
 * CLIコマンドインターフェースです。
 */
public interface Command {

    /**
     * コマンドを実行します。
     *
     * @throws Exception コマンド実行中にエラーが発生した場合。
     */
    void execute() throws Exception;

    /**
     * コマンドの使用方法を返します。
     *
     * @return コマンドの使用方法の説明。
     */
    String usage();
}
