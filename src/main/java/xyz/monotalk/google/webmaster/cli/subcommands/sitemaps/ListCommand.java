package xyz.monotalk.google.webmaster.cli.subcommands.sitemaps;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SitemapsListResponse;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ListCommand
 */
@Component
public class ListCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    private ResponseWriter responseWriter;

    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    protected String siteUrl;

    @Option(name = "-format", usage = "Output format", required = false)
    protected Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "Output file path", required = false)
    protected String filePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);

    /**
     * サイトマップ一覧を取得し、指定された形式で出力します。
     *
     * @throws CommandLineInputOutputException 入出力エラーが発生した場合
     * @throws CmdLineArgmentException 引数の検証エラーが発生した場合
     * @throws CmdLineIOException API実行エラーが発生した場合
     */
    @Override
    public void execute() throws CmdLineArgmentException, CmdLineIOException {
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

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Lists the sitemaps for a given site URL.";
    }
}
