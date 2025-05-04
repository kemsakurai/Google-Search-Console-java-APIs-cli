package xyz.monotalk.google.webmaster.cli;

import org.apache.commons.lang3.tuple.Pair;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * WebmastersCommandRunnerは、コマンドライン引数を処理し、対応するコマンドを実行します。
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    /** ロガーインスタンス */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebmastersCommandRunner.class);

    /**
     * Springコンテキストの注入
     */
    @Autowired
    private ApplicationContext context;

    /**
     * 引数によって実行するオブジェクトを切り替える
     */
    @Argument(handler = SubCommandHandler.class, metaVar = "subCommands")
    @SubCommands({@SubCommand(name = "webmasters.searchanalytics.query",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics.QueryCommand.class), @SubCommand(
            name = "webmasters.sitemaps.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.ListCommand.class), @SubCommand(
            name = "webmasters.sitemaps.delete",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.DeleteCommand.class), @SubCommand(
            name = "webmasters.sitemaps.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.GetCommand.class), @SubCommand(
            name = "webmasters.sitemaps.submit",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SubmitCommand.class), @SubCommand(
            name = "webmasters.sites.add",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.AddCommand.class), @SubCommand(
            name = "webmasters.sites.delete",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.DeleteCommand.class), @SubCommand(
            name = "webmasters.sites.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.GetCommand.class), @SubCommand(
            name = "webmasters.sites.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.sites.ListCommand.class), @SubCommand(
            name = "webmasters.urlcrawlerrorscounts.query",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts.QueryCommand.class), @SubCommand(
            name = "webmasters.urlcrawlerrorssamples.get",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.GetCommand.class), @SubCommand(
            name = "webmasters.urlcrawlerrorssamples.list",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.ListCommand.class), @SubCommand(
            name = "webmasters.urlcrawlerrorssamples.markAsFixed",
            impl = xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorssamples.MarkAsFixedCommand.class),})

    private Command command;

    @Option(name = "-?", aliases = "--help", usage = "show this help message and exit") private boolean usageFlag;

    @Override
    public void run(final String... args) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting command execution");
        }
        // ------------------------------------------------
        // "--application" を含むコマンドライン引数を除外
        // ----------------
        final List<String> cmdArgs = new ArrayList<>();
        for (final String arg : args) {
            if (!arg.contains("--application")) {
                cmdArgs.add(arg);
            }
        }
        if (cmdArgs.isEmpty()) {
            final CmdLineParser parser = new CmdLineParser(this);
            parser.parseArgument(cmdArgs);
            LOGGER.error("--------------------------------------------------------------------------");
            LOGGER.error(usage());
            parser.printUsage(System.err);
            LOGGER.error("------------");
            return;
        }
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(cmdArgs);
            // Output help
            if (usageFlag) {
                LOGGER.error("--------------------------------------------------------------------------");
                LOGGER.error(usage());
                parser.printUsage(System.err);
                LOGGER.error("---------------------------");
                return;
            }
            final AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this.command);
            this.command.execute();
        } catch (CmdLineArgmentException | CmdLineException e) {
            LOGGER.error("error occurred: " + e.getMessage());
            LOGGER.error("--------------------------------------------------------------------------");
            LOGGER.error(usage());
            parser.printUsage(System.err);
            LOGGER.error("------------");
            return;
        }
    }

    /**
     * usage
     */
    private String usage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("usage: xyz.monotalk.google.webmaster.cli.CliApplication\n\n");
        final Field field;
        try {
            field = this.getClass().getDeclaredField("command");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
        final SubCommands subCommands = field.getAnnotation(SubCommands.class);
        final StringJoiner joiner = new StringJoiner(",", "{", "}");
        Arrays.stream(subCommands.value()).forEach(e -> joiner.add(e.name()));
        sb.append("  ").append(joiner.toString()).append("\n  ...\n\n");
        sb.append("positional arguments:\n\n");
        sb.append("  ").append(joiner.toString()).append("\n");
        Arrays.stream(subCommands.value()).map(e -> {
            try {
                return Pair.of(e.name(), (Command) e.impl().getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new IllegalStateException(ex);
            }
        }).forEach(e -> {
            sb.append("    ").append(e.getLeft()).append("  |  ").append(e.getRight().usage()).append("\n");
        });
        sb.append("\noptional arguments:\n\n");
        return sb.toString();
    }
}
