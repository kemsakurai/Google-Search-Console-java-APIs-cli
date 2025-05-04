package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * サイトのサイトマップをリストアップするコマンドです。
 * 
 * @throws CommandLineInputOutputException 入出力処理中にエラーが発生した場合。
 * @throws CmdLineArgmentException 引数の検証中にエラーが発生した場合。
 * @throws CmdLineIOException API要求の実行中にエラーが発生した場合。
 */
@Component
public class ListCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    private ResponseWriter responseWriter;

    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl = null;

    @Option(name = "-format", usage = "Output format [console or json]")
    private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "Output file path for JSON format", depends = {"-format"})
    private String filePath = null;

    /**
     * サイトマップの一覧を取得して表示します。
     * 
     * @throws CommandLineInputOutputException 入出力処理中にエラーが発生した場合。
     * @throws CmdLineArgmentException 引数の検証中にエラーが発生した場合。
     * @throws CmdLineIOException API要求の実行中にエラーが発生した場合。
     */
    @Override
    public void execute() throws CommandLineInputOutputException, CmdLineArgmentException, CmdLineIOException {
        LOGGER.info("Starting site {} sitemap list command.", siteUrl);
        try {
            Webmasters webmasters = factory.create();
            Webmasters.Sitemaps.List list = webmasters.sitemaps().list(siteUrl);
            SitemapsListResponse response = list.execute();
            responseWriter.writeJson(response, format, filePath);
            LOGGER.info("Command completed successfully.");
        } catch (IOException e) {
            LOGGER.error("API execution failed", e);
            throw new CmdLineIOException("API execution failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String usage() {
        return "Lists the sitemaps-entries submitted for this site.";
    }
}
