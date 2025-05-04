package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import java.io.IOException;
import java.net.URL;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.URLOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.CmdLineIOException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;
import xyz.monotalk.google.webmaster.cli.CmdLineArgmentException;

/**
 * QueryCommand.
 */
@Component
public class QueryCommand implements Command {

    @Autowired private WebmastersFactory factory;

    @Autowired private ResponseWriter responseWriter;

    @Option(name = "-siteUrl", usage = "Url of site", metaVar = "<siteUrl>", required = true,
            handler = URLOptionHandler.class) private URL siteUrl = null;

    @Option(name = "-format", usage = "Format of output ",
            metaVar = "[console or json]") private Format format = Format.CONSOLE;

    @Option(name = "-filePath", usage = "File name of json ", metaVar = "<filename>",
            depends = {"-format"}) private String filePath = null;

    /**
     * コマンドを実行します。
     * エラーカウント情報を取得し、指定された形式で出力します。
     * 
     * @throws CmdLineIOException 入出力操作中にエラーが発生した場合。
     * @throws CommandLineInputOutputException レスポンス出力処理中にエラーが発生した場合。
     * @throws CmdLineArgmentException 引数のバリデーションでエラーが発生した場合。
     */
    @Override
    public void execute() throws CmdLineIOException, CommandLineInputOutputException, CmdLineArgmentException {
        Webmasters.Urlcrawlerrorscounts.Query request;
        try {
            request = factory.create().urlcrawlerrorscounts().query(siteUrl.toString());
        } catch (IOException e) {
            throw new CmdLineIOException(e.getMessage(), e);
        }
        UrlCrawlErrorsCountsQueryResponse response;
        try {
            response = request.execute();
        } catch (IOException e) {
            throw new CmdLineIOException(e.getMessage(), e);
        }
        responseWriter.writeJson(response, format, filePath);
    }

    @Override
    public String usage() {
        return "Retrieves a time series of the number of URL crawl errors per error category and platform.";
    }
}
