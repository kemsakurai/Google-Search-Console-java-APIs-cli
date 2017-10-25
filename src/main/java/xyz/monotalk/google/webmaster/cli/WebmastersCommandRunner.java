package xyz.monotalk.google.webmaster.cli;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StrBuilder;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static java.lang.System.err;

/**
 * WebmastersCommandRunner
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    @Autowired private ApplicationContext context;

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
    public void run(String... args) throws Exception {
        // ------------------------------------------------
        // "--application" を含むコマンドライン引数を除外
        // ----------------
        List<String> cmdArgs = new ArrayList<>();
        for (String arg : args) {
            if (!arg.contains("--application")) {
                cmdArgs.add(arg);
            }
        }
        if (cmdArgs.isEmpty()) {
            CmdLineParser parser = new CmdLineParser(this);
            parser.parseArgument(cmdArgs);
            err.println("--------------------------------------------------------------------------");
            err.println(usage());
            parser.printUsage(err);
            err.println("------------");
            return;
        }
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(cmdArgs);
            // Output help
            if (usageFlag) {
                err.println("--------------------------------------------------------------------------");
                err.println(usage());
                parser.printUsage(err);
                err.println("---------------------------");
                return;
            }
            AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this.command);
            this.command.execute();
        } catch (CmdLineArgmentException | CmdLineException e) {
            err.println("error occurred: " + e.getMessage());
            err.println("--------------------------------------------------------------------------");
            err.println(usage());
            parser.printUsage(err);
            err.println("------------");
            return;
        }
    }

    /**
     * usage
     */
    private String usage() {
        StrBuilder sb = new StrBuilder();
        sb.appendln("usage: xyz.monotalk.google.webmaster.cli.CliApplication");
        sb.appendNewLine();
        Field field;
        try {
            field = this.getClass().getDeclaredField("command");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
        SubCommands subCommands = field.getAnnotation(SubCommands.class);
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        Arrays.stream(subCommands.value()).forEach(e -> joiner.add(e.name()));
        sb.appendln("  " + joiner.toString());
        sb.appendln("  " + "...");
        sb.appendNewLine();
        sb.appendln("positional arguments:");
        sb.appendNewLine();
        sb.appendln("  " + joiner.toString());
        Arrays.stream(subCommands.value()).map(e -> {
            try {
                return Pair.of(e.name(), (Command) e.impl().newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        }).forEach(e -> {
            sb.appendln("    " + e.getLeft() + "  |  " + e.getRight().usage());
        });
        sb.appendNewLine();
        sb.append("optional arguments:");
        sb.appendNewLine();
        return sb.toString();
    }
}
