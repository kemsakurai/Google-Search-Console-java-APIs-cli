package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import java.io.IOException;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * URLクロールエラー数を取得するコマンド
 */
@Component
public class QueryCommand implements Command {

    /**
     * ロガーインスタンス
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /**
     * Webmasters APIクライアント生成ファクトリ
     */
    @Autowired
    private WebmastersFactory factory;

    /**
     * サイトURL。
     */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /**
     * 出力フォーマット。
     */
    @Option(name = "-format", usage = "Output format", required = false)
    private Format format = Format.CONSOLE;

    /**
     * 出力ファイルパス。
     */
    @Option(name = "-filePath", usage = "Output file path", required = false)
    private String filePath;
    
    /**
     * デフォルトコンストラクタ。
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * URLクロールエラー数を取得し、指定された形式で出力します。
     * Google APIは具象型を使用しているため、一部の警告は抑制します。
     * 
     * @throws CmdLineIOException if an error occurs while retrieving the URL crawl error counts
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Retrieving URL crawl error counts for site: {}", siteUrl);
        }
        
        try {
            final Webmasters.Urlcrawlerrorscounts.Query request = 
                factory.create().urlcrawlerrorscounts().query(siteUrl);
            final UrlCrawlErrorsCountsQueryResponse response = request.execute();
            ResponseWriter.writeJson(response, format, filePath);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("URL crawl error counts retrieved successfully");
            }
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to retrieve URL crawl error counts", e);
            }
            throw new CommandLineInputOutputException("URLクロールエラー数の取得に失敗しました", e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明。
     */
    @Override
    public String usage() {
        return "Retrieves a time series of the number of URL crawl errors per error category and platform.";
    }
}
