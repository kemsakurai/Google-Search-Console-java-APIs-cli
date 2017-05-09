package xyz.monotalk.google.webmaster.cli;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.subcommands.*;

import java.util.ArrayList;
import java.util.List;

/**
 * WebmastersCommandRunner
 */
@Component
public class WebmastersCommandRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    /**
     * 引数によって実行するオブジェクトを切り替える
     */
    @Argument(handler = SubCommandHandler.class)
    @SubCommands({@SubCommand(name = "searchAnalytics", impl = SearchAnalyticsCommand.class), @SubCommand(name = "sitemaps", impl = SiteMapsCommand.class), @SubCommand(name = "sites", impl = SitesCommand.class), @SubCommand(name = "urlCrawlErrorsCounts", impl = UrlCrawErrorsCountsCommand.class), @SubCommand(name = "urlCrawlErrorsSamples", impl = UrlsCrawlErrorsSamplesCommand.class)})
    private Command command;

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
        try {
            new CmdLineParser(this).parseArgument(cmdArgs);
            AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
            autowireCapableBeanFactory.autowireBean(this.command);
            this.command.execute();
        } catch (CmdLineException e) {
            e.printStackTrace();
        }
    }
}
