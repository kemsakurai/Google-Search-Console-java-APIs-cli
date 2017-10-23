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
import xyz.monotalk.google.webmaster.cli.subcommands.SearchAnalyticsCommand;
import xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SiteMapsDeleteCommand;
import xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SiteMapsGetCommand;
import xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SiteMapsListCommand;
import xyz.monotalk.google.webmaster.cli.subcommands.sitemaps.SiteMapsSubmitCommand;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static java.lang.System.out;

/**
 * WebmastersCommandRunner
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    @Autowired private ApplicationContext context;

    /**
     * 引数によって実行するオブジェクトを切り替える
     */
    @Argument(handler = SubCommandHandler.class)
    @SubCommands({@SubCommand(name = "webmasters.sitemaps.list", impl = SiteMapsListCommand.class), @SubCommand(
            name = "webmasters.sitemaps.delete", impl = SiteMapsDeleteCommand.class), @SubCommand(
            name = "webmasters.sitemaps.get", impl = SiteMapsGetCommand.class), @SubCommand(
            name = "webmasters.sitemaps.submit", impl = SiteMapsSubmitCommand.class), @SubCommand(
            name = "webmasters.searchanalytics.query", impl = SearchAnalyticsCommand.class),})
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
            out.println("--------------------------------------------------------------------------");
            out.println(usage());
            parser.printUsage(out);
            out.println("------------");
            return;
        }
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(cmdArgs);
            // Output help
            if (usageFlag) {
                out.println("--------------------------------------------------------------------------");
                out.println(usage());
                parser.printUsage(out);
                out.println("---------------------------");
                return;
            }
            AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this.command);
            this.command.execute();
        } catch (CmdLineException e) {
            out.println("error occurred: " + e.getMessage());
            out.println("--------------------------------------------------------------------------");
            out.println(usage());
            parser.printUsage(out);
            out.println("------------");
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
            sb.appendln("    " + e.getLeft() + "    " + e.getRight().usage());
        });
        sb.appendNewLine();
        sb.append("optional arguments:");
        sb.appendNewLine();
        return sb.toString();
    }
}
