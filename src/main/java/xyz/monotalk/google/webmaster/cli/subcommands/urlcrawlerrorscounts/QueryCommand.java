package xyz.monotalk.google.webmaster.cli.subcommands.urlcrawlerrorscounts;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.*;

import java.io.IOException;

/**
 * URLクロールエラー数を取得するコマンド
 */
@Component
public class QueryCommand implements Command {

    /** Webmasters APIクライアント生成ファクトリ */
    @Autowired
    private WebmastersFactory factory;

    /** サイトURL */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    private String siteUrl;

    /** 出力フォーマット */
    @Option(name = "-format", usage = "Output format", required = false)
    private static final Format format = Format.CONSOLE;

    /** 出力ファイルパス */
    @Option(name = "-filePath", usage = "Output file path", required = false)
    private String filePath;
    
    /**
     * デフォルトコンストラクタ
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * URLクロールエラー数を取得し、指定された形式で出力します。
     * 
     * Google APIは具象型を使用しているため、一部の警告は抑制します。
     */
    @Override
    public void execute() {
        try {
            final Webmasters.Urlcrawlerrorscounts.Query request = factory.create().urlcrawlerrorscounts().query(siteUrl);
            final UrlCrawlErrorsCountsQueryResponse response = request.execute();
            ResponseWriter.writeJson(response, format, filePath);
        } catch (IOException e) {
            throw new CmdLineIOException("URLクロールエラー数の取得に失敗しました", e);
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
