package xyz.monotalk.google.webmaster.cli;

/**
 * CLIコマンドインターフェースです。
 */
public interface Command {

    /**
     * コマンドを実行します。
     *
     * @throws CommandLineInputOutputException 入出力エラーが発生した場合
     * @throws CmdLineArgmentException コマンドライン引数に問題がある場合
     */
    void execute();

    /**
     * コマンドの使用方法を返します。
     *
     * @return コマンドの使用方法の説明
     */
    String usage();
}
