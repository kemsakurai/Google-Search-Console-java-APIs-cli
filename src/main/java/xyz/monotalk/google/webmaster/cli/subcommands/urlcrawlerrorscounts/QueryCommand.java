package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;

/**
 * QueryCommand
 */
@Component
public class QueryCommand implements Command {

    @Autowired
    private WebmastersFactory factory;

    @Autowired
    private ResponseWriter responseWriter;

    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    @Option(name = "-format", usage = "Output format", required = false)
    private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "Output file path", required = false)
    private String filePath;

    /**
     * URLクロールエラー数を取得し、指定された形式で出力します。
     *
     * @throws CmdLineIOException API実行エラーが発生した場合
     */
    @Override
    public void execute() throws CmdLineIOException {
        try {
            Webmasters.Urlcrawlerrorscounts.Query request = factory.create().urlcrawlerrorscounts().query(siteUrl);
            UrlCrawlErrorsCountsQueryResponse response = request.execute();
            responseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CmdLineIOException("Failed to query URL crawl errors counts", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "Retrieves a time series of the number of URL crawl errors per error category and platform.";
    }
}
