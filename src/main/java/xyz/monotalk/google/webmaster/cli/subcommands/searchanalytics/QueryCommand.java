package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import static java.lang.System.out;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDataRow;
import com.google.api.services.webmasters.model.ApiDimensionFilter;
import com.google.api.services.webmasters.model.ApiDimensionFilterGroup;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.monotalk.google.webmaster.cli.Command;
import xyz.monotalk.google.webmaster.cli.CommandLineInputOutputException;
import xyz.monotalk.google.webmaster.cli.Format;
import xyz.monotalk.google.webmaster.cli.ResponseWriter;
import xyz.monotalk.google.webmaster.cli.WebmastersFactory;

/**
 * Google Search Consoleの検索アナリティクスデータを取得するコマンドクラス。
 */
@Component
public class QueryCommand implements Command {

    /** ロガーインスタンス。 */
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryCommand.class);

    /** WebmastersファクトリーインスタンスDI用。 */
    @Autowired
    private WebmastersFactory factory;

    /** 開始日。 */
    @Option(name = "-startDate", usage = "Start date (YYYY-MM-DD)", required = true)
    protected String startDate;

    /** 終了日。 */
    @Option(name = "-endDate", usage = "End date (YYYY-MM-DD)", required = true)
    protected String endDate;

    /** サイトURL。 */
    @Option(name = "-siteUrl", usage = "Site URL", required = true)
    protected String siteUrl;

    /** 出力フォーマット。 */
    @Option(name = "-format", usage = "Output format [console or json]")
    protected Format format = Format.CONSOLE;

    /** 出力ファイルパス。 */
    @Option(name = "-filePath", usage = "Output file path")
    protected String filePath;

    /**
     * デフォルトコンストラクタ。
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * 検索アナリティクスデータを取得し、指定された形式で出力します。
     *
     * @throws CommandLineInputOutputException API実行エラーが発生した場合。
     */
    @Override
    public void execute() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("開始日: {}, 終了日: {}, サイトURL: {}", startDate, endDate, siteUrl);
        }

        try {
            final Webmasters webmasters = factory.createClient();
            if (webmasters == null) {
                throw new CommandLineInputOutputException(new IOException("Webmastersクライアントの生成に失敗しました"));
            }

            final SearchAnalyticsQueryRequest request = new SearchAnalyticsQueryRequest()
                .setStartDate(startDate)
                .setEndDate(endDate);

            final Webmasters.Searchanalytics.Query query = webmasters.searchanalytics().query(siteUrl, request);
            final SearchAnalyticsQueryResponse response = query.execute();

            // コンソール出力の場合、空の文字列をfilePathとして渡す
            final String outputPath = (format == Format.CONSOLE && (filePath == null || filePath.isEmpty())) 
                ? "" : filePath;
            
            // レスポンスの出力
            ResponseWriter.writeJson(response, format, outputPath);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("検索アナリティクスデータの取得が完了しました");
            }

        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("APIの実行に失敗しました", e);
            }
            throw new CommandLineInputOutputException("APIの実行に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * コマンドの使用方法を返します。
     *
     * @return 使用方法の説明
     */
    @Override
    public String usage() {
        return "検索アナリティクスデータを取得します";
    }

    /**
     * 開始日を設定します。
     *
     * @param startDate 開始日 (YYYY-MM-DD)
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * 終了日を設定します。
     *
     * @param endDate 終了日 (YYYY-MM-DD)
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * サイトURLを設定します。
     *
     * @param siteUrl サイトURL
     */
    public void setSiteUrl(final String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * 出力フォーマットを設定します。
     *
     * @param format 出力フォーマット
     */
    public void setFormat(final Format format) {
        this.format = format;
    }

    /**
     * 出力ファイルパスを設定します。
     *
     * @param filePath 出力ファイルパス
     */
    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }
}
