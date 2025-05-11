package xyz.monotalk.google.webmaster.cli.subcommands.searchanalytics;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
     * コンストラクタ。
     */
    public QueryCommand() {
        // デフォルトコンストラクタ
    }

    /**
     * パラメータを検証します。
     */
    private void validateParameters() {
        if (!isValidDateFormat(startDate)) {
            throw new CommandLineInputOutputException("開始日のフォーマットが不正です (YYYY-MM-DD形式で指定してください)");
        }
        if (!isValidDateFormat(endDate)) {
            throw new CommandLineInputOutputException("終了日のフォーマットが不正です (YYYY-MM-DD形式で指定してください)");
        }
        if (siteUrl == null || siteUrl.isEmpty()) {
            throw new CommandLineInputOutputException("サイトURLが指定されていません");
        }
    }

    /**
     * 日付形式が正しいかチェックします。
     *
     * @param date チェックする日付文字列
     * @return 日付形式が正しい場合はtrue
     */
    private boolean isValidDateFormat(final String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * 検索アナリティクスデータのリクエストを作成します。
     *
     * @return 検索アナリティクスクエリリクエスト
     */
    private SearchAnalyticsQueryRequest createSearchRequest() {
        return new SearchAnalyticsQueryRequest()
            .setStartDate(startDate)
            .setEndDate(endDate);
    }

    /**
     * Webmastersクライアントを取得します。
     *
     * @return Webmastersクライアントインスタンス
     */
    private Webmasters getWebmastersClient() {
        try {
            final Webmasters webmasters = factory.create();
            if (webmasters == null) {
                throw new CommandLineInputOutputException("Webmastersクライアントの生成に失敗しました");
            }
            return webmasters;
        } catch (IOException | GeneralSecurityException e) {
            throw new CommandLineInputOutputException("Webmastersクライアントの生成に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * 出力パスを決定します。
     *
     * @return 出力パス（コンソール出力の場合は空文字列）
     */
    private String determineOutputPath() {
        return (format == Format.CONSOLE && (filePath == null || filePath.isEmpty())) 
            ? "" : filePath;
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
            validateParameters();
            final Webmasters webmasters = getWebmastersClient();
            final SearchAnalyticsQueryRequest request = createSearchRequest();
            final Webmasters.Searchanalytics.Query query = webmasters.searchanalytics().query(siteUrl, request);
            final SearchAnalyticsQueryResponse response = query.execute();
            
            ResponseWriter.writeJson(response, format, determineOutputPath());

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
