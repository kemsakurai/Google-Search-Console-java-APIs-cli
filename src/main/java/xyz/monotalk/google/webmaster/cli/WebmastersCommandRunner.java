package xyz.monotalk.google.webmaster.cli;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * WebmastersCommandRunnerは、コマンドライン引数を処理し、対応するコマンドを実行します。
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    /** ロガーインスタンスです。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebmastersCommandRunner.class);

    /** Springアプリケーションコンテキストです。 */
    @Autowired
    private ApplicationContext context;

    /**
     * サブコマンド実装オブジェクトです。
     * 引数によって実行するオブジェクトを切り替えます。
     */
    @Argument(handler = SubCommandHandler.class, metaVar = "subCommands")
    @SubCommands({
        @SubCommand(name = "webmasters.searchanalytics.query",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics.QueryCommand.class),
        @SubCommand(name = "webmasters.sitemaps.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.ListCommand.class),
        @SubCommand(name = "webmasters.sitemaps.delete",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.DeleteCommand.class),
        @SubCommand(name = "webmasters.sitemaps.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.GetCommand.class),
        @SubCommand(name = "webmasters.sitemaps.submit",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SubmitCommand.class),
        @SubCommand(name = "webmasters.sites.add",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.AddCommand.class),
        @SubCommand(name = "webmasters.sites.delete",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.DeleteCommand.class),
        @SubCommand(name = "webmasters.sites.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.GetCommand.class),
        @SubCommand(name = "webmasters.sites.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand.class),
        @SubCommand(name = "webmasters.urlcrawlerrorscounts.query",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts.QueryCommand.class),
        @SubCommand(name = "webmasters.urlcrawlerrorssamples.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.GetCommand.class),
        @SubCommand(name = "webmasters.urlcrawlerrorssamples.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.ListCommand.class),
        @SubCommand(name = "webmasters.urlcrawlerrorssamples.markAsFixed",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.MarkAsFixedCommand.class),
    })
    private Command command;

    /** ヘルプ表示フラグです。 */
    @Option(name = "-?", aliases = "--help", usage = "show this help message and exit")
    private boolean usageFlag;

    /**
     * デフォルトコンストラクタです。
     */
    public WebmastersCommandRunner() {
        // デフォルトコンストラクタ
    }

    /**
     * コマンドライン引数に基づいてコマンドを実行します。
     * 
     * @param args コマンドライン引数
     * @throws Exception 実行中に例外が発生した場合
     */
    @Override
    public void run(final String... args) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting command execution");
        }
        
        // コマンドライン引数を処理
        final List<String> filteredArgs = filterApplicationArgs(args);
        final CmdLineParser parser = new CmdLineParser(this);
        
        // 引数が空の場合はヘルプを表示
        if (filteredArgs.isEmpty()) {
            displayHelp(parser);
            return;
        }
        
        try {
            // コマンドライン引数を解析
            parser.parseArgument(filteredArgs);
            
            // ヘルプフラグが指定されている場合はヘルプを表示
            if (usageFlag) {
                displayHelp(parser);
                return;
            }
            // コマンドを実行
            executeCommand();
        } catch (CmdLineArgmentException | CmdLineException e) {
            displayError(parser, e);
        }
    }
    
    /**
     * "--application" を含むコマンドライン引数を除外します。
     * 
     * @param args 元のコマンドライン引数
     * @return フィルタリングされた引数リスト
     */
    private List<String> filterApplicationArgs(final String... args) {
        final List<String> filteredArgs = new ArrayList<>();
        for (final String arg : args) {
            if (!arg.contains("--application")) {
                filteredArgs.add(arg);
            }
        }
        return filteredArgs;
    }
    
    /**
     * コマンドを実行します。
     * 
     * @throws CmdLineException コマンドライン処理中にエラーが発生した場合
     */
    private void executeCommand() throws CmdLineException {
        context.getAutowireCapableBeanFactory().autowireBean(this.command);
        try {
            this.command.execute();
        } catch (CommandLineInputOutputException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Command execution failed due to I/O error: {}", e.getMessage());
            }
            throw new CmdLineException(e);
        } catch (CmdLineArgmentException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Command execution failed due to invalid argument: {}", e.getMessage());
            }
            throw new CmdLineException(e);
        }
    }
    
    /**
     * ヘルプ情報を表示します。
     * 
     * @param parser コマンドラインパーサー
     */
    private void displayHelp(final CmdLineParser parser) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("--------------------------------------------------------------------------");
            LOGGER.error(usage());
            parser.printUsage(System.err);
            LOGGER.error("---------------------------");
        }
    }
    
    /**
     * エラー情報を表示します。
     * 
     * @param parser コマンドラインパーサー
     * @param exception 発生した例外
     */
    private void displayError(final CmdLineParser parser, final Exception exception) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("error occurred: {}", exception.getMessage());
            LOGGER.error("--------------------------------------------------------------------------");
            LOGGER.error(usage());
            parser.printUsage(System.err);
            LOGGER.error("------------");
        }
    }

    /**
     * 使用方法（usage）情報を生成します。
     * 
     * @return 使用方法の文字列
     */
    private String usage() {
        final StringBuilder builder = new StringBuilder(200);
        builder.append("usage: xyz.monotalk.google.webmaster.cli.CliApplication\n\n");
        
        final Field field;
        try {
            field = this.getClass().getDeclaredField("command");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
        
        final SubCommands subCommands = field.getAnnotation(SubCommands.class);
        final StringJoiner joiner = new StringJoiner(",", "{", "}");
        Arrays.stream(subCommands.value()).forEach(e -> joiner.add(e.name()));
        
        builder.append("  ").append(joiner.toString()).append("\n  ...\n\n")
               .append("positional arguments:\n\n")
               .append("  ").append(joiner.toString()).append('\n');
               
        Arrays.stream(subCommands.value()).map(e -> {
            try {
                return Pair.of(e.name(), (Command) e.impl().getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException 
                | InvocationTargetException | NoSuchMethodException ex) {
                throw new IllegalStateException(ex);
            }
        }).forEach(e -> {
            builder.append("    ")
                   .append(e.getLeft())
                   .append("  |  ")
                   .append(e.getRight().usage())
                   .append('\n');
        });
        
        builder.append("\noptional arguments:\n\n");
        return builder.toString();
    }
}
