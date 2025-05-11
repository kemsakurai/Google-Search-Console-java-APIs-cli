package xyz.monotalk.google.webmaster.cli;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * WebmastersコマンドランナークラスはコマンドラインからのGoogle Search Console APIの
 * 操作を実行するためのエントリーポイントを提供します。
 */
@Component
public class WebmastersCommandRunner {

    /**
     * ロガーインスタンス。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebmastersCommandRunner.class);

    /**
     * コマンドの接頭辞。
     */
    private static final String COMMAND_PREFIX = "webmasters.";

    /**
     * 設定のパッケージ名。
     */
    private static final String CONFIG_PKG = "xyz.monotalk.google.webmaster.cli.subcommands";

    /**
     * コマンドの接尾辞。
     */
    private static final String COMMAND_SUFFIX = "Command";

    /**
     * Spring ApplicationContext。
     */
    private final ApplicationContext context;

    /**
     * コンストラクタ。
     *
     * @param context アプリケーションコンテキスト
     */
    public WebmastersCommandRunner(final ApplicationContext context) {
        this.context = context;
    }

    /**
     * コマンドを実行します。
     *
     * @param command 実行するコマンド
     * @param args コマンドライン引数
     * @throws CmdLineArgmentException コマンドライン引数が不正な場合
     */
    public void run(final String command, final String... args) {
        validateCommand(command);

        try {
            final Command cmd = createCommand(command);
            parseArguments(cmd, args);
            cmd.execute();
        } catch (CmdLineArgmentException | CommandLineInputOutputException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(e.getMessage(), e);
            }
            throw e;
        }
    }

    /**
     * コマンドを検証します。
     *
     * @param command 検証するコマンド
     * @throws CmdLineArgmentException コマンドが無効な場合
     */
    private void validateCommand(final String command) {
        if (command == null || !command.startsWith(COMMAND_PREFIX)) {
            throw new CmdLineArgmentException("Invalid command format. Command must start with 'webmasters.'");
        }
    }

    /**
     * コマンドオブジェクトを生成します。
     *
     * @param command コマンド文字列
     * @return 生成されたコマンドオブジェクト
     * @throws CmdLineArgmentException コマンドの生成に失敗した場合
     */
    private Command createCommand(final String command) {
        try {
            final String className = buildClassName(command);
            return instantiateCommand(className);
        } catch (ClassNotFoundException e) {
            throw new CmdLineArgmentException("Command not found: " + command, e);
        }
    }

    /**
     * コマンドのクラス名を生成します。
     *
     * @param command コマンド文字列
     * @return 完全修飾クラス名
     */
    private String buildClassName(final String command) {
        return CONFIG_PKG + "." 
            + command.substring(COMMAND_PREFIX.length()).replace(".", ".") 
            + COMMAND_SUFFIX;
    }

    /**
     * コマンドをインスタンス化します。
     *
     * @param className クラス名
     * @return インスタンス化されたコマンド
     * @throws ClassNotFoundException クラスが見つからない場合
     * @throws CmdLineArgmentException インスタンス化に失敗した場合
     */
    private Command instantiateCommand(final String className) throws ClassNotFoundException {
        try {
            final Class<?> cmdClass = Class.forName(className);
            final AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
            return (Command) factory.createBean(cmdClass);
        } catch (ClassCastException e) {
            throw new CmdLineArgmentException("Invalid command class: " + className, e);
        }
    }

    /**
     * コマンドライン引数を解析します。
     *
     * @param cmd コマンドオブジェクト
     * @param args コマンドライン引数
     * @throws CmdLineArgmentException 引数の解析に失敗した場合
     */
    private void parseArguments(final Command cmd, final String... args) {
        final CmdLineParser parser = new CmdLineParser(cmd);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new CmdLineArgmentException(e.getMessage(), e);
        }
    }
}
